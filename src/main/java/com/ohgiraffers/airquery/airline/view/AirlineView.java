package com.ohgiraffers.airquery.airline.view;

import com.ohgiraffers.airquery.airline.dto.AirlineDTO;

import java.util.List;

public class AirlineView {

    public void printAirlineList(List<AirlineDTO> airlineList) {

        System.out.println();
        System.out.println("========== 항공사 목록 ==========");

        if (airlineList == null || airlineList.isEmpty()) {
            System.out.println("등록된 항공사가 없습니다.");
            return;
        }

        for (AirlineDTO airline : airlineList) {

            System.out.println("---------------------------------");
            System.out.println("항공사 번호 : " + airline.getAirlineCode());
            System.out.println("항공사명     : " + airline.getAirlineName());
            System.out.println("고객센터 번호: " + airline.getCustomerServiceNumber());
            System.out.println("등록일       : " + airline.getFirstCreatedDate());
            System.out.println("최종 수정일  : " + airline.getLastModifiedDate());
        }

        System.out.println("---------------------------------");
    }


    public void printSuccessMessage(String message) {

        System.out.println();
        System.out.println("[성공] " + message);
    }


    public void printErrorMessage(String message) {

        System.out.println();
        System.out.println("[실패] " + message);
    }
}