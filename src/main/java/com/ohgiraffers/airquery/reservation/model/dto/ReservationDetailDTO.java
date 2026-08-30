package com.ohgiraffers.airquery.reservation.model.dto;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;
import com.ohgiraffers.airquery.payment.model.dto.PaymentDTO;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationDetailDTO {

    // 예매
    private int reservationCode;                    // 예약 번호
    private int flightCode;                         // 항공편번호
    private LocalDateTime reservationCreatedDate;   // 예매날짜
    private LocalDateTime reservationUpdatedDate;   // 최종수정일
    private boolean baggageCarrying;                // 수하물 지참 여부
    private boolean isDeleted;                      // 취소여부
    // 항공편
    private int ticketPrice;                        // 항공편 티켓 가격

    // 결제
    private int paymentCode;                        // 결제번호
    private int paymentAmount;                      // 총 결제 금액(티켓 가격 + 추가금액)
    private String paymentMethod;                   // 결제 수단
    private boolean refundStatus;                   // 환불여부
    private LocalDateTime paymentCreatedDate;       // 결제일자
    private LocalDateTime paymentUpdatedDate;       // 최종수정일

    // 좌석
    private int seatCode;           // 좌석번호
    private String seatId;          // 좌석식별번호
    private String flightClass;     // 좌석등급
    private int additionalAmount;   // 추가금액

    // 수하물
    private int baggageCode;        // 수하물 번호
    private double baggageWeight;   // 수하물 무게

    private List<BaggageDTO> baggageList;

    public boolean isBaggageCarrying() {
        return baggageCarrying;
    }

    public void setBaggageCarrying(boolean baggageCarrying) {
        this.baggageCarrying = baggageCarrying;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<BaggageDTO> getBaggageList() {
        return baggageList;
    }

    public void setBaggageList(List<BaggageDTO> baggageList) {
        this.baggageList = baggageList;
    }

    public int getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(int reservationCode) {
        this.reservationCode = reservationCode;
    }

    public int getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(int flightCode) {
        this.flightCode = flightCode;
    }

    public LocalDateTime getReservationCreatedDate() {
        return reservationCreatedDate;
    }

    public void setReservationCreatedDate(LocalDateTime reservationCreatedDate) {
        this.reservationCreatedDate = reservationCreatedDate;
    }

    public LocalDateTime getReservationUpdatedDate() {
        return reservationUpdatedDate;
    }

    public void setReservationUpdatedDate(LocalDateTime reservationUpdatedDate) {
        this.reservationUpdatedDate = reservationUpdatedDate;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(int paymentCode) {
        this.paymentCode = paymentCode;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(boolean refundStatus) {
        this.refundStatus = refundStatus;
    }

    public LocalDateTime getPaymentCreatedDate() {
        return paymentCreatedDate;
    }

    public void setPaymentCreatedDate(LocalDateTime paymentCreatedDate) {
        this.paymentCreatedDate = paymentCreatedDate;
    }

    public LocalDateTime getPaymentUpdatedDate() {
        return paymentUpdatedDate;
    }

    public void setPaymentUpdatedDate(LocalDateTime paymentUpdatedDate) {
        this.paymentUpdatedDate = paymentUpdatedDate;
    }

    public int getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(int seatCode) {
        this.seatCode = seatCode;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public int getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(int additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public int getBaggageCode() {
        return baggageCode;
    }

    public void setBaggageCode(int baggageCode) {
        this.baggageCode = baggageCode;
    }

    public double getBaggageWeight() {
        return baggageWeight;
    }

    public void setBaggageWeight(double baggageWeight) {
        this.baggageWeight = baggageWeight;
    }

    // 예매 상세 정보 DTO 조립
    public static ReservationDetailDTO of(ReservationDTO r, PaymentDTO p, SeatDTO s, List<BaggageDTO> b) {

        ReservationDetailDTO dto = new ReservationDetailDTO();

        // 예매 정보
        dto.setReservationCode(r.getReservationCode());
        dto.setFlightCode(r.getFlightCode());
        dto.setReservationCreatedDate(r.getCreatedAt());
        dto.setReservationUpdatedDate(r.getUpdatedAt());
        dto.setBaggageCarrying(r.isBaggageCarrying());
        dto.setDeleted(r.isDeleted());

        // 결제 정보
        if (p != null) {

            dto.setPaymentCode(p.getPaymentCode());
            dto.setPaymentAmount(p.getPaymentAmount());
            dto.setPaymentMethod(p.getPaymentMethod());
            dto.setRefundStatus(p.isRefundStatus());
            dto.setPaymentCreatedDate(p.getCreatedAt());
            dto.setPaymentUpdatedDate(p.getUpdatedAt());
        }

         if(s != null) {

             dto.setSeatCode(s.getSeatCode());
             dto.setSeatId(s.getSeatId());
             dto.setFlightClass(s.getFlightClass());
             dto.setAdditionalAmount(s.getAdditionalAmount());
         }

         if(b != null) {

             dto.setBaggageList(b);
         }

        return dto;
    }

    @Override
    public String toString() {
        return "ReservationDetailDTO{" +
                "reservationCode=" + reservationCode +
                ", flightCode=" + flightCode +
                ", reservationCreatedDate=" + reservationCreatedDate +
                ", reservationUpdatedDate=" + reservationUpdatedDate +
                ", ticketPrice=" + ticketPrice +
                ", paymentCode=" + paymentCode +
                ", paymentAmount=" + paymentAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", refundStatus=" + refundStatus +
                ", paymentCreatedDate=" + paymentCreatedDate +
                ", paymentUpdatedDate=" + paymentUpdatedDate +
//                ", seatCode=" + seatCode +
//                ", seatId='" + seatId + '\'' +
//                ", flightClass='" + flightClass + '\'' +
//                ", additionalAmount=" + additionalAmount +
//                ", baggageCode=" + baggageCode +
//                ", baggageWeight=" + baggageWeight +
                '}';
    }
}
