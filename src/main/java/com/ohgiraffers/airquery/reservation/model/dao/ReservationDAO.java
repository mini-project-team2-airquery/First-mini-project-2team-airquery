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

        List<ReservationDTO> reservationList = new ArrayList<>();

        String query = "" +
                "SELECT * FROM tbl_reservation " +
                "WHERE member_code=? " +
                "AND is_deleted = FALSE";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);

            rs = pstmt.executeQuery();

            while(rs.next()) {

                ReservationDTO reservation = new ReservationDTO();

                reservation.setReservationCode(rs.getInt("reservation_code"));
                reservation.setMemberCode(rs.getInt("member_code"));
                reservation.setFlightCode(rs.getInt("flight_code"));
                reservation.setSeatCode(rs.getInt("seat_code"));
                reservation.setBaggageCarrying(rs.getBoolean("baggage_carrying"));
                reservation.setCreatedAt(rs.getObject("first_created_date", LocalDateTime.class));
                reservation.setUpdatedAt(rs.getObject("last_modified_date", LocalDateTime.class));

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

    /* 결제가 안된 예매 내역 조회 */
    public List<ReservationDTO> findByPaymentIsNull (Connection con, int memberCode) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<ReservationDTO> reservationList = new ArrayList<>();

        // 결제 테이블에 존재하지 않는 예약 코드만 조회
        String query = "SELECT * FROM tbl_reservation t " +
                "WHERE t.member_code = ? " +
                "AND t.reservation_code NOT IN " +
                "   (SELECT p.reservation_code FROM tbl_payment p)" +
                "AND is_deleted = FALSE";
        
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);

            rs = pstmt.executeQuery();

            while(rs.next()) {

                ReservationDTO reservation = new ReservationDTO();

                reservation.setReservationCode(rs.getInt("reservation_code"));
                reservation.setMemberCode(rs.getInt("member_code"));
                reservation.setFlightCode(rs.getInt("flight_code"));
                reservation.setSeatCode(rs.getInt("seat_code"));
                reservation.setBaggageCarrying(rs.getBoolean("baggage_carrying"));
                reservation.setCreatedAt(rs.getObject("first_created_date", LocalDateTime.class));
                reservation.setUpdatedAt(rs.getObject("last_modified_date", LocalDateTime.class));

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

        String query = "" +
                "SELECT * FROM tbl_reservation " +
                "WHERE reservation_code = ? " +
                "AND member_code = ? " +
                "AND is_deleted = FALSE" +
                "";

        try {

            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, reservationCode);
            pstmt.setInt(2, memberCode);

            rs = pstmt.executeQuery();

            if(rs.next()) {

                reservation = new ReservationDTO();

                reservation.setReservationCode(rs.getInt("reservation_code"));
                reservation.setMemberCode(rs.getInt("member_code"));
                reservation.setFlightCode(rs.getInt("flight_code"));
                reservation.setSeatCode(rs.getInt("seat_code"));
                reservation.setBaggageCarrying(rs.getBoolean("baggage_carrying"));
                reservation.setCreatedAt(rs.getObject("first_created_date", LocalDateTime.class));
                reservation.setUpdatedAt(rs.getObject("last_modified_date", LocalDateTime.class));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }

        return reservation;
    }

    /* 예약 등록 */
    public int insertReservation(Connection con, ReservationDTO dto) {

        PreparedStatement pstmt = null;
        int rs = 0;
        ResultSet rset = null;

        // 한 사람이 같은 항공편에 대해서 이미 생성된 예매 내역이 있는지 체크
        String checkAvailabilityQuery = "" +
                "SELECT * " +
                "FROM tbl_reservation " +
                "WHERE member_code = ? " +
                "AND flight_code = ? " +
                "AND is_deleted = FALSE" +
                "" ;

        // 좌석번호는 기본적으로 미선택, null이 default
        String insertQuery = "" +
                "INSERT INTO tbl_reservation " +
                "(member_code, flight_code, baggage_carrying) " +
                "VALUES (?, ?, ?)" +
                "";

        try {

            // 해당 항공편에 대한 예매 가능 여부 체크
            pstmt = con.prepareStatement(checkAvailabilityQuery);
            pstmt.setInt(1, dto.getMemberCode());
            pstmt.setInt(2, dto.getFlightCode());

            rset = pstmt.executeQuery();

            // 일단 이렇게 막아놓음
            if(rset.next()) {
                System.out.println("이미 예매 완료한 항공편입니다.");
                throw new RuntimeException();
            }

            pstmt = con.prepareStatement(insertQuery);
            pstmt.setInt(1, dto.getMemberCode());               // 회원 번호
            pstmt.setInt(2, dto.getFlightCode());               // 항공편 선택
            pstmt.setBoolean(3, dto.isBaggageCarrying());       // 수하물 지참 여부 선택 (True or False)

            rs = pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return rs;
    }

    /* 예약 취소 */
    public int deleteReservation(Connection con, int reservationCode) {

        int result = 0;
        PreparedStatement pstmt = null;

        String deleteBaggageQuery = "" +
                "DELETE FROM tbl_baggage " +
                "WHERE reservation_code = ?";

        String updateSeatQuery = "" +
                "UPDATE tbl_seat SET is_reserved = FALSE " +
                "WHERE seat_code = (SELECT seat_code FROM tbl_reservation WHERE reservation_code = ?)";

        String updatePaymentQuery = "" +
                "UPDATE tbl_payment SET refund_status = TRUE " +
                "WHERE reservation_code = ?";

        String updateReservationQuery = "" +
                "UPDATE tbl_reservation SET is_deleted = TRUE, seat_code = null " +
                "WHERE reservation_code = ?";
        try {

            // 1. 수하물 삭제 (있으면 지워지고, 없으면 0건 삭제되고 끝)
            pstmt = con.prepareStatement(deleteBaggageQuery);
            pstmt.setInt(1, reservationCode);
            pstmt.executeUpdate();

            // 2. 좌석 선점 해제
            pstmt = con.prepareStatement(updateSeatQuery);
            pstmt.setInt(1, reservationCode);
            pstmt.executeUpdate();

            // 3. 결제 환불 처리
            pstmt = con.prepareStatement(updatePaymentQuery);
            pstmt.setInt(1, reservationCode);
            pstmt.executeUpdate();

            // 4. 예매 취소
            pstmt = con.prepareStatement(updateReservationQuery);
            pstmt.setInt(1, reservationCode);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return result;
    }

    /* 특정 예약 건에 대한 좌석 및 수하물 정보 조회 */

    /* 예약 변경 (취소 후 재결제) */
}
