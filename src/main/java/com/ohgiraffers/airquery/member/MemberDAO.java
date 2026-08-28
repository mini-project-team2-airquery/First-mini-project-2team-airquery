package com.ohgiraffers.airquery.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;

public class MemberDAO {

    /*
     * =========================
     * 로그인 / 회원 조회
     * =========================
     */

    // 회원 ID로 회원 정보 조회
    public MemberDTO findMemberById(Connection con, String memberId) {

        MemberDTO member = null;

        String query =
                "SELECT " +
                        "       member_code, " +
                        "       member_name, " +
                        "       member_id, " +
                        "       member_pw, " +
                        "       member_auth, " +
                        "       member_phone, " +
                        "       member_dob, " +
                        "       member_address, " +
                        "       member_country, " +
                        "       member_gender, " +
                        "       passport_number, " +
                        "       first_created_date, " +
                        "       last_modified_date " +
                        "FROM tbl_member " +
                        "WHERE member_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {

            pstmt = con.prepareStatement(query);
            pstmt.setString(1, memberId);

            rset = pstmt.executeQuery();

            if (rset.next()) {

                member = new MemberDTO();

                member.setMemberCode(rset.getInt("member_code"));
                member.setMemberName(rset.getString("member_name"));
                member.setMemberId(rset.getString("member_id"));
                member.setMemberPw(rset.getString("member_pw"));
                member.setMemberAuth(rset.getString("member_auth"));
                member.setMemberPhone(rset.getString("member_phone"));
                member.setMemberDob(rset.getDate("member_dob"));
                member.setMemberAddress(rset.getString("member_address"));
                member.setMemberCountry(rset.getString("member_country"));
                member.setMemberGender(rset.getString("member_gender"));
                member.setPassportNumber(rset.getString("passport_number"));
                member.setFirstCreatedDate(rset.getTimestamp("first_created_date"));
                member.setLastModifiedDate(rset.getTimestamp("last_modified_date"));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            close(rset);
            close(pstmt);
        }

        return member;
    }


    /*
     * =========================
     * 회원가입
     * =========================
     */

    // 회원가입
    public int insertMember(Connection con, MemberDTO member) {

        int result = 0;

        String query =
                "INSERT INTO tbl_member " +
                        "(member_name, member_id, member_pw, member_auth, " +
                        " member_phone, member_dob, member_address, member_country, " +
                        " member_gender, passport_number) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = null;

        try {

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, member.getMemberName());
            pstmt.setString(2, member.getMemberId());
            pstmt.setString(3, member.getMemberPw());
            pstmt.setString(4, member.getMemberAuth());
            pstmt.setString(5, member.getMemberPhone());

            if (member.getMemberDob() != null) {
                pstmt.setDate(6, member.getMemberDob());
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }

            pstmt.setString(7, member.getMemberAddress());
            pstmt.setString(8, member.getMemberCountry());
            pstmt.setString(9, member.getMemberGender());
            pstmt.setString(10, member.getPassportNumber());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            close(pstmt);
        }

        return result;
    }


    /*
     * =========================
     * 회원 ID 중복 확인
     * =========================
     */

    // 회원 ID가 이미 존재하는지 확인
    public boolean isMemberIdExists(Connection con, String memberId) {

        boolean exists = false;

        String query =
                "SELECT COUNT(*) " +
                        "FROM tbl_member " +
                        "WHERE member_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {

            pstmt = con.prepareStatement(query);
            pstmt.setString(1, memberId);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                exists = rset.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            close(rset);
            close(pstmt);
        }

        return exists;
    }


    /*
     * =========================
     * 회원번호로 회원 조회
     * =========================
     */

    // 회원번호로 회원 정보 조회
    public MemberDTO findMemberByCode(Connection con, int memberCode) {

        MemberDTO member = null;

        String query =
                "SELECT " +
                        "       member_code, " +
                        "       member_name, " +
                        "       member_id, " +
                        "       member_pw, " +
                        "       member_auth, " +
                        "       member_phone, " +
                        "       member_dob, " +
                        "       member_address, " +
                        "       member_country, " +
                        "       member_gender, " +
                        "       passport_number, " +
                        "       first_created_date, " +
                        "       last_modified_date " +
                        "FROM tbl_member " +
                        "WHERE member_code = ?";

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {

            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberCode);

            rset = pstmt.executeQuery();

            if (rset.next()) {

                member = new MemberDTO();

                member.setMemberCode(rset.getInt("member_code"));
                member.setMemberName(rset.getString("member_name"));
                member.setMemberId(rset.getString("member_id"));
                member.setMemberPw(rset.getString("member_pw"));
                member.setMemberAuth(rset.getString("member_auth"));
                member.setMemberPhone(rset.getString("member_phone"));
                member.setMemberDob(rset.getDate("member_dob"));
                member.setMemberAddress(rset.getString("member_address"));
                member.setMemberCountry(rset.getString("member_country"));
                member.setMemberGender(rset.getString("member_gender"));
                member.setPassportNumber(rset.getString("passport_number"));
                member.setFirstCreatedDate(rset.getTimestamp("first_created_date"));
                member.setLastModifiedDate(rset.getTimestamp("last_modified_date"));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            close(rset);
            close(pstmt);
        }

        return member;
    }


    /*
     * =========================
     * 회원정보 수정
     * =========================
     */

    // 회원번호를 기준으로 회원정보 수정
    public int updateMember(Connection con, MemberDTO member) {

        int result = 0;

        String query =
                "UPDATE tbl_member " +
                        "SET member_name = ?, " +
                        "    member_phone = ?, " +
                        "    member_dob = ?, " +
                        "    member_address = ?, " +
                        "    member_country = ?, " +
                        "    member_gender = ?, " +
                        "    passport_number = ?, " +
                        "    last_modified_date = CURRENT_TIMESTAMP " +
                        "WHERE member_code = ?";

        PreparedStatement pstmt = null;

        try {

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, member.getMemberName());
            pstmt.setString(2, member.getMemberPhone());

            if (member.getMemberDob() != null) {
                pstmt.setDate(3, member.getMemberDob());
            } else {
                pstmt.setNull(3, java.sql.Types.DATE);
            }

            pstmt.setString(4, member.getMemberAddress());
            pstmt.setString(5, member.getMemberCountry());
            pstmt.setString(6, member.getMemberGender());
            pstmt.setString(7, member.getPassportNumber());
            pstmt.setInt(8, member.getMemberCode());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            close(pstmt);
        }

        return result;
    }


    /*
     * =========================
     * 비밀번호 수정
     * =========================
     */

    // 회원 비밀번호 수정
    public int updatePassword(Connection con, int memberCode, String newPassword) {

        int result = 0;

        String query =
                "UPDATE tbl_member " +
                        "SET member_pw = ?, " +
                        "    last_modified_date = CURRENT_TIMESTAMP " +
                        "WHERE member_code = ?";

        PreparedStatement pstmt = null;

        try {

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, memberCode);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            close(pstmt);
        }

        return result;
    }


    /*
     * =========================
     * 고객목록 조회
     * =========================
     */

    // 전체 회원 목록 조회
    public List<MemberDTO> selectAllMembers(Connection con) {

        List<MemberDTO> memberList = new ArrayList<>();

        String query =
                "SELECT " +
                        "       member_code, " +
                        "       member_name, " +
                        "       member_id, " +
                        "       member_pw, " +
                        "       member_auth, " +
                        "       member_phone, " +
                        "       member_dob, " +
                        "       member_address, " +
                        "       member_country, " +
                        "       member_gender, " +
                        "       passport_number, " +
                        "       first_created_date, " +
                        "       last_modified_date " +
                        "FROM tbl_member " +
                        "ORDER BY member_code";

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {

            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();

            while (rset.next()) {

                MemberDTO member = new MemberDTO();

                member.setMemberCode(rset.getInt("member_code"));
                member.setMemberName(rset.getString("member_name"));
                member.setMemberId(rset.getString("member_id"));
                member.setMemberPw(rset.getString("member_pw"));
                member.setMemberAuth(rset.getString("member_auth"));
                member.setMemberPhone(rset.getString("member_phone"));
                member.setMemberDob(rset.getDate("member_dob"));
                member.setMemberAddress(rset.getString("member_address"));
                member.setMemberCountry(rset.getString("member_country"));
                member.setMemberGender(rset.getString("member_gender"));
                member.setPassportNumber(rset.getString("passport_number"));
                member.setFirstCreatedDate(rset.getTimestamp("first_created_date"));
                member.setLastModifiedDate(rset.getTimestamp("last_modified_date"));

                memberList.add(member);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            close(rset);
            close(pstmt);
        }

        return memberList;
    }
}