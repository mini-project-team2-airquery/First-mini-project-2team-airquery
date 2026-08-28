package com.ohgiraffers.airquery.flight.view;

import com.ohgiraffers.airquery.airline.menu.AirlineMenu;
import com.ohgiraffers.airquery.flight.controller.FlightController;
import com.ohgiraffers.airquery.flight.model.dto.FlightDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class FlightMenu {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final FlightController flightController = new FlightController();

    public void displayAdminMenu(Scanner sc) {
        while (true) {

            System.out.println();
            System.out.println("===== 항공편 메뉴 =====");
            System.out.println("1. 항공편 전체 조회");
            System.out.println("2. 항공사별 조회");
            System.out.println("3. 항공편 생성");
            System.out.println("4. 항공편 수정");
            System.out.println("5. 항공편 삭제");
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

                case "3":
                    insertFlight(sc);
                    break;

                case "4":
                    displayUpdateMenu(sc);
                    break;

                case "5":
                    deleteFlight(sc);
                    break;

                case "9":
                    return;

                default:
                    System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
                    break;
            }
        }
    }

    public void displayNormalMenu(Scanner sc) {
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

    public void selectAllFlight() {
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

    public void selectByAirline(Scanner sc) {

        AirlineMenu airlineMenu = new AirlineMenu();
        airlineMenu.selectAllAirlines();

        System.out.print("조회할 항공사명을 입력하세요: ");

        String airlineName = sc.nextLine();

        List<FlightDTO> flightList = flightController.selectByAirline(airlineName);

        System.out.println();
        System.out.println("================================ 항공사별 항공편 목록 ================================");

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

    private void insertFlight(Scanner sc) {

        try {
            System.out.println();
            System.out.println("===== 항공편 등록 =====");

            AirlineMenu airlineMenu = new AirlineMenu();
            airlineMenu.selectAllAirlines();

            System.out.print("항공사 번호: ");
            String airlineCodeInput = sc.nextLine().trim();

            System.out.print("출발지: ");
            String departure = sc.nextLine().trim();

            System.out.print("도착지: ");
            String arrival = sc.nextLine().trim();

            System.out.print("출발시간(yyyy-MM-dd HH:mm): ");
            String departureTimeInput = sc.nextLine().trim();

            System.out.print("도착시간(yyyy-MM-dd HH:mm): ");
            String arrivalTimeInput = sc.nextLine().trim();

            System.out.print("비행기 기종: ");
            String airplaneType = sc.nextLine().trim();

            System.out.print("게이트 번호: ");
            String gateNumber = sc.nextLine().trim();

            System.out.print("티켓 가격: ");
            String ticketPriceInput = sc.nextLine().trim();

            // 모든 입력값의 필수 입력 여부 확인
            if (airlineCodeInput.isEmpty()
                    || departure.isEmpty()
                    || arrival.isEmpty()
                    || departureTimeInput.isEmpty()
                    || arrivalTimeInput.isEmpty()
                    || airplaneType.isEmpty()
                    || gateNumber.isEmpty()
                    || ticketPriceInput.isEmpty()) {

                System.out.println("모든 항목은 반드시 입력해야 합니다.");
                return;
            }

            int airlineCode = Integer.parseInt(airlineCodeInput);
            int ticketPrice = Integer.parseInt(ticketPriceInput);

            LocalDateTime departureTime =
                    LocalDateTime.parse(departureTimeInput, DATE_TIME_FORMATTER);

            LocalDateTime arrivalTime =
                    LocalDateTime.parse(arrivalTimeInput, DATE_TIME_FORMATTER);

            if (airlineCode <= 0) {
                System.out.println("항공사 번호는 1 이상이어야 합니다.");
                return;
            }

            if (!flightController.existsAirline(airlineCode)) {
                System.out.println("존재하지 않는 항공사 번호입니다.");
                return;
            }

            if (departure.equals(arrival)) {
                System.out.println("출발지와 도착지는 같을 수 없습니다.");
                return;
            }

            if (!arrivalTime.isAfter(departureTime)) {
                System.out.println("도착시간은 출발시간보다 이후여야 합니다.");
                return;
            }

            if (ticketPrice <= 0) {
                System.out.println("티켓 가격은 0보다 커야 합니다.");
                return;
            }

            FlightDTO flight = new FlightDTO();

            flight.setAirlineCode(airlineCode);
            flight.setDeparture(departure);
            flight.setArrival(arrival);
            flight.setDepartureTime(departureTime);
            flight.setArrivalTime(arrivalTime);
            flight.setAirplaneType(airplaneType);
            flight.setGateNumber(gateNumber);
            flight.setTicketPrice(ticketPrice);

            boolean result = flightController.insertFlight(flight);

            if (result) {
                System.out.println("항공편이 성공적으로 등록되었습니다.");
            } else {
                System.out.println("항공편 등록에 실패했습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("항공사 번호와 티켓 가격은 숫자로 입력해야 합니다.");

        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("날짜와 시간을 yyyy-MM-dd HH:mm 형식으로 입력해야 합니다.");
        }
    }

    public void displayUpdateMenu(Scanner sc) {
        while (true) {

            System.out.println();
            System.out.println("===== 항공편 수정 메뉴 =====");
            System.out.println("1. 항공편 정보 수정");
            System.out.println("2. 항공편 일정 수정");
            System.out.println("3. 항공편 가격 수정");
            System.out.println("9. 이전 메뉴로 돌아가기");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    updateFlightInfo(sc);
                    break;

                case "2":
                    updateFlightSchedule(sc);
                    break;

                case "3":
                    updateFlightPrice(sc);
                    break;

                case "9":
                    return;

                default:
                    System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
                    break;
            }
        }
    }

    private void updateFlightInfo(Scanner sc) {

        try {
            System.out.println();
            System.out.println("===== 항공편 정보 수정 =====");

            System.out.print("항공편 번호: ");
            String codeInput = sc.nextLine().trim();

            System.out.print("출발지: ");
            String departure = sc.nextLine().trim();

            System.out.print("도착지: ");
            String arrival = sc.nextLine().trim();

            System.out.print("비행기 기종: ");
            String airplaneType = sc.nextLine().trim();

            System.out.print("게이트 번호: ");
            String gateNumber = sc.nextLine().trim();

            // 모든 입력값의 필수 입력 여부 확인
            if (codeInput.isEmpty()
                    || departure.isEmpty()
                    || arrival.isEmpty()
                    || airplaneType.isEmpty()
                    || gateNumber.isEmpty()) {

                System.out.println("모든 항목은 반드시 입력해야 합니다.");
                return;
            }

            int code = Integer.parseInt(codeInput);

            if (code <= 0) {
                System.out.println("항공편 번호는 1 이상이어야 합니다.");
                return;
            }

            if (!flightController.existsFlight(code)) {
                System.out.println("존재하지 않는 항공편 번호입니다.");
                return;
            }

            if (departure.equals(arrival)) {
                System.out.println("출발지와 도착지는 같을 수 없습니다.");
                return;
            }

            FlightDTO flight = new FlightDTO();

            flight.setCode(code);
            flight.setDeparture(departure);
            flight.setArrival(arrival);
            flight.setAirplaneType(airplaneType);
            flight.setGateNumber(gateNumber);

            boolean result = flightController.updateFlightInfo(flight);

            if (result) {
                System.out.println("항공편이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("항공편 수정에 실패했습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("항공편 번호는 숫자로 입력해야 합니다.");

        }
    }

    private void updateFlightSchedule(Scanner sc) {

        try {
            System.out.println();
            System.out.println("===== 항공편 일정 수정 =====");

            System.out.print("항공편 번호: ");
            String codeInput = sc.nextLine().trim();

            System.out.print("출발시간(yyyy-MM-dd HH:mm): ");
            String departureTimeInput = sc.nextLine().trim();

            System.out.print("도착시간(yyyy-MM-dd HH:mm): ");
            String arrivalTimeInput = sc.nextLine().trim();

            // 모든 입력값의 필수 입력 여부 확인
            if (codeInput.isEmpty()
                    || departureTimeInput.isEmpty()
                    || arrivalTimeInput.isEmpty()) {

                System.out.println("모든 항목은 반드시 입력해야 합니다.");
                return;
            }

            int code = Integer.parseInt(codeInput);

            LocalDateTime departureTime =
                    LocalDateTime.parse(departureTimeInput, DATE_TIME_FORMATTER);

            LocalDateTime arrivalTime =
                    LocalDateTime.parse(arrivalTimeInput, DATE_TIME_FORMATTER);

            if (code <= 0) {
                System.out.println("항공편 번호는 1 이상이어야 합니다.");
                return;
            }

            if (!flightController.existsFlight(code)) {
                System.out.println("존재하지 않는 항공편 번호입니다.");
                return;
            }

            if (!arrivalTime.isAfter(departureTime)) {
                System.out.println("도착시간은 출발시간보다 이후여야 합니다.");
                return;
            }

            FlightDTO flight = new FlightDTO();

            flight.setCode(code);
            flight.setDepartureTime(departureTime);
            flight.setArrivalTime(arrivalTime);

            boolean result = flightController.updateFlightSchedule(flight);

            if (result) {
                System.out.println("항공편이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("항공편 수정에 실패했습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("항공편 번호는 숫자로 입력해야 합니다.");

        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("날짜와 시간을 yyyy-MM-dd HH:mm 형식으로 입력해야 합니다.");
        }
    }

    private void updateFlightPrice(Scanner sc) {

        try {
            System.out.println();
            System.out.println("===== 항공편 가격 수정 =====");

            System.out.print("항공편 번호: ");
            String codeInput = sc.nextLine().trim();

            System.out.print("티켓 가격: ");
            String ticketPriceInput = sc.nextLine().trim();

            // 모든 입력값의 필수 입력 여부 확인
            if (codeInput.isEmpty()
                    || ticketPriceInput.isEmpty()) {

                System.out.println("모든 항목은 반드시 입력해야 합니다.");
                return;
            }

            int code = Integer.parseInt(codeInput);
            int ticketPrice = Integer.parseInt(ticketPriceInput);

            if (code <= 0) {
                System.out.println("항공편 번호는 1 이상이어야 합니다.");
                return;
            }

            if (!flightController.existsFlight(code)) {
                System.out.println("존재하지 않는 항공편 번호입니다.");
                return;
            }

            if (ticketPrice <= 0) {
                System.out.println("티켓 가격은 0보다 커야 합니다.");
                return;
            }

            FlightDTO flight = new FlightDTO();

            flight.setCode(code);
            flight.setTicketPrice(ticketPrice);

            boolean result = flightController.updateFlightPrice(flight);

            if (result) {
                System.out.println("항공편이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("항공편 수정에 실패했습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("항공편 번호와 티켓 가격은 숫자로 입력해야 합니다.");

        }
    }

    private void deleteFlight(Scanner sc) {

        try {
            System.out.println();
            System.out.println("===== 항공편 삭제 =====");

            System.out.print("항공편 번호: ");
            String codeInput = sc.nextLine().trim();

            // 모든 입력값의 필수 입력 여부 확인
            if (codeInput.isEmpty()) {

                System.out.println("항공편 번호를 반드시 입력해야 합니다.");
                return;
            }

            int code = Integer.parseInt(codeInput);

            if (code <= 0) {
                System.out.println("항공편 번호는 1 이상이어야 합니다.");
                return;
            }

            if (!flightController.existsFlight(code)) {
                System.out.println("존재하지 않는 항공편 번호입니다.");
                return;
            }

            if(flightController.existsReservation(code)) {
                System.out.println("예매 내역이 존재하는 항공편은 삭제할 수 없습니다.");
                return;
            }

            FlightDTO flight = new FlightDTO();

            flight.setCode(code);

            boolean result = flightController.deleteFlight(flight);

            if (result) {
                System.out.println("항공편이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("항공편 삭제가 실패했습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("항공편 번호는 숫자로 입력해야 합니다.");

        }
    }
}
