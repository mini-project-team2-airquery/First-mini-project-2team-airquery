package com.ohgiraffers.airquery.flight.model.dao;

import com.ohgiraffers.airquery.common.JDBCTemplate;
import com.ohgiraffers.airquery.flight.model.dto.FlightDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
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
    public int insertFlight(Connection con, FlightDTO flight) {

        PreparedStatement pstmt = null;

        int result = 0;

        System.out.println("추가할 항공편 정보: " + flight);

        String query = prop.getProperty("insertFlight");

        try {

            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, flight.getAirlineCode());
            pstmt.setString(2, flight.getDeparture());
            pstmt.setString(3, flight.getArrival());
            pstmt.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
            pstmt.setTimestamp(5, Timestamp.valueOf(flight.getArrivalTime()));
            pstmt.setString(6, flight.getAirplaneType());
            pstmt.setString(7, flight.getGateNumber());
            pstmt.setInt(8, flight.getTicketPrice());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }

        return result;
    }

    public boolean existsAirline(Connection con, int airlineCode) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        boolean exists = false;

        String query = prop.getProperty("existsAirline");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, airlineCode);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                exists = rset.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rset);
            JDBCTemplate.close(pstmt);
        }

        return exists;
    }

    public boolean existsFlight(Connection con, int code) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        boolean exists = false;

        String query = prop.getProperty("existsFlight");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, code);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                exists = rset.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rset);
            JDBCTemplate.close(pstmt);
        }

        return exists;
    }

    public int updateFlightInfo(Connection con, FlightDTO flight) {

        PreparedStatement pstmt = null;

        int result = 0;

        System.out.println("==== 변경할 항공편 정보 ====");
        System.out.println("항공편 코드: " + flight.getCode());
        System.out.println("출발지: " + flight.getDeparture());
        System.out.println("도착지: " + flight.getArrival());
        System.out.println("기종: " + flight.getAirplaneType());
        System.out.println("게이트번호: " + flight.getGateNumber());

        String query = prop.getProperty("updateFlightInfo");

        try {

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, flight.getDeparture());
            pstmt.setString(2, flight.getArrival());
            pstmt.setString(3, flight.getAirplaneType());
            pstmt.setString(4, flight.getGateNumber());
            pstmt.setInt(5, flight.getCode());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }

        return result;
    }

    public int updateFlightSchedule(Connection con, FlightDTO flight) {

        PreparedStatement pstmt = null;

        int result = 0;

        System.out.println("==== 변경할 항공편 정보 ====");
        System.out.println("항공편 코드: " + flight.getCode());
        System.out.println("출발시간: " + flight.getDepartureTime());
        System.out.println("도착시간: " + flight.getArrivalTime());

        String query = prop.getProperty("updateFlightSchedule");

        try {

            pstmt = con.prepareStatement(query);

            pstmt.setTimestamp(1, Timestamp.valueOf(flight.getDepartureTime()));
            pstmt.setTimestamp(2, Timestamp.valueOf(flight.getArrivalTime()));
            pstmt.setInt(3, flight.getCode());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }

        return result;
    }

    public int updateFlightPrice(Connection con, FlightDTO flight) {

        PreparedStatement pstmt = null;

        int result = 0;

        System.out.println("==== 변경할 항공편 정보 ====");
        System.out.println("항공편 코드: " + flight.getCode());
        System.out.println("가격: " + flight.getTicketPrice());

        String query = prop.getProperty("updateFlightPrice");

        try {

            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, flight.getTicketPrice());
            pstmt.setInt(2, flight.getCode());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }

        return result;
    }

    public boolean existsReservation(Connection con, int code) {

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        boolean exists = false;

        String query = prop.getProperty("existsReservation");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, code);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                exists = rset.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rset);
            JDBCTemplate.close(pstmt);
        }

        return exists;
    }

    public int deleteFlight(Connection con, FlightDTO flight) {

        PreparedStatement pstmt = null;

        int result = 0;

        String query = prop.getProperty("deleteFlight");

        try {

            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, flight.getCode());

            result = pstmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("예매 내역이 존재하는 항공편은 삭제할 수 없습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }

        return result;
    }
}
