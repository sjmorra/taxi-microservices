package com.taxi.pricing_service.dto;

public class OrderPricingRequest {
    private String customerName;
    private String vehicleType;
    private String rideDistance;

    public OrderPricingRequest() {
    }

    public OrderPricingRequest(String customerName, String vehicleType, String rideDistance) {
        this.customerName = customerName;
        this.vehicleType = vehicleType;
        this.rideDistance = rideDistance;
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
}
