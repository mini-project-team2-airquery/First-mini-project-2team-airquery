package com.ohgiraffers.airquery.reservation.model.dto;

import java.time.LocalDateTime;

public class ReservationDTO {

    private int reservationCode;        // 예매번호
    private int memberCode;             // 회원번호
    private int flightCode;             // 항공편번호
    private int seatCode;               // 좌석번호
    private boolean baggageCarrying;    // 수하물지참여부
    private LocalDateTime createdAt;    // 최초생성일
    private LocalDateTime updatedAt;    // 최종수정일

    public int getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(int reservationCode) {
        this.reservationCode = reservationCode;
    }

    public int getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(int memberCode) {
        this.memberCode = memberCode;
    }

    public int getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(int flightCode) {
        this.flightCode = flightCode;
    }

    public int getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(int seatCode) {
        this.seatCode = seatCode;
    }

    public boolean isBaggageCarrying() {
        return baggageCarrying;
    }

    public void setBaggageCarrying(boolean baggageCarrying) {
        this.baggageCarrying = baggageCarrying;
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

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationCode=" + reservationCode +
                ", memberCode=" + memberCode +
                ", flightCode=" + flightCode +
                ", seatCode=" + seatCode +
                ", baggageCarrying=" + baggageCarrying +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
