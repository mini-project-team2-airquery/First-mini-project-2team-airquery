package com.ohgiraffers.airquery.seat.controller;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;
import com.ohgiraffers.airquery.seat.model.service.SeatService;

import java.util.List;

public class SeatController {

    // 좌석 관련 비지니스 로직 객체 생성 및 필드로 이동
    // final 사용하여 서비스 호출 및 결과를 반환
    private final SeatService seatService = new SeatService();

    // DB에 저장된 모든 좌석을 요청한다.
    public List<SeatDTO> getAllSeats() {

        return seatService.selectAllSeats();
    }

    // 모든 항공편에서 아직 예약되지 않은 좌석만 요청한다.
    public List<SeatDTO> getAvailableSeats() {

        return seatService.selectAvailableSeats();
    }

    // flightCode와 같은 항공편의 좌석을 예약 여부와 관계없이 요청한다.
    public List<SeatDTO> getSeatsByFlightCode(int flightCode) {
        return seatService.selectSeatsByFlightCode(flightCode);
    }

    // flightCode와 같은 항공편에서 예약 가능한 좌석만 요청한다.
    public List<SeatDTO> getAvailableSeatsByFlightCode(int flightCode) {

        return seatService.selectAvailableSeatsByFlightCode(flightCode);
    }

    // 예매와 연결하지 않고 좌석번호만으로 빈 좌석을 예약한다.
    public boolean reserveSeat(int memberCode, int seatCode) {
        return seatService.reserveSeat(memberCode, seatCode);
    }

    // 해당 회원과 항공편에 좌석 선택 완료 예매가 있는지 확인한다.
    public boolean hasReservationWithSeat(int memberCode, int flightCode) {

        return seatService.hasReservationWithSeat(memberCode, flightCode);
    }

    // 해당 회원의 기존 좌석을 newSeatCode 좌석으로 변경하도록 요청한다.
    public boolean changeSeat(int memberCode, int newSeatCode, int flightCode) {

        return seatService.changeSeat(memberCode, newSeatCode, flightCode);
    }

    // 로그인 회원이 현재 선택한 좌석의 등급 조회
    public String getSelectedSeatClass(int memberCode, int flightCode) {
        return seatService.getSelectedSeatClass(memberCode, flightCode);
    }

    // 같은 항공편에서 새로 선택하려는 빈 좌석의 등급 조회
    public String getAvailableSeatClass(int seatCode, int flightCode) {
        return seatService.getAvailableSeatClass(seatCode, flightCode);
    }

}
