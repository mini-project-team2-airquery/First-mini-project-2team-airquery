package com.ohgiraffers.airquery.reservation.view;

import com.ohgiraffers.airquery.flight.view.FlightMenu;
import com.ohgiraffers.airquery.reservation.controller.ReservationController;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDetailDTO;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class ReservationMenu {

    private final ReservationController reservationController = new ReservationController();

    private final ResultView resultView = new ResultView();

    private final FlightMenu flightMenu = new FlightMenu();

    public void displayMenu(Scanner sc) {

        System.out.println("=================== 예매 관리 화면 ===================");

        System.out.println("현재 로그인한 회원 번호를 입력해주세요: ");
        int memberCode = sc.nextInt();
        sc.nextLine();
        
        // 진짜 존재하는 회원 번호인지 체크 필요하긴 함

        while(true) {

            System.out.println("1. 예매 목록 조회");
            System.out.println("2. 예매 상세 조회");
            System.out.println("3. 예매 등록");
            System.out.println("4. 예매 취소");
            System.out.println("5. 예매 변경");
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

                    resultView.printReservationList(reservationList);

                    break;
                case 2:
                    System.out.println("============== 나의 예매 내역 ==============");

                    List<ReservationDTO> list =
                            reservationController.getAllReservations(memberCode);

                    if(list.isEmpty()) {
                        System.out.println("현재 예매 내역이 존재하지 않습니다.");
                        continue;
                    }

                    resultView.printReservationList(list);

                    System.out.println("위 예매 내역 중 상세 정보를 조회할 예매 번호를 선택해주세요: ");

                    int selectedReservationCode = sc.nextInt();
                    sc.nextLine();

                    ReservationDetailDTO reservation =
                            reservationController.getReservationDetail(selectedReservationCode, memberCode);

                    if(reservation == null || reservation.isDeleted()) {
                        System.out.println("현재 예매 내역이 존재하지 않습니다.");
                        continue;
                    }

                    resultView.printReservationDetail(reservation);
                    break;
                case 3:
                    System.out.println("============== 항공편 리스트 =============");
                    flightMenu.selectAllFlight();

                    int selectedFlightCode;
                    boolean selectedBaggageCarrying = false;

                    System.out.println("예매를 원하시는 항공편을 선택해주세요: ");
                    selectedFlightCode = sc.nextInt();
                    sc.nextLine();

                    System.out.println("수하물 지참 여부를 입력해주세요(Y/N): ");
                    char baggageCarrying = sc.next().charAt(0);

                    if (toUpperCase(baggageCarrying) == 'Y') {
                        selectedBaggageCarrying = true;
                    } else if(toUpperCase(baggageCarrying) == 'N') {
                        selectedBaggageCarrying = false;
                    } else {
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                    }

                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("memberCode", memberCode);
                    requestMap.put("flightCode", selectedFlightCode);
                    requestMap.put("baggageCarrying", selectedBaggageCarrying);

                    reservationController.registerReservation(requestMap);
                    break;
                case 4:
                    resultView.printReservationList(reservationController.getAllReservations(memberCode));
                    System.out.println("취소할 예매 번호를 입력해주세요.");

                    selectedReservationCode = sc.nextInt();
                    sc.nextLine();

                    ReservationDetailDTO cancleReservation =
                            reservationController.getReservationDetail(selectedReservationCode, memberCode);

                    if(cancleReservation == null) {
                        System.out.println("예매 내역이 이미 취소되었거나, 존재하지 않는 예매 번호입니다.");
                        continue;
                    }

                    System.out.println("정말 취소하시겠습니까? (Y/N)");
                    char check = sc.nextLine().charAt(0);

                    if(toUpperCase(check) == 'Y') {

                        // 예매 내역 삭제(소프트 삭제)
                        reservationController.cancleReservation(selectedReservationCode, memberCode);
                    } else {
                        System.out.println("취소 실패했습니다. 다시 시도해주세요.");
                    }
                    break;
                case 5:

                    list = reservationController.getAllReservations(memberCode);

                    if(list.isEmpty()) {
                        System.out.println("현재 예매 내역이 존재하지 않습니다.");
                        continue;
                    }

                    resultView.printReservationList(list);

                    System.out.println("변경할 예매 번호를 입력해주세요.");
                    int targetReservationCode = sc.nextInt();
                    sc.nextLine();

                    ReservationDetailDTO targetReservation =
                            reservationController.getReservationDetail(targetReservationCode, memberCode);

                    if(targetReservation == null || targetReservation.isDeleted()) {
                        System.out.println("예매 내역이 존재하지 않거나 이미 취소된 예매입니다.");
                        continue;
                    }

                    System.out.println("해당 예매 건의 무엇을 변경하시겠습니까?");
                    System.out.println("1. 항공편 번호");
                    System.out.println("2. 좌석 등급");
                    System.out.println("3. 수하물 지참 여부");


                    int changeType = sc.nextInt();
                    sc.nextLine();

                    switch (changeType) {
                        case 1:
                            // 항공편 자체는 바꿀 수 없으므로 추가 입력 없이 안내만 진행
                            System.out.println("항공편 변경을 원하시면 새로운 항공편을 예매하시거나 " +
                                    "기존 예매를 취소하고 새로운 항공편으로 다시 예매해주세요.");
                            break;
                        case 2:
                            System.out.println("============ 예약 가능한 다른 등급 좌석 목록 ===============");
                            // 선택한 예매건 항공편의 예약 가능한 다른 등급의 좌석 목록 출력
                            List<SeatDTO> availableList = reservationController.
                                    getAvailableSeatsForFlight(
                                            targetReservation.getFlightCode(), targetReservation.getFlightClass()
                                    );

                            if(availableList == null || availableList.isEmpty()) {
                                System.out.println("해당 항공편에 현재 다른 등급의 예약 가능한 좌석 목록이 존재하지 않습니다.");
                                continue;
                            }

                            resultView.printAvailableSeatList(availableList);
                            System.out.println("원하시는 좌석 번호를 입력해주세요.");
                            int selectedSeatCode = sc.nextInt();
                            sc.nextLine();

                            System.out.println("좌석 등급을 변경할 경우, " +
                                    "기존 결제가 취소되고 다시 결제하셔야 합니다. 그래도 하시겠습니까?(Y/N)");
                            char confirm = sc.nextLine().charAt(0);

                            if(toUpperCase(confirm) == 'Y') {
                                // 좌석 변경 실행
                                reservationController.changeSeatClass(
                                        targetReservation.getSeatCode(), selectedSeatCode,
                                        targetReservation.getFlightCode(), targetReservationCode);
                                System.out.println("좌석이 변경되었습니다. 결제 메뉴에서 다시 결제해주세요.");
                            } else {
                                System.out.println("좌석 변경이 취소되었습니다.");
                            }
                            break;
                        case 3:
                            System.out.println("현재 수하물 지참 여부: " + targetReservation.isBaggageCarrying());
                            if(targetReservation.isBaggageCarrying() &&
                                    (targetReservation.getBaggageList() == null || targetReservation.getBaggageList().isEmpty())) {
                                System.out.println("수하물 지참 여부를 FALSE로 변경하시겠습니까? (Y/N)");
                                confirm = sc.nextLine().charAt(0);
                                if(toUpperCase(confirm) == 'Y') {
                                    reservationController.changeBaggageCarrying(targetReservationCode, false);
                                    System.out.println("성공적으로 변경되었습니다.");
                                } else {
                                    System.out.println("변경이 취소되었습니다.");
                                }
                            } else if(targetReservation.isBaggageCarrying() &&
                                    (targetReservation.getBaggageList() != null && !targetReservation.getBaggageList().isEmpty())) {
                                System.out.println("기존에 등록된 수하물이 있으므로 수하물 지참 여부를 변경할 수 없습니다.");
                            } else if(!targetReservation.isBaggageCarrying()) {
                                System.out.println("수하물 지참 여부를 TRUE로 변경하시겠습니까? (Y/N)");
                                confirm = sc.nextLine().charAt(0);
                                if(toUpperCase(confirm) == 'Y') {
                                    reservationController.changeBaggageCarrying(targetReservationCode, true);
                                    System.out.println("성공적으로 변경되었습니다.");
                                } else {
                                    System.out.println("변경이 취소되었습니다.");
                                }
                            }
                            break;
                        default:
                            System.out.println("유효하지 않은 번호입니다. 다시 입력해주세요.");
                            break;
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
