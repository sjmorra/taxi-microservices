package com.taxi.order_service;

import com.taxi.order_service.dto.OrderRequest;
import com.taxi.order_service.dto.OrderResponse;
import com.taxi.order_service.dto.PricingResponse;
import com.taxi.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class SimpleOrderTest {

	@Autowired
	private OrderService orderService;

	@MockBean
	private RestTemplate restTemplate;

	@Test
	void createOrder_ShouldWork(){
		PricingResponse pricingResponse = new PricingResponse();
		pricingResponse.setTotalAmount(25.99);
		ResponseEntity<PricingResponse> responseEntity = ResponseEntity.ok(pricingResponse);

		when(restTemplate.postForEntity(anyString(), any(), eq(PricingResponse.class)))
				.thenReturn(responseEntity);

		OrderRequest request = new OrderRequest();
		request.setCustomerName("Test Customer");
		request.setVehicleType("XL");
		request.setRideDistance("Local");

		OrderResponse response = orderService.createOrder(request);

		assertNotNull(response);
		assertEquals("Test Customer", response.getCustomerName());
		assertEquals("XL", response.getVehicleType());
		assertEquals("Local", response.getRideDistance());
		assertEquals(25.99, response.getTotalPrice());
		assertEquals("CONFIRMED", response.getStatus());
	}

}
