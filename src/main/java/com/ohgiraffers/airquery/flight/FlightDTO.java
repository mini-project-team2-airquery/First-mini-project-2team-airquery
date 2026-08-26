package com.ohgiraffers.airquery.flight;

import java.time.LocalDateTime;

public class FlightDTO {

    private int code;
    private int airlineCode;
    private String departure;
    private String arrival;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String airplaneType;
    private String gateNumber;
    private int ticket_price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FlightDTO() {
    }

    public FlightDTO(int code, int airlineCode, String departure, String arrival,
                     LocalDateTime departureTime, LocalDateTime arrivalTime, String airplaneType, String gateNumber,
                     int ticket_price, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.code = code;
        this.airlineCode = airlineCode;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.airplaneType = airplaneType;
        this.gateNumber = gateNumber;
        this.ticket_price = ticket_price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(int airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getAirplaneType() {
        return airplaneType;
    }

    public void setAirplaneType(String airplaneType) {
        this.airplaneType = airplaneType;
    }

    public String getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(String gateNumber) {
        this.gateNumber = gateNumber;
    }

    public int getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(int ticket_price) {
        this.ticket_price = ticket_price;
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
        return "FlightDTO{" +
                "code=" + code +
                ", airlineCode=" + airlineCode +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", airplaneType='" + airplaneType + '\'' +
                ", gateNumber='" + gateNumber + '\'' +
                ", ticket_price=" + ticket_price +
                ", firstCreatedDate=" + createdAt +
                ", lastModifiedDate=" + updatedAt +
                '}';
    }
}
