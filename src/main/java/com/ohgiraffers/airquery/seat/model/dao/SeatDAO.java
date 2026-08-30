package com.ohgiraffers.airquery.seat.model.dao;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;

/*
 * 좌석과 예매 테이블에 SQL을 실행하는 클래스이다.
 * SELECT 결과는 SeatDTO/List/Map으로 바꾸고, UPDATE 결과는 변경된 행의 수로 반환한다.
 * Connection의 생성, commit, rollback, close는 SeatService가 담당한다.
 * PreparedStatement의 ?에는 setInt 등으로 값을 넣어 SQL Injection을 방지한다.
 */
public class SeatDAO {

    private static final Logger LOGGER = Logger.getLogger(SeatDAO.class.getName());

    /* 로그인 회원이 관리자인지 확인한다. */
    public boolean isAdmin(Connection con, int memberCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        boolean admin = false;

        String query = "SELECT member_auth FROM tbl_member WHERE member_code = ?";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                admin = "Admin".equalsIgnoreCase(rset.getString("member_auth"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "관리자 권한 확인 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return admin;
    }
    /*
     * 좌석 전체 조회 메서드
     * 좌석 정보를 조회
     */
    public List<SeatDTO> selectAllSeats(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<SeatDTO> seatList = new ArrayList<>(); // 조회된 좌석 리스트


        // 조건이 없으므로 tbl_seat의 모든 행을 가져온다.
        String query = "SELECT seat_code, flight_code, seat_id, flight_class, " +
                "additional_amount, is_reserved " +
                "FROM tbl_seat " +
                "ORDER BY seat_code";

        try {

            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();

            // 조회된 행마다 SeatDTO 한 개를 만들어 목록에 추가한다.
            while (rset.next()) {
                SeatDTO seat = new SeatDTO();

                seat.setSeatCode(rset.getInt("seat_code"));
                seat.setFlightCode(rset.getInt("flight_code"));
                seat.setSeatId(rset.getString("seat_id"));
                seat.setFlightClass(rset.getString("flight_class"));
                seat.setAdditionalAmount(rset.getInt("additional_amount"));
                seat.setReserved(rset.getBoolean("is_reserved"));

                seatList.add(seat);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "좌석 전체 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return seatList;
    }

    /*
     * 예약 가능한 좌석 조회 메서드
     * is_reserved 값이 false인 좌석만 조회한다.
     */
    public List<SeatDTO> selectAvailableSeats(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<SeatDTO> seatList = new ArrayList<>();

        // is_reserved=false가 아직 아무도 선택하지 않은 좌석이다.
        String query = "SELECT seat_code, flight_code, seat_id, flight_class, " +
                "additional_amount, is_reserved " +
                "FROM tbl_seat " +
                "WHERE is_reserved = false " +
                "ORDER BY seat_code";

        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                SeatDTO seat = new SeatDTO();

                seat.setSeatCode(rset.getInt("seat_code"));
                seat.setFlightCode(rset.getInt("flight_code"));
                seat.setSeatId(rset.getString("seat_id"));
                seat.setFlightClass(rset.getString("flight_class"));
                seat.setAdditionalAmount(rset.getInt("additional_amount"));
                seat.setReserved(rset.getBoolean("is_reserved"));

                seatList.add(seat);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "예약 가능 좌석 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return seatList;
    }

    // flight_code만 조건으로 사용하므로 예약 좌석과 빈 좌석이 모두 포함된다.
    public List<SeatDTO> selectSeatsByFlightCode(Connection con, int flightCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<SeatDTO> seatList = new ArrayList<>();

        // ? 자리에는 메서드로 받은 flightCode가 들어간다.
        String query = "SELECT seat_code, flight_code, seat_id, flight_class, " +
                "additional_amount, is_reserved FROM tbl_seat " +
                "WHERE flight_code = ? ORDER BY seat_code";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, flightCode);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                SeatDTO seat = new SeatDTO();
                seat.setSeatCode(rset.getInt("seat_code"));
                seat.setFlightCode(rset.getInt("flight_code"));
                seat.setSeatId(rset.getString("seat_id"));
                seat.setFlightClass(rset.getString("flight_class"));
                seat.setAdditionalAmount(rset.getInt("additional_amount"));
                seat.setReserved(rset.getBoolean("is_reserved"));
                seatList.add(seat);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "항공편 좌석 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return seatList;
    }

    /*
     * 특정 항공편의 예약 가능한 좌석 조회 메서드
     * 사용자가 입력한 항공편번호와 같은 좌석 중 예약되지 않은 좌석만 조회한다.
     */
    public List<SeatDTO> selectAvailableSeatsByFlightCode(Connection con, int flightCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<SeatDTO> seatList = new ArrayList<>();

        String query = "SELECT seat_code, flight_code, seat_id, flight_class, " +
                "additional_amount, is_reserved " +
                "FROM tbl_seat " +
                "WHERE flight_code = ? " +
                "AND is_reserved = false " +
                "ORDER BY seat_code";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, flightCode);

            rset = pstmt.executeQuery();

            while (rset.next()) {
                SeatDTO seat = new SeatDTO();

                seat.setSeatCode(rset.getInt("seat_code"));
                seat.setFlightCode(rset.getInt("flight_code"));
                seat.setSeatId(rset.getString("seat_id"));
                seat.setFlightClass(rset.getString("flight_class"));
                seat.setAdditionalAmount(rset.getInt("additional_amount"));
                seat.setReserved(rset.getBoolean("is_reserved"));

                seatList.add(seat);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "항공편의 예약 가능 좌석 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return seatList;
    }

    /*
     * 좌석 예약 메서드
     * 사용자가 입력한 좌석번호의 is_reserved 값을 true로 변경한다.
     */
    public int reserveSeat(Connection con, int seatCode) {

        PreparedStatement pstmt = null;

        // UPDATE 성공 여부를 담는 변수
        // 1이면 예약 성공, 0이면 예약 실패
        int result = 0;

        /*
         * seat_code = ?
         * 사용자가 입력한 좌석번호를 의미 한다
         *
         * is_reserved = false
         * 아직 예약되지 않은 좌석만 예약할 수 있게 하는 조건이다
         */
        // 이미 예약된 좌석이면 WHERE 조건을 만족하지 않아 결과가 0이 된다.
        String query = "UPDATE tbl_seat " +
                "SET is_reserved = true " +
                "WHERE seat_code = ? " +
                "AND is_reserved = false";

        try {
            pstmt = con.prepareStatement(query);

            // SQL의 ? 자리에 사용자가 입력한 좌석번호를 넣는다.
            pstmt.setInt(1, seatCode);

            // INSERT, UPDATE, DELETE는 executeUpdate()로 실행한다.
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "좌석 예약 중 오류가 발생했습니다.", e);
        } finally {
            close(pstmt);
        }

        return result;
    }

    /* 로그인 회원의 좌석 미선택 예매를 예매번호와 항공편번호로 조회한다. */
    public Map<Integer, Integer> selectReservationsWithoutSeat(Connection con, int memberCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Map<Integer, Integer> reservationMap = new LinkedHashMap<>();

        String query = "SELECT reservation_code, flight_code " +
                "FROM tbl_reservation " +
                "WHERE member_code = ? " +
                "AND seat_code IS NULL " +
                "AND is_deleted = false " +
                "ORDER BY reservation_code";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                reservationMap.put(
                        rset.getInt("reservation_code"),
                        rset.getInt("flight_code")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "좌석 미선택 예매 목록 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return reservationMap;
    }

    /* 선택 좌석과 같은 항공편에 좌석을 아직 선택하지 않은 로그인 회원의 예매번호를 찾는다. */
    public int selectReservationCodeWithoutSeat(Connection con, int memberCode, int seatCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        int reservationCode = 0;

        String query = "SELECT r.reservation_code " +
                "FROM tbl_reservation r " +
                "JOIN tbl_seat s ON r.flight_code = s.flight_code " +
                "WHERE r.member_code = ? " +
                "AND s.seat_code = ? " +
                "AND r.seat_code IS NULL " +
                "AND r.is_deleted = false " +
                "ORDER BY r.reservation_code " +
                "LIMIT 1";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);
            pstmt.setInt(2, seatCode);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                reservationCode = rset.getInt("reservation_code");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "좌석 미선택 예매 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return reservationCode;
    }

    /* 선택한 좌석번호를 앞에서 찾은 정확한 로그인 회원 예매에 저장한다. */
    public int updateMemberReservationSeat(Connection con, int reservationCode,
                                           int memberCode, int seatCode) {
        PreparedStatement pstmt = null;
        int result = 0;

        String query = "UPDATE tbl_reservation " +
                "SET seat_code = ? " +
                "WHERE reservation_code = ? " +
                "AND member_code = ? " +
                "AND seat_code IS NULL " +
                "AND is_deleted = false";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, seatCode);
            pstmt.setInt(2, reservationCode);
            pstmt.setInt(3, memberCode);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "예매 좌석번호 저장 중 오류가 발생했습니다.", e);
        } finally {
            close(pstmt);
        }

        return result;
    }

    /*
     * 예매 등록에서 사용하는 좌석 예약 메서드
     * 선택한 좌석이 선택한 항공편의 좌석이고, 아직 예약되지 않은 경우에만 예약한다.
     */
    public int reserveSeatForFlight(Connection con, int seatCode, int flightCode) {

        PreparedStatement pstmt = null;
        int result = 0; // 성공하면 1, 실패하면 0으로 담김

        /* 좌석(?번호) 과 비행기(?코드)가 일치시 아직 예약 되지않은 상태(False)인 데이터를 찾아서
            예약상태(True)를 변경 */
        String query = "UPDATE tbl_seat " +
                "SET is_reserved = true " +
                "WHERE seat_code = ? " +
                "AND flight_code = ? " +
                "AND is_reserved = false";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, seatCode);
            pstmt.setInt(2, flightCode);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "항공편 좌석 예약 중 오류가 발생했습니다.", e);
        } finally {
            close(pstmt);
        }

        return result;
    }

    /*
     * 특정 항공편에 이미 좌석 선택한 예매가 있는지 확인하는 메서드
     * seat_code가 NULL이 아니면 이미 좌석을 선택했다는 뜻이다.
     */
    public boolean hasReservationWithSeat(Connection con, int memberCode, int flightCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        boolean result = false;

        String query = "SELECT reservation_code " +
                "FROM tbl_reservation " +
                "WHERE member_code = ? " +
                "AND flight_code = ? " +
                "AND seat_code IS NOT NULL";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, memberCode);
            pstmt.setInt(2, flightCode);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                result = true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "좌석 선택 예매 확인 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return result;
    }

    /*
     * 회원번호와 항공편번호로 현재 선택된 좌석번호를 조회하는 메서드
     */
    public int selectReservedSeatCode(Connection con, int memberCode, int flightCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        int seatCode = 0;

        // 회원번호와 항공편번호를 함께 사용해 다른 회원의 좌석을 가져오지 않는다.
        String query = "SELECT seat_code " +
                "FROM tbl_reservation " +
                "WHERE member_code = ? " +
                "AND flight_code = ? " +
                "AND seat_code IS NOT NULL " +
                "ORDER BY reservation_code " +
                "LIMIT 1";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, memberCode);
            pstmt.setInt(2, flightCode);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                seatCode = rset.getInt("seat_code");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "현재 좌석번호 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return seatCode;
    }

    /*
     * 예매 테이블의 기존 좌석번호를 새 좌석번호로 변경하는 메서드
     */
    public int changeReservationSeatCode(Connection con, int memberCode, int oldSeatCode, int newSeatCode, int flightCode) {

        PreparedStatement pstmt = null;
        int result = 0;

        // 현재 예매의 oldSeatCode만 newSeatCode로 교체한다.
        String query = "UPDATE tbl_reservation " +
                "SET seat_code = ? " +
                "WHERE member_code = ? " +
                "AND flight_code = ? " +
                "AND seat_code = ?";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, newSeatCode);
            pstmt.setInt(2, memberCode);
            pstmt.setInt(3, flightCode);
            pstmt.setInt(4, oldSeatCode);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "예매 좌석 변경 중 오류가 발생했습니다.", e);
        } finally {
            close(pstmt);
        }

        return result;
    }

    /*
     * 기존 좌석을 다시 예약 가능한 상태로 바꾸는 메서드
     */
    public int cancelSeatReservation(Connection con, int seatCode) {

        PreparedStatement pstmt = null;
        int result = 0;

        // 변경이 끝난 기존 좌석을 다시 선택할 수 있도록 false로 돌린다.
        String query = "UPDATE tbl_seat " +
                "SET is_reserved = false " +
                "WHERE seat_code = ?";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, seatCode);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "기존 좌석 해제 중 오류가 발생했습니다.", e);
        } finally {
            close(pstmt);
        }

        return result;
    }

    /*
     * 로그인 회원의 예매와 좌석을 JOIN하여 현재 선택한 좌석등급을 조회한다.
     * member_code와 flight_code를 함께 검사하므로 다른 회원의 좌석은 조회되지 않는다.
     */
    public String selectSelectedSeatClass(Connection con, int memberCode, int flightCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String flightClass = null;

        String query = "SELECT s.flight_class " +
                "FROM tbl_reservation r " +
                "JOIN tbl_seat s ON r.seat_code = s.seat_code " +
                "WHERE r.member_code = ? " +
                "AND r.flight_code = ? " +
                "AND r.seat_code IS NOT NULL " +
                "ORDER BY r.reservation_code " +
                "LIMIT 1";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);
            pstmt.setInt(2, flightCode);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                flightClass = rset.getString("flight_class");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "현재 좌석등급 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return flightClass;
    }

    /* 새 좌석이 같은 항공편의 예약 가능한 좌석일 때 좌석등급을 조회한다. */
    public String selectAvailableSeatClass(Connection con, int seatCode, int flightCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String flightClass = null;

        String query = "SELECT flight_class " +
                "FROM tbl_seat " +
                "WHERE seat_code = ? " +
                "AND flight_code = ? " +
                "AND is_reserved = false";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, seatCode);
            pstmt.setInt(2, flightCode);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                flightClass = rset.getString("flight_class");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "예약 가능 좌석등급 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return flightClass;
    }

    /* 로그인 회원번호에 해당하는 이름을 조회한다. */
    public String selectMemberName(Connection con, int memberCode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String memberName = null;

        String query = "SELECT member_name FROM tbl_member WHERE member_code = ?";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                memberName = rset.getString("member_name");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "회원 이름 조회 중 오류가 발생했습니다.", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return memberName;
    }
}
