package com.ohgiraffers.airquery.seat.model.service;

import com.ohgiraffers.airquery.seat.model.dto.SeatDTO;
import com.ohgiraffers.airquery.seat.model.dao.SeatDAO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import static com.ohgiraffers.airquery.common.JDBCTemplate.commit;
import static com.ohgiraffers.airquery.common.JDBCTemplate.getConnection;
import static com.ohgiraffers.airquery.common.JDBCTemplate.rollback;

/*
 * 좌석 업무 규칙과 DB 트랜잭션을 담당한다.
 * 조회는 DAO 결과를 반환하고, 예약/변경은 성공하면 commit, 실패하면 rollback한다.
 * Connection은 Service에서 열고 닫으며 DAO에는 빌려준다.
 */
public class SeatService {

    private final SeatDAO seatDAO = new SeatDAO();

    // DB 컬럼을 추가하지 않고 실행 중에만 예약 회원을 기억한다.
    // key=좌석번호, value=로그인 회원번호
    private static final Map<Integer, Integer> reservedSeatMembers = new HashMap<>();
    // key=좌석번호, value=로그인 회원 이름
    private static final Map<Integer, String> reservedSeatMemberNames = new HashMap<>();

    // 로그인 회원의 관리자 권한을 조회한다.
    public boolean isAdmin(int memberCode) {
        Connection con = getConnection();
        boolean admin = seatDAO.isAdmin(con, memberCode);
        close(con);
        return admin;
    }

    // 전체 좌석 조회가 끝나면 Connection을 닫고 목록을 반환한다.
    public List<SeatDTO> selectAllSeats() {

        Connection con = getConnection();

        List<SeatDTO> seatList = seatDAO.selectAllSeats(con);

        addReservedMemberCodes(seatList);

        close(con);

        return seatList;
    }

    /*
     * 예약 가능한 좌석 조회 서비스 메서드
     */
    public List<SeatDTO> selectAvailableSeats() {

        Connection con = getConnection();

        List<SeatDTO> seatList = seatDAO.selectAvailableSeats(con);

        addReservedMemberCodes(seatList);

        close(con);

        return seatList;
    }

    // 특정 항공편의 모든 좌석을 조회한다(예약된 좌석 포함).
    public List<SeatDTO> selectSeatsByFlightCode(int flightCode) {
        Connection con = getConnection();
        List<SeatDTO> seatList = seatDAO.selectSeatsByFlightCode(con, flightCode);
        addReservedMemberCodes(seatList);
        close(con);
        return seatList;
    }

    /*
     * 특정 항공편의 예약 가능한 좌석 조회 서비스 메서드
     */
    public List<SeatDTO> selectAvailableSeatsByFlightCode(int flightCode) {

        Connection con = getConnection();

        List<SeatDTO> seatList = seatDAO.selectAvailableSeatsByFlightCode(con, flightCode);

        addReservedMemberCodes(seatList);

        close(con);

        return seatList;
    }

    // 로그인 회원의 좌석 미선택 예매번호와 항공편번호를 반환한다.
    public Map<Integer, Integer> selectReservationsWithoutSeat(int memberCode) {
        Connection con = getConnection();
        Map<Integer, Integer> reservationMap =
                seatDAO.selectReservationsWithoutSeat(con, memberCode);
        close(con);
        return reservationMap;
    }

    // 좌석 예약 상태와 로그인 회원의 예매 좌석번호를 하나의 트랜잭션으로 저장한다.
    public boolean reserveSeat(int memberCode, int seatCode) {
        Connection con = getConnection();

        // 좌석번호가 100을 넘어도 그대로 조회하며, 해당 좌석과 같은 항공편의 로그인 회원 예매를 찾는다.
        int reservationCode = seatDAO.selectReservationCodeWithoutSeat(con, memberCode, seatCode);
        int seatResult = 0;
        int reservationResult = 0;

        if (reservationCode > 0) {
            seatResult = seatDAO.reserveSeat(con, seatCode);
        }

        if (seatResult > 0) {
            // 앞에서 찾은 정확한 예매에 선택한 좌석번호를 저장한다.
            reservationResult = seatDAO.updateMemberReservationSeat(
                    con, reservationCode, memberCode, seatCode
            );
        }

        // 좌석 예약과 예매 연결이 모두 성공해야 DB에 확정한다.
        if (seatResult > 0 && reservationResult > 0) {
            // 로그인 회원번호로 이름을 조회하여 좌석 조회 화면에 함께 표시한다.
            String memberName = seatDAO.selectMemberName(con, memberCode);
            commit(con);
            reservedSeatMembers.put(seatCode, memberCode);
            reservedSeatMemberNames.put(seatCode, memberName);
        } else {
            rollback(con);
        }

        close(con);
        return seatResult > 0 && reservationResult > 0;
    }

    /*
     * 특정 항공편에 이미 좌석 선택한 예매가 있는지 확인하는 메서드
     */
    public boolean hasReservationWithSeat(int memberCode, int flightCode) {

        Connection con = getConnection();

        boolean result = seatDAO.hasReservationWithSeat(con, memberCode, flightCode);

        close(con);

        return result;
    }

    /*
     * 좌석 변경을 하나의 트랜잭션으로 처리한다.
     * 새 좌석 예약 -> 예매 좌석번호 변경 -> 기존 좌석 해제 순서이다.
     */
    public boolean changeSeat(int memberCode, int newSeatCode, int flightCode) {

        Connection con = getConnection();

        // 회원번호와 항공편번호로 현재 예매에 연결된 기존 좌석을 찾는다.
        int oldSeatCode = seatDAO.selectReservedSeatCode(con, memberCode, flightCode);
        int newSeatResult = 0;
        int reservationResult = 0;
        int oldSeatResult = 0;

        if (oldSeatCode != 0 && oldSeatCode != newSeatCode) {
            // 1단계: 새 좌석을 예약 상태로 만든다.
            newSeatResult = seatDAO.reserveSeatForFlight(con, newSeatCode, flightCode);

            if (newSeatResult > 0) {
                // 2단계: 예매가 가리키는 좌석번호를 새 좌석번호로 바꾼다.
                reservationResult = seatDAO.changeReservationSeatCode(con, memberCode, oldSeatCode, newSeatCode, flightCode);
            }

            if (reservationResult > 0) {
                // 3단계: 기존 좌석을 다시 예약 가능 상태로 만든다.
                oldSeatResult = seatDAO.cancelSeatReservation(con, oldSeatCode);
            }
        }

        // 세 단계가 모두 성공한 경우에만 DB에 확정한다.
        if (newSeatResult > 0 && reservationResult > 0 && oldSeatResult > 0) {
            String memberName = seatDAO.selectMemberName(con, memberCode);
            commit(con);
            // 좌석 변경 후 예약자 기록도 기존 좌석에서 새 좌석으로 이동한다.
            reservedSeatMembers.remove(oldSeatCode);
            reservedSeatMembers.put(newSeatCode, memberCode);
            reservedSeatMemberNames.remove(oldSeatCode);
            reservedSeatMemberNames.put(newSeatCode, memberName);
        } else {
            rollback(con);
        }

        close(con);

        return newSeatResult > 0 && reservationResult > 0 && oldSeatResult > 0;
    }

    // 회원의 현재 좌석등급을 조회한다.
    public String getSelectedSeatClass(int memberCode, int flightCode) {
        Connection con = getConnection();
        String flightClass = seatDAO.selectSelectedSeatClass(con, memberCode, flightCode);
        close(con);
        return flightClass;
    }

    // 새 좌석이 같은 항공편의 빈 좌석일 때 해당 등급을 반환한다.
    public String getAvailableSeatClass(int seatCode, int flightCode) {
        Connection con = getConnection();
        String flightClass = seatDAO.selectAvailableSeatClass(con, seatCode, flightCode);
        close(con);
        return flightClass;
    }

    // 조회된 DTO에 실행 중 저장한 예약 회원번호를 붙인다.
    private void addReservedMemberCodes(List<SeatDTO> seatList) {
        for (SeatDTO seat : seatList) {
            Integer memberCode = reservedSeatMembers.get(seat.getSeatCode());
            seat.setReservedMemberCode(memberCode == null ? 0 : memberCode);
            seat.setReservedMemberName(reservedSeatMemberNames.get(seat.getSeatCode()));
        }
    }

}
