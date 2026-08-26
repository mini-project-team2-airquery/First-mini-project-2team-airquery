-- 외래 키 의존성의 역순으로 삭제
DROP TABLE IF EXISTS tbl_payment;
DROP TABLE IF EXISTS tbl_baggage;
DROP TABLE IF EXISTS tbl_reservation;
DROP TABLE IF EXISTS tbl_seat;
DROP TABLE IF EXISTS tbl_flight;
DROP TABLE IF EXISTS tbl_airline;
DROP TABLE IF EXISTS tbl_member;

CREATE TABLE tbl_member
(
    member_code       INT AUTO_INCREMENT COMMENT '회원번호',
    member_name       VARCHAR(50)  NOT NULL COMMENT '이름',
    member_id         VARCHAR(50)  NOT NULL COMMENT '아이디',
    member_pw         VARCHAR(255) NOT NULL COMMENT '비밀번호',
    member_auth       VARCHAR(20)  NOT NULL COMMENT '권한',
    member_phone      VARCHAR(20)  NOT NULL COMMENT '전화번호',
    member_dob        DATE         NOT NULL COMMENT '생년월일',
    member_address    VARCHAR(255) NOT NULL COMMENT '주소',
    member_country    VARCHAR(50)  NOT NULL COMMENT '국적',
    member_gender     VARCHAR(10)  NOT NULL COMMENT '성별',
    passport_number   VARCHAR(50)  NOT NULL COMMENT '여권번호',
    first_created_date DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입일자',
    last_modified_date DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최종수정일',
    CONSTRAINT pk_member_code PRIMARY KEY (member_code)
) ENGINE = InnoDB COMMENT = '회원';

CREATE TABLE tbl_airline
(
    airline_code            INT AUTO_INCREMENT COMMENT '항공사번호',
    airline_name            VARCHAR(100) NOT NULL COMMENT '항공사명',
    customer_service_number VARCHAR(30)  NOT NULL COMMENT '고객센터번호',
    first_created_date      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    last_modified_date      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최종수정일',
    CONSTRAINT pk_airline_code PRIMARY KEY (airline_code)
) ENGINE = InnoDB COMMENT = '항공사';

CREATE TABLE tbl_flight
(
    flight_code           INT AUTO_INCREMENT COMMENT '항공편번호',
    airline_code          INT          NOT NULL COMMENT '항공사번호',
    flight_departure      VARCHAR(100) NOT NULL COMMENT '출발지',
    flight_arrival        VARCHAR(100) NOT NULL COMMENT '도착지',
    flight_departure_time DATETIME     NOT NULL COMMENT '출발시간',
    flight_arrival_time   DATETIME     NOT NULL COMMENT '도착시간',
    airplane_type         VARCHAR(100) NOT NULL COMMENT '비행기기종',
    flight_gate_number    VARCHAR(10)  NOT NULL COMMENT '게이트번호',
    flight_ticket_price   INT          NOT NULL COMMENT '티켓가격',
    first_created_date    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    last_modified_date    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최종수정일',
    CONSTRAINT pk_flight_code PRIMARY KEY (flight_code),
    CONSTRAINT fk_flight_airline_code
        FOREIGN KEY (airline_code) REFERENCES tbl_airline (airline_code)
) ENGINE = InnoDB COMMENT = '항공편';

CREATE TABLE tbl_seat
(
    seat_code         INT AUTO_INCREMENT COMMENT '좌석번호',
    flight_code       INT         NOT NULL COMMENT '항공편번호',
    seat_id           VARCHAR(30) NOT NULL COMMENT '좌석식별번호',
    flight_class      VARCHAR(30) NOT NULL COMMENT '좌석등급',
    additional_amount INT         NOT NULL DEFAULT 0 COMMENT '추가금액',
    is_reserved       BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '선점여부',
    CONSTRAINT pk_seat_code PRIMARY KEY (seat_code),
    CONSTRAINT uk_seat_flight_seat_id UNIQUE (flight_code, seat_id),
    CONSTRAINT fk_seat_flight_code
        FOREIGN KEY (flight_code) REFERENCES tbl_flight (flight_code)
) ENGINE = InnoDB COMMENT = '좌석';

CREATE TABLE tbl_reservation
(
    reservation_code  INT AUTO_INCREMENT COMMENT '예매번호',
    member_code       INT     NOT NULL COMMENT '회원번호',
    flight_code       INT     NOT NULL COMMENT '항공편번호',
    seat_code         INT              COMMENT '좌석번호',
    baggage_carrying  BOOLEAN NOT NULL COMMENT '수하물지여부',
    first_created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '예매일자',
    last_modified_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최종수정일',
    CONSTRAINT pk_reservation_code PRIMARY KEY (reservation_code),
    CONSTRAINT fk_reservation_member_code
        FOREIGN KEY (member_code) REFERENCES tbl_member (member_code),
    CONSTRAINT fk_reservation_flight_code
        FOREIGN KEY (flight_code) REFERENCES tbl_flight (flight_code),
    CONSTRAINT fk_reservation_seat_code
        FOREIGN KEY (seat_code) REFERENCES tbl_seat (seat_code)
) ENGINE = InnoDB COMMENT = '예매';

CREATE TABLE tbl_baggage
(
    baggage_code     INT AUTO_INCREMENT COMMENT '수하물번호',
    reservation_code INT          NOT NULL COMMENT '예매번호',
    baggage_weight   DECIMAL(5,2) NOT NULL COMMENT '수하물무게',
    CONSTRAINT pk_baggage_code PRIMARY KEY (baggage_code),
    CONSTRAINT fk_baggage_reservation_code
        FOREIGN KEY (reservation_code) REFERENCES tbl_reservation (reservation_code)
) ENGINE = InnoDB COMMENT = '수하물';

CREATE TABLE tbl_payment
(
    payment_code      INT AUTO_INCREMENT COMMENT '결제번호',
    reservation_code  INT         NOT NULL COMMENT '예매번호',
    payment_amount     INT         NOT NULL COMMENT '결제금액',
    payment_method     VARCHAR(30) NOT NULL COMMENT '결제수단',
    refund_status      BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '환불여부',
    first_created_date DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '결제일자',
    last_modified_date DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최종수정일',
    CONSTRAINT pk_payment_code PRIMARY KEY (payment_code),
    CONSTRAINT uk_payment_reservation_code UNIQUE (reservation_code),
    CONSTRAINT fk_payment_reservation_code
        FOREIGN KEY (reservation_code) REFERENCES tbl_reservation (reservation_code)
) ENGINE = InnoDB COMMENT = '결제';

-- 더미 데이터 INSERT

-- =====================================================
-- 1. 회원 Dummy Data
-- =====================================================

INSERT INTO tbl_member
(
    member_code, member_name, member_id, member_pw, member_auth,
    member_phone, member_dob, member_address, member_country,
    member_gender, passport_number,
    first_created_date, last_modified_date
)
VALUES
    (1, '김민수', 'minsu01', 'test123', 'Admin', '01012341234',
     '1998-05-12', '인천광역시', 'South Korea', 'M', 'M12345678',
     '2026-08-25', '2026-08-25'),

    (2, '이서연', 'seoyeon02', 'test123', 'Member', '01023452345',
     '2001-03-21', '서울특별시', 'South Korea', 'F', 'M23456789',
     '2026-08-25', '2026-08-25'),

    (3, '박지훈', 'jihoon03', 'test123', 'Member', '01034563456',
     '1997-11-08', '경기도 수원시', 'South Korea', 'M', 'M34567890',
     '2026-08-25', '2026-08-25'),

    (4, '최유진', 'yujin04', 'test123', 'Member', '01045674567',
     '2002-08-17', '대전광역시', 'South Korea', 'F', 'M45678901',
     '2026-08-25', '2026-08-25'),

    (5, '정현우', 'hyunwoo05', 'test123', 'Member', '01056785678',
     '1996-02-04', '부산광역시', 'South Korea', 'M', 'M56789012',
     '2026-08-25', '2026-08-25'),

    (6, '한지민', 'jimin06', 'test123', 'Member', '01067896789',
     '2000-11-25', '광주광역시', 'South Korea', 'F', 'M67890123',
     '2026-08-25', '2026-08-25'),

    (7, '오준호', 'junho07', 'test123', 'Member', '01078907890',
     '1999-05-30', '대구광역시', 'South Korea', 'M', 'M78901234',
     '2026-08-25', '2026-08-25'),

    (8, '강수빈', 'subin08', 'test123', 'Member', '01089018901',
     '2003-04-14', '울산광역시', 'South Korea', 'F', 'M89012345',
     '2026-08-25', '2026-08-25'),

    (9, '윤도현', 'dohyun09', 'test123', 'Member', '01090129012',
     '1998-12-09', '제주특별자치도', 'South Korea', 'M', 'M90123456',
     '2026-08-25', '2026-08-25'),

    (10, '임채원', 'chaewon10', 'test123', 'Member', '01011230123',
     '2001-07-07', '충청남도 천안시', 'USA', 'F', 'M01234567',
     '2026-08-25', '2026-08-25');


-- =====================================================
-- 2. 항공사 Dummy Data
-- =====================================================

INSERT INTO tbl_airline
(
    airline_code, airline_name, customer_service_number,
    first_created_date, last_modified_date
)
VALUES
    (1, '대한항공', '15882001', '2026-08-25', '2026-08-25'),
    (2, '아시아나항공', '15888000', '2026-08-25', '2026-08-25'),
    (3, '제주항공', '15991500', '2026-08-25', '2026-08-25'),
    (4, '진에어', '16006200', '2026-08-25', '2026-08-25'),
    (5, '티웨이항공', '16888686', '2026-08-25', '2026-08-25'),
    (6, '에어부산', '16663060', '2026-08-25', '2026-08-25'),
    (7, '에어서울', '18008100', '2026-08-25', '2026-08-25'),
    (8, '이스타항공', '15440080', '2026-08-25', '2026-08-25'),
    (9, '에어프레미아', '18002626', '2026-08-25', '2026-08-25'),
    (10, '에어로케이', '18902296', '2026-08-25', '2026-08-25');


-- =====================================================
-- 3. 항공편 Dummy Data
-- =====================================================

INSERT INTO tbl_flight
(
    flight_code, airline_code,
    flight_departure, flight_arrival,
    flight_departure_time, flight_arrival_time,
    airplane_type, flight_gate_number, flight_ticket_price,
    first_created_date, last_modified_date
)
VALUES
    (1, 1, '인천', '도쿄',
     '2026-09-01 08:00:00', '2026-09-01 10:30:00',
     'B737-8', 'G12', 320000, '2026-08-25', '2026-08-25'),

    (2, 2, '인천', '오사카',
     '2026-09-01 09:30:00', '2026-09-01 11:20:00',
     'A321', 'G15', 280000, '2026-08-25', '2026-08-25'),

    (3, 3, '인천', '후쿠오카',
     '2026-09-02 07:30:00', '2026-09-02 09:00:00',
     'B737-800', 'G21', 180000, '2026-08-25', '2026-08-25'),

    (4, 4, '인천', '방콕',
     '2026-09-02 10:00:00', '2026-09-02 14:00:00',
     'B737-800', 'G24', 410000, '2026-08-25', '2026-08-25'),

    (5, 5, '인천', '타이베이',
     '2026-09-03 11:20:00', '2026-09-03 13:10:00',
     'A330-300', 'G31', 260000, '2026-08-25', '2026-08-25'),

    (6, 6, '부산', '도쿄',
     '2026-09-03 13:00:00', '2026-09-03 15:10:00',
     'A321neo', 'G07', 230000, '2026-08-25', '2026-08-25'),

    (7, 7, '인천', '오사카',
     '2026-09-04 14:00:00', '2026-09-04 15:50:00',
     'A321', 'G18', 210000, '2026-08-25', '2026-08-25'),

    (8, 8, '인천', '후쿠오카',
     '2026-09-04 16:30:00', '2026-09-04 18:00:00',
     'B737-8', 'G25', 170000, '2026-08-25', '2026-08-25'),

    (9, 9, '인천', '로스앤젤레스',
     '2026-09-05 18:00:00', '2026-09-05 12:30:00',
     'B787-9', 'G42', 980000, '2026-08-25', '2026-08-25'),

    (10, 10, '청주', '도쿄',
     '2026-09-05 09:00:00', '2026-09-05 11:20:00',
     'A320', 'G04', 240000, '2026-08-25', '2026-08-25');


-- =====================================================
-- 4. 좌석 Dummy Data
-- =====================================================

INSERT INTO tbl_seat
(
    seat_code, flight_code, seat_id,
    flight_class, additional_amount, is_reserved
)
VALUES
    (1, 1, '1A',  'FIRST',    300000, TRUE),
    (2, 2, '2A',  'BUSINESS', 150000, TRUE),
    (3, 3, '10A', 'ECONOMY',       0, TRUE),
    (4, 4, '10B', 'ECONOMY',       0, TRUE),
    (5, 5, '5A',  'BUSINESS', 150000, TRUE),
    (6, 6, '12C', 'ECONOMY',       0, TRUE),
    (7, 7, '12D', 'ECONOMY',       0, TRUE),
    (8, 8, '15A', 'ECONOMY',       0, TRUE),
    (9, 9, '3A',  'BUSINESS', 250000, TRUE),
    (10, 10, '18F', 'ECONOMY',     0, TRUE);


-- =====================================================
-- 5. 예매 Dummy Data
-- 기존 '예매별 좌석'의 seat_code를 여기에 통합
-- =====================================================

INSERT INTO tbl_reservation
(
    reservation_code, member_code, flight_code,
    seat_code, baggage_carrying,
    first_created_date, last_modified_date
)
VALUES
    (1,  1,  1,  1,  TRUE,  '2026-08-26', '2026-08-26'),
    (2,  2,  2,  2,  FALSE, '2026-08-26', '2026-08-26'),
    (3,  3,  3,  3,  TRUE,  '2026-08-26', '2026-08-26'),
    (4,  4,  4,  4,  FALSE, '2026-08-27', '2026-08-27'),
    (5,  5,  5,  5,  TRUE,  '2026-08-27', '2026-08-27'),
    (6,  6,  6,  6,  TRUE,  '2026-08-28', '2026-08-28'),
    (7,  7,  7,  7,  FALSE, '2026-08-28', '2026-08-28'),
    (8,  8,  8,  8,  TRUE,  '2026-08-29', '2026-08-29'),
    (9,  9,  9,  9,  TRUE,  '2026-08-29', '2026-08-29'),
    (10, 10, 10, 10, FALSE, '2026-08-30', '2026-08-30');


-- =====================================================
-- 6. 수하물 Dummy Data
-- =====================================================

INSERT INTO tbl_baggage
(
    baggage_code, reservation_code, baggage_weight
)
VALUES
    (1,  1, 15.00),
    (2,  1,  7.00),
    (3,  3, 20.00),
    (4,  5, 18.00),
    (5,  5,  5.00),
    (6,  6, 23.00),
    (7,  8, 12.00),
    (8,  8,  8.00),
    (9,  9, 20.00),
    (10, 9,  6.00);


-- =====================================================
-- 7. 결제 Dummy Data
-- =====================================================

INSERT INTO tbl_payment
(
    payment_code, reservation_code,
    payment_amount, payment_method, refund_status,
    first_created_date, last_modified_date
)
VALUES
    (1,  1, 320000, 'CARD',      FALSE, '2026-08-26', '2026-08-26'),
    (2,  2, 280000, 'CARD',      FALSE, '2026-08-26', '2026-08-26'),
    (3,  3, 180000, 'KAKAO_PAY', FALSE, '2026-08-26', '2026-08-26'),
    (4,  4, 410000, 'CARD',      FALSE, '2026-08-27', '2026-08-27'),
    (5,  5, 260000, 'NAVER_PAY', FALSE, '2026-08-27', '2026-08-27'),
    (6,  6, 230000, 'CARD',      FALSE, '2026-08-28', '2026-08-28'),
    (7,  7, 210000, 'KAKAO_PAY', TRUE,  '2026-08-28', '2026-08-29'),
    (8,  8, 170000, 'CARD',      FALSE, '2026-08-29', '2026-08-29'),
    (9,  9, 980000, 'CARD',      FALSE, '2026-08-29', '2026-08-29'),
    (10, 10, 240000, 'NAVER_PAY', FALSE, '2026-08-30', '2026-08-30');

COMMIT;