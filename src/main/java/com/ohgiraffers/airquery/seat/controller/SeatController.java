package com.ohgiraffers.airquery.seat.controller;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;
import com.ohgiraffers.airquery.seat.model.service.SeatService;

import java.util.List;
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

    // 특정 좌석 예약하는 메서드
    public boolean reserveSeat(int seatCode) {

        return seatService.reserveSeat(seatCode);
    }

}