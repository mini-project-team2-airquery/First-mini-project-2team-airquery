package com.ohgiraffers.airquery.common;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/* 필기. JDBCTemplate의 역할
 *  JDBC를 쓰다 보면 커넥션을 만들고 자원을 닫는 코드가 클래스마다 똑같이 반복된다.
 *  그 반복되는 코드를 한 곳에 모아둔 도구 클래스가 JDBCTemplate이다.
 *
 *  모든 메소드가 static인 이유는 이 클래스가 상태를 갖지 않기 때문이다.
 *  객체를 만들어 쓸 이유가 없으므로 다른 클래스에서는 static import로 바로 가져다 쓴다.
 *
 *  특정 계층에 속하지 않고 어디서나 쓰이므로 common 패키지에 둔다.
 * */
public class JDBCTemplate {

    /* 설명. 접속 정보를 읽어 Connection을 만들어 반환하는 메소드 */
    public static Connection getConnection() {

        Connection con = null;

        Properties prop = new Properties();
        try {
            prop.load(new FileReader("src/main/java/com/ohgiraffers/airquery/config/connection-info.properties"));
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");

            Class.forName(driver);

            con = DriverManager.getConnection(url, prop);

            /* 설명. autoCommit 설정 변경 */
            /* 필기. JDBC의 기본값은 autoCommit이 true라서 쿼리 하나를 실행할 때마다 즉시 확정된다.
             *  그러면 주문 등록처럼 여러 쿼리를 하나로 묶어 되돌리는 일이 불가능해진다.
             *  false로 바꿔두면 Service가 commit()을 호출하는 시점에야 확정되므로
             *  트랜잭션의 시작과 끝을 Service가 직접 정할 수 있다.
             * */
            con.setAutoCommit(false);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }

    /* 필기. close 메소드가 세 개인 것은 오버로딩이다.
     *  닫아야 할 자원의 타입만 다를 뿐 하는 일이 같으므로 이름을 통일해두면
     *  호출하는 쪽에서는 타입을 신경 쓰지 않고 close(...)만 쓰면 된다.
     *  자원은 만든 순서의 역순(ResultSet → Statement → Connection)으로 닫는다.
     * */
    public static void close(Connection con) {
        try {
            if(con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement stmt) {
        try {
            if(stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(PreparedStatement pstmt) {
        try {
            if (pstmt != null && !pstmt.isClosed()) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rset) {
        try {
            if(rset != null && !rset.isClosed()) {
                rset.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 설명. 지금까지의 작업을 DB에 확정한다. */
    public static void commit(Connection con) {
        try {
            if(con != null && !con.isClosed()) {
                con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 설명. 마지막 commit 이후의 작업을 모두 취소해 이전 상태로 되돌린다. */
    public static void rollback(Connection con) {
        try {
            if(con != null && !con.isClosed()) {
                con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}