package com.ohgiraffers.airquery.reservation.model.dao;

import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;

public class ReservationDAO {

    private Properties prop = new Properties();

    public ReservationDAO() {

        try {
            prop.load(new FileReader("src/main/java/com/ohgiraffers/airquery/config/connection-info.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 예약 목록 조회 */
    public List<ReservationDTO> findAll(Connection con, int memberCode) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<ReservationDTO> reservationList = null;

        String query = "SELECT * FROM tbl_reservation WHERE member_code=? ";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);

            rs = pstmt.executeQuery();

            reservationList = new ArrayList<>();

            while(rs.next()) {

                ReservationDTO reservation = new ReservationDTO();

                reservation.setReservationCode(rs.getInt("reservation_code"));
                reservation.setMemberCode(rs.getInt("member_code"));
                reservation.setFlightCode(rs.getInt("flight_code"));
                reservation.setSeatCode(rs.getInt("seat_code"));
                reservation.setBaggageCarrying(rs.getBoolean("baggage_carrying"));
                //reservation.setCreatedAt(rs.getObject("first_created_date", LocalDateTime.class));
                //reservation.setUpdatedAt(rs.getObject("last_modified_date", LocalDateTime.class));

                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }

        return reservationList;
    }

    /* 예약 상세 조회 */
    public ReservationDTO findById(Connection con, int reservationCode, int memberCode) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ReservationDTO reservation = null;

        String query = "SELECT * FROM tbl_reservation WHERE reservation_code = ? AND member_code = ?";

        try {

            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, reservationCode);
            pstmt.setInt(2, memberCode);

            rs = pstmt.executeQuery();
            rs.next();

            reservation = new ReservationDTO();

            reservation.setReservationCode(rs.getInt("reservation_code"));
            reservation.setMemberCode(rs.getInt("member_code"));
            reservation.setFlightCode(rs.getInt("flight_code"));
            reservation.setSeatCode(rs.getInt("seat_code"));
            reservation.setBaggageCarrying(rs.getBoolean("baggage_carrying"));
            reservation.setCreatedAt(rs.getObject("first_created_date", LocalDateTime.class));
            reservation.setUpdatedAt(rs.getObject("last_modified_date", LocalDateTime.class));
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }

        return reservation;
    }

    /* 예약 등록 */

    /* 예약 취소 */

    /* 예약 변경 (취소 후 재결제) */
}
