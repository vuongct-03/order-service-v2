package com.kltn.order_service.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kltn.order_service.client.dto.SpecificationDTO;
import com.kltn.order_service.client.dto.UserDTO;
import com.kltn.order_service.client.service.ProductClientService;
import com.kltn.order_service.client.service.SpecificationClientService;
import com.kltn.order_service.client.service.UserClientService;
import com.kltn.order_service.dto.GHN.CancelOrderRequest;
import com.kltn.order_service.dto.request.OrderItemRequestDTO;
import com.kltn.order_service.dto.request.OrderRequestDTO;
import com.kltn.order_service.dto.response.OrderItemResponse;
import com.kltn.order_service.dto.response.OrderResponse;
import com.kltn.order_service.kafka.OrderKafkaProducer;
import com.kltn.order_service.kafka.PaymentSuccessProducer;
import com.kltn.order_service.dto.response.OrderRes;
import com.kltn.order_service.model.CartItem;
import com.kltn.order_service.model.Order;
import com.kltn.order_service.model.OrderItem;
import com.kltn.order_service.repository.CartItemRepository;
import com.kltn.order_service.repository.OrderItemRepository;
import com.kltn.order_service.repository.OrderRepository;
import com.kltn.order_service.service.CartItemService;
import com.kltn.order_service.service.CloudinaryService;
import com.kltn.order_service.service.DistanceService;
import com.kltn.order_service.service.OrderService;
import com.kltn.order_service.service.ShippingOrderService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final SpecificationClientService specificationService;
    private final UserClientService userService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CloudinaryService cloudinaryService;
    private final ShippingOrderService shippingOrderService;
    private final PaymentSuccessProducer paymentSuccessProducer;
    private final CartItemRepository cartItemRepository;
    private final CartItemService cartItemService;

    private final OrderKafkaProducer  orderKafkaProducer;

    public OrderServiceImpl(SpecificationClientService specificationService,
            UserClientService userService,
            OrderRepository orderRepository,
            DistanceService distanceService,
            OrderItemRepository orderItemRepository,
            CloudinaryService cloudinaryService,
            ProductClientService productService,
            ShippingOrderService shippingOrderService,
            OrderKafkaProducer orderKafkaProducer,
            PaymentSuccessProducer paymentSuccessProducer,
            CartItemRepository cartItemRepository,
            CartItemService cartItemService,
            UserClientService userClientService) {

        this.specificationService = specificationService;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cloudinaryService = cloudinaryService;
        this.shippingOrderService = shippingOrderService;
        this.orderKafkaProducer = orderKafkaProducer;
        this.paymentSuccessProducer = paymentSuccessProducer;
        this.cartItemRepository = cartItemRepository;
        this.cartItemService = cartItemService;
        
    }

    private SpecificationDTO getSpecById(String id) {
        return specificationService.getSpecificationById(id);
    }

    private Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Not foud order"));
    }

    private Set<OrderItem> convertToOrderItem(Set<OrderItemRequestDTO> items, Order order) {
        Set<OrderItem> result = new HashSet<>();
        List<String> errors = new ArrayList<>();

        // B1: Kiểm tra tồn kho và cập nhật giỏ hàng nếu cần
        Map<String, SpecificationDTO> specCache = new HashMap<>();

        for (OrderItemRequestDTO item : items) {
            SpecificationDTO spec = getSpecById(item.getSpecId());
            
            specCache.put(item.getSpecId(), spec); // Lưu cache để tránh gọi lại sau

            int stock = spec.getQuantity();
            int requested = item.getQuantity();

            if (stock == 0) {
                // Nếu sản phẩm hết hàng → xóa khỏi giỏ
                CartItem cartItem = cartItemRepository.findBySpecId(spec.getId());
                if (cartItem != null) {
                    cartItemService.deleteItem(cartItem.getId());
                }
                errors.add("❌ Sản phẩm " + spec.getName() + " đã hết hàng. \n");
            } 
            else if (requested > stock) {
                // Nếu vượt số lượng tồn → cập nhật giỏ = stock
                CartItem cartItem = cartItemRepository.findBySpecId(spec.getId());
                if (cartItem != null) {
                    cartItemService.updateQuantity(cartItem.getId(), stock);
                }
                errors.add("⚠️ Sản phẩm " + spec.getName() + " chỉ còn " + stock + " sản phẩm trong kho. \n");
            }
        }

        // Nếu có lỗi thì thông báo chung
        if (!errors.isEmpty()) {
            String errorMessage = String.join("\n", errors);
            throw new RuntimeException(errorMessage);
        }

        // B2: Tạo OrderItem khi tất cả hợp lệ
        for (OrderItemRequestDTO item : items) {
            SpecificationDTO spec = specCache.get(item.getSpecId());
            double totalPrice = item.getQuantity() * spec.getPrice() * ((100 - item.getDiscountPercent())/100);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .specId(spec.getId())
                    .specName(spec.getName())
                    .quantity(item.getQuantity())
                    .price(totalPrice)
                    .build();

            // UserBehaviorDTO userBehaviorDTO = UserBehaviorDTO.builder()
            //     .userId(order.getUserId())
            //     .specId(spec.getId())
            //     .action("buy")
            // .build();
    
            // userClientService.saveUserBehavior(userBehaviorDTO);

            result.add(orderItem);
        }

        return result;
    }


    private void sendOrderKafka(Order order){
        OrderResponse response = OrderResponse.builder()
            .id(order.getId())
            .fullName(getFullName(order.getUserId()))
            .orderCode(order.getOrderCode())
            .phone(getPhone(order.getUserId()))
            .shippingAddress(order.getShippingAddress())
            .shippingFee(order.getShippingFee())
            .totalPrice(order.getTotalPrice())
            .status(order.getStatus().toString())
            .orderDate(order.getCreateAt())
            .userId(order.getUserId())
            .deliveredImageURL(order.getDeliveredImageURL())
            .transType(order.getTransType())
            .expectedDeliveryTime(order.getExpectedDeliveryTime())
            .isPayment(order.isPayment())
            .referralCode(order.getReferralCode())
            .items(buildItemsByOrder(order.getId()))
        .build();

        orderKafkaProducer.sendOrderCreated(response);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('OWNER', 'STAFF')")
    public Long createOrder(OrderRequestDTO requestDTO) {
        try {
            UserDTO user = userService.getUserById(requestDTO.getUserId());

            Order order = Order.builder()
                    .userId(user.getId())
                    .orderCode(requestDTO.getOrderCode())
                    .transType(requestDTO.getTransType())
                    .shippingFee(requestDTO.getTotalShippingFee())
                    .shippingAddress(requestDTO.getShippingAddress())
                    .expectedDeliveryTime(requestDTO.getExpectedDeliveryTime())
                    .isPayment(requestDTO.isPayment())
                    .referralCode(requestDTO.getReferralCode())
                    .status("ready_to_pick")
                    .build();

            Order savedOrder = orderRepository.save(order);

            // Bước 2: Chuyển đổi và lưu OrderItem
            Set<OrderItem> orderItems = convertToOrderItem(requestDTO.getOrderItems(), savedOrder);
            savedOrder.setOrderItems(orderItems);
            savedOrder
                    .setTotalPrice(orderItems.stream().mapToDouble(item -> item.getPrice()).sum()
                            + requestDTO.getTotalShippingFee());

            // Bước 3: Lưu lại Order đã cập nhật OrderItem
            orderRepository.save(savedOrder);

            if(savedOrder.isPayment()){
                paymentSuccessProducer.sendCommissionMoney(savedOrder.getReferralCode(), savedOrder.getTotalPrice());
            }
            sendOrderKafka(savedOrder);

            cartItemRepository.deleteAllByUserId(savedOrder.getUserId());
            
            

            
            return savedOrder.getId();
        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    // @Override
    // @Transactional
    // @PreAuthorize("hasAnyRole('OWNER', 'STAFF')")
    // public Long updateSpecQuantityandcreateOrder(OrderRequestDTO requestDTO) {
    //     try {
    //         UserDTO user = userService.getUserById(requestDTO.getUserId());

    //         Order order = Order.builder()
    //                 .userId(user.getId())
    //                 .orderCode(requestDTO.getOrderCode())
    //                 .transType(requestDTO.getTransType())
    //                 .shippingFee(requestDTO.getTotalShippingFee())
    //                 .shippingAddress(requestDTO.getShippingAddress())
    //                 .expectedDeliveryTime(requestDTO.getExpectedDeliveryTime())
    //                 .isPayment(requestDTO.isPayment())
    //                 .referralCode(requestDTO.getReferralCode())
    //                 .status("ready_to_pick")
    //                 .build();

    //         Order savedOrder = orderRepository.save(order);

    //         // Bước 2: Chuyển đổi và lưu OrderItem
    //         Set<OrderItem> orderItems = convertToOrderItem(requestDTO.getOrderItems(), savedOrder);
    //         savedOrder.setOrderItems(orderItems);
    //         savedOrder
    //                 .setTotalPrice(orderItems.stream().mapToDouble(item -> item.getPrice()).sum()
    //                         + requestDTO.getTotalShippingFee());

    //         // Bước 3: Lưu lại Order đã cập nhật OrderItem
    //         orderRepository.save(savedOrder);

    //         if(savedOrder.isPayment()){
    //             paymentSuccessProducer.sendCommissionMoney(savedOrder.getReferralCode(), savedOrder.getTotalPrice());
    //         }
    //         sendOrderKafka(savedOrder);

    //         cartItemRepository.deleteAllByUserId(savedOrder.getUserId());
    //         return savedOrder.getId();
    //     } catch (Exception e) {
    //         throw new RuntimeException("lỗi", e);
    //     }
    // }

    @Override
    @PreAuthorize("hasAnyRole('SHIPPER')")
    public void shippingOrder(Long orderId, Long shipperId) {
        try {
            Order order = getOrderById(orderId);

            order.setStatus("shipping");
            orderRepository.save(order);

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('SHIPPER')")
    public void deliveredOrder(Long orderId, List<MultipartFile> images, String folder) {
        try {
            Order order = getOrderById(orderId);

            List<String> uploadedUrls = images.stream()
                    .map(image -> {
                        try {
                            // return null;
                            return cloudinaryService.uploadFile(image, folder);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return folder;
                    })
                    .collect(Collectors.toList());

            order.setDeliveredImageURL(uploadedUrls);

            Date currentDate = new Date(); // Lấy ngày hiện tại
            order.setDeliveryDate(currentDate);
            if (order.getDeliveredImageURL() == null || order.getDeliveredImageURL().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chụp ảnh để xác nhận đã giao hàng!");
            }

            order.setStatus("delivered");
            orderRepository.save(order);

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER','STAFF', 'ADMIN')")
    public OrderResponse getOrder(Long id) {
        try {

            List<OrderItem> items = orderItemRepository.findByOrderId(id);
            List<OrderItemResponse> itemsRes = items.stream().map(i -> OrderItemResponse.builder()
                    .id(i.getId())
                    .specId(i.getSpecId())
                    .specName(i.getSpecName())
                    .quantity(i.getQuantity())
                    .price(i.getPrice())
                    .orderId(i.getOrder().getId())
                    .build())
                    .toList();
            Order order = getOrderById(id);

            UserDTO userRes = userService.getUserById(order.getUserId());

            OrderResponse response = OrderResponse.builder()
                    .fullName(userRes.getFirstName() + " " + userRes.getLastName())
                    .totalPrice(order.getTotalPrice())
                    .shippingFee(order.getShippingFee())
                    .orderDate(order.getCreateAt())
                    .status(order.getStatus().toString())
                    .isPayment(order.isPayment())
                    .referralCode(order.getReferralCode())
                    .items(itemsRes)
                    .build();

            return response;

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public List<OrderRes<Object>> getAllOrder() {
        try {

            List<Order> orders = orderRepository.findAll();

            List<OrderRes<Object>> response = orders.stream().map(order -> OrderRes.builder()
                    .id(order.getId())
                    .shippingFee(order.getShippingFee())
                    .totalPrice(order.getTotalPrice())
                    .status(order.getStatus().toString())
                    .orderDate(order.getCreateAt())
                    .userId(order.getUserId())
                    .isPayment(order.isPayment())
                    .referralCode(order.getReferralCode())
                    .deliveredImageURL(order.getDeliveredImageURL())
                    .build()).toList();

            return response;

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER','STAFF', 'ADMIN')")
    public List<OrderItemResponse> getAllItem() {
        try {

            List<OrderItem> orderItems = orderItemRepository.findAll();

            List<OrderItemResponse> response = orderItems.stream().map(item -> OrderItemResponse.builder()
                    .id(item.getId())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .specId(item.getSpecId())
                    .specName(item.getSpecName())
                    .orderId(item.getOrder().getId())
                    .build()).toList();

            return response;

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    private String getFullName(Long userId) {
        UserDTO user = userService.getUserById(userId);
        return user.getFirstName() + " " + user.getLastName();
    }

    private String getPhone(Long userId) {
        UserDTO user = userService.getUserById(userId);
        return user.getPhone();
    }

    private List<OrderItemResponse> buildItemsByOrder(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<OrderItemResponse> specResponse = orderItems.stream().map(item -> OrderItemResponse.builder()
                .id(item.getId())
                .specId(item.getSpecId())
                .specName(item.getSpecName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .orderId(item.getOrder().getId())
                .image(getSpecById(item.getSpecId()).getImageURLs().get(0))
                .productId(getSpecById(item.getSpecId()).getProduct().getId())
                .build()).toList();

        return specResponse;
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER','STAFF', 'ADMIN')")
    public List<OrderResponse> getAllOrders() {
        try {

            List<Order> orders = orderRepository.findAll();
            List<OrderResponse> response = orders.stream()
                    .map(order -> OrderResponse.builder()
                            .id(order.getId())
                            .fullName(getFullName(order.getUserId()))
                            .orderCode(order.getOrderCode())
                            .phone(getPhone(order.getUserId()))
                            .shippingAddress(order.getShippingAddress())
                            .shippingFee(order.getShippingFee())
                            .totalPrice(order.getTotalPrice())
                            .status(order.getStatus().toString())
                            .orderDate(order.getCreateAt())
                            .userId(order.getUserId())
                            .deliveredImageURL(order.getDeliveredImageURL())
                            .transType(order.getTransType())
                            .isPayment(order.isPayment())
                            .referralCode(order.getReferralCode())
                            .expectedDeliveryTime(order.getExpectedDeliveryTime())
                            .isPayment(order.isPayment())
                            .referralCode(order.getReferralCode())
                            .items(buildItemsByOrder(order.getId()))
                            .build())
                    .toList();

            return response;

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }


    @Override
    @PreAuthorize("hasAnyRole('OWNER', 'STAFF', 'ADMIN')")
    public List<OrderResponse> getOrdersByUser(Long userId) {
        try {

            List<Order> orders = orderRepository.findOrderByUserId(userId);
            List<OrderResponse> response = orders.stream()
                    .map(order -> OrderResponse.builder()
                            .id(order.getId())
                            .fullName(getFullName(order.getUserId()))
                            .orderCode(order.getOrderCode())
                            .phone(getPhone(order.getUserId()))
                            .shippingAddress(order.getShippingAddress())
                            .shippingFee(order.getShippingFee())
                            .totalPrice(order.getTotalPrice())
                            .status(order.getStatus().toString())
                            .orderDate(order.getCreateAt())
                            .userId(order.getUserId())
                            .deliveredImageURL(order.getDeliveredImageURL())
                            .transType(order.getTransType())
                            .isPayment(order.isPayment())
                            .referralCode(order.getReferralCode())
                            .expectedDeliveryTime(order.getExpectedDeliveryTime())
                            .isPayment(order.isPayment())
                            .referralCode(order.getReferralCode())
                            .items(buildItemsByOrder(order.getId()))
                            .build())
                    .toList();

            return response;

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN', 'SELLER')")
    public List<OrderResponse> getOrdersByReferralCode(String referralCode) {
        try{

        List<Order> orders = orderRepository.findByReferralCode(referralCode);
        List<OrderResponse> response = orders.stream()
                .map(order -> OrderResponse.builder()
                        .id(order.getId())
                        .fullName(getFullName(order.getUserId()))
                        .phone(getPhone(order.getUserId()))
                        .shippingAddress("")
                        .shippingFee(order.getShippingFee())
                        .totalPrice(order.getTotalPrice())
                        .status(order.getStatus().toString())
                        .orderDate(order.getCreateAt())
                        .userId(order.getUserId())
                        .isPayment(order.isPayment())
                        .referralCode(order.getReferralCode())
                        .deliveredImageURL(order.getDeliveredImageURL())
                        .items(buildItemsByOrder(order.getId()))
                        .build())
                .toList();

            return response;
        }catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    public List<OrderResponse> getAllOrdersByStatus(String status) {
        try {
            List<Order> orders = orderRepository.findByStatus(status);
            List<OrderResponse> response = orders.stream()
                    .map(order -> OrderResponse.builder()
                            .id(order.getId())
                            .fullName(getFullName(order.getUserId()))
                            .orderCode(order.getOrderCode())
                            .phone(getPhone(order.getUserId()))
                            .shippingAddress(order.getShippingAddress())
                            .shippingFee(order.getShippingFee())
                            .totalPrice(order.getTotalPrice())
                            .status(order.getStatus().toString())
                            .orderDate(order.getCreateAt())
                            .userId(order.getUserId())
                            .isPayment(order.isPayment())
                            .referralCode(order.getReferralCode())
                            .deliveredImageURL(order.getDeliveredImageURL())
                            .transType(order.getTransType())
                            .expectedDeliveryTime(order.getExpectedDeliveryTime())
                            .items(buildItemsByOrder(order.getId()))
                            .build())
                    .toList();

            return response;

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    public String updateOrderPayment(Long id) {
        try{
            Order order = getOrderById(id);

            if(order.isPayment()){
                return "Đơn hàng đã thanh toán!";
            }else{
                order.setPayment(true);
                orderRepository.save(order);
                if(order.getReferralCode() != null){
                    paymentSuccessProducer.sendCommissionMoney(order.getReferralCode(), order.getTotalPrice());
                }
                return "Thanh toán thành công!";
            }
            
        }catch(Exception e){
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public String deliveringOrder(Long id) {
        try{
            Order order = getOrderById(id);
            order.setStatus("delivering");
            Order saved =  orderRepository.save(order);
            
            if(saved.getId() != null){
                return "Chuyển sang bước giao hàng thành công!";
            }else{
                return "Chuyển sang bước giao hàng thất bại";
            }
            
        
        }catch(Exception e){
            throw new RuntimeException("lỗi", e);
        }
    }

    @Override
    public String cancelOrder(Long id, String orderCode) {
        try{
            Order order = getOrderById(id);


            if ("delivering".equals(order.getStatus())){
                return "Đơn hàng đang quá trình giao. Bạn không thể hủy đơn!";
            }if("cancel".equals(order.getStatus())){
                return "Đơn hàng đã được hủy";
            }else{

                if(orderCode != null && orderCode != ""){
                    CancelOrderRequest request = CancelOrderRequest.builder()
                    .orderCodes(List.of(orderCode))
                    .build();
                    shippingOrderService.cancelOrderGHN(request);
                }

                order.setStatus("cancel");
                orderRepository.save(order);

                return "Hủy đơn hàng thành công!";
            }

        }catch(Exception e){
            throw new RuntimeException("lỗi", e);
        }
    }

}
