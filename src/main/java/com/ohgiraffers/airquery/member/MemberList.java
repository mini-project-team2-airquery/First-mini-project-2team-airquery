package com.ohgiraffers.airquery.member;

import com.ohgiraffers.airquery.common.JDBCTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CustomerList {

    public static void customerList(Scanner sc) {

        Connection con = JDBCTemplate.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            System.out.println();
            System.out.println("===== 고객목록 조회 =====");

            String query =
                    "SELECT member_code, member_name, member_id, member_auth, " +
                            "member_phone, member_dob, member_address, member_country, " +
                            "member_gender, passport_number FROM tbl_member ORDER BY member_code";

            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();
            boolean hasMember = false;

            while (rset.next()) {
                hasMember = true;
                System.out.println("---------------------------------");
                System.out.println("회원번호 : " + rset.getInt("member_code"));
                System.out.println("이름     : " + rset.getString("member_name"));
                System.out.println("아이디   : " + rset.getString("member_id"));
                System.out.println("권한     : " + rset.getString("member_auth"));
                System.out.println("전화번호 : " + rset.getString("member_phone"));
                System.out.println("생년월일 : " + rset.getDate("member_dob"));
                System.out.println("주소     : " + rset.getString("member_address"));
                System.out.println("국가     : " + rset.getString("member_country"));
                System.out.println("성별     : " + rset.getString("member_gender"));
                System.out.println("여권번호 : " + rset.getString("passport_number"));
            }

            if (!hasMember) {
                System.out.println("등록된 고객이 없습니다.");
            } else {
                System.out.println("---------------------------------");
                System.out.println("고객목록 조회가 완료되었습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rset);
            JDBCTemplate.close(pstmt);
            JDBCTemplate.close(con);
        }
    }
}
