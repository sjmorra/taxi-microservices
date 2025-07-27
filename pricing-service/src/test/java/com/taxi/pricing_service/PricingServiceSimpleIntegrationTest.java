package com.taxi.pricing_service;

import com.taxi.pricing_service.controller.PricingController;
import com.taxi.pricing_service.dto.OrderPricingRequest;
import com.taxi.pricing_service.dto.PricingResponse;
import com.taxi.pricing_service.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;


class PricingServiceSimpleIntegrationTest {

	private PricingController pricingController;
	private PricingService pricingService;

	@BeforeEach
	void setUp() {
		pricingService = new PricingService();
		pricingController = new PricingController(pricingService);
	}

	@Test
	void endToEndPricingFlow_ShouldWorkCorrectly(){
		OrderPricingRequest request = new OrderPricingRequest();
		request.setCustomerName("Integration Customer");
		request.setVehicleType("standard");
		request.setRideDistance("local");

		ResponseEntity<PricingResponse> response = pricingController.calculateOrderPrice(request);

		assertEquals(200, response.getStatusCodeValue());
		assertNotNull(response.getBody());

		double totalAmount = response.getBody().getTotalAmount();
		assertTrue(totalAmount > 0, "Total amount should be positive");
		assertNotNull(response.getBody());

		double expectedBasePrice = 12.0;
		assertTrue(totalAmount > expectedBasePrice, "Total price should be greater than base price");
	}

	@Test
	void pricingCalculation_ShouldHaveVariousScenarios(){
		OrderPricingRequest basicRequest = new OrderPricingRequest();
		basicRequest.setCustomerName("Basic Customer");
		basicRequest.setVehicleType("standard");
		basicRequest.setRideDistance("local");

		OrderPricingRequest premiumRequest = new OrderPricingRequest();
		premiumRequest.setCustomerName("Premium Customer");
		premiumRequest.setVehicleType("xl");
		premiumRequest.setRideDistance("far");

		ResponseEntity<PricingResponse> basicResponse = pricingController.calculateOrderPrice(basicRequest);
		ResponseEntity<PricingResponse> premiumResponse = pricingController.calculateOrderPrice(premiumRequest);

		assertNotNull(basicResponse.getBody());
		assertNotNull(premiumResponse.getBody());

		assertTrue(premiumResponse.getBody().getTotalAmount() > basicResponse.getBody().getTotalAmount(),
				"Premium add-ons should cost more than a basic ride");

	}

	@Test
	void serviceHealthCheck_ShouldReturnPositiveStatus(){
		ResponseEntity<String> healthResponse = pricingController.healthCheck();

		assertEquals(200, healthResponse.getStatusCodeValue());
		assertEquals("Pricing Service is running!",  healthResponse.getBody());
	}
}
