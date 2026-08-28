package com.ohgiraffers.airquery.reservation.model.service;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;
import com.ohgiraffers.airquery.payment.model.dao.PaymentDAO;
import com.ohgiraffers.airquery.payment.model.dto.PaymentDTO;
import com.ohgiraffers.airquery.reservation.model.dao.ReservationDAO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDetailDTO;
import com.ohgiraffers.airquery.seat.model.dao.SeatDAO;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /* 결제 안된 예매 목록 조회 */
    public List<ReservationDTO> selectReservationsPaymentIsNull(int memberCode) {

        Connection con = getConnection();

        List<ReservationDTO> reservationList = reservationDAO.findByPaymentIsNull(con, memberCode);

        close(con);

        return reservationList;
    }

    /* 예매 상세 내역 조회 */
    public ReservationDetailDTO findReservationDetail(int reservationCode, int memberCode) {

        Connection con = getConnection();

        ReservationDetailDTO detail = null;

        // 예매(티켓), 결제, 수하물, 좌석
        ReservationDTO reservation = reservationDAO.findById(con, reservationCode, memberCode);
        if(reservation == null) {
            close(con);
            return null;
        }

        // 결제 정보
        PaymentDTO payment = paymentDAO.findByReservation(con, reservationCode);

        // 수하물, 좌석 정보 같이 조회
        Map<String, Object> otherInfo =
                reservationDAO.getSeatAndBaggageInfoOfReservation(con, reservationCode);

        close(con);

        detail = ReservationDetailDTO.of(reservation, payment,
                (SeatDTO) otherInfo.get("seatDTO"), (List<BaggageDTO>) otherInfo.get("baggageList"));

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

    /* 예매 취소 */
    public int cancleReservation(int reservationCode, int memberCode) {

        Connection con = getConnection();

        // ReservationDTO dto = reservationDAO.findById(con, reservationCode, memberCode);

        // 예매 정보, 결제 정보, 수하물 정보, 좌석 정보 모두 취소처리되어야 함
        int result = reservationDAO.deleteReservation(con, reservationCode);

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
