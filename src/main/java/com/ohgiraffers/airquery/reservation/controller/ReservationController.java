package com.ohgiraffers.airquery.reservation.controller;

import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDetailDTO;
import com.ohgiraffers.airquery.reservation.model.service.ReservationService;
import com.ohgiraffers.airquery.reservation.view.ResultView;

import java.util.List;
import java.util.Map;

public class ReservationController {

    private final ReservationService reservationService = new ReservationService();
    private final ResultView resultView = new ResultView();

    /* 전체 예매 목록 조회 */
    public List<ReservationDTO> getAllReservations(int memberCode) {

        return reservationService.selectAllReservations(memberCode);
    }

    /* 결제 안된 예매 목록만 조회 */
    public List<ReservationDTO> getReservationsPaymentIsNull(int memberCode) {

        return reservationService.selectReservationsPaymentIsNull(memberCode);
    }

    /* 예매 상세 내역 조회(결제, 수하물, 좌석) */
    public ReservationDetailDTO getReservationDetail(int reservationCode, int memberCode) {

        return reservationService.findReservationDetail(reservationCode, memberCode);
    }

    /* 예매 등록 */
    public void registerReservation(Map<String, Object> requestMap) {

        ReservationDTO reservationDTO = new ReservationDTO();

        reservationDTO.setMemberCode((int) requestMap.get("memberCode"));
        reservationDTO.setFlightCode((int) requestMap.get("flightCode"));
        reservationDTO.setBaggageCarrying((boolean)  requestMap.get("baggageCarrying"));

        int result = reservationService.registerReservation(reservationDTO);

        if(result == 1) {
            resultView.printReservationDetail(ReservationDetailDTO.of(reservationDTO, null, null, null));
        } else {
            System.out.println("예매 등록 실패");
        }
    }
}
