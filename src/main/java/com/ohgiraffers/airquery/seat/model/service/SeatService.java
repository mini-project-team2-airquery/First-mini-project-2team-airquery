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
    public boolean reserveSeat(int seatCode) {

        Connection con = getConnection();

        int result = seatDAO.reserveSeat(con, seatCode);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }
}
