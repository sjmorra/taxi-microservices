package com.taxi.pricing_service.service;

import com.taxi.pricing_service.dto.OrderPricingRequest;
import com.taxi.pricing_service.dto.PricingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PricingServiceTest {
    @InjectMocks
    private PricingService pricingService;

    private OrderPricingRequest pricingRequest;

    @BeforeEach
    public void setup() {
        pricingRequest = new OrderPricingRequest();
        pricingRequest.setCustomerName("Test Customer");
        pricingRequest.setVehicleType("standard");
        pricingRequest.setRideDistance("local");
    }

    @Test
    void calculateOrderPrice_ShouldReturnBasicPrice_WhenSimpleOrder(){
        pricingRequest.setVehicleType("standard");
        pricingRequest.setRideDistance("local");

        PricingResponse result = pricingService.calculateOrderPrice(pricingRequest);

        assertNotNull(result);
        double expectedBasePrice = 12.0;
        double expectedVehicleTypeFactor = 1.0;
        double expectedRideDistanceFactor = 1.0;
        double expectedAdjustedPrice = expectedBasePrice * expectedVehicleTypeFactor * expectedRideDistanceFactor;
        double expectedTax = expectedAdjustedPrice * 0.08;
        double expectedTotal = expectedAdjustedPrice + expectedTax;

        assertEquals(expectedTotal, result.getTotalAmount(), 0.01);
        assertTrue(result.getTotalAmount() > 0);
    }

    @Test
    void calculateOrderPrice_ShouldReturnHigherPrice_WhenVehicleTypeXl(){
        pricingRequest.setVehicleType("xl");
        pricingRequest.setRideDistance("local");

        PricingResponse result = pricingService.calculateOrderPrice(pricingRequest);

        assertNotNull(result);
        double expectedBasePrice = 12.0;
        double expectedVehicleTypeFactor = 1.5;
        double expectedRideDistanceFactor = 1.0;
        double expectedAdjustedPrice = expectedBasePrice * expectedVehicleTypeFactor * expectedRideDistanceFactor;
        double expectedTax = expectedAdjustedPrice * 0.08;
        double expectedTotal = expectedAdjustedPrice + expectedTax;

        assertEquals(expectedTotal, result.getTotalAmount(), 0.01);
        assertTrue(result.getTotalAmount() > 18);
    }

    @Test
    void calculateOrderPrice_ShouldHandleNullVehicleType_WhenNoTypeOffered(){
        pricingRequest.setVehicleType(null);
        pricingRequest.setRideDistance("local");
        PricingResponse result = pricingService.calculateOrderPrice(pricingRequest);

        assertNotNull(result);
        double expectedBasePrice = 12.0;
        double expectedVehicleTypeFactor = 1.0;
        double expectedRideDistanceFactor = 1.0;
        double expectedAdjustedPrice = expectedBasePrice * expectedVehicleTypeFactor * expectedRideDistanceFactor;
        double expectedTax = expectedAdjustedPrice * 0.08;
        double expectedTotal = expectedAdjustedPrice + expectedTax;

        assertEquals(expectedTotal, result.getTotalAmount(), 0.01);
        assertEquals(12.96, result.getTotalAmount(), 0.01);
    }
}
