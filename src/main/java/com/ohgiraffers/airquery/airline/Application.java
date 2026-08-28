package com.ohgiraffers.airquery.airline;

import com.ohgiraffers.airquery.airline.menu.AirlineMenu;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        AirlineMenu airlineMenu = new AirlineMenu();
        Scanner sc = new Scanner(System.in);

        airlineMenu.displayMenu(sc);
    }
}