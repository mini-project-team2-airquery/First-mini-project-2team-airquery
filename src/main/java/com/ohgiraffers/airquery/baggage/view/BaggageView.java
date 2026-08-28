package com.ohgiraffers.airquery.baggage.view;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;

import java.util.List;

public class BaggageView {

    public void displayBaggageMenu() {
        System.out.println();
        System.out.println("===== 수하물 메뉴 =====");
        System.out.println("1. 수하물 등록");
        System.out.println("2. 수하물 조회");
        System.out.println("3. 수하물 변경");
        System.out.println("9. 메인 메뉴로 돌아가기");
        System.out.print("메뉴 선택 : ");
    }

    public void displayBaggageList(List<BaggageDTO> baggageList) {
        if (baggageList.isEmpty()) {
            System.out.println("조회된 수하물이 없습니다.");
            return;
        }

        System.out.println();
        System.out.println("===================== 수하물 조회 =====================");

        for (BaggageDTO baggage : baggageList) {
            System.out.println("수하물 : 수하물" + baggage.getBaggageCode());
            System.out.println("수하물번호 : " + baggage.getBaggageCode());
            System.out.println("예매번호 : " + baggage.getReservationCode());
            System.out.println("수하물무게 : " + baggage.getBaggageWeight() + "kg");
            System.out.println("------------------------------------------------------");
        }
    }

    public void displayInputReservationCodeMessage() {
        System.out.print("예매번호를 입력하세요 : ");
    }

    public void displayInputBaggageCodeMessage() {
        System.out.print("수하물번호를 입력하세요 : ");
    }

    public void displayInputBaggageWeightMessage() {
        System.out.print("수하물무게를 입력하세요(kg) : ");
    }

    public void displayNumberOnlyMessage() {
        System.out.println("숫자만 입력해야 합니다.");
    }

    public void displayWeightOnlyMessage() {
        System.out.println("수하물무게는 숫자 또는 소수점 숫자로 입력해야 합니다.");
    }

    public void displayRegistBaggageResult(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("수하물 등록이 완료되었습니다.");
        } else {
            System.out.println("수하물 등록에 실패했습니다. 없는 예매번호일 수 있습니다.");
        }
    }

    public void displayUpdateBaggageWeightResult(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("수하물 무게 변경이 완료되었습니다.");
        } else {
            System.out.println("수하물 무게 변경에 실패했습니다. 없는 수하물번호일 수 있습니다.");
        }
    }

    public void displayBackToBaggageMenuMessage() {
        System.out.println();
        System.out.println("되돌아가려면 1번을 입력하세요.");
        System.out.print("번호 입력 : ");
    }

    public void displayInvalidBackInputMessage() {
        System.out.println("1번만 입력해주세요.");
    }

    public void displayInvalidMenuMessage() {
        System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
    }
}
