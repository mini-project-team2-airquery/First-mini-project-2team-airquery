package com.ohgiraffers.airquery.seat.view;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.util.List;
import java.util.Map;

/*
 * 좌석 기능에서 사용자에게 보여줄 문구와 목록 출력을 담당한다.
 * 입력을 처리하거나 DB를 조회하지 않고, 전달받은 데이터만 콘솔에 출력한다.
 */
public class SeatView {

    public void displaySeatMenu() {
        System.out.println();
        System.out.println("+==========================================+");
        System.out.println("|                 좌석 메뉴                |");
        System.out.println("+==========================================+");
        System.out.println("|  1. 좌석 조회                            |");
        System.out.println("|  2. 좌석 예약                            |");
        System.out.println("|  3. 좌석 변경                            |");
        System.out.println("|  9. 메인 메뉴로 돌아가기                   |");
        System.out.println("+------------------------------------------+");
        System.out.print("  메뉴 선택 > ");
    }

    // 전체 좌석 목록을 좌석 한 개씩 반복해서 출력한다.
    public void displaySeatList(List<SeatDTO> seatList) {
        if (seatList.isEmpty()) {
            System.out.println("[안내] 조회된 좌석이 없습니다.");
            return;
        }

        displaySeatCards("좌석 전체 조회", seatList, true);
    }

    // DAO에서 is_reserved=false 조건으로 조회한 좌석 목록을 출력한다.
    public void displayAvailableSeatList(List<SeatDTO> seatList) {
        if (seatList.isEmpty()) {
            System.out.println("[안내] 예약 가능한 좌석이 없습니다.");
            return;
        }

        displaySeatCards("예약 가능 좌석", seatList, true);
    }

    public void displaySeatSearchMenu() {
        System.out.println();
        System.out.println("+==========================================+");
        System.out.println("|                 좌석 조회                |");
        System.out.println("+==========================================+");
        System.out.println("|  1. 좌석 전체 조회                       |");
        System.out.println("|  2. 예약 가능 좌석만 보기                 |");
        System.out.println("|  3. 항공편번호로 좌석 보기                |");
        System.out.println("|  9. 좌석 메뉴로 돌아가기                  |");
        System.out.println("+------------------------------------------+");
        System.out.print("  메뉴 선택 > ");
    }

    public void displayInvalidSeatSearchMenuMessage() {
        System.out.println("잘못 입력했습니다. 1번, 2번, 3번 또는 9번을 입력해주세요.");
    }

    // 입력한 항공편번호에 속한 좌석만 출력한다.
    public void displayFlightSeatList(int flightCode, List<SeatDTO> seatList) {
        if (seatList.isEmpty()) {
            System.out.println("[안내] 항공편번호 " + flightCode + "에 해당하는 좌석이 없습니다.");
            return;
        }

        displaySeatCards("항공편 " + flightCode + " 좌석 조회", seatList, false);
    }

    // 좌석 정보를 카드 형태로 출력하여 여러 좌석을 빠르게 비교할 수 있게 한다.
    private void displaySeatCards(String title, List<SeatDTO> seatList, boolean showFlightCode) {
        System.out.println();
        System.out.println("+======================================================+");
        System.out.println("  " + title + "  (총 " + seatList.size() + "석)");
        System.out.println("+======================================================+");

        for (SeatDTO seat : seatList) {
            System.out.println("  [좌석 " + seat.getSeatCode() + "] " + seat.getSeatId());
            if (showFlightCode) {
                System.out.println("   항공편번호: " + seat.getFlightCode());
            }
            System.out.println("   좌석등급 : " + changeFlightClassToKorean(seat.getFlightClass()));
            System.out.println("   추가금액 : " + String.format("%,d", seat.getAdditionalAmount()) + "원");
            System.out.println("   예약상태 : " + changeReservedToKorean(seat.isReserved()));
            System.out.println("   예약회원 : " + changeReservedMemberToText(seat));
            System.out.println("+------------------------------------------------------+");
        }
    }

    // DB의 영어 좌석등급을 사용자에게 보여줄 한글로 바꾼다
    private String changeFlightClassToKorean(String flightClass) {
        if (flightClass == null) {
            return "";
        }

        return switch (flightClass) {
            case "FIRST" -> "퍼스트";
            case "BUSINESS" -> "비즈니스";
            case "ECONOMY" -> "이코노미";
            default -> flightClass;
        };
    }

    // boolean 값을 그대로 출력하지 않고 이해하기 쉬운 문구로 바꾼다.
    private String changeReservedToKorean(boolean reserved) {
        if (reserved) {
            return "예약됨";
        }

        return "예약 가능";
    }

    // 새로 예약한 좌석은 로그인 회원 이름과 번호를, 기존 DB 예약 좌석은 확인 불가로 표시한다.
    private String changeReservedMemberToText(SeatDTO seat) {
        if (!seat.isReserved()) {
            return "없음";
        }

        if (seat.getReservedMemberCode() == 0) {
            return "확인 불가";
        }

        if (seat.getReservedMemberName() == null) {
            return "회원번호 " + seat.getReservedMemberCode();
        }

        return seat.getReservedMemberName() + " (회원번호 " + seat.getReservedMemberCode() + ")";
    }

    public void displayBackToSeatMenuMessage() {
        System.out.println();
        System.out.println("[안내] 좌석 메뉴로 돌아가려면 1번을 입력하세요.");
        System.out.print("  번호 입력 > ");
    }

    public void displayInvalidBackInputMessage() {
        System.out.println("1번만 입력해주세요.");
    }

    public void displayInputSeatCodeMessage() {
        System.out.print("  예약할 좌석번호(seat_code) > ");
    }

    public void displayInputReservationMemberCodeMessage() {
        System.out.print("  예매 메뉴에서 입력했던 회원번호 > ");
    }

    public void displayAdminSeatAccessMessage() {
        System.out.println("[관리자] 예매 메뉴에서 사용한 회원번호 기준으로 좌석을 선택합니다.");
        System.out.println("[안내] 이미 등록된 예매도 좌석을 아직 선택하지 않았다면 좌석 선택이 가능합니다.");
    }

    public void displayMemberCodeNumberOnlyMessage() {
        System.out.println("[안내] 회원번호는 숫자로 입력해주세요.");
    }

    public void displayReservationsWithoutSeat(Map<Integer, Integer> reservationMap) {
        System.out.println();
        System.out.println("+==========================================+");
        System.out.println("|    좌석을 선택할 수 있는 기존 예매 목록   |");
        System.out.println("+==========================================+");
        for (Map.Entry<Integer, Integer> entry : reservationMap.entrySet()) {
            System.out.println("  예매번호: " + entry.getKey()
                    + " | 항공편번호: " + entry.getValue());
        }
        System.out.println("+------------------------------------------+");
    }

    public void displayNoReservationWithoutSeatMessage() {
        System.out.println("[안내] 해당 회원에게 좌석을 선택할 수 있는 예매가 없습니다.");
        System.out.println("[안내] 예매 메뉴에서 입력한 회원번호와 같은 번호인지 확인해주세요.");
    }

    public void displaySelectReservationFlightMessage() {
        System.out.print("  위 예매 목록 중 좌석을 선택할 항공편번호 > ");
    }

    public void displayInvalidReservationFlightMessage() {
        System.out.println("[안내] 위 예매 목록에 있는 항공편번호만 선택할 수 있습니다.");
    }

    public void displaySeatCodeNumberOnlyMessage() {
        System.out.println("좌석번호는 숫자만 입력해야 합니다.");
    }

    public void displayUnavailableSeatCodeMessage() {
        System.out.println("[안내] 존재하지 않거나 이미 예약된 좌석번호입니다.");
        System.out.println("[안내] 좌석 ID(예: 5D)가 아니라 [좌석 120]처럼 보이는 숫자를 입력해주세요.");
    }

    public void displayRetrySeatCodeMessage() {
        System.out.println("[안내] 위의 예약 가능 좌석번호를 확인한 후 다시 입력해주세요.");
    }

    public void displayReserveSeatResult(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("[성공] 좌석 예약이 완료되었습니다.");
            System.out.println("[성공] 선택한 좌석번호가 예매 정보에도 저장되었습니다.");
        } else {
            System.out.println("[실패] 해당 항공편의 예매가 없거나 이미 예약된 좌석입니다.");
        }
    }

    public void displayInputFlightCodeMessage() {
        System.out.print("  항공편번호 > ");
    }

    public void displayNumberOnlyMessage() { System.out.println("숫자만 입력해야 합니다."); }

    public void displayInvalidMenuMessage() {
        System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
    }

    public void displayNeedSeatFirstMessage() {
        System.out.println("먼저 좌석을 선택한 뒤 변경해주세요.");
    }

    public void displayInputNewSeatCodeMessage() {
        System.out.print("  새로 선택할 좌석번호 > ");
    }

    public void displayChangeSeatResult(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("[성공] 좌석 변경이 완료되었습니다.");
        } else {
            System.out.println("[실패] 이미 예약된 좌석이거나 존재하지 않는 좌석입니다.");
        }
    }

    public void displayNeedReservationChangeMessage() {
        System.out.println("+------------------------------------------------------+");
        System.out.println("| [안내] 다른 등급의 좌석으로는 변경할 수 없습니다.  |");
        System.out.println("|        등급 변경은 예매 변경 메뉴를 이용해주세요.  |");
        System.out.println("+------------------------------------------------------+");
    }
}