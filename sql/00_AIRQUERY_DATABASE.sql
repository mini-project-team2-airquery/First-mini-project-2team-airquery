-- 0) 먼저 관리자 계정(root)으로 접속
-- 1) 디폴트 데이터베이스인 mysql로 이동
SELECT DATABASE();
USE mysql;

-- 2) 연습문제를 풀기위한 데이터베이스 생성 (employeedb)
CREATE DATABASE airquerydb;
SHOW DATABASES;

-- 3) 유저 생성이 필요하다면 생성 (ID: ohgiraffers/PW: ohgiraffers)
-- CREATE USER 'ohgiraffers'@'%' IDENTIFIED BY  'ohgiraffers';
-- SELECT * FROM user;

-- 4) 유저에게 권한 부여
GRANT ALL PRIVILEGES ON airquerydb.* TO 'ohgiraffers'@'%';
SHOW GRANTS FOR 'ohgiraffers'@'%';

-- 5) 데이터베이스(employeedb)로 이동
USE airquerydb;