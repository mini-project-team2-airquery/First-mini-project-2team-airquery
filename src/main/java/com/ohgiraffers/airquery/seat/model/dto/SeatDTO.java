package com.ohgiraffers.airquery.seat.model.dto;

public class SeatDTO {

    private int seatCode; // 좌석번호, PK
    private int flightCode; // 항공편번호, FK
    private String seatId; // 좌석식별번호
    private String flightClass; // 좌석등급 -> 이코노미, 비지니스, 퍼스트
    private int additionalAmount; // 추가금액 / 기본값은 0으로 나타냄
    private boolean reserved; // 좌석 선점여부, 예약되었는지 확인하는 값
    private int reservedMemberCode; // 프로그램 실행 중 이 좌석을 예약한 회원번호
    private String reservedMemberName; // 프로그램 실행 중 이 좌석을 예약한 회원 이름


    public SeatDTO() {

    }

    // seatCode 값을 꺼내는 메서드
    public int getSeatCode() {
        return seatCode;
    }

    // seatCode 값을 넣거나 수정 메서드
    public void setSeatCode(int seatCode) {
        this.seatCode = seatCode;
    }

    // fightCode 값을 꺼내는 메서드
    public int getFlightCode() {
        return flightCode;
    }

    // flightCode 값을 넣거나 수정하는 메서드
    public void setFlightCode(int flightCode) {
        this.flightCode = flightCode;
    }

    // seatId 값을 꺼내는 메서드
    public String getSeatId() {
        return seatId;
    }

    // seatId 값을 넣거나 수정하는 메서드
    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    // flightClass 값을 꺼내는 메서드
    public String getFlightClass() {
        return flightClass;
    }

    // flightClass 값을 넣거나 수정하는 메서드
    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    // additionalAmount 값을 꺼내는 메서드
    public int getAdditionalAmount() {
        return additionalAmount;
    }

    // additionalAmount 값을 넣거나 수정하는 메서드
    public void setAdditionalAmount(int additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    // reserved 값을 꺼내는 메서드
    // 좌석이 예약되었는지 아닌지를 의마함
    public boolean isReserved() {
        return reserved;
    }

    // reserved 값을 꺼내는 메서드
    // true를 넣으면 예약됨
    // false 넣으면 예약이 안됨
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public int getReservedMemberCode() {
        return reservedMemberCode;
    }

    public void setReservedMemberCode(int reservedMemberCode) {
        this.reservedMemberCode = reservedMemberCode;
    }

    public String getReservedMemberName() {
        return reservedMemberName;
    }

    public void setReservedMemberName(String reservedMemberName) {
        this.reservedMemberName = reservedMemberName;
    }

    @Override
    public String toString() {
        return "SeatDTO{" +
                "seatCode=" + seatCode +
                ", flightCode=" + flightCode +
                ", seatId='" + seatId + '\''+
                ", flightClass='" + flightClass + '\'' +
                ", additionalAmount=" + additionalAmount +
                ", reserved=" + reserved +
                ", reservedMemberCode=" + reservedMemberCode +
                ", reservedMemberName='" + reservedMemberName + '\'' +
                '}';
    }
}
