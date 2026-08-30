package com.ohgiraffers.airquery.seat.model.dto;

/*
 * tbl_seat 테이블의 좌석 한 행을 Java 객체로 옮겨 담는 클래스이다.
 * DAO가 조회한 값을 setter로 저장하고, Menu/View가 getter로 꺼내 사용한다.
 * DTO는 데이터를 보관만 하며 SQL 실행이나 화면 출력은 하지 않는다.
 */
public class SeatDTO {

    private int seatCode;          // seat_code: 좌석 데이터의 고유번호(PK)
    private int flightCode;        // flight_code: 좌석이 속한 항공편번호(FK)
    private String seatId;         // seat_id: 승객이 보는 좌석명(예: 1A, 10B)
    private String flightClass;    // flight_class: ECONOMY, BUSINESS, FIRST
    private int additionalAmount;  // additional_amount: 좌석등급 추가금액
    private boolean reserved;      // is_reserved: true면 예약됨, false면 예약 가능
    private int reservedMemberCode; // 프로그램 실행 중 이 좌석을 예약한 회원번호(0이면 확인 불가)
    private String reservedMemberName; // 프로그램 실행 중 이 좌석을 예약한 회원 이름


    // DAO가 빈 객체를 만든 후 setter로 값을 하나씩 넣을 때 사용한다.
    public SeatDTO() {

    }

    // 좌석의 모든 값을 한 번에 전달해 객체를 만들 때 사용한다.
    public SeatDTO(int seatCode, int flightCode, String seatId, String flightClass,
                   int additionalAmount, boolean reserved) {

        this.seatCode = seatCode;
        this.flightCode = flightCode;
        this.seatId = seatId;
        this.flightClass = flightClass;
        this.additionalAmount = additionalAmount;
        this.reserved = reserved;
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
