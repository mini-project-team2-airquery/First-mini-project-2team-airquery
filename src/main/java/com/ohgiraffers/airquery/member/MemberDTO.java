package com.ohgiraffers.airquery.member;

import java.sql.Date;
import java.sql.Timestamp;

public class MemberDTO {

    private int memberCode;
    private String memberName;
    private String memberId;
    private String memberPw;
    private String memberAuth;
    private String memberPhone;
    private Date memberDob;
    private String memberAddress;
    private String memberCountry;
    private String memberGender;
    private String passportNumber;
    private Timestamp firstCreatedDate;
    private Timestamp lastModifiedDate;


    public MemberDTO() {
    }


    public MemberDTO(int memberCode, String memberName, String memberId,
                     String memberPw, String memberAuth, String memberPhone,
                     Date memberDob, String memberAddress, String memberCountry,
                     String memberGender, String passportNumber,
                     Timestamp firstCreatedDate, Timestamp lastModifiedDate) {

        this.memberCode = memberCode;
        this.memberName = memberName;
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.memberAuth = memberAuth;
        this.memberPhone = memberPhone;
        this.memberDob = memberDob;
        this.memberAddress = memberAddress;
        this.memberCountry = memberCountry;
        this.memberGender = memberGender;
        this.passportNumber = passportNumber;
        this.firstCreatedDate = firstCreatedDate;
        this.lastModifiedDate = lastModifiedDate;
    }


    public int getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(int memberCode) {
        this.memberCode = memberCode;
    }


    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }


    public String getMemberPw() {
        return memberPw;
    }

    public void setMemberPw(String memberPw) {
        this.memberPw = memberPw;
    }


    public String getMemberAuth() {
        return memberAuth;
    }

    public void setMemberAuth(String memberAuth) {
        this.memberAuth = memberAuth;
    }


    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }


    public Date getMemberDob() {
        return memberDob;
    }

    public void setMemberDob(Date memberDob) {
        this.memberDob = memberDob;
    }


    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }


    public String getMemberCountry() {
        return memberCountry;
    }

    public void setMemberCountry(String memberCountry) {
        this.memberCountry = memberCountry;
    }


    public String getMemberGender() {
        return memberGender;
    }

    public void setMemberGender(String memberGender) {
        this.memberGender = memberGender;
    }


    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }


    public Timestamp getFirstCreatedDate() {
        return firstCreatedDate;
    }

    public void setFirstCreatedDate(Timestamp firstCreatedDate) {
        this.firstCreatedDate = firstCreatedDate;
    }


    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}