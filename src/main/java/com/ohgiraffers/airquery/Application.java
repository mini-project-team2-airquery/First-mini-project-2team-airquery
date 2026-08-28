package com.ohgiraffers.airquery;

import com.ohgiraffers.airquery.airline.menu.AirlineMenu;
import com.ohgiraffers.airquery.flight.view.FlightMenu;
import com.ohgiraffers.airquery.member.*;
import com.ohgiraffers.airquery.payment.view.PaymentMenu;
import com.ohgiraffers.airquery.reservation.view.ReservationMenu;
import com.ohgiraffers.airquery.seat.view.SeatMenu;


import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println();
            System.out.println("                _\n" +
                    "              -=\\`\\\n" +
                    "          |\\ ____\\_\\__\n" +
                    "        -=\\c`\"\"\"\"\"\"\" \"`)\n" +
                    "           `~~~~~/ /~~`\n" +
                    "             -==/ /\n" +
                    "               '-'       AIRQUERY");
            System.out.println();
            System.out.println();
            System.out.println("===== Airquery Reservation System =====");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("9. 프로그램 종료");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":

                    MemberDTO loginMember = Login.login(sc);
                    mainMenu(sc, loginMember);

                    break;


                case "2":

                    JoinMember.joinMember(sc);
                    break;


                case "9":

                    System.out.println("프로그램을 종료합니다.");
                    sc.close();
                    return;


                default:

                    System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
                    break;
            }
        }
    }



    // ================= 메인 메뉴 =================

    public static void mainMenu(Scanner sc, MemberDTO loginMember) {

        while (true) {

            System.out.println();
            System.out.println("===================================");
            System.out.println("          항공 예약 시스템");
            System.out.println("===================================");
            System.out.println("1. 항공사");
            System.out.println("2. 항공편");
            System.out.println("3. 예약");
            System.out.println("4. 결제");
            System.out.println("5. 수하물");
            System.out.println("6. 좌석");
            System.out.println("9. 로그아웃");
            System.out.println("=================================");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    airlineMenu(sc, loginMember);
                    break;

                case "2":
                    flightMenu(sc, loginMember);
                    break;

                case "3":
                    reservationMenu(sc, loginMember);
                    break;

                case "4":
                    paymentMenu(sc, loginMember);
                    break;

                case "5":
                    baggageMenu(sc, loginMember);
                    break;

                case "6":
                    seatMenu(sc, loginMember);
                    break;

                case "9":
                    System.out.println("로그아웃합니다.");
                    return;

                default:
                    System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
                    break;
            }
        }
    }

    // ================= 항공사 =================

    public static void airlineMenu(Scanner sc, MemberDTO loginMember) {

        AirlineMenu airlineMenu = new AirlineMenu();

        if("Admin".equals(loginMember.getMemberAuth())){

            airlineMenu.displayMenu(sc);
        } else if("Member".equals(loginMember.getMemberAuth())){

        } else {
            System.out.println("알수없는 회원입니다.");
        }

    }


    // ================= 항공편 =================

    public static void flightMenu(Scanner sc, MemberDTO loginMember) {

        FlightMenu flightMenu = new FlightMenu();

        if("Admin".equals(loginMember.getMemberAuth())){

            flightMenu.displayMenu(sc);
        } else if("Member".equals(loginMember.getMemberAuth())){

        } else {
            System.out.println("알수없는 회원입니다.");
        }
    }


    // ================= 예약 =================

    public static void reservationMenu(Scanner sc, MemberDTO loginMember) {

        ReservationMenu reservationMenu = new ReservationMenu();

        if("Admin".equals(loginMember.getMemberAuth())){

            reservationMenu.displayMenu(sc);
        } else if("Member".equals(loginMember.getMemberAuth())){

        } else {
            System.out.println("알수없는 회원입니다.");
        }
    }


    // ================= 결제 =================

    public static void paymentMenu(Scanner sc, MemberDTO loginMember) {

        PaymentMenu paymentMenu = new PaymentMenu();

        if("Admin".equals(loginMember.getMemberAuth())){

            paymentMenu.displayMenu(sc);
        } else if("Member".equals(loginMember.getMemberAuth())){

        } else {
            System.out.println("알수없는 회원입니다.");
        }
    }


    // ================= 수하물 =================

    public static void baggageMenu(Scanner sc, MemberDTO loginMember) {

        while (true) {

            System.out.println();
            System.out.println("===== 수하물 메뉴 =====");
            System.out.println("1. 수하물 기능");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    System.out.println("수하물 기능 구현 예정");
                    break;

                case "9":
                    return;

                default:
                    System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
                    break;
            }
        }
    }


    // ================= 좌석 =================

    public static void seatMenu(Scanner sc, MemberDTO loginMember) {

        SeatMenu seatMenu = new SeatMenu();

        if("Admin".equals(loginMember.getMemberAuth())){

            seatMenu.displayMenu(sc);
        } else if("Member".equals(loginMember.getMemberAuth())){

            seatMenu.displayMenu(sc, loginMember.getMemberCode());
        } else {
            System.out.println("알수없는 회원입니다.");
        }
    }
}
