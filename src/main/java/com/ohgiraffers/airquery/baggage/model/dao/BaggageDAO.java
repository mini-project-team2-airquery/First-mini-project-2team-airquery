package com.ohgiraffers.airquery.baggage.model.dao;

import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;

public class BaggageDAO {

    /*
     * 수하물 전체 조회 메서드
     * tbl_baggage 테이블에 있는 모든 수하물 정보를 가져온다.
     */
    public List<BaggageDTO> selectAllBaggages(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
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
     * 예매번호로 수하물 조회 메서드
     * 사용자가 입력한 reservation_code와 연결된 수하물만 가져온다.
     */
    public List<BaggageDTO> selectBaggagesByReservationCode(Connection con, int reservationCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<BaggageDTO> baggageList = new ArrayList<>();

        String query = "SELECT baggage_code, reservation_code, baggage_weight " +
                "FROM tbl_baggage " +
                "WHERE reservation_code = ? " +
                "ORDER BY baggage_code";

        try {
            pstmt = con.prepareStatement(query);

            // SQL의 ? 자리에 사용자가 입력한 예매번호를 넣는다.
            pstmt.setInt(1, reservationCode);

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
     * 수하물 등록 메서드
     * 예매번호와 수하물 무게를 DB에 새로 저장한다.
     */
    public int insertBaggage(Connection con, BaggageDTO baggage) {

        PreparedStatement pstmt = null;
        int result = 0;

        String query = "INSERT INTO tbl_baggage (reservation_code, baggage_weight) " +
                "VALUES (?, ?)";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, baggage.getReservationCode());
            pstmt.setDouble(2, baggage.getBaggageWeight());

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
        int result = 0;

        String query = "UPDATE tbl_baggage " +
                "SET baggage_weight = ? " +
                "WHERE baggage_code = ?";

        try {
            pstmt = con.prepareStatement(query);

            pstmt.setDouble(1, baggageWeight);
            pstmt.setInt(2, baggageCode);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return result;
    }
}
