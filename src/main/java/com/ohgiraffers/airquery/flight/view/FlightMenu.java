package com.ohgiraffers.airquery.flight.view;

import com.ohgiraffers.airquery.flight.controller.FlightController;
import com.ohgiraffers.airquery.flight.model.dto.FlightDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class FlightMenu {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final FlightController flightController = new FlightController();

    public void displayMenu(Scanner sc) {
        while (true) {

            System.out.println();
            System.out.println("===== 항공편 메뉴 =====");
            System.out.println("1. 항공편 전체 조회");
            System.out.println("2. 항공사별 조회");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    selectAllFlight();
                    break;

                case "2":
                    selectByAirline(sc);
                    break;

                case "9":
                    return;

                default:
                    System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
                    break;
            }
        }
    }

    private void selectAllFlight() {
        List<FlightDTO> flightList = flightController.selectAllFlight();

        System.out.println();
        System.out.println("================================ 항공편 전체 목록 ================================");

        if (flightList == null) {
            System.out.println("항공편 조회 중 오류가 발생했습니다.");
            return;
        }

        if (flightList.isEmpty()) {
            System.out.println("등록된 항공편이 없습니다.");
            return;
        }

        System.out.printf("%-6s %-12s %-8s %-8s %-18s %-18s %-12s %-8s %12s%n",
                "번호", "항공사", "출발지", "도착지", "출발시간", "도착시간", "기종", "게이트", "가격");
        System.out.println("----------------------------------------------------------------------------------");

        for (FlightDTO flight : flightList) {
            System.out.printf("%-6d %-12s %-8s %-8s %-18s %-18s %-12s %-8s %,12d원%n",
                    flight.getCode(),
                    flight.getAirlineName(),
                    flight.getDeparture(),
                    flight.getArrival(),
                    flight.getDepartureTime().format(DATE_TIME_FORMATTER),
                    flight.getArrivalTime().format(DATE_TIME_FORMATTER),
                    flight.getAirplaneType(),
                    flight.getGateNumber(),
                    flight.getTicketPrice());
        }
    }

    private void selectByAirline(Scanner sc) {
        System.out.print("조회할 항공사명을 입력하세요: ");

        String airlineName = sc.nextLine();

        List<FlightDTO> flightList = flightController.selectByAirline(airlineName);

        System.out.println();
        System.out.println("================================ 항공편 전체 목록 ================================");

        if (flightList == null) {
            System.out.println("항공편 조회 중 오류가 발생했습니다.");
            return;
        }

        if (flightList.isEmpty()) {
            System.out.println("등록된 항공편이 없습니다.");
            return;
        }

        System.out.printf("%-6s %-12s %-8s %-8s %-18s %-18s %-12s %-8s %12s%n",
                "번호", "항공사", "출발지", "도착지", "출발시간", "도착시간", "기종", "게이트", "가격");
        System.out.println("----------------------------------------------------------------------------------");

        for (FlightDTO flight : flightList) {
            System.out.printf("%-6d %-12s %-8s %-8s %-18s %-18s %-12s %-8s %,12d원%n",
                    flight.getCode(),
                    flight.getAirlineName(),
                    flight.getDeparture(),
                    flight.getArrival(),
                    flight.getDepartureTime().format(DATE_TIME_FORMATTER),
                    flight.getArrivalTime().format(DATE_TIME_FORMATTER),
                    flight.getAirplaneType(),
                    flight.getGateNumber(),
                    flight.getTicketPrice());
        }
    }
}
