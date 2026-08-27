package com.ohgiraffers.airquery.airline.dto;

import java.time.LocalDateTime;

public class AirlineDTO {

    private int airlineCode;
    private String airlineName;
    private String customerServiceNumber;
    private LocalDateTime firstCreatedDate;
    private LocalDateTime lastModifiedDate;

    public AirlineDTO() {
    }

    public AirlineDTO(int airlineCode,
                      String airlineName,
                      String customerServiceNumber,
                      LocalDateTime firstCreatedDate,
                      LocalDateTime lastModifiedDate) {

        this.airlineCode = airlineCode;
        this.airlineName = airlineName;
        this.customerServiceNumber = customerServiceNumber;
        this.firstCreatedDate = firstCreatedDate;
        this.lastModifiedDate = lastModifiedDate;
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

    public String getCustomerServiceNumber() {
        return customerServiceNumber;
    }

    public void setCustomerServiceNumber(String customerServiceNumber) {
        this.customerServiceNumber = customerServiceNumber;
    }

    public LocalDateTime getFirstCreatedDate() {
        return firstCreatedDate;
    }

    public void setFirstCreatedDate(LocalDateTime firstCreatedDate) {
        this.firstCreatedDate = firstCreatedDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "AirlineDTO{" +
                "airlineCode=" + airlineCode +
                ", airlineName='" + airlineName + '\'' +
                ", customerServiceNumber='" + customerServiceNumber + '\'' +
                ", firstCreatedDate=" + firstCreatedDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}