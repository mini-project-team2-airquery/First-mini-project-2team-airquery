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

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;

public class SeatDAO {
    /*
     * 좌석 전체 조회 메서드
     * 좌석 정보를 조회
     */
    public List<SeatDTO> selectAllSeats(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<SeatDTO> seatList = new ArrayList<>(); // 조회된 좌석 리스트


        String query = "SELECT seat_code, flight_code, seat_id, flight_class, " +
                "additional_amount, is_reserved " +
                "FROM tbl_seat " +
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return seatList;
    }

    /*
     * 회원의 예매 중 아직 좌석을 선택하지 않은 예매 목록을 조회하는 메서드
     * Map의 key는 예매번호, value는 항공편번호이다.
     */
    public Map<Integer, Integer> selectReservationsWithoutSeat(Connection con, int memberCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Map<Integer, Integer> reservationMap = new LinkedHashMap<>();

        String query = "SELECT reservation_code, flight_code " +
                "FROM tbl_reservation " +
                "WHERE member_code = ? " +
                "AND seat_code IS NULL " +
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
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return reservationMap;
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
            e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return result;
    }

    /*
     * 예매 테이블에 선택한 좌석번호를 넣는 메서드
     * 같은 항공편 예매 중 seat_code가 비어 있는 예매 하나에 좌석번호를 저장한다.
     */
    public int updateReservationSeatCode(Connection con, int memberCode, int reservationCode, int seatCode, int flightCode) {

        PreparedStatement pstmt = null;
        int result = 0;

        String query = "UPDATE tbl_reservation " +
                "SET seat_code = ? " +
                "WHERE reservation_code = ? " +
                "AND member_code = ? " +
                "AND flight_code = ? " +
                "AND seat_code IS NULL";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, seatCode);
            pstmt.setInt(2, reservationCode);
            pstmt.setInt(3, memberCode);
            pstmt.setInt(4, flightCode);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return result;
    }

    /*
     * 특정 항공편에 좌석 선택 안 된 예매가 있는지 확인하는 메서드
     * tbl_reservation에서 flight_code가 같고 seat_code가 NULL인 예매를 찾는다.
     */
    public boolean hasReservationWithoutSeat(Connection con, int memberCode, int flightCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        boolean result = false;

        String query = "SELECT reservation_code " +
                "FROM tbl_reservation " +
                "WHERE member_code = ? " +
                "AND flight_code = ? " +
                "AND seat_code IS NULL";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, memberCode);
            pstmt.setInt(2, flightCode);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rset);
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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

        String query = "UPDATE tbl_seat " +
                "SET is_reserved = false " +
                "WHERE seat_code = ?";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, seatCode);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return result;
    }

    /*
     * 회원이 현재 선택한 좌석의 등급을 조회하는 메서드
     * 좌석 변경할 때 기존 좌석 등급을 알아야 새 좌석 등급과 비교할 수 있다.
     */
    public String selectSelectedSeatClass(Connection con, int memberCode, int flightCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String flightClass = null;

        /*
         * tbl_reservation에는 선택한 seat_code가 들어있고,
         * tbl_seat에는 seat_code에 해당하는 좌석등급(flight_class)이 들어있다.
         * 그래서 두 테이블을 JOIN해서 현재 선택한 좌석의 등급을 가져온다.
         */
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

            // 조회 결과가 있으면 현재 선택한 좌석의 등급을 꺼낸다.
            if (rset.next()) {
                flightClass = rset.getString("flight_class");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return flightClass;
    }

    /*
     * 새로 선택하려는 좌석의 등급을 조회하는 메서드
     * 새 좌석이 같은 등급인지, 상위 등급인지 확인하기 위해 사용한다.
     */
    public String selectSeatClass(Connection con, int seatCode, int flightCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String flightClass = null;

        /*
         * 새 좌석은 아래 조건을 모두 만족해야 한다.
         * 1. 사용자가 입력한 seat_code와 일치해야 한다.
         * 2. 현재 예매한 flight_code와 같은 항공편이어야 한다.
         * 3. 아직 예약되지 않은 좌석이어야 한다.
         */
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

            // 조건에 맞는 좌석이 있으면 그 좌석의 등급을 가져온다.
            if (rset.next()) {
                flightClass = rset.getString("flight_class");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return flightClass;
    }
}
