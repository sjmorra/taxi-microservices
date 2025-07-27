package com.taxi.order_service.dto;

public class OrderResponse {

    private Long orderId;
    private String customerName;
    private String vehicleType;
    private String rideDistance;
    private double totalPrice;
    private String status;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, String customerName, String vehicleType, String rideDistance, double totalPrice, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.vehicleType = vehicleType;
        this.rideDistance = rideDistance;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getRideDistance() {
        return rideDistance;
    }

    public void setRideDistance(String rideDistance) {
        this.rideDistance = rideDistance;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
