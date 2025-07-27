package com.taxi.order_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxi.order_service.dto.OrderRequest;
import com.taxi.order_service.dto.OrderResponse;
import com.taxi.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest();
        orderRequest.setCustomerName("Jane Smith");
        orderRequest.setVehicleType("XL");
        orderRequest.setRideDistance("Local");

        orderResponse = new OrderResponse(1L, "Jane Smith", "XL", "Local", 55.75, "CONFIRMED");
    }

    @Test
    void createOrder_ShouldReturnOrderResponse_WhenValidRequestProvided() throws Exception {
        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.customerName").value("Jane Smith"))
                .andExpect(jsonPath("$.vehicleType").value("XL"))
                .andExpect(jsonPath("$.rideDistance").value("Local"))
                .andExpect(jsonPath("$.totalPrice").value(55.75))
                .andExpect(jsonPath("$.orderStatus").value("CONFIRMED"));
    }

    @Test
    void createOrder_ShouldReturnBadRequest_WhenInvalidRequestProvided() throws Exception {
        OrderRequest invalidRequest = new OrderRequest();

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

}
