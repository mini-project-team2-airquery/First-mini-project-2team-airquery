package com.ohgiraffers.airquery.payment.view;

import com.ohgiraffers.airquery.payment.controller.PaymentController;
import com.ohgiraffers.airquery.reservation.controller.ReservationController;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.reservation.view.ResultView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class PaymentMenu {

    private final PaymentController paymentController = new PaymentController();

    private final ResultView reservationResultView = new ResultView();
    private final ReservationController reservationController = new ReservationController();

    public void displayMenu(Scanner sc) {

        System.out.println("============== 결제 메뉴 ===============");

        System.out.println("현재 로그인한 회원 번호를 입력해주세요: ");
        int memberCode = sc.nextInt();
        sc.nextLine();

        while(true) {

            System.out.println("1. 결제 등록");
            System.out.println("9. 메인으로 돌아가기");
            System.out.println("원하시는 메뉴를 선택해주세요: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:

                    List<ReservationDTO> unpaidList = reservationController.getReservationsPaymentIsNull(memberCode);
                    // 미결제 예매 목록 출력
                    reservationResultView.printReservationList(unpaidList);

                    System.out.println("결제하실 예매 번호를 입력해주세요: ");
                    int reservationCode = sc.nextInt();
                    sc.nextLine();

                    if(!paymentController.isPayable(reservationCode, unpaidList)) {

                        System.out.println("이미 결제되었거나, 존재하지 않는 예매 번호입니다.");
                        continue;
                    }

                    int paymentAmount = paymentController.getTotalPaymentAmount(reservationCode);

                    if(paymentAmount <= 0) {
                        System.out.println("좌석 먼저 선택 후 결제 진행해주세요.");
                        continue;
                    }

                    System.out.println("해당 예매 건의 총 결제 금액: ");
                    System.out.println(paymentAmount);

                    System.out.println("결제 하시겠습니까?(Y/N)");
                    char input = sc.nextLine().charAt(0);

                    if(toUpperCase(input) == 'Y') {

                        System.out.println("결제 수단을 선택해주세요.");
                        String method = sc.nextLine();

                        Map<String, Object> requestMap = new HashMap<>();

                        requestMap.put("reservation_code", reservationCode);
                        requestMap.put("payment_amount", paymentAmount);
                        requestMap.put("payment_method", method);
                        requestMap.put("refund_status", false);         // default는 미환불 상태

                        // 결제 등록
                        paymentController.registerPayment(requestMap);
                    } else if(toUpperCase(input) == 'N') {
                        System.out.println("결제를 취소하고 다시 돌아갑니다.");
                    } else {
                        System.out.println("올바른 값을 입력해주세요.");
                    }
                    break;
                case 9:
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
            }
        }
    }
}
