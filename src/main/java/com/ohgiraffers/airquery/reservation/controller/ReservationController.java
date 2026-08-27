package com.ohgiraffers.airquery.reservation.controller;

import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDetailDTO;
import com.ohgiraffers.airquery.reservation.model.service.ReservationService;

import java.util.List;

public class ReservationController {

    private final ReservationService reservationService = new ReservationService();

    public List<ReservationDTO> getAllReservations(int memberCode) {

        return reservationService.selectAllReservations(memberCode);
    }

    public ReservationDetailDTO getReservationDetail(int reservationCode, int memberCode) {

        return reservationService.findReservationDetail(reservationCode, memberCode);
    }
}
