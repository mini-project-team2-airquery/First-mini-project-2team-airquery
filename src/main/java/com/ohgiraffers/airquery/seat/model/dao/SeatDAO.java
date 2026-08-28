package com.ohgiraffers.airquery.seat.model.dao;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;

public class SeatDAO {
    /*
    * 좌석 전체 조회 메서드
    * 좌석 정보를 조회
    */
    public List<SeatDTO> selectAllSeats(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<SeatDTO> seatList = new ArrayList<>(); // 조회된 좌석 리스트


        String query = "SELECT seat_code, flight_code, seat_id, flight_class, " +
                "additional_amount, is_reserved " +
                "FROM tbl_seat " +
                "ORDER BY seat_code";

        try {

            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                SeatDTO seat = new SeatDTO();

                seat.setSeatCode(rset.getInt("seat_code"));
                seat.setFlightCode(rset.getInt("flight_code"));
                seat.setSeatId(rset.getString("seat_id"));
                seat.setFlightClass(rset.getString("flight_class"));
                seat.setAdditionalAmount(rset.getInt("additional_amount"));
                seat.setReserved(rset.getBoolean("is_reserved"));

                seatList.add(seat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return seatList;
    }

    /*
     * 예약 가능한 좌석 조회 메서드
     * is_reserved 값이 false인 좌석만 조회한다.
     */
    public List<SeatDTO> selectAvailableSeats(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<SeatDTO> seatList = new ArrayList<>();

        String query = "SELECT seat_code, flight_code, seat_id, flight_class, " +
                "additional_amount, is_reserved " +
                "FROM tbl_seat " +
                "WHERE is_reserved = false " +
                "ORDER BY seat_code";

        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                SeatDTO seat = new SeatDTO();

                seat.setSeatCode(rset.getInt("seat_code"));
                seat.setFlightCode(rset.getInt("flight_code"));
                seat.setSeatId(rset.getString("seat_id"));
                seat.setFlightClass(rset.getString("flight_class"));
                seat.setAdditionalAmount(rset.getInt("additional_amount"));
                seat.setReserved(rset.getBoolean("is_reserved"));

                seatList.add(seat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return seatList;
    }

    /*
     * 좌석 예약 메서드
     * 사용자가 입력한 좌석번호의 is_reserved 값을 true로 변경한다.
     */
    public int reserveSeat(Connection con, int seatCode) {

        PreparedStatement pstmt = null;

        // UPDATE 성공 여부를 담는 변수
        // 1이면 예약 성공, 0이면 예약 실패
        int result = 0;

        /*
         * seat_code = ?
         * 사용자가 입력한 좌석번호를 의미 한다
         *
         * is_reserved = false
         * 아직 예약되지 않은 좌석만 예약할 수 있게 하는 조건이다
         */
        String query = "UPDATE tbl_seat " +
                "SET is_reserved = true " +
                "WHERE seat_code = ? " +
                "AND is_reserved = false";

        try {
            pstmt = con.prepareStatement(query);

            // SQL의 ? 자리에 사용자가 입력한 좌석번호를 넣는다.
            pstmt.setInt(1, seatCode);

            // INSERT, UPDATE, DELETE는 executeUpdate()로 실행한다.
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return result;
    }

    /*
     * 좌석 변경 메서드
     * seat_code로 변경할 좌석을 찾고, 나머지 좌석 정보를 수정한다.
     */
    public int updateSeat(Connection con, SeatDTO seat) {

        PreparedStatement pstmt = null;
        int result = 0;

        String query = "UPDATE tbl_seat " +
                "SET flight_code = ?, " +
                "seat_id = ?, " +
                "flight_class = ?, " +
                "additional_amount = ?, " +
                "is_reserved = ? " +
                "WHERE seat_code = ?";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, seat.getFlightCode());
            pstmt.setString(2, seat.getSeatId());
            pstmt.setString(3, seat.getFlightClass());
            pstmt.setInt(4, seat.getAdditionalAmount());
            pstmt.setBoolean(5, seat.isReserved());
            pstmt.setInt(6, seat.getSeatCode());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return result;
    }
}
