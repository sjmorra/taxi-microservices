package com.taxi.order_service.service;

import com.taxi.order_service.dto.OrderRequest;
import com.taxi.order_service.dto.OrderResponse;
import com.taxi.order_service.dto.OrderPricingRequest;
import com.taxi.order_service.dto.PricingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest orderRequest;
    private PricingResponse pricingResponse;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(orderService, "pricingServiceUrl", "http://localhost:8081/api/pricing/calculate");

        orderRequest = new OrderRequest();
        orderRequest.setCustomerName("John Doe");
        orderRequest.setVehicleType("XL");
        orderRequest.setRideDistance("Local");

        pricingResponse = new PricingResponse();
        pricingResponse.setTotalAmount(45.50);
    }

    @Test
    void createOrder_ShouldReturnOrderWithCalculatedPrice_WhenPricingServiceReturnsPrice(){
        ResponseEntity<PricingResponse> responseEntity = new ResponseEntity<>(pricingResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(PricingResponse.class)))
                .thenReturn(responseEntity);

        OrderResponse result = orderService.createOrder(orderRequest);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals("XL", result.getVehicleType());
        assertEquals("Local", result.getRideDistance());
        assertEquals(45.50, result.getTotalPrice());
        assertEquals("CONFIRMED", result.getStatus());

        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(PricingResponse.class));
    }

    @Test
    void createOrder_ShouldReturnOrderWithZeroPrice_WhenPricingServiceThrowsException(){
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(PricingResponse.class)))
                .thenThrow(new RestClientException("Service Unavailable"));

        OrderResponse result = orderService.createOrder(orderRequest);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals("XL", result.getVehicleType());
        assertEquals("Local", result.getRideDistance());
        assertEquals(0.0, result.getTotalPrice());
        assertEquals("CONFIRMED", result.getStatus());

        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(PricingResponse.class));
    }

    @Test
    void createOrder_ShouldGenerateUniqueOrderIds_WhenMultipleOrdersCreated(){
        ResponseEntity<PricingResponse> responseEntity = new ResponseEntity<>(pricingResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(PricingResponse.class)))
                .thenReturn(responseEntity);

        OrderResponse firstOrder = orderService.createOrder(orderRequest);
        OrderResponse secondOrder = orderService.createOrder(orderRequest);

        assertNotNull(firstOrder);
        assertNotNull(secondOrder);
        assertNotEquals(firstOrder.getOrderId(), secondOrder.getOrderId());
        assertEquals(1L, firstOrder.getOrderId());
        assertEquals(2L, secondOrder.getOrderId());

        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(PricingResponse.class));
    }
}
