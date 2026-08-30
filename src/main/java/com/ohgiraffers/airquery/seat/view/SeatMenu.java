package com.ohgiraffers.airquery.seat.view;

import com.ohgiraffers.airquery.seat.controller.SeatController;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.util.List;
import java.util.Scanner;


/*
 * 사용자의 입력을 받고 좌석 기능의 실행 순서를 결정하는 클래스이다.
 * DB 작업은 SeatController에 요청하고, 문구 출력은 SeatView에 맡긴다.
 * 조회, 예약, 변경 기능을 서로 연결하는 좌석 기능의 시작점이다.
 */
public class SeatMenu {

    // 화면의 요청을 넘겨줄 컴트롤러와 메시지 출력을 점담할 뷰 객체를 생성
    private final SeatController seatController = new SeatController();
    private final SeatView seatView = new SeatView();

    // memberCode를 이미 알고 있을 때 회원번호 입력 없이 실행하는 진입점이다.
    public void displayMenu(Scanner sc, int memberCode) {
        while (true) {

            seatView.displaySeatMenu();

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    displaySeatSearchMenu(sc);
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

    // 전체/예약 가능/항공편별 조회 중 하나를 반복해서 선택한다.
    private void displaySeatSearchMenu(Scanner sc) {
        while (true) {
            seatView.displaySeatSearchMenu();

            String input = sc.nextLine();

            switch (input) {
                case "1":
                    selectAllSeats();
                    break;

                case "2":
                    selectAvailableSeats();
                    break;

                case "3":
                    selectSeatsByFlightCode(sc);
                    break;

                case "9":
                    return;

                default:
                    seatView.displayInvalidSeatSearchMenuMessage();
                    break;
            }
        }
    }

    // 조건 없이 tbl_seat의 모든 좌석을 조회한다.
    private void selectAllSeats() {
        List<SeatDTO> seatList = seatController.getAllSeats();
        seatView.displaySeatList(seatList);
    }

    // 모든 항공편에서 is_reserved=false인 좌석만 조회한다.
    private void selectAvailableSeats() {

        List<SeatDTO> seatList = seatController.getAvailableSeats();

        seatView.displayAvailableSeatList(seatList);
    }

    // 입력값을 항공편번호로 바꾼 후 그 항공편 좌석만 조회한다.
    private void selectSeatsByFlightCode(Scanner sc) {
        seatView.displayInputFlightCodeMessage();
        String flightCodeInput = sc.nextLine();

        if (!flightCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            return;
        }

        try {
            int flightCode = Integer.parseInt(flightCodeInput);
            List<SeatDTO> seatList = seatController.getSeatsByFlightCode(flightCode);
            seatView.displayFlightSeatList(flightCode, seatList);
        } catch (NumberFormatException e) {
            seatView.displayNumberOnlyMessage();
        }
    }

    /*
     * 예매와 관계없이 빈 좌석을 직접 예약한다.
     * tbl_seat의 is_reserved만 true로 변경하며 tbl_reservation에는 연결하지 않는다.
     */
    private void reserveSeat(Scanner sc, int memberCode) {

        List<SeatDTO> seatList = seatController.getAvailableSeats();
        seatView.displayAvailableSeatList(seatList);

        if (seatList.isEmpty()) {
            backToSeatMenu(sc);
            return;
        }

        while (true) {
            seatView.displayInputSeatCodeMessage();
            String seatCodeInput = sc.nextLine();

            // 문자나 음수를 입력하면 메뉴로 나가지 않고 좌석번호를 다시 입력받는다.
            if (!seatCodeInput.matches("[0-9]+")) {
                seatView.displaySeatCodeNumberOnlyMessage();
                seatView.displayRetrySeatCodeMessage();
                continue;
            }

            int seatCode;
            try {
                seatCode = Integer.parseInt(seatCodeInput);
            } catch (NumberFormatException e) {
                seatView.displaySeatCodeNumberOnlyMessage();
                seatView.displayRetrySeatCodeMessage();
                continue;
            }

            // 처음에 보여준 예약 가능 목록에 있는 좌석번호인지 먼저 확인한다.
            boolean isAvailableSeatCode = false;
            for (SeatDTO seat : seatList) {
                if (seat.getSeatCode() == seatCode) {
                    isAvailableSeatCode = true;
                    break;
                }
            }

            if (!isAvailableSeatCode) {
                seatView.displayUnavailableSeatCodeMessage();
                seatView.displayRetrySeatCodeMessage();
                continue;
            }

            // 로그인 회원번호를 함께 전달해 실행 중 예약자 정보로 기록한다.
            boolean isSuccess = seatController.reserveSeat(memberCode, seatCode);
            seatView.displayReserveSeatResult(isSuccess);

            if (isSuccess) {
                break;
            }

            // 다른 사용자가 먼저 예약한 경우에도 다시 선택할 수 있게 한다.
            seatView.displayRetrySeatCodeMessage();
        }

        backToSeatMenu(sc);
    }

    /*
     * 변경 순서: 회원의 좌석 선택 여부 확인 -> 같은 항공편의 빈 좌석 선택 ->
     * 새 좌석 예약 -> 예매 좌석번호 변경 -> 기존 좌석 해제
     */
    private void changeSeat(Scanner sc, int memberCode) {

        seatView.displayInputFlightCodeMessage();
        String flightCodeInput = sc.nextLine();

        if (!flightCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        int flightCode = Integer.parseInt(flightCodeInput);

        // 이 항공편이 실제로 현재 회원의 좌석 선택 완료 예매인지 확인한다.
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

        // 현재 로그인 회원의 기존 좌석등급과 새 좌석등급을 조회한다.
        String oldFlightClass = seatController.getSelectedSeatClass(memberCode, flightCode);
        String newFlightClass = seatController.getAvailableSeatClass(newSeatCode, flightCode);

        // 존재하지 않거나 이미 예약된 좌석, 다른 항공편의 좌석이면 변경할 수 없다.
        if (oldFlightClass == null || newFlightClass == null) {
            seatView.displayChangeSeatResult(false);
            backToSeatMenu(sc);
            return;
        }

        // 이코노미/비즈니스/퍼스트 중 기존 등급과 새 등급이 정확히 같아야 한다.
        if (!normalizeFlightClass(oldFlightClass).equals(normalizeFlightClass(newFlightClass))) {
            seatView.displayNeedReservationChangeMessage();
            backToSeatMenu(sc);
            return;
        }

        // 기존 좌석번호는 Service가 회원번호와 항공편번호를 이용해 DB에서 찾는다.
        boolean isSuccess = seatController.changeSeat(memberCode, newSeatCode, flightCode);

        seatView.displayChangeSeatResult(isSuccess);
        backToSeatMenu(sc);
    }

    // DB에 좌석등급이 한글 또는 영어로 저장되어 있어도 같은 값으로 비교한다.
    private String normalizeFlightClass(String flightClass) {
        if (flightClass == null) {
            return "";
        }

        switch (flightClass.trim().toUpperCase()) {
            case "이코노미":
            case "ECONOMY":
                return "ECONOMY";
            case "비즈니스":
            case "BUSINESS":
                return "BUSINESS";
            case "퍼스트":
            case "FIRST":
                return "FIRST";
            default:
                return flightClass.trim().toUpperCase();
        }
    }

    // 결과를 확인한 사용자가 1번을 눌렀을 때 좌석 메뉴로 돌아간다.
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
