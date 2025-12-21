package com.kltn.order_service.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kltn.order_service.dto.response.OrderResponse;
import com.kltn.order_service.service.InvoiceService;
import com.kltn.order_service.service.OrderService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private OrderService orderService;

    @Override
    public String generateInvoice() {
        try {
            // Lấy danh sách đơn hàng
            List<OrderResponse> orders = orderService.getAllOrders();

            // Duyệt qua mỗi đơn hàng và tạo hóa đơn
            for (OrderResponse order : orders) {
                // Tạo nội dung cho hóa đơn
                StringBuilder invoiceContent = new StringBuilder();
                invoiceContent.append("------------------------------------------------\n");
                invoiceContent.append("                 INVOICE \n");
                invoiceContent.append("------------------------------------------------\n");
                invoiceContent.append("Order Number: ").append(order.getOrderCode()).append("\n");
                invoiceContent.append("Customer: ").append(order.getFullName()).append("\n");
                invoiceContent.append("Date: ").append(order.getOrderDate()).append("\n\n");

                // Thêm chi tiết các sản phẩm vào hóa đơn
                for (OrderResponse o : orders) {
                    invoiceContent.append("- ").append(o.toString()).append("\n");
                }

                invoiceContent.append("\n------------------------------------------------\n");
                invoiceContent.append("Total: $").append(order.getTotalPrice()).append("\n");
                invoiceContent.append("------------------------------------------------\n");

                // Tạo file TXT cho từng hóa đơn
                String fileName = "invoice_" + order.getId() + ".txt";
                File file = new File(fileName);

                // Ghi nội dung hóa đơn vào file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(invoiceContent.toString());
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return "Invoices generated successfully.";

        } catch (Exception e) {
            throw new RuntimeException(e.getCause().getMessage());
        }
    }

}
