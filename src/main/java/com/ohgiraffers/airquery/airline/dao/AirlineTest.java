package com.ohgiraffers.airquery.airline.dao;

import com.ohgiraffers.airquery.airline.dto.AirlineDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import static com.ohgiraffers.airquery.common.JDBCTemplate.getConnection;

public class AirlineTest {

    public static void main(String[] args) {

        Connection con = getConnection();

        AirlineDAO dao = new AirlineDAO();

        List<AirlineDTO> airlineList = dao.selectAllAirlines(con);

        for (AirlineDTO airline : airlineList) {
            System.out.println(airline);
        }

        close(con);
    }
}