package com.ohgiraffers.airquery.seat.model.service;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;
import com.ohgiraffers.airquery.seat.model.dao.SeatDAO;

import java.sql.Connection;
import java.util.List;

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
     * 좌석 예약 서비스 메서드
     * DB 연결을 만들고, DAO에게 좌석 예약
     */
    public boolean reserveSeat(int seatCode, int flightCode) {

        Connection con = getConnection();

        int seatResult = seatDAO.reserveSeatForFlight(con, seatCode, flightCode);
        int reservationResult = 0;

        if (seatResult > 0) {
            reservationResult = seatDAO.updateReservationSeatCode(con, seatCode, flightCode);
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
    public boolean hasReservationWithoutSeat(int flightCode) {

        Connection con = getConnection();

        boolean result = seatDAO.hasReservationWithoutSeat(con, flightCode);

        close(con);

        return result;
    }
}
