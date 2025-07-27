package com.taxi.pricing_service.controller;

import com.taxi.pricing_service.dto.OrderPricingRequest;
import com.taxi.pricing_service.dto.PricingResponse;
import com.taxi.pricing_service.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/*
 * LESSON NOTE: Simplified Pricing Controller
 * - Focuses on core pricing calculation functionality
 * - Demonstrates microservice communication pattern
 * - Similar to "calculate fare" functionality in capstone
 */
@RestController
@RequestMapping("/api/pricing")
@Validated
public class PricingController {

    private final PricingService pricingService;

    @Autowired
    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<PricingResponse> calculateOrderPrice(@Valid @RequestBody OrderPricingRequest request) {
        try{
            PricingResponse response = pricingService.calculateOrderPrice(request);
            return ResponseEntity.ok(response);
        } catch (Exception e){
            System.out.println("Error calculating price: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Pricing Service is running!");
    }
}
