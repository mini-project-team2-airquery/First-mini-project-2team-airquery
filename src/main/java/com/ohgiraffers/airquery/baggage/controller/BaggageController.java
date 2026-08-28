package com.ohgiraffers.airquery.baggage.controller;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;
import com.ohgiraffers.airquery.baggage.model.service.BaggageService;

import java.util.List;

// 수하물 관련 요청을 받아서 Service로 넘겨주는 클래스
public class BaggageController {

    private final BaggageService baggageService = new BaggageService();

    // 수하물 전체 목록을 조회한다.
    public List<BaggageDTO> getAllBaggages() {

        return baggageService.selectAllBaggages();
    }

    // 특정 예매번호에 연결된 수하물 목록을 조회한다.
    public List<BaggageDTO> getBaggagesByReservationCode(int reservationCode) {

        return baggageService.selectBaggagesByReservationCode(reservationCode);
    }

    // 수하물을 새로 등록한다.
    public boolean registBaggage(BaggageDTO baggage) {

        return baggageService.registBaggage(baggage);
    }

    // 수하물 무게를 변경한다.
    public boolean updateBaggageWeight(int baggageCode, double baggageWeight) {

        return baggageService.updateBaggageWeight(baggageCode, baggageWeight);
    }
}
