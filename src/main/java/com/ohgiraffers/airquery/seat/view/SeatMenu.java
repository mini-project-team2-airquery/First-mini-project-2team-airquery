package com.ohgiraffers.airquery.seat.view;

import com.ohgiraffers.airquery.seat.controller.SeatController;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.util.List;
import java.util.Scanner;


public class SeatMenu {

    private final SeatController seatController = new SeatController();
    private final SeatView seatView = new SeatView();

    public void displayMenu(Scanner sc) {

        while (true) {

            seatView.displaySeatMenu();

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    selectAllSeats(sc);
                    break;

                case "2":
                    reserveSeat(sc);
                    break;

                case "3":
                    updateSeat(sc);
                    break;

                case "9":
                    return;

                default:
                    seatView.displayInvalidMenuMessage();
                    break;
            }
        }
    }

    private void selectAllSeats(Scanner sc) {

        List<SeatDTO> seatList = seatController.getAllSeats();

        seatView.displaySeatList(seatList);

        while (true) {
            seatView.displayAfterSelectAllSeatsMenu();

            String input = sc.nextLine();

            switch (input) {
                case "1":
                    return;

                case "2":
                    selectAvailableSeats();
                    break;

                default:
                    seatView.displayInvalidAfterSelectAllSeatsMenuMessage();
                    break;
            }
        }
    }

    private void selectAvailableSeats() {

        List<SeatDTO> seatList = seatController.getAvailableSeats();

        seatView.displayAvailableSeatList(seatList);
    }

    private void reserveSeat(Scanner sc) {

        seatView.displayInputSeatCodeMessage();
        String seatCodeInput = sc.nextLine();

        if (!seatCodeInput.matches("[0-9]+")) {
            seatView.displaySeatCodeNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        int seatCode = Integer.parseInt(seatCodeInput);

        boolean isSuccess = seatController.reserveSeat(seatCode);

        seatView.displayReserveSeatResult(isSuccess);
        backToSeatMenu(sc);
    }

    private void updateSeat(Scanner sc) {

        seatView.displayInputUpdateSeatCodeMessage();
        String seatCodeInput = sc.nextLine();

        if (!seatCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        seatView.displayInputFlightCodeMessage();
        String flightCodeInput = sc.nextLine();

        if (!flightCodeInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        seatView.displayInputSeatIdMessage();
        String seatId = sc.nextLine();

        seatView.displayInputFlightClassMessage();
        String flightClass = sc.nextLine();

        seatView.displayInputAdditionalAmountMessage();
        String additionalAmountInput = sc.nextLine();

        if (!additionalAmountInput.matches("[0-9]+")) {
            seatView.displayNumberOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        seatView.displayInputReservedMessage();
        String reservedInput = sc.nextLine();

        if (!reservedInput.equals("true") && !reservedInput.equals("false")) {
            seatView.displayBooleanOnlyMessage();
            backToSeatMenu(sc);
            return;
        }

        SeatDTO seat = new SeatDTO();

        seat.setSeatCode(Integer.parseInt(seatCodeInput));
        seat.setFlightCode(Integer.parseInt(flightCodeInput));
        seat.setSeatId(seatId);
        seat.setFlightClass(flightClass);
        seat.setAdditionalAmount(Integer.parseInt(additionalAmountInput));
        seat.setReserved(Boolean.parseBoolean(reservedInput));

        boolean isSuccess = seatController.updateSeat(seat);

        seatView.displayUpdateSeatResult(isSuccess);
        backToSeatMenu(sc);
    }

    private void backToSeatMenu(Scanner sc) {

        while (true) {
            seatView.displayBackToSeatMenuMessage();

            String backInput = sc.nextLine();

            if (backInput.equals("1")) {
                break;
            }

            seatView.displayInvalidBackInputMessage();
        }
    }
}
