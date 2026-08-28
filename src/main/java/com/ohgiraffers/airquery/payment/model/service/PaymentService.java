package com.ohgiraffers.airquery.payment.model.service;

import com.ohgiraffers.airquery.payment.model.dao.PaymentDAO;
import com.ohgiraffers.airquery.payment.model.dto.PaymentDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.*;

public class PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAO();

    /* 결제 가능 예매 번호인지 확인 */
    public boolean isPayable(int reservationCode, List<ReservationDTO> unpaidReservations) {

        // 미결제 목록 안에 속하는지 체크 (결제 가능 여부 확인)
        return unpaidReservations.stream()
                .anyMatch(reservation -> reservation.getReservationCode() == reservationCode);
    }

    /* 결제 등록 */
    public int registerPayment(PaymentDTO paymentDTO) {

        Connection con = getConnection();

        int result = paymentDAO.insertPayment(con, paymentDTO);

        // 성공하면 트랜잭션 커밋
        if (result == 1) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result;
    }

    /* 총 결제 금액 조회 */
    public int getTotalPaymentAmount(int reservationCode) {

        Connection con = getConnection();

        int result = paymentDAO.getPaymentAmount(con, reservationCode);

        close(con);

        return result;
    }
}
