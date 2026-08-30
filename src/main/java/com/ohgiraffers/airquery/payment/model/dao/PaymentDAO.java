package com.ohgiraffers.airquery.payment.model.dao;

import com.ohgiraffers.airquery.payment.model.dto.PaymentDTO;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;

public class PaymentDAO {

    private Properties prop = new Properties();

    public PaymentDAO() {

        try {
            prop.load(new FileReader("src/main/java/com/ohgiraffers/airquery/config/connection-info.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 특정 예약 건에 대한 결제 정보 조회 */
    public PaymentDTO findByReservation(Connection con, int reservationCode) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PaymentDTO payment = null;

        try {

            pstmt = con.prepareStatement("" +
                    "select * " +
                    "from tbl_payment " +
                    "where tbl_payment.reservation_code = ?" +
                    "");

            pstmt.setInt(1, reservationCode);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                payment = new PaymentDTO();

                payment.setPaymentCode(rs.getInt("payment_code"));
                payment.setReservationCode(rs.getInt("reservation_code"));
                payment.setPaymentAmount(rs.getInt("payment_amount"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setRefundStatus(rs.getBoolean("refund_status"));
                payment.setCreatedAt(rs.getObject("first_created_date", LocalDateTime.class));
                payment.setUpdatedAt(rs.getObject("last_modified_date", LocalDateTime.class));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }

        return payment;
    }

    /* 해당 예매에 결제 이력이 이미 존재하는지 확인 (reservation_code는 UNIQUE라 평생 최대 1건) */
    public boolean existsPayment(Connection con, int reservationCode) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        String query = "SELECT payment_code FROM tbl_payment WHERE reservation_code = ?";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, reservationCode);
            rs = pstmt.executeQuery();
            exists = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }

        return exists;
    }

    /*
     * 결제 등록
     * tbl_payment.reservation_code는 UNIQUE라서 같은 예매로는 새 row를 또 넣을 수 없다.
     * 이미 결제 이력(취소/환불 포함)이 있는 예매면 그 row를 그대로 갱신해서 재결제 처리한다.
     */
    public int insertPayment(Connection con, PaymentDTO payment) {

        PreparedStatement pstmt = null;
        int rs = 0;

        String insertQuery = "" +
                "INSERT INTO tbl_payment " +
                "(reservation_code, payment_amount, payment_method, refund_status) " +
                "VALUES (?, ?, ?, ?)" +
                "";

        String updateQuery = "" +
                "UPDATE tbl_payment SET " +
                "payment_amount = ?, payment_method = ?, refund_status = ? " +
                "WHERE reservation_code = ?" +
                "";

        // 총 결제 금액 계산 = 좌석 추가 금액 + 티켓 가격 (default 금액은 0원)
        int paymentAmount = getPaymentAmount(con, payment.getReservationCode());

        try {

            if (existsPayment(con, payment.getReservationCode())) {

                pstmt = con.prepareStatement(updateQuery);
                pstmt.setInt(1, paymentAmount);
                pstmt.setString(2, payment.getPaymentMethod());
                pstmt.setBoolean(3, payment.isRefundStatus());
                pstmt.setInt(4, payment.getReservationCode());

            } else {

                pstmt = con.prepareStatement(insertQuery);
                pstmt.setInt(1, payment.getReservationCode());
                pstmt.setInt(2, paymentAmount);
                pstmt.setString(3, payment.getPaymentMethod());
                pstmt.setBoolean(4, payment.isRefundStatus());
            }

            rs = pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return rs;
    }

    public int getPaymentAmount(Connection con, int reservationCode) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int paymentAmount = 0;

        String calculateAmountQuery = "" +
                "SELECT f.flight_ticket_price, s.additional_amount " +
                "                FROM tbl_reservation r " +
                "                JOIN tbl_flight f ON f.flight_code = r.flight_code " +
                "                JOIN tbl_seat s ON r.seat_code = s.seat_code " +
                "                WHERE r.reservation_code = ?" +
                "";

        try {

            pstmt = con.prepareStatement(calculateAmountQuery);
            pstmt.setInt(1, reservationCode);

            // 결제, 좌석 등록 내역이 없다면 추가 금액 0원 처리
            rs =  pstmt.executeQuery();
            if(rs.next()) {

                paymentAmount = rs.getInt("flight_ticket_price") +
                        rs.getInt("additional_amount");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }

        return paymentAmount;
    }

    /* 결제 취소 */
    public int deletePayment(Connection con, int reservationCode) {

        PreparedStatement pstmt = null;
        int rs = 0;

        String query = "" +
                "UPDATE tbl_payment " +
                "SET refund_status = true " +
                "WHERE reservation_code = ?" +
                "";

        try {

            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, reservationCode);

            rs = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return rs;
    }
}
