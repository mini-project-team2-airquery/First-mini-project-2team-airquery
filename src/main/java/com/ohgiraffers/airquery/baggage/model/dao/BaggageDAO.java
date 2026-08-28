package com.ohgiraffers.airquery.baggage.model.dao;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;

/*
 * DAO(Data Access Object)
 * DB와 직접 대화하는 클래스이다.
 * SELECT, INSERT, UPDATE 같은 SQL문은 이 클래스에서 실행한다.
 */
public class BaggageDAO {

    /*
     * 수하물 전체 조회 메서드
     * 수하물 조회 메뉴에 들어갔을 때 전체 수하물을 먼저 보여주기 위해 사용한다.
     */
    public List<BaggageDTO> selectAllBaggages(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        // 전체 수하물이 여러 개일 수 있으므로 List에 담는다.
        List<BaggageDTO> baggageList = new ArrayList<>();

        String query = "SELECT baggage_code, reservation_code, baggage_weight " +
                "FROM tbl_baggage " +
                "ORDER BY baggage_code";

        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                BaggageDTO baggage = new BaggageDTO();

                baggage.setBaggageCode(rset.getInt("baggage_code"));
                baggage.setReservationCode(rset.getInt("reservation_code"));
                baggage.setBaggageWeight(rset.getDouble("baggage_weight"));

                baggageList.add(baggage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return baggageList;
    }

    /*
     * 예매 존재 여부 확인 메서드
     * tbl_reservation에 사용자가 입력한 reservation_code가 있는지 확인한다.
     */
    public boolean existsReservation(Connection con, int reservationCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        boolean isExist = false;

        String query = "SELECT reservation_code " +
                "FROM tbl_reservation " +
                "WHERE reservation_code = ?";

        try {
            pstmt = con.prepareStatement(query);

            // SQL의 ? 자리에 사용자가 입력한 예매번호를 넣는다.
            pstmt.setInt(1, reservationCode);

            rset = pstmt.executeQuery();

            // 조회 결과가 한 줄이라도 있으면 예매내역이 존재한다는 뜻이다.
            if (rset.next()) {
                isExist = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return isExist;
    }

    /*
     * 예매 시 수하물을 신청했는지 확인하는 메서드
     * tbl_reservation의 baggage_carrying 값이 true이면 수하물 신청을 한 예매이다.
     */
    public boolean isBaggageCarrying(Connection con, int reservationCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        boolean isBaggageCarrying = false;

        String query = "SELECT baggage_carrying " +
                "FROM tbl_reservation " +
                "WHERE reservation_code = ?";

        try {
            pstmt = con.prepareStatement(query);

            // SQL의 ? 자리에 사용자가 입력한 예매번호를 넣는다.
            pstmt.setInt(1, reservationCode);

            rset = pstmt.executeQuery();

            /*
             * 조회된 예매가 있고 baggage_carrying 값이 true이면
             * 예매할 때 수하물 신청을 했다는 뜻이다.
             */
            if (rset.next()) {
                isBaggageCarrying = rset.getBoolean("baggage_carrying");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rset);
            close(pstmt);
        }

        return isBaggageCarrying;
    }

    /*
     * 예매번호로 수하물 조회 메서드
     * 사용자가 입력한 reservation_code와 연결된 수하물만 가져온다.
     */
    public List<BaggageDTO> selectBaggagesByReservationCode(Connection con, int reservationCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        // 조회 결과가 여러 개일 수 있으므로 List에 담는다.
        List<BaggageDTO> baggageList = new ArrayList<>();

        /*
         * ? 는 아직 값이 정해지지 않은 자리이다.
         * 아래 pstmt.setInt(1, reservationCode)에서 ? 자리에 예매번호를 넣는다.
         */
        String query = "SELECT baggage_code, reservation_code, baggage_weight " +
                "FROM tbl_baggage " +
                "WHERE reservation_code = ? " +
                "ORDER BY baggage_code";

        try {
            // SQL문을 DB에 보낼 준비를 한다.
            pstmt = con.prepareStatement(query);

            // SQL의 ? 자리에 사용자가 입력한 예매번호를 넣는다.
            pstmt.setInt(1, reservationCode);

            // SELECT문을 실행하고 결과를 ResultSet으로 받는다.
            rset = pstmt.executeQuery();

            // rset.next()는 조회 결과를 한 줄씩 읽는다.
            while (rset.next()) {
                BaggageDTO baggage = new BaggageDTO();

                // DB 컬럼 값을 DTO 필드에 옮겨 담는다.
                baggage.setBaggageCode(rset.getInt("baggage_code"));
                baggage.setReservationCode(rset.getInt("reservation_code"));
                baggage.setBaggageWeight(rset.getDouble("baggage_weight"));

                // 값이 담긴 DTO를 목록에 추가한다.
                baggageList.add(baggage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // ResultSet과 PreparedStatement는 사용 후 닫아야 한다.
            close(rset);
            close(pstmt);
        }

        // 조회된 수하물 목록을 Service로 돌려준다.
        return baggageList;
    }

    /*
     * 수하물 등록 메서드
     * 예매번호와 수하물 무게를 DB에 새로 저장한다.
     */
    public int insertBaggage(Connection con, BaggageDTO baggage) {

        PreparedStatement pstmt = null;

        // INSERT 성공 시 1, 실패 시 0이 들어간다.
        int result = 0;

        /*
         * baggage_code는 AUTO_INCREMENT라 직접 넣지 않는다.
         * DB가 자동으로 수하물번호를 만들어준다.
         */
        String query = "INSERT INTO tbl_baggage (reservation_code, baggage_weight) " +
                "VALUES (?, ?)";

        try {
            // INSERT SQL문을 DB에 보낼 준비를 한다.
            pstmt = con.prepareStatement(query);

            // 첫 번째 ? 에 예매번호를 넣는다.
            pstmt.setInt(1, baggage.getReservationCode());

            // 두 번째 ? 에 수하물 무게를 넣는다.
            pstmt.setDouble(2, baggage.getBaggageWeight());

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
     * 수하물 무게 변경 메서드
     * baggage_code로 수하물을 찾고 baggage_weight 값을 수정한다.
     */
    public int updateBaggageWeight(Connection con, int baggageCode, double baggageWeight) {

        PreparedStatement pstmt = null;

        // UPDATE 성공 시 1, 실패 시 0이 들어간다.
        int result = 0;

        /*
         * baggage_code가 일치하는 수하물을 찾아서
         * baggage_weight 값만 새 무게로 변경한다.
         */
        String query = "UPDATE tbl_baggage " +
                "SET baggage_weight = ? " +
                "WHERE baggage_code = ?";

        try {
            // UPDATE SQL문을 DB에 보낼 준비를 한다.
            pstmt = con.prepareStatement(query);

            // 첫 번째 ? 에 새 수하물 무게를 넣는다.
            pstmt.setDouble(1, baggageWeight);

            // 두 번째 ? 에 변경할 수하물번호를 넣는다.
            pstmt.setInt(2, baggageCode);

            // UPDATE문을 실행한다.
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return result;
    }
}
