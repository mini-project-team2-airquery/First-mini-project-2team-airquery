package com.ohgiraffers.airquery.airline.menu;

import com.ohgiraffers.airquery.airline.controller.AirlineController;
import com.ohgiraffers.airquery.airline.dto.AirlineDTO;
import com.ohgiraffers.airquery.airline.view.AirlineView;

import java.util.List;
import java.util.Scanner;

public class AirlineMenu {

    private final AirlineController airlineController =
            new AirlineController();

    private final AirlineView resultView =
            new AirlineView();


    public void displayMenu() {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("          항공사 관리");
            System.out.println("=================================");
            System.out.println("1. 항공사 등록");
            System.out.println("2. 항공사 조회");
            System.out.println("3. 항공사 정보 변경");
            System.out.println("4. 항공사 삭제");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.println("=================================");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    insertAirline(sc);
                    break;

                case "2":
                    selectAllAirlines();
                    break;

                case "3":
                    updateAirline(sc);
                    break;

                case "4":
                    deleteAirline(sc);
                    break;

                case "9":
                    return;

                default:
                    System.out.println(
                            "잘못된 입력입니다. 다시 선택해주세요."
                    );
                    break;
            }
        }
    }


    // FR-07 항공사 조회
    private void selectAllAirlines() {

        List<AirlineDTO> airlineList =
                airlineController.selectAllAirlines();

        resultView.printAirlineList(airlineList);
    }


    // FR-06 항공사 등록
    private void insertAirline(Scanner sc) {

        System.out.println();
        System.out.println("========== 항공사 등록 ==========");

        System.out.print("항공사명을 입력해주세요 : ");
        String airlineName = sc.nextLine();

        System.out.print("고객센터 번호를 입력해주세요 : ");
        String customerServiceNumber = sc.nextLine();

        boolean result =
                airlineController.insertAirline(
                        airlineName,
                        customerServiceNumber
                );

        if (result) {
            resultView.printSuccessMessage(
                    "항공사가 정상적으로 등록되었습니다."
            );
        } else {
            resultView.printErrorMessage(
                    "항공사 등록에 실패했습니다."
            );
        }
    }


    // FR-08 항공사 변경
    private void updateAirline(Scanner sc) {

        System.out.println();
        System.out.println("========== 항공사 정보 변경 ==========");

        try {

            System.out.print("변경할 항공사 번호를 입력해주세요 : ");
            int airlineCode =
                    Integer.parseInt(sc.nextLine());

            System.out.print("변경할 항공사명을 입력해주세요 : ");
            String airlineName = sc.nextLine();

            System.out.print("변경할 고객센터 번호를 입력해주세요 : ");
            String customerServiceNumber = sc.nextLine();

            boolean result =
                    airlineController.updateAirline(
                            airlineCode,
                            airlineName,
                            customerServiceNumber
                    );

            if (result) {
                resultView.printSuccessMessage(
                        "항공사 정보가 정상적으로 변경되었습니다."
                );
            } else {
                resultView.printErrorMessage(
                        "해당 항공사를 찾을 수 없습니다."
                );
            }

        } catch (NumberFormatException e) {

            resultView.printErrorMessage(
                    "항공사 번호는 숫자로 입력해주세요."
            );
        }
    }


    // FR-09 항공사 삭제
    private void deleteAirline(Scanner sc) {

        System.out.println();
        System.out.println("========== 항공사 삭제 ==========");

        try {

            System.out.print("삭제할 항공사 번호를 입력해주세요 : ");
            int airlineCode =
                    Integer.parseInt(sc.nextLine());

            System.out.print(
                    "해당 항공사를 삭제하시겠습니까? (Y/N) : "
            );

            String confirm = sc.nextLine();

            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("항공사 삭제를 취소했습니다.");
                return;
            }

            boolean result =
                    airlineController.deleteAirline(airlineCode);

            if (result) {
                resultView.printSuccessMessage(
                        "항공사가 정상적으로 삭제되었습니다."
                );
            } else {
                resultView.printErrorMessage(
                        "해당 항공사를 찾을 수 없습니다."
                );
            }

        } catch (NumberFormatException e) {

            resultView.printErrorMessage(
                    "항공사 번호는 숫자로 입력해주세요."
            );

        } catch (RuntimeException e) {

            resultView.printErrorMessage(
                    "해당 항공사를 사용하는 항공편이 존재하거나 " +
                            "삭제할 수 없는 상태입니다."
            );
        }
    }
}