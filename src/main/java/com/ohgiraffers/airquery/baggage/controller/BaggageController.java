package com.ohgiraffers.airquery.baggage.controller;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;
import com.ohgiraffers.airquery.baggage.model.service.BaggageService;

import java.util.List;

public class BaggageController {

    // 수하물 기능을 처리할 Service 객체를 준비한다.
    private final BaggageService baggageService = new BaggageService();

    // 수하물 전체 목록을 조회한다.
    public List<BaggageDTO> getAllBaggages() {

        return baggageService.selectAllBaggages();
    }

    // 로그인한 회원이 YES로 신청한 예매의 수하물만 조회한다
    public List<BaggageDTO> getBaggagesByMemberCode(int memberCode) {
        return baggageService.selectBaggagesByMemberCode(memberCode);
    }

    // 입력한 예매번호가 존재하지 않으면 true를 반환한다.
    public boolean isReservationMissing(int reservationCode) {

        return !baggageService.existsReservation(reservationCode);
    }

    // 예매할 때 수하물 지참 여부가 NO이면 true를 반환한다.
    public boolean isBaggageNotCarried(int reservationCode) {

        return !baggageService.isBaggageCarrying(reservationCode);
    }

    // 특정 예매번호에 연결된 수하물 목록을 조회한다.
    // Menu -> Controller -> Service 순서로 호출된다.
    public List<BaggageDTO> getBaggagesByReservationCode(int reservationCode) {

        return baggageService.selectBaggagesByReservationCode(reservationCode);
    }

    // 수하물을 새로 등록한다. , 사용자가 입력한 예매번호와 무게가 담긴 DTO를 Service로 넘긴다.
    public boolean registBaggage(BaggageDTO baggage) {

        return baggageService.registBaggage(baggage);
    }

    // 수하물 무게를 변경한다.
    public boolean updateBaggageWeight(int reservationCode, int baggageCode, double baggageWeight) {

        return baggageService.updateBaggageWeight(reservationCode, baggageCode, baggageWeight);
    }
}
