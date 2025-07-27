package com.taxi.pricing_service.dto;

public class PricingResponse {

    private Double totalAmount;

    public PricingResponse() {
    }

    public PricingResponse(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "PricingResponse{" +
                "totalAmount=" + totalAmount +
                '}';
    }
}
