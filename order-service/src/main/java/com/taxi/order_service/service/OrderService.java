package com.taxi.order_service.service;

import com.taxi.order_service.dto.OrderRequest;
import com.taxi.order_service.dto.OrderResponse;
import com.taxi.order_service.dto.OrderPricingRequest;
import com.taxi.order_service.dto.PricingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final RestTemplate restTemplate;
    private final Map<Long, OrderResponse> orders = new ConcurrentHashMap<>();
    private final AtomicLong orderIdCounter = new AtomicLong(1);

    @Value("http://pricing-service/api/pricing/calculate")
    private String pricingServiceUrl;

    @Autowired
    public OrderService(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

    public OrderResponse createOrder(OrderRequest request) {
        Long orderId = orderIdCounter.getAndIncrement();

        OrderPricingRequest pricingRequest = new OrderPricingRequest();
        pricingRequest.setCustomerName(request.getCustomerName());
        pricingRequest.setVehicleType(request.getVehicleType());
        pricingRequest.setRideDistance(request.getRideDistance());

        PricingResponse pricingResponse = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<OrderPricingRequest> entity = new HttpEntity<>(pricingRequest, headers);
            ResponseEntity<PricingResponse> response = restTemplate.postForEntity(pricingServiceUrl, entity, PricingResponse.class);
            pricingResponse = response.getBody();
        } catch (RestClientException e) {
            System.out.println("Pricing service unavailable" + e.getMessage());
        }

        String status = pricingResponse != null ? "CONFIRMED" : "PRICING_PENDING";
        double totalAmount = pricingResponse != null ? pricingResponse.getTotalAmount() : 0.0;

        OrderResponse orderResponse = new OrderResponse(
                orderId,
                request.getCustomerName(),
                request.getVehicleType(),
                request.getRideDistance(),
                totalAmount,
                status
        );

        orders.put(orderId, orderResponse);
        return orderResponse;
    }
}
