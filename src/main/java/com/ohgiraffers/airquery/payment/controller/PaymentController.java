package com.ohgiraffers.airquery.payment.controller;

import com.ohgiraffers.airquery.payment.model.dto.PaymentDTO;
import com.ohgiraffers.airquery.payment.model.service.PaymentService;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;

import java.util.List;
import java.util.Map;

public class PaymentController {

    private final PaymentService paymentService = new PaymentService();

    /* 결제 가능 여부 체크 */
    public boolean isPayable(int reservationCode, List<ReservationDTO> unpaidReservations) {

        return paymentService.isPayable(reservationCode, unpaidReservations);
    }

    /* 결제 등록 */
    public int registerPayment(Map<String, Object> requestMap) {

        PaymentDTO paymentDTO = new PaymentDTO();

        paymentDTO.setReservationCode((int) requestMap.get("reservation_code"));
        paymentDTO.setPaymentAmount((int) requestMap.get("payment_amount"));
        paymentDTO.setPaymentMethod((String) requestMap.get("payment_method"));
        paymentDTO.setRefundStatus((boolean) requestMap.get("refund_status"));

        int result = paymentService.registerPayment(paymentDTO);

        if(result == 1) {
            System.out.println("결제 성공");
        } else {
            System.out.println("결제 실패");
        }

        return result;
    }

    /* 전체 결제할 금액 조회 */
    public int getTotalPaymentAmount(int reservationCode) {

        return paymentService.getTotalPaymentAmount(reservationCode);
    }
}
