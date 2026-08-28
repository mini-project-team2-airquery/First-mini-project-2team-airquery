package com.ohgiraffers.airquery.seat.model.service;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;
import com.ohgiraffers.airquery.seat.model.dao.SeatDAO;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import static com.ohgiraffers.airquery.common.JDBCTemplate.commit;
import static com.ohgiraffers.airquery.common.JDBCTemplate.getConnection;
import static com.ohgiraffers.airquery.common.JDBCTemplate.rollback;

public class SeatService {

    private final SeatDAO seatDAO = new SeatDAO();

    public List<SeatDTO> selectAllSeats() {

        Connection con = getConnection();

        List<SeatDTO> seatList = seatDAO.selectAllSeats(con);

        close(con);

        return seatList;
    }

    /*
     * 예약 가능한 좌석 조회 서비스 메서드
     */
    public List<SeatDTO> selectAvailableSeats() {

        Connection con = getConnection();

        List<SeatDTO> seatList = seatDAO.selectAvailableSeats(con);

        close(con);

        return seatList;
    }

    /*
     * 특정 항공편의 예약 가능한 좌석 조회 서비스 메서드
     */
    public List<SeatDTO> selectAvailableSeatsByFlightCode(int flightCode) {

        Connection con = getConnection();

        List<SeatDTO> seatList = seatDAO.selectAvailableSeatsByFlightCode(con, flightCode);

        close(con);

        return seatList;
    }

    /*
     * 회원의 예매 중 아직 좌석을 선택하지 않은 예매 목록을 조회하는 메서드
     */
    public Map<Integer, Integer> selectReservationsWithoutSeat(int memberCode) {

        Connection con = getConnection();

        Map<Integer, Integer> reservationMap = seatDAO.selectReservationsWithoutSeat(con, memberCode);

        close(con);

        return reservationMap;
    }

    /*
     * 좌석 예약 서비스 메서드
     * DB 연결을 만들고, DAO에게 좌석 예약
     */
    public boolean reserveSeat(int memberCode, int reservationCode, int seatCode, int flightCode) {

        Connection con = getConnection();

        int seatResult = seatDAO.reserveSeatForFlight(con, seatCode, flightCode);
        int reservationResult = 0;

        if (seatResult > 0) {
            reservationResult = seatDAO.updateReservationSeatCode(con, memberCode, reservationCode, seatCode, flightCode);
        }

        if (seatResult > 0 && reservationResult > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return seatResult > 0 && reservationResult > 0;
    }

    /*
     * 특정 항공편에 좌석 선택 안 된 예매가 있는지 확인하는 메서드
     */
    public boolean hasReservationWithoutSeat(int memberCode, int flightCode) {

        Connection con = getConnection();

        boolean result = seatDAO.hasReservationWithoutSeat(con, memberCode, flightCode);

        close(con);

        return result;
    }

    /*
     * 특정 항공편에 이미 좌석 선택한 예매가 있는지 확인하는 메서드
     */
    public boolean hasReservationWithSeat(int memberCode, int flightCode) {

        Connection con = getConnection();

        boolean result = seatDAO.hasReservationWithSeat(con, memberCode, flightCode);

        close(con);

        return result;
    }

    /*
     * 좌석 변경 서비스 메서드
     * 기존 좌석은 예약 가능으로 바꾸고, 새 좌석은 예약됨으로 바꾸고, 예매 테이블의 seat_code를 새 좌석으로 변경한다.
     */
    public boolean changeSeat(int memberCode, int newSeatCode, int flightCode) {

        Connection con = getConnection();

        int oldSeatCode = seatDAO.selectReservedSeatCode(con, memberCode, flightCode);
        int newSeatResult = 0;
        int reservationResult = 0;
        int oldSeatResult = 0;

        if (oldSeatCode != 0 && oldSeatCode != newSeatCode) {
            newSeatResult = seatDAO.reserveSeatForFlight(con, newSeatCode, flightCode);

            if (newSeatResult > 0) {
                reservationResult = seatDAO.changeReservationSeatCode(con, memberCode, oldSeatCode, newSeatCode, flightCode);
            }

            if (reservationResult > 0) {
                oldSeatResult = seatDAO.cancelSeatReservation(con, oldSeatCode);
            }
        }

        if (newSeatResult > 0 && reservationResult > 0 && oldSeatResult > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return newSeatResult > 0 && reservationResult > 0 && oldSeatResult > 0;
    }

}
