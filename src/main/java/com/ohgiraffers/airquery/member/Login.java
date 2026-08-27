package com.ohgiraffers.airquery.member;

import com.ohgiraffers.airquery.common.JDBCTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login {

    public static MemberDTO login(Scanner sc) {

        Connection con = JDBCTemplate.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        MemberDTO loginMember = null;

        try {

            System.out.println();
            System.out.println("===== 로그인 =====");

            System.out.print("아이디 : ");
            String memberId = sc.nextLine();

            System.out.print("비밀번호 : ");
            String memberPw = sc.nextLine();

            String query =
                    "SELECT " +
                            "member_code, member_name, member_id, member_pw, member_auth, " +
                            "member_phone, member_dob, member_address, member_country, " +
                            "member_gender, passport_number, first_created_date, last_modified_date " +
                            "FROM tbl_member WHERE member_id = ? AND member_pw = ?";

            pstmt = con.prepareStatement(query);
            pstmt.setString(1, memberId);
            pstmt.setString(2, memberPw);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                loginMember = new MemberDTO();
                loginMember.setMemberCode(rset.getInt("member_code"));
                loginMember.setMemberName(rset.getString("member_name"));
                loginMember.setMemberId(rset.getString("member_id"));
                loginMember.setMemberPw(rset.getString("member_pw"));
                loginMember.setMemberAuth(rset.getString("member_auth"));
                loginMember.setMemberPhone(rset.getString("member_phone"));
                loginMember.setMemberDob(rset.getDate("member_dob"));
                loginMember.setMemberAddress(rset.getString("member_address"));
                loginMember.setMemberCountry(rset.getString("member_country"));
                loginMember.setMemberGender(rset.getString("member_gender"));
                loginMember.setPassportNumber(rset.getString("passport_number"));
                loginMember.setFirstCreatedDate(rset.getTimestamp("first_created_date"));
                loginMember.setLastModifiedDate(rset.getTimestamp("last_modified_date"));

                System.out.println();
                System.out.println("로그인 성공!");
                System.out.println("안녕하세요, " + loginMember.getMemberName() + "님.");
                JDBCTemplate.commit(con);
            } else {
                System.out.println();
                System.out.println("아이디 또는 비밀번호가 올바르지 않습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JDBCTemplate.rollback(con);
        } finally {
            JDBCTemplate.close(rset);
            JDBCTemplate.close(pstmt);
            JDBCTemplate.close(con);
        }

        return loginMember;
    }
}
