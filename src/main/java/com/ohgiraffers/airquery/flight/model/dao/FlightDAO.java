package com.ohgiraffers.airquery.flight.model.dao;

import com.ohgiraffers.airquery.common.JDBCTemplate;
import com.ohgiraffers.airquery.flight.model.dto.FlightDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FlightDAO {

    private Properties prop = new Properties();

    public FlightDAO() {

        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/ohgiraffers/airquery/flight/mapper/flight-query.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FlightDTO> selectAllFlight(Connection con) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<FlightDTO> flightList = null;

        String query = prop.getProperty("selectAllFlight");

        try {
            pstmt = con.prepareStatement(query);

            rset = pstmt.executeQuery();

            flightList = new ArrayList<>();

            while (rset.next()) {
                FlightDTO flight = new FlightDTO();
                flight.setCode(rset.getInt("flight_code"));
                flight.setAirlineCode(rset.getInt("airline_code"));
                flight.setAirlineName(rset.getString("airline_name"));
                flight.setDeparture(rset.getString("flight_departure"));
                flight.setArrival(rset.getString("flight_arrival"));
                flight.setDepartureTime(
                        rset.getObject("flight_departure_time", LocalDateTime.class)
                );
                flight.setArrivalTime(
                        rset.getObject("flight_arrival_time", LocalDateTime.class)
                );
                flight.setAirplaneType(rset.getString("airplane_type"));
                flight.setGateNumber(rset.getString("flight_gate_number"));
                flight.setTicketPrice(rset.getInt("flight_ticket_price"));
//                flight.setCreatedAt(
//                        rset.getObject("first_created_date", LocalDateTime.class)
//                );
//                flight.setUpdatedAt(
//                        rset.getObject("last_modified_date", LocalDateTime.class)
//                );

                flightList.add(flight);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rset);
            JDBCTemplate.close(pstmt);
        }

        return flightList;
    }

    public List<FlightDTO> selectByAirline(Connection con, String airlineName) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List<FlightDTO> flightList = null;

        String query = prop.getProperty("selectByAirline");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, airlineName);

            rset = pstmt.executeQuery();

            flightList = new ArrayList<>();

            while (rset.next()) {
                FlightDTO flight = new FlightDTO();
                flight.setCode(rset.getInt("flight_code"));
                flight.setAirlineCode(rset.getInt("airline_code"));
                flight.setAirlineName(rset.getString("airline_name"));
                flight.setDeparture(rset.getString("flight_departure"));
                flight.setArrival(rset.getString("flight_arrival"));
                flight.setDepartureTime(
                        rset.getObject("flight_departure_time", LocalDateTime.class)
                );
                flight.setArrivalTime(
                        rset.getObject("flight_arrival_time", LocalDateTime.class)
                );
                flight.setAirplaneType(rset.getString("airplane_type"));
                flight.setGateNumber(rset.getString("flight_gate_number"));
                flight.setTicketPrice(rset.getInt("flight_ticket_price"));
//                flight.setCreatedAt(
//                        rset.getObject("first_created_date", LocalDateTime.class)
//                );
//                flight.setUpdatedAt(
//                        rset.getObject("last_modified_date", LocalDateTime.class)
//                );

                flightList.add(flight);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rset);
            JDBCTemplate.close(pstmt);
        }

        return flightList;
    }
}
