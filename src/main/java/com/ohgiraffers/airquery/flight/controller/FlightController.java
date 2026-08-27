package com.ohgiraffers.airquery.flight.controller;

import com.ohgiraffers.airquery.flight.model.dto.FlightDTO;
import com.ohgiraffers.airquery.flight.model.service.FlightService;

import java.util.List;

public class FlightController {

    private final FlightService flightService = new FlightService();

    public List<FlightDTO> selectAllFlight() {
        return flightService.selectAllFlight();
    }

    public List<FlightDTO> selectByAirline(String airlineName) {
        return flightService.selectByAirline(airlineName);
    }
}
