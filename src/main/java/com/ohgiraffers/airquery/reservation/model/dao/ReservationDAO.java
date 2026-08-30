package com.ohgiraffers.airquery.reservation.model.dao;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;
import com.ohgiraffers.airquery.reservation.model.dto.ReservationDTO;
import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

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

        // 결제 테이블에 존재하지 않거나, 환불된 예약 코드만 조회
        String query = "SELECT * FROM tbl_reservation t " +
                "WHERE t.member_code = ? " +
                "AND t.reservation_code NOT IN " +
                "   (SELECT p.reservation_code FROM tbl_payment p WHERE p.refund_status = FALSE)" +
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

    /* 방금 등록한 예매를 회원번호+항공편번호로 다시 조회 (예매번호, 생성일시 등 DB가 채운 값까지 가져오기 위함) */
    public ReservationDTO findByMemberAndFlight(Connection con, int memberCode, int flightCode) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ReservationDTO reservation = null;

        String query = "" +
                "SELECT * FROM tbl_reservation " +
                "WHERE member_code = ? " +
                "AND flight_code = ? " +
                "AND is_deleted = FALSE " +
                "ORDER BY reservation_code DESC " +
                "LIMIT 1";

        try {

            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);
            pstmt.setInt(2, flightCode);

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
    public Map<String, Object> getSeatAndBaggageInfoOfReservation(Connection con, int reservationCode, boolean baggageCarrying) {

        ResultSet rs = null;
        PreparedStatement pstmt = null;

        Map<String, Object> info = new HashMap<String, Object>();

        String seatQuery = "" +
                "SELECT s.seat_code, s.flight_code, s.seat_id, s.flight_class, s.additional_amount, s.is_reserved " +
                "FROM tbl_reservation r " +
                "JOIN tbl_seat s ON r.seat_code = s.seat_code " +
                "WHERE r.reservation_code = ?";

        String baggageQuery = "" +
                "SELECT baggage_code, baggage_weight " +
                "FROM tbl_baggage " +
                "WHERE reservation_code = ?" +
                "";
        try {

            pstmt = con.prepareStatement(seatQuery);
            pstmt.setInt(1, reservationCode);
            rs = pstmt.executeQuery();

            List<BaggageDTO> baggageList = new ArrayList<>();
            SeatDTO seatDTO = null;

            if(rs.next()) {

                seatDTO = new SeatDTO();

                // 좌석 정보
                seatDTO.setSeatCode(rs.getInt("seat_code"));
                seatDTO.setSeatId(rs.getString("seat_id"));
                seatDTO.setFlightClass(rs.getString("flight_class"));
                seatDTO.setAdditionalAmount(rs.getInt("additional_amount"));

                info.put("seatDTO", seatDTO);
            }

            close(rs);

            // 수하물 지참 여부가 TRUE일때만 수하물 정보를 조회함
            if(baggageCarrying) {

                pstmt = con.prepareStatement(baggageQuery);
                pstmt.setInt(1, reservationCode);
                rs = pstmt.executeQuery();

                while (rs.next()) {

                    BaggageDTO baggageDTO = new BaggageDTO();

                    // 수하물 정보
                    baggageDTO.setBaggageCode(rs.getInt("baggage_code"));
                    baggageDTO.setBaggageWeight(rs.getDouble("baggage_weight"));

                    baggageList.add(baggageDTO);
                }

                info.put("baggageList", baggageList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }

        return info;
    }

    /* 예약 변경 */

    // 등급 변경 시: 다른 등급의 좌석 목록 조회
    public List<SeatDTO> getAvailableOtherClassByFlightCode(Connection con, int flightCode, String currentFlightClass) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<SeatDTO> seatList = new ArrayList<>();

        // 현재 등급과는 다르면서, 선점되지 않은 좌석
        String query = "" +
                "SELECT seat_code, seat_id, flight_class, additional_amount " +
                "FROM tbl_seat " +
                "WHERE flight_code = ? " +
                "AND flight_class <> ? " +
                "AND is_reserved = FALSE " +
                "ORDER BY seat_code";

        try {

            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, flightCode);
            pstmt.setString(2, currentFlightClass);

            rset = pstmt.executeQuery();

            while (rset.next()) {

                SeatDTO seat = new SeatDTO();
                seat.setSeatCode(rset.getInt("seat_code"));
                seat.setSeatId(rset.getString("seat_id"));
                seat.setFlightClass(rset.getString("flight_class"));
                seat.setAdditionalAmount(rset.getInt("additional_amount"));
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

    public int updateSeatClass(Connection con,
                               int oldSeatCode, int newSeatCode, int flightCode, int reservationCode
    ) {

        PreparedStatement pstmt = null;
        int rs = 0;

        // 같은 항공편 소속  + 아직 예약 안 된 좌석일 때만 선점되도록 조건을 걸어서 유효성 검증
        String updateNewSeatCodeQuery = "" +
                "UPDATE tbl_seat SET " +
                "is_reserved = TRUE " +
                "WHERE  seat_code = ? " +
                "AND flight_code = ? " +
                "AND is_reserved = FALSE";

        String updateOldSeatCodeQuery = "" +
                "UPDATE tbl_seat SET " +
                "is_reserved = FALSE " +
                "WHERE seat_code = ?";

        String updateCurrentReservationQuery = "" +
                "UPDATE tbl_reservation SET " +
                "seat_code = ? " +
                "WHERE  reservation_code = ?";

        // 기존 결제 정보는 취소
        String updateOldPaymentQuery = "" +
                "UPDATE tbl_payment SET " +
                "refund_status = TRUE " +
                "WHERE reservation_code = ?";

        try {

            // 1. 새 좌석 선점 시도 (여기서 0건이면 잘못된 좌석 번호 -> 아무것도 건드리지 않음)
            pstmt = con.prepareStatement(updateNewSeatCodeQuery);
            pstmt.setInt(1, newSeatCode);
            pstmt.setInt(2, flightCode);
            int newSeatResult = pstmt.executeUpdate();

            if(newSeatResult != 1) {
                return 0;
            }

            // 2. 기존 좌석은 선점 여부 다시 FALSE로(선점 해제)
            pstmt = con.prepareStatement(updateOldSeatCodeQuery);
            pstmt.setInt(1, oldSeatCode);
            pstmt.executeUpdate();

            // 3. 기존 예매 정보의 좌석 정보를 새로운 좌석번호로 chagne
            pstmt = con.prepareStatement(updateCurrentReservationQuery);
            pstmt.setInt(1, newSeatCode);
            pstmt.setInt(2, reservationCode);
            int reservationResult = pstmt.executeUpdate();

            // 4. 기존 결제 정보는 취소처리 (재결제 안내)
            pstmt = con.prepareStatement(updateOldPaymentQuery);
            pstmt.setInt(1, reservationCode);
            pstmt.executeUpdate();

            rs = reservationResult;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return rs;
    }

    public int updateBaggageCarrying(Connection con, int reservationCode, boolean baggageCarrying) {

        PreparedStatement pstmt = null;
        int rs = 0;

        String query = "" +
                "UPDATE tbl_reservation SET " +
                "baggage_carrying = ? " +
                "WHERE reservation_code = ?";

        try {

            pstmt = con.prepareStatement(query);
            pstmt.setBoolean(1, baggageCarrying);
            pstmt.setInt(2, reservationCode);

            rs = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return rs;
    }
}
