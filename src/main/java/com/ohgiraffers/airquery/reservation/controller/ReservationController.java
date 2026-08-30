package com.ohgiraffers.airquery.reservation.controller;

import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDetailDTO;
import com.ohgiraffers.airquery.reservation.model.service.ReservationService;
import com.ohgiraffers.airquery.reservation.view.ResultView;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

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

        ReservationDTO savedReservation = reservationService.registerReservation(reservationDTO);

        if(savedReservation != null) {
            resultView.printReservationDetail(ReservationDetailDTO.of(savedReservation, null, null, null));
        } else {
            System.out.println("예매 등록 실패");
        }
    }

    /* 예매 취소 */
    public void cancleReservation(int reservationCode, int memberCode) {

        int result = reservationService.cancleReservation(reservationCode, memberCode);

        if(result == 1) {
            System.out.println("예매 취소 성공");
        } else {
            System.out.println("예매 취소 실패");
        }
    }
    
    /* 예매 변경 */
    
    // 좌석 등급 변경 시 보여줄, 현재 등급 제외 예약 가능 좌석 목록 조회
    public List<SeatDTO> getAvailableSeatsForFlight(int flightCode, String currentFightClass) {

        return reservationService.selectAvailableOtherClassSeats(flightCode, currentFightClass);
    }
    
    public void changeSeatClass(int oldSeatCode, int newSeatCode, int flightCode, int reservationCode) {
        
        int result =  reservationService.changeSeatClass(oldSeatCode, newSeatCode, flightCode, reservationCode);
        
        if(result == 1) {
            System.out.println("좌석 등급 변경 성공");
        } else {
            System.out.println("좌석 등급 변경 실패");
        }
    }
    
    // 수하물 지참여부 변경
    public void changeBaggageCarrying(int reservationCode, boolean baggageCarrying) {
        
        int result = reservationService.changeBaggageCarrying(reservationCode, baggageCarrying);
        
        if(result == 1) {
            System.out.println("수하물 지참 여부 변경 성공");
        } else {
            System.out.println("수하물 지참 여부 변경 실패");
        }
    }
    
}
