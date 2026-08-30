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
                reservationDAO.getSeatAndBaggageInfoOfReservation(con, reservationCode, reservation.isBaggageCarrying());

        close(con);

        detail = ReservationDetailDTO.of(reservation, payment,
                (SeatDTO) otherInfo.get("seatDTO"), (List<BaggageDTO>) otherInfo.get("baggageList"));

        return detail;
    }

    /* 예매 등록 */
    public ReservationDTO registerReservation(ReservationDTO dto) {

        Connection con = getConnection();

        int result = reservationDAO.insertReservation(con, dto);

        ReservationDTO savedReservation = null;

        // 성공하면 트랜잭션 커밋
        if (result == 1) {
            commit(con);
            // 예매번호, 예매일시 등 DB에서 채워진 값까지 포함해서 다시 조회
            savedReservation = reservationDAO.findByMemberAndFlight(con, dto.getMemberCode(), dto.getFlightCode());
        } else {
            rollback(con);
        }

        close(con);

        return savedReservation;
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

    /* 예매 변경 */
    // case 1. 좌석 등급 변경 시, 기존 결제 정보 취소 후 재결제 (기존 항공편 티켓가격 + 변경된 좌석의 추가 금액으로 재결제)
    /*
        case 2. 수하물 지참 여부 변경
        1. N -> Y: 그냥 변경
        2. Y -> N: 기존 수하물이 있는 경우, 예외처리. 취소 후 재예매 안내 문구 출력
    */
    // case 3. 항공편 번호 변경 시, 취소 후 재예매 안내 문구 출력

    /* 특정 항공편의 예약 가능한 다른 등급의 좌석 목록 조회 */
    public List<SeatDTO> selectAvailableOtherClassSeats(int flightCode, String currentFightClass) {

        Connection con = getConnection();

        List<SeatDTO> seatList = reservationDAO.getAvailableOtherClassByFlightCode(con, flightCode, currentFightClass);

        close(con);

        return seatList;
    }

    public int changeSeatClass(int oldSeatCode, int newSeatCode, int flightCode, int reservationCode) {

        Connection con = getConnection();

        int result = reservationDAO.updateSeatClass(con,  oldSeatCode, newSeatCode, flightCode, reservationCode);

        // 성공하면 트랜잭션 커밋
        if (result == 1) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result;
    }

    public int changeBaggageCarrying(int reservationCode, boolean baggageCarrying) {

        Connection con = getConnection();

        int result = reservationDAO.updateBaggageCarrying(con, reservationCode, baggageCarrying);

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
