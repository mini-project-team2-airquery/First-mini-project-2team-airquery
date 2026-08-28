package com.ohgiraffers.airquery.airline.service;

import com.ohgiraffers.airquery.airline.dao.AirlineDAO;
import com.ohgiraffers.airquery.airline.dto.AirlineDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import static com.ohgiraffers.airquery.common.JDBCTemplate.commit;
import static com.ohgiraffers.airquery.common.JDBCTemplate.getConnection;
import static com.ohgiraffers.airquery.common.JDBCTemplate.rollback;

public class AirlineService {

    private final AirlineDAO airlineDAO = new AirlineDAO();

    // FR-07 항공사 조회
    public List<AirlineDTO> selectAllAirlines() {

        Connection con = getConnection();

        List<AirlineDTO> airlineList =
                airlineDAO.selectAllAirlines(con);

        close(con);

        return airlineList;
    }

    // FR-06 항공사 등록
    public boolean insertAirline(AirlineDTO airline) {

        Connection con = getConnection();

        int result = airlineDAO.insertAirline(con, airline);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }

    // FR-08 항공사 변경
    public boolean updateAirline(AirlineDTO airline) {

        Connection con = getConnection();

        int result = airlineDAO.updateAirline(con, airline);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }

    // FR-09 항공사 삭제
    public boolean deleteAirline(int airlineCode) {

        Connection con = getConnection();

        int result = airlineDAO.deleteAirline(con, airlineCode);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }
}