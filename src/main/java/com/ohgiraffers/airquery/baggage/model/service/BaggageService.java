package com.ohgiraffers.airquery.baggage.model.service;

import com.ohgiraffers.airquery.baggage.model.dao.BaggageDAO;
import com.ohgiraffers.airquery.baggage.model.dto.BaggageDTO;

import java.sql.Connection;
import java.util.List;

import static com.ohgiraffers.airquery.common.JDBCTemplate.close;
import static com.ohgiraffers.airquery.common.JDBCTemplate.commit;
import static com.ohgiraffers.airquery.common.JDBCTemplate.getConnection;
import static com.ohgiraffers.airquery.common.JDBCTemplate.rollback;

public class BaggageService {

    private final BaggageDAO baggageDAO = new BaggageDAO();

    /*
     * 수하물 전체 조회 서비스 메서드
     * DB 연결을 만들고 DAO에게 전체 조회를 요청한다.
     */
    public List<BaggageDTO> selectAllBaggages() {

        Connection con = getConnection();

        List<BaggageDTO> baggageList = baggageDAO.selectAllBaggages(con);

        close(con);

        return baggageList;
    }

    /*
     * 예매번호로 수하물 조회 서비스 메서드
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

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }

    /*
     * 수하물 무게 변경 서비스 메서드
     */
    public boolean updateBaggageWeight(int baggageCode, double baggageWeight) {

        Connection con = getConnection();

        int result = baggageDAO.updateBaggageWeight(con, baggageCode, baggageWeight);

        if (result > 0) {
            commit(con);
        } else {
            rollback(con);
        }

        close(con);

        return result > 0;
    }
}
