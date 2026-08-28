package com.ohgiraffers.airquery.baggage.view;

import com.ohgiraffers.airquery.baggage.controller.BaggageController;
import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;

import java.util.List;
import java.util.Scanner;

public class BaggageMenu {

    private final BaggageController baggageController = new BaggageController();
    private final BaggageView baggageView = new BaggageView();

    public void displayMenu(Scanner sc) {

        while (true) {

            baggageView.displayBaggageMenu();

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    registBaggage(sc);
                    break;

                case "2":
                    selectBaggagesByReservationCode(sc);
                    break;

                case "3":
                    updateBaggageWeight(sc);
                    break;

                case "9":
                    return;

                default:
                    baggageView.displayInvalidMenuMessage();
                    break;
            }
        }
    }

    private void selectBaggagesByReservationCode(Scanner sc) {

        baggageView.displayInputReservationCodeMessage();
        String reservationCodeInput = sc.nextLine();

        if (!reservationCodeInput.matches("[0-9]+")) {
            baggageView.displayNumberOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        int reservationCode = Integer.parseInt(reservationCodeInput);

        List<BaggageDTO> baggageList = baggageController.getBaggagesByReservationCode(reservationCode);

        baggageView.displayBaggageList(baggageList);
        backToBaggageMenu(sc);
    }

    private void registBaggage(Scanner sc) {

        baggageView.displayInputReservationCodeMessage();
        String reservationCodeInput = sc.nextLine();

        if (!reservationCodeInput.matches("[0-9]+")) {
            baggageView.displayNumberOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        baggageView.displayInputBaggageWeightMessage();
        String baggageWeightInput = sc.nextLine();

        if (!baggageWeightInput.matches("[0-9]+(\\.[0-9]+)?")) {
            baggageView.displayWeightOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        BaggageDTO baggage = new BaggageDTO();

        baggage.setReservationCode(Integer.parseInt(reservationCodeInput));
        baggage.setBaggageWeight(Double.parseDouble(baggageWeightInput));

        boolean isSuccess = baggageController.registBaggage(baggage);

        baggageView.displayRegistBaggageResult(isSuccess);
        backToBaggageMenu(sc);
    }

    private void updateBaggageWeight(Scanner sc) {

        baggageView.displayInputBaggageCodeMessage();
        String baggageCodeInput = sc.nextLine();

        if (!baggageCodeInput.matches("[0-9]+")) {
            baggageView.displayNumberOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        baggageView.displayInputBaggageWeightMessage();
        String baggageWeightInput = sc.nextLine();

        if (!baggageWeightInput.matches("[0-9]+(\\.[0-9]+)?")) {
            baggageView.displayWeightOnlyMessage();
            backToBaggageMenu(sc);
            return;
        }

        int baggageCode = Integer.parseInt(baggageCodeInput);
        double baggageWeight = Double.parseDouble(baggageWeightInput);

        boolean isSuccess = baggageController.updateBaggageWeight(baggageCode, baggageWeight);

        baggageView.displayUpdateBaggageWeightResult(isSuccess);
        backToBaggageMenu(sc);
    }

    private void backToBaggageMenu(Scanner sc) {

        while (true) {
            baggageView.displayBackToBaggageMenuMessage();

            String backInput = sc.nextLine();

            if (backInput.equals("1")) {
                break;
            }

            baggageView.displayInvalidBackInputMessage();
        }
    }
}
