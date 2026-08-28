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

    public int insertPayment(Connection con, PaymentDTO payment) {

        PreparedStatement pstmt = null;
        int rs = 0;

        String insertQuery = "" +
                "INSERT INTO tbl_payment " +
                "(reservation_code, payment_amount, payment_method, refund_status) " +
                "VALUES (?, ?, ?, ?)" +
                "";

        // 총 결제 금액 계산 = 좌석 추가 금액 + 티켓 가격 (default 금액은 0원)
        int paymentAmount = getPaymentAmount(con, payment.getReservationCode());

        try {

            pstmt = con.prepareStatement(insertQuery);

            pstmt.setInt(1, payment.getReservationCode());
            pstmt.setInt(2, paymentAmount);
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setBoolean(4, payment.isRefundStatus());

            rs =  pstmt.executeUpdate();
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
}
