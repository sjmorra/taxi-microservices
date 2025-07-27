package com.taxi.pricing_service.controller;

import com.taxi.pricing_service.dto.OrderPricingRequest;
import com.taxi.pricing_service.dto.PricingResponse;
import com.taxi.pricing_service.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class PricingControllerSimpleTest {

    private PricingController pricingController;
    private PricingService pricingService;
    private OrderPricingRequest pricingRequest;

    @BeforeEach
    void setUp(){
        pricingService = new PricingService();
        pricingController = new PricingController(pricingService);

        pricingRequest = new OrderPricingRequest();
        pricingRequest.setCustomerName("Test Customer");
        pricingRequest.setVehicleType("standard");
        pricingRequest.setRideDistance("local");
    }

    @Test
    void calculateOrderPrice_ShouldReturnOkResponse_WhenValidRequestProvided(){
        ResponseEntity<PricingResponse> response = pricingController.calculateOrderPrice(pricingRequest);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getTotalAmount() > 0);
    }

    @Test
    void healthCheck_ShouldReturnOkWithMessage(){
        ResponseEntity<String> response = pricingController.healthCheck();
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Pricing Service is running!", response.getBody());
    }

    @Test
    void calculateOrderPrice_ShouldHandleFarDistance(){
        pricingRequest.setRideDistance("far");
        ResponseEntity<PricingResponse> response = pricingController.calculateOrderPrice(pricingRequest);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getTotalAmount() > 24);
    }
}
