package com.ohgiraffers.airquery;

import com.ohgiraffers.airquery.flight.view.FlightMenu;
import com.ohgiraffers.airquery.member.*;
import com.ohgiraffers.airquery.reservation.view.ReservationMenu;
import com.ohgiraffers.airquery.seat.view.SeatMenu;


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
            System.out.println("6. 수하물");
            System.out.println("7. 좌석");
            System.out.println("9. 프로그램 종료");
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

                case "6":
                    baggageMenu(sc);
                    break;

                case "7":
                    seatMenu(sc);
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


    // ================= 회원 =================

    public static void memberMenu(Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println("===== 회원 메뉴 =====");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":

                    MemberDTO loginMember = Login.login(sc);

                    if (loginMember != null) {

                        // 로그인 성공 후 권한에 따라 메뉴 분기
                        if ("Admin".equals(loginMember.getMemberAuth())) {

                            adminMemberMenu(sc, loginMember);

                        } else if ("Member".equals(loginMember.getMemberAuth())) {

                            normalMemberMenu(sc, loginMember);

                        } else {

                            System.out.println("알 수 없는 권한입니다.");
                        }
                    }

                    break;


                case "2":

                    JoinMember.joinMember(sc);
                    break;


                case "9":

                    return;


                default:

                    System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
                    break;
            }
        }
    }


    // ================= 일반회원 메뉴 =================

    public static void normalMemberMenu(Scanner sc, MemberDTO loginMember) {

        while (true) {

            System.out.println();
            System.out.println("===== 일반회원 메뉴 =====");
            System.out.println("현재 회원 : " + loginMember.getMemberName());
            System.out.println("---------------------------------");
            System.out.println("1. 내 정보 조회");
            System.out.println("2. 회원정보 수정");
            System.out.println("9. 로그아웃");
            System.out.println("---------------------------------");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":

                    System.out.println();
                    System.out.println("===== 내 정보 =====");
                    System.out.println("회원번호 : " + loginMember.getMemberCode());
                    System.out.println("이름 : " + loginMember.getMemberName());
                    System.out.println("아이디 : " + loginMember.getMemberId());
                    System.out.println("전화번호 : " + loginMember.getMemberPhone());
                    System.out.println("주소 : " + loginMember.getMemberAddress());
                    System.out.println("권한 : " + loginMember.getMemberAuth());

                    break;


                case "2":

                    UpdateMember.updateMember(sc, loginMember);

                    break;


                case "9":

                    System.out.println("로그아웃합니다.");

                    return;


                default:

                    System.out.println("잘못 눌렀습니다.");
                    break;
            }
        }
    }


    // ================= 관리자 메뉴 =================

    public static void adminMemberMenu(Scanner sc, MemberDTO loginMember) {

        while (true) {

            System.out.println();
            System.out.println("===== 관리자 메뉴 =====");
            System.out.println("관리자 : " + loginMember.getMemberName());
            System.out.println("---------------------------------");
            System.out.println("1. 고객목록 조회");
            System.out.println("9. 로그아웃");
            System.out.println("---------------------------------");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":

                    CustomerList.customerList(sc);

                    break;


                case "9":

                    System.out.println("로그아웃합니다.");

                    return;


                default:

                    System.out.println("잘못 눌렀습니다.");
                    break;
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
                    System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
                    break;
            }
        }
    }


    // ================= 항공편 =================

    public static void flightMenu(Scanner sc) {

        FlightMenu flightMenu = new FlightMenu();

        flightMenu.displayMenu(sc);
    }


    // ================= 예약 =================

    public static void reservationMenu(Scanner sc) {

        ReservationMenu reservationMenu = new ReservationMenu();

        reservationMenu.displayMenu(sc);
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
                    System.out.println("잘못 눌렀습니다. 메뉴로 돌아갑니다.");
                    break;
            }
        }
    }


    // ================= 수하물 =================

    public static void baggageMenu(Scanner sc) {

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

    public static void seatMenu(Scanner sc) {

        SeatMenu seatMenu = new SeatMenu();

        seatMenu.displayMenu(sc);
    }
}
