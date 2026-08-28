package com.ohgiraffers.airquery.seat.view;

import com.ohgiraffers.airquery.seat.controller.SeatController;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.util.List;
import java.util.Map;
import java.util.Scanner;


// 사용자가 보게 되는 좌석 관련 클래스
public class SeatMenu {

    // 화면의 요청을 넘겨줄 컴트롤러와 메시지 출력을 점담할 뷰 객체를 생성
    private final SeatController seatController = new SeatController();
    private final SeatView seatView = new SeatView();

    // 메인화면
    public void displayMenu(Scanner sc) {

        int memberCode = inputMemberCode(sc);

        if (memberCode == 0) {
            return;
        }

        while (true) {

            seatView.displaySeatMenu();

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    selectAllSeats(sc);
                    break;

                case "2":
                    reserveSeat(sc, memberCode);
                    break;

                case "3":
                    changeSeat(sc, memberCode);
                    break;

                case "9":
                    return;

                default:
                    seatView.displayInvalidMenuMessage();
                    break;
            }
        }
    }

    // 회원번호를 입력받는 메서드
    private int inputMemberCode(Scanner sc) {

        seatView.displayInputMemberCodeMessage();
        String memberCodeInput = sc.nextLine();

        if (!memberCodeInput.matches("[0-9]+")) {
            seatView.displayMemberCodeNumberOnlyMessage();
            return 0;
        }

        return Integer.parseInt(memberCodeInput);
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
    private void reserveSeat(Scanner sc, int memberCode) {

        Map<Integer, Integer> reservationMap = seatController.getReservationsWithoutSeat(memberCode);

        if (reservationMap.isEmpty()) {
            seatView.displayNeedReservationFirstMessage();
            backToSeatMenu(sc);
            return;
        }

        seatView.displayReservationsWithoutSeat(reservationMap);

        seatView.displayInputReservationCodeMessage();
        String reservationCodeInput = sc.nextLine();

        if (!reservationCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        int reservationCode = Integer.parseInt(reservationCodeInput);

        if (!reservationMap.containsKey(reservationCode)) {
            seatView.displayInvalidReservationCodeMessage();
            backToSeatMenu(sc);
            return;
        }

        int flightCode = reservationMap.get(reservationCode);

        List<SeatDTO> seatList = seatController.getAvailableSeatsByFlightCode(flightCode);
        seatView.displayAvailableSeatList(seatList);

        if (seatList.isEmpty()) {
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

        boolean isSuccess = seatController.reserveSeat(memberCode, reservationCode, seatCode, flightCode);

        seatView.displayReserveSeatResult(isSuccess);
        backToSeatMenu(sc);
    }

    // 이미 선택한 좌석을 새 좌석으로 변경하는 메서드
    private void changeSeat(Scanner sc, int memberCode) {

        seatView.displayInputFlightCodeMessage();
        String flightCodeInput = sc.nextLine();

        if (!flightCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        int flightCode = Integer.parseInt(flightCodeInput);

        boolean hasSelectedSeat = seatController.hasReservationWithSeat(memberCode, flightCode);

        if (!hasSelectedSeat) {
            seatView.displayNeedSeatFirstMessage();
            backToSeatMenu(sc);
            return;
        }

        List<SeatDTO> seatList = seatController.getAvailableSeatsByFlightCode(flightCode);
        seatView.displayAvailableSeatList(seatList);

        if (seatList.isEmpty()) {
            backToSeatMenu(sc);
            return;
        }

        seatView.displayInputNewSeatCodeMessage();
        String seatCodeInput = sc.nextLine();

        if (!seatCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        int newSeatCode = Integer.parseInt(seatCodeInput);

        boolean isSuccess = seatController.changeSeat(memberCode, newSeatCode, flightCode);

        seatView.displayChangeSeatResult(isSuccess);
        backToSeatMenu(sc);
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
