package com.ohgiraffers.airquery.seat.view;

import com.ohgiraffers.airquery.seat.controller.SeatController;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.util.List;
import java.util.Scanner;


// 사용자가 보게 되는 좌석 관련 클래스
public class SeatMenu {

    // 화면의 요청을 넘겨줄 컴트롤러와 메시지 출력을 점담할 뷰 객체를 생성
    private final SeatController seatController = new SeatController();
    private final SeatView seatView = new SeatView();

    // 메인화면
    public void displayMenu(Scanner sc) {

        while (true) {

            seatView.displaySeatMenu();

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    selectAllSeats(sc);
                    break;

                case "2":
                    reserveSeat(sc);
                    break;

                case "9":
                    return;

                default:
                    seatView.displayInvalidMenuMessage();
                    break;
            }
        }
    }

    // 좌석 조회 메뉴
    private void selectAllSeats(Scanner sc) {

        List<SeatDTO> seatList = seatController.getAllSeats();

        seatView.displaySeatList(seatList);

        while (true) {
            seatView.displayAfterSelectAllSeatsMenu();

            String input = sc.nextLine();

            switch (input) {
                case "1":
                    return;

                case "2":
                    selectAvailableSeats();
                    break;

                default:
                    seatView.displayInvalidAfterSelectAllSeatsMenuMessage();
                    break;
            }
        }
    }

    // 빈좌석만 필러팅해서 보여주는 메서드
    private void selectAvailableSeats() {

        List<SeatDTO> seatList = seatController.getAvailableSeats();

        seatView.displayAvailableSeatList(seatList);
    }

    // 비행기 편면과 좌석 번호를 입력해서 예약하는 메서드
    private void reserveSeat(Scanner sc) {

        seatView.displayInputFlightCodeMessage();
        String flightCodeInput = sc.nextLine();

        if (!flightCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        int flightCode = Integer.parseInt(flightCodeInput);

        boolean hasReservation = seatController.hasReservationWithoutSeat(flightCode);

        if (!hasReservation) {
            seatView.displayNeedReservationFirstMessage();
            backToSeatMenu(sc);
            return;
        }

        seatView.displayInputSeatCodeMessage();
        String seatCodeInput = sc.nextLine();

        if (!seatCodeInput.matches("[0-9]+")) {
            seatView.displaySeatCodeNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        int seatCode = Integer.parseInt(seatCodeInput);

        boolean isSuccess = seatController.reserveSeat(seatCode, flightCode);

        seatView.displayReserveSeatResult(isSuccess);
        backToSeatMenu(sc);
    }

    // 상세정보를 수정하는 메서드
    private void updateSeat(Scanner sc) {

        seatView.displayInputUpdateSeatCodeMessage();
        String seatCodeInput = sc.nextLine();

        if (!seatCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        seatView.displayInputFlightCodeMessage();
        String flightCodeInput = sc.nextLine();

        if (!flightCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        seatView.displayInputSeatIdMessage();
        String seatId = sc.nextLine();

        seatView.displayInputFlightClassMessage();
        String flightClass = sc.nextLine();

        seatView.displayInputAdditionalAmountMessage();
        String additionalAmountInput = sc.nextLine();

        if (!additionalAmountInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        seatView.displayInputReservedMessage();
        String reservedInput = sc.nextLine();

        if (!reservedInput.equals("true") && !reservedInput.equals("false")) {
            seatView.displayBooleanOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        SeatDTO seat = new SeatDTO();

        seat.setSeatCode(Integer.parseInt(seatCodeInput));
        seat.setFlightCode(Integer.parseInt(flightCodeInput));
        seat.setSeatId(seatId);
        seat.setFlightClass(flightClass);
        seat.setAdditionalAmount(Integer.parseInt(additionalAmountInput));
        seat.setReserved(Boolean.parseBoolean(reservedInput));


    }

    // 안전하게 메뉴 돌려줌
    private void backToSeatMenu(Scanner sc) {

        while (true) {
            seatView.displayBackToSeatMenuMessage();

            String backInput = sc.nextLine();

            if (backInput.equals("1")) {
                break;
            }

            seatView.displayInvalidBackInputMessage();
        }
    }
}
