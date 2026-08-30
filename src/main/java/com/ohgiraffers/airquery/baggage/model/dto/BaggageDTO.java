package com.ohgiraffers.airquery.baggage.model.dto;

// DB에서 가져온 수하물 데이터나 사용자가 입력한 수하물 데이터를
public class BaggageDTO {


    private int baggageCode; // 수하물번호, PK, tbl_baggage의 baggage_code
    private int reservationCode; // 예매번호, FK, tbl_reservation의 reservation_code와 연결됨
    private double baggageWeight; // 수하물무게, kg 단위, tbl_baggage의 baggage_weight
    private int memberCode; // 수하물을 등록한 예매의 회원번호
    private String memberName; // 수하물을 등록한 회원 이름
    private boolean baggageCarrying; // 예매 시 선택한 수하물 지참 여부


    // 값을 나중에 setter로 넣고 싶을 때 사용
    public BaggageDTO() {

    }

    // 수하물번호가 몇 번인지 확인할 때 사용
    public int getBaggageCode() {
        return baggageCode;
    }

    // baggageCode 값을 넣거나 수정하는 메서드
    public void setBaggageCode(int baggageCode) {
        this.baggageCode = baggageCode;
    }

    // reservationCode 값을 꺼내는 메서드, 이 수하물이 어떤 예매와 연결되었는지 확인할 때 사용
    public int getReservationCode() {

        return reservationCode;
    }

    // reservationCode 값을 넣거나 수정하는 메서드, 수하물 등록 시 사용자가 입력한 예매번호를 DTO에 담을 때 사용
    public void setReservationCode(int reservationCode) {

        this.reservationCode = reservationCode;
    }

    // baggageWeight 값을 꺼내는 메서드, 수하물 무게를 화면에 출력할 때 사용
    public double getBaggageWeight() {

        return baggageWeight;
    }

    // baggageWeight 값을 넣거나 수정하는 메서드, 수하물 등록 또는 무게 변경 시 사용
    public void setBaggageWeight(double baggageWeight) {

        this.baggageWeight = baggageWeight;
    }

    // 이 수하물을 등록한 회원의 회원번호를 꺼낼 때 사용한다.
    public int getMemberCode() {
        return memberCode;
    }

    // DB에서 조회한 회원번호를 BaggageDTO의 memberCode에 저장할 때 사용한다.
    public void setMemberCode(int memberCode) {
        this.memberCode = memberCode;
    }

    // 이 수하물을 등록한 회원의 이름을 꺼낼 때 사용한다.
    public String getMemberName() {
        return memberName;
    }

    // DB에서 조회한 회원 이름을 BaggageDTO의 memberName에 저장할 때 사용한다.
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    // 예매할 때 수하물 지참 여부를 YES로 선택했는지 확인할 때 사용한다.
    // true이면 YES, false이면 NO를 뜻한다.
    public boolean isBaggageCarrying() {
        return baggageCarrying;
    }

    // DB에서 조회한 수하물 지참 여부를 BaggageDTO에 저장할 때 사용한다.
    public void setBaggageCarrying(boolean baggageCarrying) {
        this.baggageCarrying = baggageCarrying;
    }

    @Override
    public String toString() {
        return "BaggageDTO{" +
                "baggageCode=" + baggageCode +
                ", reservationCode=" + reservationCode +
                ", baggageWeight=" + baggageWeight +
                ", memberCode=" + memberCode +
                ", memberName='" + memberName + '\'' +
                ", baggageCarrying=" + baggageCarrying +
                '}';

    }
}
