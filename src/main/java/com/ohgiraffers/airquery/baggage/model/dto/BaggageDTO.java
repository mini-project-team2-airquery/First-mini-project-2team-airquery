package com.ohgiraffers.airquery.baggage.model.dto;

public class BaggageDTO {
    private int baggageCode; // 수하물 번호를 저장하는 값
    private int reservationCode; // 이 수하물이 어떤 예매와 연결되어 있는지 나타내는 예매 번호
    private double baggageWeight; // 수하물 무게를 저장 하는 값

    // 값을 나중에 setter로 넣고 싶을 때 사용
    public BaggageDTO() {

    }

    // 모든 필드 값을 한번에 넣어서 객체를 만들 때 사용하는 생성자
    // 수하물 무게가 소수점이 들어갈 수 있으므로 double 사용
    public BaggageDTO(int baggageCode, int reservationCode, double baggageWeight) {
        this.baggageCode = baggageCode;
        this.reservationCode = reservationCode;
        this.baggageWeight = baggageWeight;
    }

    // baggageCode 값을 꺼내는 메서드
    public int getBaggageCode() {
        return baggageCode;
    }

    // baggageCode 값을 넣거나 수정 메서드
    public void setBaggageCode(int baggageCode) {
        this.baggageCode = baggageCode;
    }

    // reservationCode 값을 넣거나 수정하는 메서드
    public int getReservationCode() {

        return reservationCode;
    }

    // reservationCode 값을 넣거나 수정하는 메서드
    public void setReservationCode(int reservationCode) {

        this.reservationCode = reservationCode;
    }

    // baggageWeight 값을 꺼내는 메서드
    public double getBaggageWeight() {

        return baggageWeight;
    }

    // baggageWeight 값을 넣거나 수정 하는 메서드
    public void setBaggageWeight(double baggageWeight) {

        this.baggageWeight = baggageWeight;
    }

    // 객체 안에 들어있는 값을 출력해서 확인할 때 사용
    @Override
    public String toString() {
        return "BaggageDTO{" +
                "baggageCode=" + baggageCode +
                ", reservationCode=" + reservationCode +
                ", baggageWeight=" + baggageWeight +
                '}';
    }
}
