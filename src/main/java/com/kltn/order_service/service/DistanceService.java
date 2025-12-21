package com.kltn.order_service.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.order_service.dto.response.CoordinateReponse;
import com.kltn.order_service.dto.response.DistanceResponse;
import com.kltn.order_service.repository.CartItemRepository;
import com.kltn.order_service.service.impl.CartItemServiceImpl;

@Service
public class DistanceService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${distance.api.key}")
    private String apiKey;

    // private static final String DISTANCE_API_URL =
    // "https://api.distancematrix.ai/maps/api/distancematrix/json";

    public DistanceService(RestTemplate restTemplate, ObjectMapper objectMapper, CartItemRepository cartItemRepository,
            CartItemServiceImpl cartItemServiceImpl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public CoordinateReponse getCoordinates(String address) {
        try {
            String url = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("nominatim.openstreetmap.org")
                    .path("/search")
                    .queryParam("format", "json")
                    .queryParam("q", address)
                    .build()
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);

            JsonNode jsonNode = objectMapper.readTree(response);

            if (jsonNode.isArray() && jsonNode.size() > 0) {
                JsonNode firtResult = jsonNode.get(0);

                CoordinateReponse coordinateReponse = CoordinateReponse.builder()
                        .lat(firtResult.get("lat").asText())
                        .lon(firtResult.get("lon").asText())
                        .address(firtResult.get("display_name").asText())
                        .build();

                return coordinateReponse;

            } else {
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi", e);
        }
    }

    public DistanceResponse getDistance(String origin, String destination) {
        CoordinateReponse originCoordinate = getCoordinates(origin);
        CoordinateReponse destinationCoordinate = getCoordinates(destination);

        String originStr = originCoordinate.getLat() + "," + originCoordinate.getLon();
        String destinationStr = destinationCoordinate.getLat() + "," + destinationCoordinate.getLon();

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.distancematrix.ai")
                .path("/maps/api/distancematrix/json")
                .queryParam("origins", originStr)
                .queryParam("destinations", destinationStr)
                .queryParam("key", apiKey).toUriString();

        // restTemplate.getForObject sẽ gọi tới api bên ngoài và tra về dữ liệu
        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response);

            String des = StreamSupport.stream(jsonNode.get("destination_addresses").spliterator(), false)
                    .map(JsonNode::asText)
                    .collect(Collectors.joining(", "));

            String ori = StreamSupport.stream(jsonNode.get("origin_addresses").spliterator(), false)
                    .map(JsonNode::asText)
                    .collect(Collectors.joining(", "));

            DistanceResponse distanceResponse = DistanceResponse.builder()
                    .destinationAddresses(des)
                    .originAddresses(ori)
                    .distanse(jsonNode.get("rows").get(0).get("elements").get(0).get("distance").get("text")
                            .asText())
                    .duration(jsonNode.get("rows").get(0).get("elements").get(0).get("duration").get("text")
                            .asText())
                    .build();

            return distanceResponse;

        } catch (Exception e) {
            throw new RuntimeException("lỗi", e);
        }
    }
}
