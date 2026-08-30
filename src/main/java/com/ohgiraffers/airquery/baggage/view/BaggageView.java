package com.ohgiraffers.airquery.baggage.view;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;

import java.util.List;

/*
 * View
 * 사용자에게 보여줄 출력문을 모아둔 클래스이다.
 * 입력을 직접 처리하지 않고, 화면에 보여주는 역할만 담당한다.
 */
public class BaggageView {

    // 수하물 메뉴 화면을 출력한다.
    public void displayBaggageMenu() {
        System.out.println();
        System.out.println("+==========================================+");
        System.out.println("|                수하물 메뉴               |");
        System.out.println("+==========================================+");
        System.out.println("|  1. 수하물 등록                          |");
        System.out.println("|  2. 수하물 조회                          |");
        System.out.println("|  3. 수하물 무게 변경                      |");
        System.out.println("|  9. 메인 메뉴로 돌아가기                   |");
        System.out.println("+------------------------------------------+");
        System.out.print("  메뉴 선택 > ");
    }

    // 조회된 수하물 목록을 화면에 출력한다.
    public void displayBaggageList(List<BaggageDTO> baggageList) {
        displayBaggageList("수하물 조회", baggageList);
    }

    // 모든 회원의 등록된 수하물을 한 번 보여준다.
    public void displayAllBaggageList(List<BaggageDTO> baggageList) {
        displayBaggageList("전체 수하물 조회", baggageList);
    }

    // 현재 로그인한 회원의 수하물만 보여준다.
    public void displayMemberBaggageList(List<BaggageDTO> baggageList) {
        displayBaggageList("내 수하물 조회", baggageList);
    }

    private void displayBaggageList(String title, List<BaggageDTO> baggageList) {

        // 조회 결과가 비어 있으면 아래 반복문을 실행하지 않고 바로 끝낸다.
        if (baggageList.isEmpty()) {
            System.out.println();
            System.out.println("+======================================================+");
            System.out.println("  " + title);
            System.out.println("+======================================================+");
            System.out.println("  [안내] 수하물이 없습니다.");
            System.out.println("+------------------------------------------------------+");
            return;
        }

        System.out.println();
        System.out.println("+======================================================+");
        System.out.println("  " + title + "  (총 " + baggageList.size() + "건)");
        System.out.println("+======================================================+");

        // List 안에 들어있는 수하물을 하나씩 꺼내서 출력한다.
        for (BaggageDTO baggage : baggageList) {
            System.out.println("  [수하물 " + baggage.getBaggageCode() + "]");
            System.out.println("   수하물번호 : " + baggage.getBaggageCode());
            System.out.println("   예매번호   : " + baggage.getReservationCode());
            if (baggage.getMemberName() != null) {
                System.out.println("   예약회원   : " + baggage.getMemberName()
                        + " (회원번호 " + baggage.getMemberCode() + ")");
                System.out.println("   지참여부   : " + (baggage.isBaggageCarrying() ? "YES" : "NO"));
            }
            System.out.println("   수하물무게 : " + baggage.getBaggageWeight() + " kg");
            System.out.println("+------------------------------------------------------+");
        }
    }

    // 예매번호 입력 안내 문구
    public void displayInputReservationCodeMessage() {
        System.out.print("  예매번호 > ");
    }

    // 수하물번호 입력 안내 문구
    public void displayInputBaggageCodeMessage() {
        System.out.print("  수하물번호 > ");
    }

    // 수하물무게 입력 안내 문구
    public void displayInputBaggageWeightMessage() {
        System.out.print("  수하물무게(kg) > ");
    }

    public void displayInputNewBaggageWeightMessage() {
        System.out.print("  변경할 수하물무게(kg) > ");
    }

    // 숫자가 아닌 값을 입력했을 때 출력
    public void displayNumberOnlyMessage() {
        System.out.println("[안내] 숫자만 입력해주세요.");
    }

    // 입력한 예매번호에 맞는 예매내역이 없을 때 출력
    public void displayNoReservationMessage() {
        System.out.println("[안내] 해당 예매내역이 없습니다.");
    }

    // 예매할 때 수하물 신청을 하지 않은 경우 출력
    public void displayNoBaggageCarryingMessage() {
        System.out.println("[안내] 예매 시 수하물 지참 여부가 NO이므로 등록할 수 없습니다.");
    }

    // 예매할 때 수하물 신청을 하지 않은 경우 변경할 수 없다는 문구 출력
    public void displayNoBaggageCarryingUpdateMessage() {
        System.out.println("[안내] 예매 시 수하물 지참 여부가 NO이므로 수하물을 등록할 수 없습니다.");
    }

    // 수하물무게 형식이 맞지 않을 때 출력
    public void displayWeightOnlyMessage() {
        System.out.println("[안내] 수하물무게는 숫자 또는 소수점 숫자로 입력해주세요.");
    }

    // 수하물 등록 성공/실패 결과 출력
    public void displayRegistBaggageResult(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("[성공] 수하물 등록이 완료되었습니다.");
        } else {
            System.out.println("[실패] 수하물 등록에 실패했습니다. 예매번호를 확인해주세요.");
        }
    }

    // 수하물 무게 변경 성공/실패 결과 출력
    public void displayUpdateBaggageWeightResult(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("[성공] 수하물 무게 변경이 완료되었습니다.");
        } else {
            System.out.println("[실패] 해당 예매의 수하물번호인지 확인해주세요.");
        }
    }

    // 기능 실행 후 수하물 메뉴로 돌아가기 위한 안내 문구
    public void displayBackToBaggageMenuMessage() {
        System.out.println();
        System.out.println("[안내] 수하물 메뉴로 돌아가려면 1번을 입력하세요.");
        System.out.print("  번호 입력 > ");
    }

    // 되돌아가기에서 1번이 아닌 값을 입력했을 때 출력
    public void displayInvalidBackInputMessage() {
        System.out.println("[안내] 1번만 입력해주세요.");
    }

    // 메뉴에서 없는 번호를 입력했을 때 출력
    public void displayInvalidMenuMessage() {
        System.out.println("[안내] 잘못 입력했습니다. 1번, 2번, 3번 또는 9번을 입력해주세요.");
    }
}
