package com.ohgiraffers.airquery.member;

import com.ohgiraffers.airquery.common.JDBCTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class JoinMember {

    public static void joinMember(Scanner sc) {

        Connection con = JDBCTemplate.getConnection();
        PreparedStatement pstmt = null;

        try {

            System.out.println();
            System.out.println("===== 회원가입 =====");

            System.out.print("이름 : ");
            String name = sc.nextLine();

            System.out.print("아이디 : ");
            String id = sc.nextLine();

            System.out.print("비밀번호 : ");
            String pw = sc.nextLine();

            System.out.print("전화번호 : ");
            String phone = sc.nextLine();

            System.out.print("생년월일 (YYYY-MM-DD) : ");
            String dob = sc.nextLine();

            System.out.print("주소 : ");
            String address = sc.nextLine();

            System.out.print("국적 : ");
            String country = sc.nextLine();

            System.out.print("성별 (M/F) : ");
            String gender = sc.nextLine();

            System.out.print("여권번호 : ");
            String passport = sc.nextLine();

            String sql =
                    "INSERT INTO tbl_member " +
                            "(member_name, member_id, member_pw, member_auth, " +
                            "member_phone, member_dob, member_address, member_country, " +
                            "member_gender, passport_number) " +
                            "VALUES (?, ?, ?, 'Member', ?, ?, ?, ?, ?, ?)";

            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, name);
            pstmt.setString(2, id);
            pstmt.setString(3, pw);
            pstmt.setString(4, phone);
            pstmt.setString(5, dob);
            pstmt.setString(6, address);
            pstmt.setString(7, country);
            pstmt.setString(8, gender);
            pstmt.setString(9, passport);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                JDBCTemplate.commit(con);
                System.out.println();
                System.out.println("회원가입이 완료되었습니다.");
            } else {
                JDBCTemplate.rollback(con);
                System.out.println("회원가입에 실패했습니다.");
            }

        } catch (SQLException e) {

            JDBCTemplate.rollback(con);
            System.out.println("회원가입 중 오류가 발생했습니다.");
            e.printStackTrace();

        } finally {

            JDBCTemplate.close(pstmt);
            JDBCTemplate.close(con);
        }
    }
}
