package com.ohgiraffers.airquery.seat.view;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.util.List;

public class SeatView {

    public void displaySeatMenu() {
        System.out.println();
        System.out.println("===== 좌석 메뉴 =====");
        System.out.println("1. 좌석 전체 조회");
        System.out.println("2. 좌석 예약");
        System.out.println("9. 메인 메뉴로 돌아가기");
        System.out.print("메뉴 선택 : ");
    }

    public void displaySeatList(List<SeatDTO> seatList) {
        if (seatList.isEmpty()) {
            System.out.println("조회된 좌석이 없습니다.");
            return;
        }

        System.out.println();
        System.out.println("===================== 좌석 전체 조회 =====================");

        for (SeatDTO seat : seatList) {
            System.out.println("좌석번호 : " + seat.getSeatCode());
            System.out.println("항공편번호 : " + seat.getFlightCode());
            System.out.println("좌석식별번호 : " + seat.getSeatId());
            System.out.println("좌석등급 : " + changeFlightClassToKorean(seat.getFlightClass()));
            System.out.println("추가금액 : " + seat.getAdditionalAmount() + "원");
            System.out.println("예약여부 : " + changeReservedToKorean(seat.isReserved()));
            System.out.println("--------------------------------------------------------");
        }
    }

    public void displayAvailableSeatList(List<SeatDTO> seatList) {
        if (seatList.isEmpty()) {
            System.out.println("예약 가능한 좌석이 없습니다.");
            return;
        }

        System.out.println();
        System.out.println("===================== 예약 가능 좌석 조회 =====================");

        for (SeatDTO seat : seatList) {
            System.out.println("좌석번호 : " + seat.getSeatCode());
            System.out.println("항공편번호 : " + seat.getFlightCode());
            System.out.println("좌석식별번호 : " + seat.getSeatId());
            System.out.println("좌석등급 : " + changeFlightClassToKorean(seat.getFlightClass()));
            System.out.println("추가금액 : " + seat.getAdditionalAmount() + "원");
            System.out.println("예약여부 : " + changeReservedToKorean(seat.isReserved()));
            System.out.println("--------------------------------------------------------");
        }
    }

    public void displayAfterSelectAllSeatsMenu() {
        System.out.println();
        System.out.println("1. 좌석 메뉴로 돌아가기");
        System.out.println("2. 예약 가능 좌석만 보기");
        System.out.print("메뉴 선택 : ");
    }

    public void displayInvalidAfterSelectAllSeatsMenuMessage() {
        System.out.println("잘못 입력했습니다. 1번 또는 2번을 입력해주세요.");
    }

    private String changeFlightClassToKorean(String flightClass) {
        if (flightClass == null) {
            return "";
        }

        switch (flightClass) {
            case "FIRST":
                return "퍼스트";
            case "BUSINESS":
                return "비즈니스";
            case "ECONOMY":
                return "이코노미";
            default:
                return flightClass;
        }
    }

    private String changeReservedToKorean(boolean reserved) {
        if (reserved) {
            return "예약됨";
        }

        return "예약 가능";
    }

    public void displayBackToSeatMenuMessage() {
        System.out.println();
        System.out.println(" 되돌아가실려면 1번을 입력하세요.");
        System.out.print("번호 입력 : ");
    }

    public void displayInvalidBackInputMessage() {
        System.out.println("1번만 입력해주세요.");
    }

    public void displayInputSeatCodeMessage() {
        System.out.print("예약할 좌석번호를 입력하세요 : ");
    }

    public void displaySeatCodeNumberOnlyMessage() {
        System.out.println("좌석번호는 숫자만 입력해야 합니다.");
    }

    public void displayReserveSeatResult(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("좌석 예약이 완료되었습니다.");
        } else {
            System.out.println("좌석 예약에 실패했습니다. 이미 예약된 좌석이거나 없는 좌석번호입니다.");
        }
    }

    public void displayInputUpdateSeatCodeMessage() {
        System.out.print("변경할 좌석번호를 입력하세요 : ");
    }

    public void displayInputFlightCodeMessage() {
        System.out.print("항공편번호를 입력하세요 : ");
    }

    public void displayInputSeatIdMessage() {
        System.out.print("좌석식별번호를 입력하세요 : ");
    }

    public void displayInputFlightClassMessage() {
        System.out.print("좌석등급을 입력하세요 : ");
    }

    public void displayInputAdditionalAmountMessage() {
        System.out.print("추가금액을 입력하세요 : ");
    }

    public void displayInputReservedMessage() {
        System.out.print("예약여부를 입력하세요(true/false) : ");
    }

    public void displayNumberOnlyMessage() { System.out.println("숫자만 입력해야 합니다."); }

    public void displayBooleanOnlyMessage() {
        System.out.println("예약여부는 true 또는 false만 입력해야 합니다.");
    }

    public void displayInvalidMenuMessage() {
        System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
    }

    public void displayNeedReservationFirstMessage() {
        System.out.println("예매 먼저 진행 후, 좌석 선택해주세요");
    }
}
