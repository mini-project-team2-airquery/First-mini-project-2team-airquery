package com.ohgiraffers.airquery.airline.controller;

import com.ohgiraffers.airquery.airline.dto.AirlineDTO;
import com.ohgiraffers.airquery.airline.service.AirlineService;

import java.util.List;

public class AirlineController {

    private final AirlineService AirlineService = new AirlineService();


    // FR-07 조회
    public List<AirlineDTO> selectAllAirlines() {

        return AirlineService.selectAllAirlines();
    }


    // FR-06 등록
    public boolean insertAirline(String airlineName,
                                 String customerServiceNumber) {

        AirlineDTO airline =
                new AirlineDTO(airlineName, customerServiceNumber);

        return AirlineService.insertAirline(airline);
    }


    // FR-08 변경
    public boolean updateAirline(int airlineCode,
                                 String airlineName,
                                 String customerServiceNumber) {

        AirlineDTO airline = new AirlineDTO();

        airline.setAirlineCode(airlineCode);
        airline.setAirlineName(airlineName);
        airline.setCustomerServiceNumber(customerServiceNumber);

        return AirlineService.updateAirline(airline);
    }


    // FR-09 삭제
    public boolean deleteAirline(int airlineCode) {

        return AirlineService.deleteAirline(airlineCode);
    }
}