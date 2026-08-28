package com.ohgiraffers.airquery.baggage.model.dto;

/*
 * DTO(Data Transfer Object)
 * DB에서 가져온 수하물 데이터나 사용자가 입력한 수하물 데이터를
 * 자바 객체 하나에 담아서 다른 클래스에 전달할 때 사용한다.
 */
public class BaggageDTO {

    private int baggageCode; // 수하물번호, PK, tbl_baggage의 baggage_code
    private int reservationCode; // 예매번호, FK, tbl_reservation의 reservation_code와 연결됨
    private double baggageWeight; // 수하물무게, kg 단위, tbl_baggage의 baggage_weight

    // 값을 나중에 setter로 넣고 싶을 때 사용
    public BaggageDTO() {

    }

    // 모든 필드 값을 한번에 넣어서 객체를 만들 때 사용하는 생성자
    public BaggageDTO(int baggageCode, int reservationCode, double baggageWeight) {
        this.baggageCode = baggageCode;
        this.reservationCode = reservationCode;
        this.baggageWeight = baggageWeight;
    }

    // baggageCode 값을 꺼내는 메서드
    // 예: 수하물번호가 몇 번인지 확인할 때 사용
    public int getBaggageCode() {
        return baggageCode;
    }

    // baggageCode 값을 넣거나 수정하는 메서드
    // 예: DB에서 조회한 baggage_code 값을 DTO에 담을 때 사용
    public void setBaggageCode(int baggageCode) {
        this.baggageCode = baggageCode;
    }

    // reservationCode 값을 꺼내는 메서드
    // 예: 이 수하물이 어떤 예매와 연결되었는지 확인할 때 사용
    public int getReservationCode() {

        return reservationCode;
    }

    // reservationCode 값을 넣거나 수정하는 메서드
    // 예: 수하물 등록 시 사용자가 입력한 예매번호를 DTO에 담을 때 사용
    public void setReservationCode(int reservationCode) {

        this.reservationCode = reservationCode;
    }

    // baggageWeight 값을 꺼내는 메서드
    // 예: 수하물 무게를 화면에 출력할 때 사용
    public double getBaggageWeight() {

        return baggageWeight;
    }

    // baggageWeight 값을 넣거나 수정하는 메서드
    // 예: 수하물 등록 또는 무게 변경 시 사용
    public void setBaggageWeight(double baggageWeight) {

        this.baggageWeight = baggageWeight;
    }

    // 객체 안에 들어있는 값을 출력해서 확인할 때 사용
    // 예: System.out.println(baggage); 로 DTO 값을 확인할 수 있음
    @Override
    public String toString() {
        return "BaggageDTO{" +
                "baggageCode=" + baggageCode +
                ", reservationCode=" + reservationCode +
                ", baggageWeight=" + baggageWeight +
                '}';
    }
}
