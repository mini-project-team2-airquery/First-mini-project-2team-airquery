package com.ohgiraffers.airquery.baggage.view;

import com.ohgiraffers.airquery.baggage.controller.BaggageController;
import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;

import java.util.List;
import java.util.Scanner;

/*
 * Menu
 * 사용자의 입력을 받는 클래스이다.
 * 입력값을 검사한 뒤 Controller에게 기능 실행을 요청한다.
 */
public class BaggageMenu {

    private static final String NUMBER_PATTERN = "[0-9]+";
    private static final String WEIGHT_PATTERN = "[0-9]+(\\.[0-9]+)?";

    // 실제 기능 요청은 Controller에게 넘긴다.
    private final BaggageController baggageController = new BaggageController();

    // 화면 출력은 View에게 맡긴다.
    private final BaggageView baggageView = new BaggageView();

    // 수하물 메뉴를 계속 보여주는 메서드
    public void displayMenu(Scanner sc, int memberCode) {

        boolean running = true;

        // 9번을 누르기 전까지 수하물 메뉴를 반복해서 보여준다.
        while (running) {

            baggageView.displayBaggageMenu();

            // 사용자가 입력한 메뉴 번호를 문자열로 받는다.
            String input = sc.nextLine();

            switch (input) {
                case "1" -> registBaggage(sc);
                case "2" -> selectBaggages(sc, memberCode);
                case "3" -> updateBaggageWeight(sc);
                case "9" -> running = false;
                default -> baggageView.displayInvalidMenuMessage();
            }
        }
    }

    /*
     * 수하물 조회 메서드
     * 1. 전체 수하물을 먼저 한 번 보여준다.
     * 2. 로그인 회원이 Y로 신청하고 등록한 수하물을 보여준다.
     * 3. 로그인 회원의 조회 결과가 없으면 "수하물이 없습니다."를 보여준다.
     */
    private void selectBaggages(Scanner sc, int memberCode) {

        // DB에 등록된 전체 수하물을 먼저 한 번 조회한다.
        List<BaggageDTO> allBaggageList = baggageController.getAllBaggages();
        baggageView.displayAllBaggageList(allBaggageList);

        // 로그인 회원의 예매 중 수하물 지참 여부가 YES(true)인 수하물만 가져온다.
        List<BaggageDTO> baggageList = baggageController.getBaggagesByMemberCode(memberCode);

        // YES인 예매의 수하물이 있으면 목록을, 없으면 "수하물이 없습니다."를 출력한다.
        baggageView.displayMemberBaggageList(baggageList);
        backToBaggageMenu(sc);
    }

    // 예매번호와 수하물무게를 입력받아 수하물을 등록하는 메서드
    private void registBaggage(Scanner sc) {

        baggageView.displayInputReservationCodeMessage();
        String reservationCodeInput = sc.nextLine();

        // 예매번호는 숫자만 입력 가능하다.
        if (!reservationCodeInput.matches(NUMBER_PATTERN)) {
            baggageView.displayNumberOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        int reservationCode = Integer.parseInt(reservationCodeInput);

        /*
         * 수하물 등록 전에 먼저 예매번호가 실제로 있는지 확인한다.
         * 없는 예매번호라면 수하물을 등록할 수 없다.
         */
        if (baggageController.isReservationMissing(reservationCode)) {
            baggageView.displayNoReservationMessage();
            backToBaggageMenu(sc);
            return;
        }

        /*
         * 예매할 때 수하물 신청을 하지 않았다면 baggage_carrying 값이 false이다.
         * 이 경우에는 수하물 등록을 진행하지 않고 안내 문구만 보여준다.
         */
        if (baggageController.isBaggageNotCarried(reservationCode)) {
            baggageView.displayNoBaggageCarryingMessage();
            backToBaggageMenu(sc);
            return;
        }

        baggageView.displayInputBaggageWeightMessage();
        String baggageWeightInput = sc.nextLine();

        // 수하물무게는 15 또는 15.5처럼 숫자와 소수점만 가능하다.
        if (!baggageWeightInput.matches(WEIGHT_PATTERN)) {
            baggageView.displayWeightOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        // 사용자가 입력한 값을 DTO에 담는다.
        BaggageDTO baggage = new BaggageDTO();

        baggage.setReservationCode(reservationCode);
        baggage.setBaggageWeight(Double.parseDouble(baggageWeightInput));

        // Controller에게 수하물 등록을 요청한다.
        boolean isSuccess = baggageController.registBaggage(baggage);

        // 등록 성공/실패 결과를 출력한다.
        baggageView.displayRegistBaggageResult(isSuccess);
        backToBaggageMenu(sc);
    }

    // 예매번호를 입력받아 수하물 변경 가능 여부를 안내하는 메서드
    private void updateBaggageWeight(Scanner sc) {

        baggageView.displayInputReservationCodeMessage();
        String reservationCodeInput = sc.nextLine();

        // 예매번호는 숫자만 입력 가능하다.
        if (!reservationCodeInput.matches(NUMBER_PATTERN)) {
            baggageView.displayNumberOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        // 입력받은 예매번호를 int로 바꾼다.
        int reservationCode = Integer.parseInt(reservationCodeInput);

        /*
         * 수하물 변경 전에 먼저 예매번호가 실제로 있는지 확인한다.
         * 없는 예매번호라면 변경할 예매내역도 없는 것이다.
         */
        if (baggageController.isReservationMissing(reservationCode)) {
            baggageView.displayNoReservationMessage();
            backToBaggageMenu(sc);
            return;
        }

        // 예매할 때 수하물 지참 여부가 NO이면 등록과 변경을 모두 막는다.
        if (baggageController.isBaggageNotCarried(reservationCode)) {
            baggageView.displayNoBaggageCarryingUpdateMessage();
            backToBaggageMenu(sc);
            return;
        }

        // YES인 예매에 실제로 등록된 수하물을 보여준다.
        List<BaggageDTO> baggageList = baggageController.getBaggagesByReservationCode(reservationCode);
        baggageView.displayBaggageList(baggageList);

        if (baggageList.isEmpty()) {
            backToBaggageMenu(sc);
            return;
        }

        baggageView.displayInputBaggageCodeMessage();
        String baggageCodeInput = sc.nextLine();

        if (!baggageCodeInput.matches(NUMBER_PATTERN)) {
            baggageView.displayNumberOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        baggageView.displayInputNewBaggageWeightMessage();
        String baggageWeightInput = sc.nextLine();

        if (!baggageWeightInput.matches(WEIGHT_PATTERN)) {
            baggageView.displayWeightOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        int baggageCode = Integer.parseInt(baggageCodeInput);
        double baggageWeight = Double.parseDouble(baggageWeightInput);

        // 선택한 예매에 속한 수하물의 무게만 변경한다.
        boolean isSuccess = baggageController.updateBaggageWeight(
                reservationCode, baggageCode, baggageWeight
        );
        baggageView.displayUpdateBaggageWeightResult(isSuccess);
        backToBaggageMenu(sc);
    }

    // 기능 실행 후 바로 메뉴가 넘어가지 않게 잠깐 멈추는 메서드
    private void backToBaggageMenu(Scanner sc) {

        // 1번을 입력할 때까지 계속 다시 입력받는다.
        while (true) {
            baggageView.displayBackToBaggageMenuMessage();

            String backInput = sc.nextLine();

            if (backInput.equals("1")) {
                // break를 만나면 while문만 끝나고 수하물 메뉴로 돌아간다.
                break;
            }

            baggageView.displayInvalidBackInputMessage();
        }
    }
}
