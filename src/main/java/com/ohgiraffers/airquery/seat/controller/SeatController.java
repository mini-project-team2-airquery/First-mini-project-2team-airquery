package com.ohgiraffers.airquery.seat.controller;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;
import com.ohgiraffers.airquery.seat.model.service.SeatService;

import java.util.List;
import java.util.Map;
// 좌석 관련 요청을 받아 제어하는 컨트롤러

public class SeatController {

    // 좌석 관련 비지니스 로직 객체 생성 및 필드로 이동
    // final 사용하여 서비스 호출 및 결과를 반환
    private final SeatService seatService = new SeatService();

    // 좌석 목록 조회하는 메서드
    public List<SeatDTO> getAllSeats() {

        return seatService.selectAllSeats();
    }

    // 예약 가능한 좌석 목록 조회하는 메서드
    public List<SeatDTO> getAvailableSeats() {

        return seatService.selectAvailableSeats();
    }

    // 특정 항공편의 예약 가능한 좌석 목록 조회하는 메서드
    public List<SeatDTO> getAvailableSeatsByFlightCode(int flightCode) {

        return seatService.selectAvailableSeatsByFlightCode(flightCode);
    }

    // 회원의 예매 중 아직 좌석을 선택하지 않은 예매 목록을 조회하는 메서드
    public Map<Integer, Integer> getReservationsWithoutSeat(int memberCode) {

        return seatService.selectReservationsWithoutSeat(memberCode);
    }

    // 특정 항공편의 좌석을 예약하는 메서드
    public boolean reserveSeat(int memberCode, int reservationCode, int seatCode, int flightCode) {

        return seatService.reserveSeat(memberCode, reservationCode, seatCode, flightCode);
    }

    // 특정 항공편에 좌석 선택 안 된 예매가 있는지 확인하는 메서드
    public boolean hasReservationWithoutSeat(int memberCode, int flightCode) {

        return seatService.hasReservationWithoutSeat(memberCode, flightCode);
    }

    // 특정 항공편에 이미 좌석 선택한 예매가 있는지 확인하는 메서드
    public boolean hasReservationWithSeat(int memberCode, int flightCode) {

        return seatService.hasReservationWithSeat(memberCode, flightCode);
    }

    // 이미 선택한 좌석을 새 좌석으로 변경하는 메서드
    public boolean changeSeat(int memberCode, int newSeatCode, int flightCode) {

        return seatService.changeSeat(memberCode, newSeatCode, flightCode);
    }

}
