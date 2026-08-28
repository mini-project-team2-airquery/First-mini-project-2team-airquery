package com.ohgiraffers.airquery.baggage.controller;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;
import com.ohgiraffers.airquery.baggage.model.service.BaggageService;

import java.util.List;

/*
 * Controller
 * View/Menu에서 들어온 요청을 Service로 넘겨주는 역할을 한다.
 * 직접 SQL을 실행하지 않고, 실제 처리는 Service에게 맡긴다.
 */
public class BaggageController {

    // 수하물 기능을 처리할 Service 객체를 준비한다.
    private final BaggageService baggageService = new BaggageService();

    // 수하물 전체 목록을 조회한다.
    public List<BaggageDTO> getAllBaggages() {

        return baggageService.selectAllBaggages();
    }

    // 예매번호가 실제 예매 테이블에 존재하는지 확인한다.
    public boolean existsReservation(int reservationCode) {

        return baggageService.existsReservation(reservationCode);
    }

    // 예매할 때 수하물 신청을 했는지 확인한다.
    public boolean isBaggageCarrying(int reservationCode) {

        return baggageService.isBaggageCarrying(reservationCode);
    }

    // 특정 예매번호에 연결된 수하물 목록을 조회한다.
    // Menu -> Controller -> Service 순서로 호출된다.
    public List<BaggageDTO> getBaggagesByReservationCode(int reservationCode) {

        return baggageService.selectBaggagesByReservationCode(reservationCode);
    }

    // 수하물을 새로 등록한다.
    // 사용자가 입력한 예매번호와 무게가 담긴 DTO를 Service로 넘긴다.
    public boolean registBaggage(BaggageDTO baggage) {

        return baggageService.registBaggage(baggage);
    }

    // 수하물 무게를 변경한다.
    // 어떤 수하물을 바꿀지 baggageCode로 찾고, baggageWeight로 새 무게를 넘긴다.
    public boolean updateBaggageWeight(int baggageCode, double baggageWeight) {

        return baggageService.updateBaggageWeight(baggageCode, baggageWeight);
    }
}
