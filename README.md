# ✈️ Airquery

콘솔(CLI) 기반 항공 예약 관리 시스템입니다. 회원가입/로그인 후 항공사·항공편·좌석 조회, 예약, 결제, 수하물 등록까지 항공권 예매의 전체 흐름을 텍스트 메뉴로 처리합니다.

## 📌 프로젝트 소개

Airquery는 Spring 같은 프레임워크 없이 **순수 Java + JDBC**로 만든 콘솔 애플리케이션입니다. 회원, 항공사, 항공편, 좌석, 예약, 결제, 수하물 총 7개 도메인으로 나뉘어 있고, 도메인마다 `Menu(View) → Controller → Service → DAO` 구조를 동일하게 반복해서 계층을 분리했습니다.

- DB 접속/자원 해제는 `common/JDBCTemplate`이 전담하고, 각 Service가 `Connection`을 직접 받아 트랜잭션(`commit`/`rollback`)을 관리합니다.
- `flight` 도메인은 SQL을 `flight-query.xml`(properties 형식의 XML)에 key-value로 분리해두고 DAO에서 키로 읽어 실행합니다. 다른 도메인은 DAO 안에 SQL을 직접 작성했습니다.
- 로그인한 회원의 `memberAuth`(`Admin` / `Member`) 값에 따라 각 도메인 메뉴에서 관리자 전용 기능(등록/수정/삭제)과 일반회원 전용 기능(조회/본인 예약 관리)이 갈립니다.

## 🛠️ 기술 스택

| 구분 | 내용 |
| --- | --- |
| Language | Java |
| Build Tool | Gradle (`build.gradle`, Gradle Wrapper) |
| Database | MySQL 8.x |
| DB 연동 | JDBC 직접 제어 (`DriverManager`, PreparedStatement), `mysql-connector-j:9.3.0` |
| 쿼리 관리 | DAO 내 인라인 SQL + 일부 도메인은 XML 쿼리 매퍼(`flight-query.xml`) |
| Test | JUnit 5 (`junit-jupiter`) |
| UI | 콘솔(System.in/System.out) 기반 텍스트 메뉴 |

## 📁 프로젝트 구조

도메인별 패키지로 분리했고, 도메인 내부는 `controller / dao(또는 model/dao) / dto(또는 model/dto) / service(또는 model/service) / view / menu` 계층으로 구성되어 있습니다.

```
src/main/java/com/ohgiraffers/airquery/
├── Application.java              # 진입점, 로그인 이후 메인 메뉴 라우팅
│
├── common/
│   └── JDBCTemplate.java         # Connection 생성, 자원 close, commit/rollback 공통 처리
│
├── config/
│   └── connection-info.properties  # DB 접속 정보(driver/url/user/password)
│
├── member/                       # 회원가입 · 로그인 · 회원 정보 관리 (단일 패키지, 계층 미분리)
│   ├── JoinMember.java / Login.java
│   ├── MemberDAO.java / MemberDTO.java
│   └── MemberMenu.java / MemberList.java / UpdateMember.java
│
├── airline/                      # 항공사 등록/조회/수정/삭제
│   ├── controller / dao / dto / service / menu
│
├── flight/                       # 항공편 등록/조회/수정/삭제
│   ├── controller / model/dao / model/dto / model/service / view
│   └── mapper/flight-query.xml   # SQL을 key-value(XML properties)로 분리 관리
│
├── seat/                         # 좌석 조회/예약/변경
│   └── controller / model/dao / model/dto / model/service / view
│
├── reservation/                  # 예매 등록/조회/변경/취소
│   └── controller / model/dao / model/dto / model/service / view
│
├── payment/                      # 결제 등록/조회
│   └── controller / model/dao / model/dto / model/service / view
│
└── baggage/                      # 수하물 등록/조회/무게 변경
    └── controller / model/dao / model/dto / model/service / view
```

## 🗄️ 데이터베이스

### 초기화 스크립트

`sql/` 디렉터리의 스크립트를 순서대로 실행하면 DB, 계정, 테이블, 더미 데이터까지 한 번에 구성됩니다.

| 파일 | 설명 |
| --- | --- |
| `00_AIRQUERY_DATABASE.sql` | `airquerydb` 데이터베이스 생성, `ohgiraffers` 계정 생성 및 권한 부여 |
| `01_DB_SCRIPT.sql` | 7개 테이블 DROP/CREATE + 회원 10건 · 항공사 10건 · 항공편 10건 · 좌석 10건 · 예매 10건 · 수하물 10건 · 결제 10건 더미 데이터 INSERT |
| `02_MODIFY_CONSTRAINT.sql` | 제약조건 수정 |

### ERD 개요

```
tbl_member ──< tbl_reservation >── tbl_flight ──< tbl_seat
                     │                                │
                     │(1:1)                           │(1:N, FK)
                     ▼                                ▼
                tbl_payment                    (tbl_reservation.seat_code)
                     
                tbl_reservation ──< tbl_baggage
tbl_flight >── tbl_airline (N:1)
```

### 테이블 요약

| 테이블 | 설명 | 주요 컬럼 |
| --- | --- | --- |
| `tbl_member` | 회원 | `member_code`(PK), `member_id/pw`, `member_auth`(`Admin`\|`Member`, CHECK 제약), `member_phone`, `member_dob`, `passport_number` 등 |
| `tbl_airline` | 항공사 | `airline_code`(PK), `airline_name`, `customer_service_number` |
| `tbl_flight` | 항공편 | `flight_code`(PK), `airline_code`(FK), 출발/도착지, 출발/도착 시각, 기종, 게이트, `flight_ticket_price` |
| `tbl_seat` | 좌석 | `seat_code`(PK), `flight_code`(FK), `seat_id`, `flight_class`(ECONOMY/BUSINESS/FIRST), `additional_amount`(0 / 500,000 / 2,000,000 중 하나, CHECK 제약), `is_reserved`. `(flight_code, seat_id)` UNIQUE |
| `tbl_reservation` | 예매 | `reservation_code`(PK), `member_code`(FK), `flight_code`(FK), `seat_code`(FK, nullable — 좌석 선택 전 예매 가능), `baggage_carrying`, `is_deleted`(취소 여부, soft delete) |
| `tbl_baggage` | 수하물 | `baggage_code`(PK), `reservation_code`(FK), `baggage_weight`(kg, `DECIMAL(5,2)`) |
| `tbl_payment` | 결제 | `payment_code`(PK), `reservation_code`(FK, UNIQUE — 예매당 결제 1건), `payment_amount`, `payment_method`, `refund_status` |

### 주요 제약/비즈니스 규칙

- `member_auth`는 `Admin` 또는 `Member`만 허용 (`chk_member_auth`).
- 좌석 등급별 추가 요금은 `0`(이코노미) / `500,000`(비즈니스) / `2,000,000`(퍼스트)만 허용 (`chk_additional_amount`).
- 한 항공편 안에서 좌석 식별번호는 중복될 수 없음 (`uk_seat_flight_seat_id`).
- 예매는 좌석을 정하지 않고도 먼저 생성할 수 있고(`seat_code` nullable), 이후 좌석 메뉴에서 별도로 좌석을 예약/변경.
- 예매 취소는 물리 삭제가 아니라 `is_deleted` 플래그로 처리(soft delete).
- 결제는 예매 1건당 최대 1건만 가능 (`uk_payment_reservation_code`).

## 🚀 시작하기

### 1. 사전 준비

- JDK 17 이상 (Gradle 8.x 기준)
- MySQL 8.x 서버 실행

### 2. 데이터베이스 설정

```bash
mysql -u root -p < sql/00_AIRQUERY_DATABASE.sql
mysql -u root -p < sql/01_DB_SCRIPT.sql
mysql -u root -p < sql/02_MODIFY_CONSTRAINT.sql
```

### 3. 접속 정보 설정

`src/main/java/com/ohgiraffers/airquery/config/connection-info.properties`에서 본인 환경에 맞게 DB 접속 정보를 입력합니다. (`JDBCTemplate`이 이 파일을 상대경로로 직접 읽으므로, **프로젝트 루트에서 실행해야** 정상적으로 파일을 찾습니다.)

```properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost/airquerydb
user=사용할_계정
password=사용할_비밀번호
```

### 4. 빌드 및 실행

`build.gradle`에 `application` 플러그인이 설정되어 있지 않아 `gradlew run`은 사용할 수 없습니다. 아래 중 한 가지 방법으로 실행하세요.

**IDE에서 실행 (권장)**
IntelliJ 등에서 프로젝트를 열고 `Application.java`의 `main()`을 직접 실행합니다.

**커맨드라인에서 실행**

```bash
./gradlew build
java -cp "build/classes/java/main:$(find ~/.gradle -name 'mysql-connector-j-9.3.0.jar')" com.ohgiraffers.airquery.Application
```

### 5. 로그인

`01_DB_SCRIPT.sql`에 더미 회원 데이터가 포함되어 있어 아래 계정으로 바로 로그인해볼 수 있습니다.

| 아이디 | 비밀번호 | 권한 |
| --- | --- | --- |
| `minsu01` | `test123` | Admin |
| `seoyeon02` | `test123` | Member |

## 🧭 메뉴 구조 및 주요 기능

로그인 후 메인 메뉴는 아래와 같이 7개 하위 메뉴로 구성됩니다. 각 메뉴는 로그인 회원의 권한(`Admin`/`Member`)에 따라 노출되는 항목이 다릅니다.

```
메인 메뉴
├── 1. 회원 정보 관리
│   ├── (Admin) 회원목록 조회
│   └── (Member) 내 정보 조회 / 회원정보 수정
├── 2. 항공사
│   ├── (Admin) 항공사 등록 / 조회 / 정보 변경 / 삭제
│   └── (Member) 항공사 조회
├── 3. 항공편
│   ├── (Admin) 전체/항공사별 조회, 항공편 생성, 항공편 수정(정보/일정/가격), 삭제
│   └── (Member) 전체/항공사별 조회
├── 4. 예약   (Member 전용)
│   └── 예매 목록 조회 / 예매 상세 조회 / 예매 등록 / 예매 취소 / 예매 변경
├── 5. 결제   (Member 전용)
│   └── 결제 등록
├── 6. 수하물
│   └── 수하물 등록 / 조회(전체 + 내 수하물) / 무게 변경
└── 7. 좌석
    ├── 좌석 조회 (전체 / 예약 가능 좌석만 / 항공편번호로 조회)
    ├── 좌석 예약 (좌석 미선택 예매에 좌석 배정, Admin은 회원번호 지정 가능)
    └── 좌석 변경 (같은 등급 내에서만 변경 가능)
```

### 도메인별 상세

**회원 (member)**
- 회원가입 시 이름/아이디/비밀번호/전화번호/생년월일/주소/국적/성별/여권번호를 입력받습니다.
- 로그인 성공 시 `MemberDTO`(권한 포함)를 세션처럼 들고 다니며 이후 모든 메뉴에 전달합니다.
- Admin은 전체 회원 목록 조회, Member는 본인 정보 조회/수정만 가능합니다.

**항공사 (airline)**
- Admin은 항공사명/고객센터번호로 등록·수정·삭제할 수 있고, Member는 조회만 가능합니다.

**항공편 (flight)**
- Admin은 항공편 등록, 정보/일정/가격 수정, 삭제가 가능하며, 조회는 전체 목록 또는 항공사별로 필터링할 수 있습니다.
- SQL은 `flight-query.xml`에 키(`selectAllFlight`, `selectByAirline` 등)로 분리되어 있어 DAO가 이를 읽어 실행합니다.

**좌석 (seat)**
- 좌석 등급은 ECONOMY/BUSINESS/FIRST 세 가지이며, 등급별 추가 요금이 DB CHECK 제약으로 고정되어 있습니다.
- 예매만 하고 좌석을 아직 고르지 않은 건을 찾아 좌석을 배정하는 방식으로 동작하며, 좌석 변경은 같은 등급 내에서만 가능합니다.
- 관리자는 회원번호를 직접 입력해 다른 회원의 좌석을 대신 예약할 수 있습니다.

**예약 (reservation)**
- 항공편·좌석 등급·수하물 지참 여부를 선택해 예매를 등록하고, 예매 목록/상세 조회, 취소(soft delete), 변경이 가능합니다.
- 상세 조회 시 항공편·좌석·결제·수하물 정보를 조인한 `ReservationDetailDTO`로 한 번에 확인할 수 있습니다.

**결제 (payment)**
- 예매 건에 대해 결제수단과 결제금액(티켓가격 + 좌석 추가금액)을 등록합니다. 예매 1건당 결제는 1건만 가능합니다.

**수하물 (baggage)**
- 예매 시 수하물 지참(`baggage_carrying`)을 `Y`로 선택한 건에 한해 수하물을 등록/조회/무게 변경할 수 있습니다.
- 조회 시 전체 수하물 목록과 로그인 회원이 등록한 수하물 목록을 함께 보여줍니다.

## 👤 회원 권한

로그인한 회원의 `memberAuth` 값에 따라 메인 메뉴 진입 이후 각 하위 메뉴의 동작이 달라집니다.

- `Admin`: 회원 목록 조회, 항공사/항공편 등록·수정·삭제, 좌석 예약 시 회원번호 지정 등 관리 기능 전반에 접근 가능
- `Member`: 본인 정보 조회/수정, 항공사/항공편 조회, 본인 명의의 예약·결제·수하물·좌석 관리만 가능
