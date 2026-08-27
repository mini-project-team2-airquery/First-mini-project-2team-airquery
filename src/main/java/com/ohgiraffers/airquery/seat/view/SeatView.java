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

        for (SeatDTO seat : seatList) {
            System.out.println(seat);
        }
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

    public void displayInvalidMenuMessage() {
        System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
    }
}
