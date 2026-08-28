package com.ohgiraffers.airquery.airline.dto;

import java.sql.Timestamp;

public class AirlineDTO {

    private int airlineCode;
    private String airlineName;
    private String customerServiceNumber;
    private Timestamp firstCreatedDate;
    private Timestamp lastModifiedDate;

    public AirlineDTO() {
    }

    public AirlineDTO(int airlineCode,
                      String airlineName,
                      String customerServiceNumber,
                      Timestamp firstCreatedDate,
                      Timestamp lastModifiedDate) {
        this.airlineCode = airlineCode;
        this.airlineName = airlineName;
        this.customerServiceNumber = customerServiceNumber;
        this.firstCreatedDate = firstCreatedDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public AirlineDTO(String airlineName, String customerServiceNumber) {
        this.airlineName = airlineName;
        this.customerServiceNumber = customerServiceNumber;
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

    public Timestamp getFirstCreatedDate() {
        return firstCreatedDate;
    }

    public void setFirstCreatedDate(Timestamp firstCreatedDate) {
        this.firstCreatedDate = firstCreatedDate;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
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