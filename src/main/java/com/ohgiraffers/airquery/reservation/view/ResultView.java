package com.ohgiraffers.airquery.reservation.view;

import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDetailDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ResultView {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final int[] LIST_WIDTHS = {8, 10, 8, 10, 16};
    private static final String[] LIST_HEADERS = {"예매번호", "항공편번호", "좌석번호", "수하물지참", "예매일시"};

    private static final int LABEL_WIDTH = 12;
    private static final int DETAIL_WIDTH = 43;

    public void printReservationList(List<ReservationDTO> reservationList) {

        System.out.println();
        printTableBorder(LIST_WIDTHS);
        printTableRow(LIST_WIDTHS, LIST_HEADERS);
        printTableBorder(LIST_WIDTHS);

        for (ReservationDTO reservation : reservationList) {
            printTableRow(LIST_WIDTHS, new String[]{
                    String.valueOf(reservation.getReservationCode()),
                    String.valueOf(reservation.getFlightCode()),
                    String.valueOf(reservation.getSeatCode()),
                    reservation.isBaggageCarrying() ? "있음" : "없음",
                    formatDate(reservation.getCreatedAt())
            });
        }

        printTableBorder(LIST_WIDTHS);
    }

    public void printReservationDetail(ReservationDetailDTO detail) {

        System.out.println();
        printDetailBorder('┌', '┐');
        printDetailTitle("예매 상세 내역");
        printDetailBorder('├', '┤');

        printDetailRow("예매번호", detail.getReservationCode());
        printDetailRow("항공편번호", detail.getFlightCode());
        printDetailRow("예매일시", formatDate(detail.getReservationCreatedDate()));
        printDetailRow("최종수정일", formatDate(detail.getReservationUpdatedDate()));

        printDetailBorder('├', '┤');

        if(detail.getPaymentCode() != 0) {
            printDetailRow("결제번호", detail.getPaymentCode());
            printDetailRow("결제금액", formatMoney(detail.getPaymentAmount()));
            printDetailRow("결제수단", detail.getPaymentMethod());
            printDetailRow("환불여부", detail.isRefundStatus() ? "환불됨" : "정상결제");
            printDetailRow("결제일시", formatDate(detail.getPaymentCreatedDate()));

            printDetailBorder('├', '┤');
        }

        if(detail.getSeatCode() != 0) {
            printDetailRow("좌석번호", detail.getSeatCode());
            printDetailRow("좌석식별번호", detail.getSeatId());
            printDetailRow("좌석등급", detail.getFlightClass());

            printDetailBorder('├', '┤');
        }

        if(detail.getBaggageCode() != 0) {
            printDetailRow("수하물번호", detail.getBaggageCode());
            printDetailRow("수하물무게", detail.getBaggageWeight() + "kg");
        }

        printDetailBorder('└', '┘');
    }

    // ---------- 목록 테이블 ----------

    private void printTableBorder(int[] widths) {

        StringBuilder sb = new StringBuilder("+");
        for (int width : widths) {
            sb.append("-".repeat(width + 2)).append("+");
        }
        System.out.println(sb);
    }

    private void printTableRow(int[] widths, String[] cells) {

        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < widths.length; i++) {
            sb.append(" ").append(padToWidth(cells[i], widths[i])).append(" |");
        }
        System.out.println(sb);
    }

    // ---------- 상세 박스 ----------

    private void printDetailBorder(char left, char right) {
        System.out.println(left + "─".repeat(DETAIL_WIDTH + 2) + right);
    }

    private void printDetailTitle(String title) {
        System.out.println("│ " + padCenter(title, DETAIL_WIDTH) + " │");
    }

    private void printDetailRow(String label, Object value) {

        String display = (value == null || "null".equals(String.valueOf(value))) ? "-" : String.valueOf(value);
        String content = padToWidth(label, LABEL_WIDTH) + ": " + display;

        System.out.println("│ " + padToWidth(content, DETAIL_WIDTH) + " │");
    }

    // ---------- 공통 포맷/정렬 유틸 ----------

    private String formatDate(LocalDateTime dateTime) {
        return dateTime == null ? "-" : dateTime.format(DATE_FORMAT);
    }

    private String formatMoney(int amount) {
        return String.format("%,d원", amount);
    }

    /** 한글은 터미널에서 2칸을 차지하므로 문자 수가 아닌 화면 폭 기준으로 오른쪽 여백을 채운다. */
    private String padToWidth(String text, int width) {

        if (text == null) {
            text = "-";
        }

        int padding = width - displayWidth(text);
        return padding > 0 ? text + " ".repeat(padding) : text;
    }

    private String padCenter(String text, int width) {

        int padding = width - displayWidth(text);
        int left = Math.max(padding / 2, 0);
        int right = Math.max(padding - left, 0);

        return " ".repeat(left) + text + " ".repeat(right);
    }

    private int displayWidth(String text) {

        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += isWide(text.charAt(i)) ? 2 : 1;
        }
        return width;
    }

    /** 한글(자모/음절), 전각기호 등 터미널에서 2칸 폭으로 그려지는 문자 범위 */
    private boolean isWide(char c) {
        return (c >= 0x1100 && c <= 0x115F)
                || (c >= 0x2E80 && c <= 0xA4CF)
                || (c >= 0xAC00 && c <= 0xD7A3)
                || (c >= 0xF900 && c <= 0xFAFF)
                || (c >= 0xFF00 && c <= 0xFF60)
                || (c >= 0xFFE0 && c <= 0xFFE6);
    }
}
