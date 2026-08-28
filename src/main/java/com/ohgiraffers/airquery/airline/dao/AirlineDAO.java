package com.ohgiraffers.airquery.airline.dao;

import com.ohgiraffers.airquery.airline.dto.AirlineDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;

public class AirlineDAO {

    // FR-07 항공사 조회
    public List<AirlineDTO> selectAllAirlines(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<AirlineDTO> airlineList = new ArrayList<>();

        String sql =
                "SELECT airline_code, " +
                        "airline_name, " +
                        "customer_service_number, " +
                        "first_created_date, " +
                        "last_modified_date " +
                        "FROM tbl_airline " +
                        "ORDER BY airline_code";

        try {
            pstmt = con.prepareStatement(sql);
            rset = pstmt.executeQuery();

            while (rset.next()) {

                AirlineDTO airline = new AirlineDTO();

                airline.setAirlineCode(rset.getInt("airline_code"));
                airline.setAirlineName(rset.getString("airline_name"));
                airline.setCustomerServiceNumber(
                        rset.getString("customer_service_number")
                );
                airline.setFirstCreatedDate(
                        rset.getTimestamp("first_created_date")
                );
                airline.setLastModifiedDate(
                        rset.getTimestamp("last_modified_date")
                );

                airlineList.add(airline);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return airlineList;
    }


    // FR-06 항공사 등록
    public int insertAirline(Connection con, AirlineDTO airline) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        int result = 0;

        // 비어 있는 가장 작은 항공사 번호를 저장
        int airlineCode = 1;

        // 현재 사용하지 않는 가장 작은 항공사 번호 조회
        String findSql =
                "SELECT MIN(t1.airline_code + 1) AS airline_code " +
                        "FROM ( " +
                        "SELECT 0 AS airline_code " +
                        "UNION ALL " +
                        "SELECT airline_code FROM tbl_airline " +
                        ") t1 " +
                        "LEFT JOIN tbl_airline t2 " +
                        "ON t1.airline_code + 1 = t2.airline_code " +
                        "WHERE t2.airline_code IS NULL";

        String sql =
                "INSERT INTO tbl_airline " +
                        "(airline_code, airline_name, customer_service_number) " +
                        "VALUES (?, ?, ?)";

        try {

            // 비어 있는 가장 작은 항공사 번호 조회
            pstmt = con.prepareStatement(findSql);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                airlineCode = rset.getInt("airline_code");
            }

            close(rset);
            close(pstmt);

            // 새로운 항공사 등록
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, airlineCode);
            pstmt.setString(2, airline.getAirlineName());
            pstmt.setString(3, airline.getCustomerServiceNumber());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return result;
    }


    // FR-08 항공사 변경
    public int updateAirline(Connection con, AirlineDTO airline) {

        PreparedStatement pstmt = null;
        int result = 0;

        String sql =
                "UPDATE tbl_airline " +
                        "SET airline_name = ?, " +
                        "customer_service_number = ? " +
                        "WHERE airline_code = ?";

        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, airline.getAirlineName());
            pstmt.setString(2, airline.getCustomerServiceNumber());
            pstmt.setInt(3, airline.getAirlineCode());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(pstmt);
        }

        return result;
    }


    // FR-09 항공사 삭제
    public int deleteAirline(Connection con, int airlineCode) {

        PreparedStatement pstmt = null;
        int result = 0;

        String sql =
                "DELETE FROM tbl_airline " +
                        "WHERE airline_code = ?";

        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, airlineCode);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(pstmt);
        }

        return result;
    }
}