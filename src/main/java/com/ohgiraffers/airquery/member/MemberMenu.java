package com.ohgiraffers.airquery.member;

import java.util.Scanner;

public class MemberMenu {

    public void displayNormalMenu(Scanner sc, MemberDTO loginMember) {
        while (true) {

            System.out.println();
            System.out.println("===== 일반회원 메뉴 =====");
            System.out.println("현재 회원 : " + loginMember.getMemberName());
            System.out.println("---------------------------------");
            System.out.println("1. 내 정보 조회");
            System.out.println("2. 회원정보 수정");
            System.out.println("9. 메인 메뉴로 돌아가기");
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

                    System.out.println("메인 메뉴로 돌아갑니다.");

                    return;


                default:

                    System.out.println("잘못 눌렀습니다.");
            }
        }
    }

    public void displayAdminMenu(Scanner sc, MemberDTO loginMember) {
        while (true) {


            System.out.println();
            System.out.println("===== 관리자 메뉴 =====");
            System.out.println("관리자 : " + loginMember.getMemberName());
            System.out.println("---------------------------------");
            System.out.println("1. 회원목록 조회");
            System.out.println("9. 메인 메뉴로 돌아가기");
            System.out.println("---------------------------------");
            System.out.print("메뉴 선택 : ");

            String input = sc.nextLine();

            switch (input) {

                case "1":

                    MemberList.memberList(sc);
                    break;

                case "9":

                    System.out.println("메인 메뉴로 돌아갑니다.");
                    return;


                default:

                    System.out.println("잘못 눌렀습니다.");
                    break;
            }
        }
    }
}
