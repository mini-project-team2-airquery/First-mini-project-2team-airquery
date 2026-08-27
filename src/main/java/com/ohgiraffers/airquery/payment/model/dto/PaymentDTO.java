package com.ohgiraffers.airquery.payment.model.dto;

import java.time.LocalDateTime;

public class PaymentDTO {

    private int paymentCode;            // 결제번호
    private int reservationCode;        // 예매번호
    private int paymentAmount;          // 결제금액
    private String paymentMethod;       // 결제수단
    private boolean refundStatus;       // 환불여부(true/false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentDTO() {
        refundStatus = false;   // 환불여부의 default value
    }

    public int getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(int paymentCode) {
        this.paymentCode = paymentCode;
    }

    public int getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(int reservationCode) {
        this.reservationCode = reservationCode;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(boolean refundStatus) {
        this.refundStatus = refundStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
