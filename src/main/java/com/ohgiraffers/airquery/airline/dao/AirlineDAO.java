package com.ohgiraffers.airquery.airline.dao;

import com.ohgiraffers.airquery.airline.dto.AirlineDTO;
import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirlineDAO {

    public List<AirlineDTO> selectAllAirlines(Connection con) {

        List<AirlineDTO> airlineList = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        String sql = "SELECT " +
                "airline_code, " +
                "airline_name, " +
                "customer_service_number, " +
                "first_created_date, " +
                "last_modified_date " +
                "FROM tbl_airline";

        try {
            pstmt = con.prepareStatement(sql);

            rset = pstmt.executeQuery();

            while (rset.next()) {

                AirlineDTO airline = new AirlineDTO();

                airline.setAirlineCode(rset.getInt("airline_code"));
                airline.setAirlineName(rset.getString("airline_name"));
                airline.setCustomerServiceNumber(rset.getString("customer_service_number"));
                airline.setFirstCreatedDate(
                        rset.getTimestamp("first_created_date").toLocalDateTime()
                );

                airline.setLastModifiedDate(
                        rset.getTimestamp("last_modified_date").toLocalDateTime()
                );

                airlineList.add(airline);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            try {
                if (rset != null && !rset.isClosed()) {
                    rset.close();
                }

                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return airlineList;
    }

    public int insertAirline(Connection con, AirlineDTO airline) {

        PreparedStatement pstmt = null;
        int result = 0;

        String sql = "INSERT INTO tbl_airline " +
                "(airline_name, customer_service_number) " +
                "VALUES (?, ?)";

        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, airline.getAirlineName());
            pstmt.setString(2, airline.getCustomerServiceNumber());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(pstmt);
        }

        return result;
    }
}