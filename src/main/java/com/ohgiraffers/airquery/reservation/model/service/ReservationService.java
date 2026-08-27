package com.ohgiraffers.airquery.reservation.model.service;

import com.ohgiraffers.airquery.payment.model.dao.PaymentDAO;
import com.ohgiraffers.airquery.payment.model.dto.PaymentDTO;
import com.ohgiraffers.airquery.reservation.model.dao.ReservationDAO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDetailDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import static com.ohgiraffers.airquery.common.JDBCTemplate.getConnection;

public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    /* 전체 예매 목록 조회 */
    public List<ReservationDTO> selectAllReservations(int memberCode) {

        Connection con = getConnection();

        List<ReservationDTO> reservationList = reservationDAO.findAll(con, memberCode);

        close(con);

        return reservationList;
    }

    /* 예매 상세 내역 조회 */
    public ReservationDetailDTO findReservationDetail(int reservationCode, int memberCode) {

        Connection con = getConnection();

        ReservationDetailDTO detail = null;

        // 예매(티켓), 결제, 수하물, 좌석
        ReservationDTO reservation = reservationDAO.findById(con, reservationCode, memberCode);
        PaymentDTO payment = paymentDAO.findByReservation(con, reservationCode);

        close(con);

        detail = ReservationDetailDTO.of(reservation, payment);

        return detail;
    }


}
