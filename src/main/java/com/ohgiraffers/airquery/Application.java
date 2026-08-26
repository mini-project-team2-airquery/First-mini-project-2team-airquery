
package com.ohgiraffers.airquery;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("          항공 예약 시스템");
            System.out.println("=================================");
            System.out.println("1. 회원");
            System.out.println("2. 항공사");
            System.out.println("3. 항공편");
            System.out.println("4. 예약");
            System.out.println("5. 결제");
            System.out.println("0. 프로그램 종료");
            System.out.println("=================================");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    memberMenu(sc);
                    break;

                case "2":
                    airlineMenu(sc);
                    break;

                case "3":
                    flightMenu(sc);
                    break;

                case "4":
                    reservationMenu(sc);
                    break;

                case "5":
                    paymentMenu(sc);
                    break;

                case "0":
                    System.out.println("프로그램을 종료합니다.");
                    sc.close();
                    return;

                default:
                    System.out.println("잘못된 번호입니다. 다시 입력해주세요.");
            }
        }
    }


    // ================= 회원 =================

    public static void memberMenu(Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println("===== 회원 메뉴 =====");
            System.out.println("1. 회원 기능");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    System.out.println("회원 기능 구현 예정");
                    break;

                case "9":
                    return;

                default:
                    System.out.println("잘못된 번호입니다.");
            }
        }
    }


    // ================= 항공사 =================

    public static void airlineMenu(Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println("===== 항공사 메뉴 =====");
            System.out.println("1. 항공사 등록");
            System.out.println("2. 항공사 조회");
            System.out.println("3. 항공사 변경");
            System.out.println("4. 항공사 삭제");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    System.out.println("항공사 등록 기능 구현 예정");
                    break;

                case "2":
                    System.out.println("항공사 조회 기능 구현 예정");
                    break;

                case "3":
                    System.out.println("항공사 변경 기능 구현 예정");
                    break;

                case "4":
                    System.out.println("항공사 삭제 기능 구현 예정");
                    break;

                case "9":
                    return;

                default:
                    System.out.println("잘못된 번호입니다.");
            }
        }
    }


    // ================= 항공편 =================

    public static void flightMenu(Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println("===== 항공편 메뉴 =====");
            System.out.println("1. 항공편 기능");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    System.out.println("항공편 기능 구현 예정");
                    break;

                case "9":
                    return;

                default:
                    System.out.println("잘못된 번호입니다.");
            }
        }
    }


    // ================= 예약 =================

    public static void reservationMenu(Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println("===== 예약 메뉴 =====");
            System.out.println("1. 예약 기능");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    System.out.println("예약 기능 구현 예정");
                    break;

                case "9":
                    return;

                default:
                    System.out.println("잘못된 번호입니다.");
            }
        }
    }


    // ================= 결제 =================

    public static void paymentMenu(Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println("===== 결제 메뉴 =====");
            System.out.println("1. 결제 기능");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":
                    System.out.println("결제 기능 구현 예정");
                    break;

                case "9":
                    return;

                default:
                    System.out.println("잘못된 번호입니다.");
            }
        }
    }
}