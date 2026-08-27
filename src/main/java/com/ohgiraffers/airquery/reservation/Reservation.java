package com.ohgiraffers.airquery.reservation;

import java.time.LocalDateTime;

public class Reservation {

    private int reservationCode;
    private int memberCode;
    private int flightCode;
    private int searCode;
    private boolean baggageCarrying;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public int getSearCode() {
        return searCode;
    }

    public void setSearCode(int searCode) {
        this.searCode = searCode;
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
}
