package com.ohgiraffers.airquery.member;

import com.ohgiraffers.airquery.common.JDBCTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class UpdateMember {

    public static void updateMember(Scanner sc, MemberDTO loginMember) {

        Connection con = JDBCTemplate.getConnection();
        PreparedStatement pstmt = null;

        try {

            System.out.println();
            System.out.println("===== 회원정보 수정 =====");

            // 현재 정보 출력
            System.out.println("현재 이름     : " + loginMember.getMemberName());
            System.out.println("현재 전화번호 : " + loginMember.getMemberPhone());
            System.out.println("현재 주소     : " + loginMember.getMemberAddress());

            System.out.println();
            System.out.println("※ 변경하지 않을 정보는 그냥 Enter를 눌러주세요.");

            // 전화번호
            System.out.print("새 전화번호 : ");
            String memberPhone = sc.nextLine();

            if (memberPhone.isEmpty()) {
                memberPhone = loginMember.getMemberPhone();
            }

            // 주소
            System.out.print("새 주소 : ");
            String memberAddress = sc.nextLine();

            if (memberAddress.isEmpty()) {
                memberAddress = loginMember.getMemberAddress();
            }

            // 비밀번호
            System.out.print("새 비밀번호 : ");
            String memberPw = sc.nextLine();

            if (memberPw.isEmpty()) {
                memberPw = loginMember.getMemberPw();
            }

            String query =
                    "UPDATE tbl_member " +
                            "SET member_phone = ?, " +
                            "member_address = ?, " +
                            "member_pw = ?, " +
                            "last_modified_date = NOW() " +
                            "WHERE member_code = ?";

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, memberPhone);
            pstmt.setString(2, memberAddress);
            pstmt.setString(3, memberPw);
            pstmt.setInt(4, loginMember.getMemberCode());

            int result = pstmt.executeUpdate();

            if (result > 0) {

                JDBCTemplate.commit(con);

                // DTO도 변경된 정보로 갱신
                loginMember.setMemberPhone(memberPhone);
                loginMember.setMemberAddress(memberAddress);
                loginMember.setMemberPw(memberPw);

                System.out.println();
                System.out.println("회원정보 수정이 완료되었습니다.");

            } else {

                JDBCTemplate.rollback(con);

                System.out.println();
                System.out.println("회원정보 수정에 실패했습니다.");
            }

        } catch (Exception e) {

            JDBCTemplate.rollback(con);
            e.printStackTrace();

        } finally {

            JDBCTemplate.close(pstmt);
            JDBCTemplate.close(con);
        }
    }
}