package com.taxi.pricing_service.service;

import com.taxi.pricing_service.dto.OrderPricingRequest;
import com.taxi.pricing_service.dto.PricingResponse;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

    private static final double BASE_PRICE = 12.0;
    private static final double TAX_RATE = 0.08;

    public PricingResponse calculateOrderPrice(OrderPricingRequest request) {
        double basePrice = BASE_PRICE;

        double vehicleTypeFactor = calculateVehicleTypeFactor(request.getVehicleType());
        double rideDistanceFactor = calculateRideDistanceFactor(request.getRideDistance());
        double adjustedPrice = basePrice * vehicleTypeFactor * rideDistanceFactor;

        double tax = adjustedPrice * TAX_RATE;

        double totalAmount = adjustedPrice + tax;

        return new PricingResponse(totalAmount);
    }

    private double calculateVehicleTypeFactor(String vehicleType) {
        if (vehicleType == null || vehicleType.isEmpty()) {
            return 1.0;
        }

        String lowerType = vehicleType.toLowerCase();
        double vehicleTypeFactor = 1.0;

        if (lowerType.contains("standard")){
            vehicleTypeFactor += 0.0;
        }
        if (lowerType.contains("xl")){
            vehicleTypeFactor += 0.5;
        }
        if (lowerType.contains("luxury")){
            vehicleTypeFactor += 1.0;
        }
        if (lowerType.contains("cheap")){
            vehicleTypeFactor -= 0.5;
        }
        return Math.max(0.5, Math.min(vehicleTypeFactor, 2.5));
    }

    private double calculateRideDistanceFactor(String rideDistance) {
        if (rideDistance == null || rideDistance.isEmpty()) {
            return 1.0;
        }

        String lowerType = rideDistance.toLowerCase();
        double rideDistanceFactor = 1.0;

        if (lowerType.contains("local")){
            rideDistanceFactor += 0.0;
        }
        if (lowerType.contains("far")){
            rideDistanceFactor += 1.0;
        }

        return Math.max(1.0, Math.min(rideDistanceFactor, 2.0));
    }
}
