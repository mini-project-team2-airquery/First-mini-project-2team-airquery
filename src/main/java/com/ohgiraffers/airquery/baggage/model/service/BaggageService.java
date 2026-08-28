package com.ohgiraffers.airquery.baggage.model.service;

import com.ohgiraffers.airquery.baggage.model.dao.BaggageDAO;
import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import static com.ohgiraffers.airquery.common.JDBCTemplate.commit;
import static com.ohgiraffers.airquery.common.JDBCTemplate.getConnection;
import static com.ohgiraffers.airquery.common.JDBCTemplate.rollback;

/*
 * Service
 * DB 연결을 만들고, 성공하면 commit, 실패하면 rollback을 처리하는 역할을 한다.
 * 실제 SQL 실행은 DAO에게 맡긴다.
 */
public class BaggageService {

    private final BaggageDAO baggageDAO = new BaggageDAO();

    /*
     * 수하물 전체 조회 서비스 메서드
     * 조회 메뉴에 들어갔을 때 전체 수하물을 먼저 보여준다.
     */
    public List<BaggageDTO> selectAllBaggages() {

        Connection con = getConnection();

        List<BaggageDTO> baggageList = baggageDAO.selectAllBaggages(con);

        close(con);

        return baggageList;
    }

    /*
     * 예매 존재 여부 확인 서비스 메서드
     * 수하물 조회 전에 예매내역이 있는지 먼저 확인할 때 사용한다.
     */
    public boolean existsReservation(int reservationCode) {


        Connection con = getConnection();

        boolean isExist = baggageDAO.existsReservation(con, reservationCode);

        close(con);

        return isExist;
    }

    /*
     * 예매 시 수하물을 신청했는지 확인하는 서비스 메서드
     * baggage_carrying 값이 true인지 확인한다.
     */
    public boolean isBaggageCarrying(int reservationCode) {

        Connection con = getConnection();

        boolean isBaggageCarrying = baggageDAO.isBaggageCarrying(con, reservationCode);

        close(con);

        return isBaggageCarrying;
    }

    /*
     * 예매번호로 수하물 조회 서비스 메서드
     * 조회는 데이터를 바꾸지 않기 때문에 commit, rollback이 필요 없다.
     */
    public List<BaggageDTO> selectBaggagesByReservationCode(int reservationCode) {

        Connection con = getConnection();

        List<BaggageDTO> baggageList = baggageDAO.selectBaggagesByReservationCode(con, reservationCode);

        close(con);

        return baggageList;
    }

    /*
     * 수하물 등록 서비스 메서드
     * 등록 성공 시 commit, 실패 시 rollback 한다.
     */
    public boolean registBaggage(BaggageDTO baggage) {

        Connection con = getConnection();

        int result = baggageDAO.insertBaggage(con, baggage);

        // result가 1 이상이면 DB에 반영된 행이 있다는 뜻이므로 성공이다.
        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        // 성공이면 true, 실패면 false를 돌려준다.
        return result > 0;
    }

    /*
     * 수하물 무게 변경 서비스 메서드
     * 수하물번호로 기존 수하물을 찾고, 무게만 수정한다.
     */
    public boolean updateBaggageWeight(int baggageCode, double baggageWeight) {

        Connection con = getConnection();

        int result = baggageDAO.updateBaggageWeight(con, baggageCode, baggageWeight);

        // result가 1 이상이면 변경된 행이 있다는 뜻이므로 성공이다.
        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        // 성공이면 true, 실패면 false를 돌려준다.
        return result > 0;
    }
}
