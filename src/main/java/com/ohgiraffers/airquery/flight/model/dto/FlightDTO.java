package com.ohgiraffers.airquery.flight.model.dto;

import java.time.LocalDateTime;

public class FlightDTO {

    private int code;
    private int airlineCode;
    private String airlineName;
    private String departure;
    private String arrival;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String airplaneType;
    private String gateNumber;
    private int ticketPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FlightDTO() {
    }

    public FlightDTO(int code, int airlineCode, String airlineName, String departure, String arrival,
                     LocalDateTime departureTime, LocalDateTime arrivalTime, String airplaneType, String gateNumber,
                     int ticketPrice, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.code = code;
        this.airlineCode = airlineCode;
        this.airlineName = airlineName;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.airplaneType = airplaneType;
        this.gateNumber = gateNumber;
        this.ticketPrice = ticketPrice;
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

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
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

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
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
                ", airlineName='" + airlineName + '\'' +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", airplaneType='" + airplaneType + '\'' +
                ", gateNumber='" + gateNumber + '\'' +
                ", ticket_price=" + ticketPrice +
                ", firstCreatedDate=" + createdAt +
                ", lastModifiedDate=" + updatedAt +
                '}';
    }
}
