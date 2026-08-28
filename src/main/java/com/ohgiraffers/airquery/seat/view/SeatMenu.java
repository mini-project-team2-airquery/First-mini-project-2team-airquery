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

        displayMenu(sc, memberCode);
    }

    // 로그인된 회원번호를 받아 좌석 메뉴를 실행하는 메서드
    public void displayMenu(Scanner sc, int memberCode) {

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

        // 좌석 변경은 "내가 예매한 항공편" 안에서만 가능하다.
        // 그래서 먼저 어떤 항공편의 좌석을 변경할지 항공편번호를 입력받는다.
        seatView.displayInputFlightCodeMessage();
        String flightCodeInput = sc.nextLine();

        // 항공편번호는 숫자만 입력해야 하므로 문자, 한글이 들어오면 메뉴로 돌려보낸다.
        if (!flightCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        int flightCode = Integer.parseInt(flightCodeInput);

        // 이 회원이 해당 항공편에서 이미 좌석을 선택했는지 확인한다.
        // 좌석 변경은 기존 좌석이 있어야 가능하기 때문에 seat_code가 있는 예매를 찾는다.
        boolean hasSelectedSeat = seatController.hasReservationWithSeat(memberCode, flightCode);

        // 아직 좌석을 선택하지 않은 예매라면 "변경"이 아니라 "좌석 예약"을 먼저 해야 한다.
        if (!hasSelectedSeat) {
            seatView.displayNeedSeatFirstMessage();
            backToSeatMenu(sc);
            return;
        }

        // 해당 항공편에서 아직 예약되지 않은 좌석만 보여준다.
        // 이미 예약된 좌석은 새 좌석으로 선택할 수 없다.
        List<SeatDTO> seatList = seatController.getAvailableSeatsByFlightCode(flightCode);
        seatView.displayAvailableSeatList(seatList);

        // 남은 좌석이 하나도 없으면 변경할 좌석이 없으므로 메뉴로 돌아간다.
        if (seatList.isEmpty()) {
            backToSeatMenu(sc);
            return;
        }

        // 사용자가 새로 바꾸고 싶은 좌석번호를 입력한다.
        seatView.displayInputNewSeatCodeMessage();
        String seatCodeInput = sc.nextLine();

        // 좌석번호도 숫자만 입력해야 한다.
        if (!seatCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        int newSeatCode = Integer.parseInt(seatCodeInput);

        // oldFlightClass : 현재 내가 선택해둔 기존 좌석 등급
        // newFlightClass : 새로 바꾸려는 좌석 등급
        // 예) 기존 좌석 = ECONOMY, 새 좌석 = FIRST
        String oldFlightClass = seatController.getSelectedSeatClass(memberCode, flightCode);
        String newFlightClass = seatController.getSeatClass(newSeatCode, flightCode);

        // 새 좌석번호가 없거나, 해당 항공편의 좌석이 아니거나, 이미 예약된 좌석이면 null이 나온다.
        if (newFlightClass == null) {
            seatView.displayChangeSeatResult(false);
            backToSeatMenu(sc);
            return;
        }

        // 이코노미 -> 비즈니스, 이코노미 -> 퍼스트처럼 등급이 올라가는 경우
        // 결제 금액이 달라질 수 있으므로 여기서 바로 변경하지 않고 안내문만 출력한다.
        if (isUpgradeSeat(oldFlightClass, newFlightClass)) {
            seatView.displayNeedRepaymentMessage();
            backToSeatMenu(sc);
            return;
        }

        // 같은 등급일 때만 좌석 변경을 허용한다.
        // 예) 이코노미 -> 이코노미 가능, 퍼스트 -> 퍼스트 가능
        // 예) 비즈니스 -> 이코노미처럼 내려가는 것도 여기서는 막는다. , 막고
        if (!changeFlightClassToCode(oldFlightClass).equals(changeFlightClassToCode(newFlightClass))) {
            seatView.displayOnlySameClassChangeMessage();
            backToSeatMenu(sc);
            return;
        }

        // 여기까지 통과했다면 같은 등급의 예약 가능한 좌석이므로 실제 좌석 변경을 진행한다.
        boolean isSuccess = seatController.changeSeat(memberCode, newSeatCode, flightCode);

        seatView.displayChangeSeatResult(isSuccess);
        backToSeatMenu(sc);
    }

    // 좌석등급이 올라가는 변경인지 확인하는 메서드
    private boolean isUpgradeSeat(String oldFlightClass, String newFlightClass) {

        // 새 좌석 등급 숫자가 기존 좌석 등급 숫자보다 크면 상위 등급으로 변경하는 것이다.
        // 예) ECONOMY(1) -> FIRST(3)이므로 true
        return getFlightClassRank(newFlightClass) > getFlightClassRank(oldFlightClass);
    }

    // 좌석등급을 비교하기 쉽게 숫자로 바꾸는 메서드
    private int getFlightClassRank(String flightClass) {

        // 등급을 숫자로 바꾸면 크기 비교가 쉬워진다.
        // 숫자가 클수록 더 높은 등급이다.
        switch (changeFlightClassToCode(flightClass)) {
            case "ECONOMY":
                return 1;
            case "BUSINESS":
                return 2;
            case "FIRST":
                return 3;
            default:
                // 알 수 없는 등급이면 비교할 수 없으므로 0으로 처리한다.
                return 0;
        }
    }

    // DB에 한글 또는 영어로 들어간 좌석등급을 영어 코드로 맞추는 메서드
    private String changeFlightClassToCode(String flightClass) {

        // null이면 비교할 수 없으므로 빈 문자열로 바꿔준다.
        if (flightClass == null) {
            return "";
        }

        // DB에 "이코노미"로 들어가든 "ECONOMY"로 들어가든 같은 값으로 비교하기 위해 영어 코드로 통일
        switch (flightClass) {
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
                // 위 세 등급이 아니면 원래 값을 그대로 반환한다.
                return flightClass;
        }
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
