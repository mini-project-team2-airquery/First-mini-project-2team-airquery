package com.ohgiraffers.airquery.reservation.view;

import com.ohgiraffers.airquery.reservation.controller.ReservationController;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;

import java.util.List;
import java.util.Scanner;

public class ReservationMenu {

    private ReservationController reservationController = new ReservationController();

    public void displayMenu() {

        Scanner sc = new Scanner(System.in);

        System.out.println("=================== 예매 관리 화면 ===================");

        System.out.println("현재 로그인한 회원 번호를 입력해주세요: ");
        int memberCode = sc.nextInt();
        sc.nextLine();
        
        // 진짜 존재하는 회원 번호인지 체크 필요하긴 함

        while(true) {

            System.out.println("1. 예매 목록 조회");
            System.out.println("2. 예매 상세 조회");
            System.out.println("9. 메인 화면으로 돌아가기");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    List<ReservationDTO> reservationList =
                            reservationController.getAllReservations(memberCode);

                    if(reservationList.isEmpty()) {
                        System.out.println("현재 예매 내역이 존재하지 않습니다.");
                        continue;
                    }

                    reservationList.forEach(System.out::println);

                    break;
                case 2:
                    System.out.println("============== 나의 예매 내역 ==============");
                    reservationController.getAllReservations(memberCode)
                            .stream()
                            .forEach(System.out::println);

                    System.out.println("위 예매 내역 중 상세 정보를 조회할 예매 번호를 선택해주세요: ");
                    int selectedReservationCode = sc.nextInt();
                    sc.nextLine();

                    ReservationDTO reservation =
                            reservationController.getReservationDetail(selectedReservationCode, memberCode);

                    if(reservation == null) {
                        System.out.println("현재 예매 내역이 존재하지 않습니다.");
                        continue;
                    }

                    // 현재는 순수 예매 정보만 출력
                    // 추후에 결제 정보 및 좌석 정보, 수하물 위탁 여부를 고려하여 전체 상세 내역을 출력할 예정
                    System.out.println("============= 선택한 예매의 상세 내역 =============");
                    System.out.println(reservation);
                    break;
                case 9:
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
            }
        }
    }
}
