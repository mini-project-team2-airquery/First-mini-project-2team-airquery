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

    public PaymentDTO findByReservation(Connection con, int reservationCode) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PaymentDTO payment = null;

        try {

            pstmt = con.prepareStatement("" +
                    "select * " +
                    "from tbl_payment " +
                    "where tbl_payment.reservation_code = ?");

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
}
