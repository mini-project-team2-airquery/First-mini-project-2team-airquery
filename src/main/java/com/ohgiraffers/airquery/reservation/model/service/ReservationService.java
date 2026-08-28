package com.ohgiraffers.airquery.reservation.model.service;

import com.ohgiraffers.airquery.payment.model.dao.PaymentDAO;
import com.ohgiraffers.airquery.payment.model.dto.PaymentDTO;
import com.ohgiraffers.airquery.reservation.model.dao.ReservationDAO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDetailDTO;
import com.ohgiraffers.airquery.seat.model.dao.SeatDAO;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import static com.ohgiraffers.airquery.common.JDBCTemplate.commit;
import static com.ohgiraffers.airquery.common.JDBCTemplate.getConnection;
import static com.ohgiraffers.airquery.common.JDBCTemplate.rollback;

public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    // private final SeatDAO seatDAO = new SeatDAO();

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

        detail = ReservationDetailDTO.of(reservation, payment, null, null);

        return detail;
    }

    /* 예매 등록 */
    public int registerReservation(ReservationDTO dto) {

        Connection con = getConnection();

        int result = reservationDAO.insertReservation(con, dto);

        // 성공하면 트랜잭션 커밋
        if (result == 1) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result;
    }
}
