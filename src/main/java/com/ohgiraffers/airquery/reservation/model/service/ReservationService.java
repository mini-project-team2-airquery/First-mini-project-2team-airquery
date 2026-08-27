package com.ohgiraffers.airquery.reservation.model.service;

import com.ohgiraffers.airquery.reservation.model.dao.ReservationDAO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import static com.ohgiraffers.airquery.common.JDBCTemplate.getConnection;

public class ReservationService {

    private ReservationDAO reservationDAO = new ReservationDAO();

    public List<ReservationDTO> selectAllReservations(int memberCode) {

        Connection con = getConnection();

        List<ReservationDTO> reservationList = reservationDAO.findAll(con, memberCode);

        close(con);

        return reservationList;
    }

    public ReservationDTO findById(int reservationCode, int memberCode) {

        Connection con = getConnection();

        ReservationDTO reservation = reservationDAO.findById(con, reservationCode, memberCode);

        close(con);

        return reservation;
    }
}
