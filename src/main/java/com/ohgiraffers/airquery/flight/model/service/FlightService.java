package com.ohgiraffers.airquery.flight.model.service;

import com.ohgiraffers.airquery.flight.model.dao.FlightDAO;
import com.ohgiraffers.airquery.flight.model.dto.FlightDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.*;

public class FlightService {

    private final FlightDAO flightDAO = new FlightDAO();

    public List<FlightDTO> selectAllFlight() {

        Connection con = getConnection();

        List<FlightDTO> flightList = flightDAO.selectAllFlight(con);

        close(con);

        return flightList;
    }

    public List<FlightDTO> selectByAirline(String airlineName) {

        Connection con = getConnection();

        List<FlightDTO> flightList = flightDAO.selectByAirline(con, airlineName);

        close(con);

        return flightList;
    }

    public boolean insertFlight(FlightDTO flight) {

        Connection con = getConnection();

        int result = flightDAO.insertFlight(con, flight);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }

    public boolean existsAirline(int airlineCode) {

        Connection con = getConnection();

        boolean exists = flightDAO.existsAirline(con, airlineCode);

        close(con);

        return exists;
    }

    public boolean existsFlight(int code) {

        Connection con = getConnection();

        boolean exists = flightDAO.existsFlight(con, code);

        close(con);

        return exists;
    }

    public boolean updateFlightInfo(FlightDTO flight) {

        Connection con = getConnection();

        int result = flightDAO.updateFlightInfo(con, flight);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }

    public boolean updateFlightSchedule(FlightDTO flight) {

        Connection con = getConnection();

        int result = flightDAO.updateFlightSchedule(con, flight);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }

    public boolean updateFlightPrice(FlightDTO flight) {

        Connection con = getConnection();

        int result = flightDAO.updateFlightPrice(con, flight);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }
}
