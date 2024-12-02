# Spring Security를 활용한 세션 기반 로그인

이 프로젝트는 Spring Security를 사용하여 세션 기반 로그인을 구현한 예제입니다. 안전한 인증 및 세션 관리를 제공하며, 사용자 인증과 권한 관리 기능을 포함합니다.

---
## **주요 기능**
- **세션 기반 인증**: Spring Security를 통해 사용자 세션을 안전하게 관리합니다.
- **사용자 로그인**: 사용자 이름과 비밀번호를 통해 인증합니다. 쿠키가 생성됩니다.
- **사용자 회원가입**: 아이디, 이메일, 비밀번호, 사용자 이름, 전화번호를 통해 회원가입을 합니다.
- **권한 기반 접근 제어**: 사용자 역할(Role)에 따라 리소스 접근을 제한합니다.
- **로그아웃 기능**: 세션을 종료하고 쿠키를 삭제하여 안전하게 로그아웃할 수 있습니다.
- **아이디 찾기 기능**: 이메일을 기반으로 인증번호를 생성해 아이디를 찾을 수 있습니다. 복구된 아이디는 가운데자리와 마지막 두자리가 *로 숨겨집니다.

---

## 사용된 기술

- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Java 17 이상

---
## 프로젝트 설정

---

### 1. 저장소 복제
```bash
   git clone <저장소 URL>
   cd <프로젝트 디렉토리>
```
---

### 2. 데이터 베이스 설정

1. MySQL 데이터 베이스 생성
 ```mysql
 CREATE DATABASE session_login;
 ```
   
2. `application-secret.yml` 파일 생성후 데이터 베이스 설정 추가
```yaml
spring:
  application:
    name: spring-security-session-login

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/login_master
    username: <DB 사용자 이름>
    password: <DB 비밀번호>

  jpa:
    properties:
      hibernate:
        show_sql: true  # SQL 쿼리를 로그에 출력
        format_sql: true  # SQL을 보기 쉽게 포맷
    hibernate:
      ddl-auto: update  # 스키마를 자동으로 업데이트
    database-platform: org.hibernate.dialect.MySQLDialect  # MySQL 방언
```
---

## API 명세서

---

### **상태 코드 정의**

---

#### **Error Code**
| HTTP 상태 코드 | 코드           | 메시지                          |
|----------------|----------------|---------------------------------|
| 403            | COMMON-001     | 접근 권한이 없습니다.           |
| 400            | COMMON-002     | 유효성 검증에 실패하였습니다.   |
| 401            | COMMON-003     | 인증정보가 존재하지 않습니다.   |
| 404            | COMMON-004     | Server Error                   |
| 404            | USER-001       | 회원정보가 없습니다.            |
| 400            | USER-002       | 이미 존재하는 아이디입니다.     |
| 400            | USER-003       | 이미 존재하는 이메일입니다.     |
| 400            | USER-004       | 이미 등록된 핸드폰 번호입니다.  |
| 404            | RECOVERY-001   | 인증번호가 올바르지 않습니다.   |
| 404            | RECOVERY-002   | 인증번호가 만료되었습니다.      |

---

#### **Success Code**
| HTTP 상태 코드 | 코드           | 메시지                          |
|----------------|----------------|---------------------------------|
| 200            | USER-001       | 로그인 성공                     |
| 201            | USER-002       | 회원가입 성공                   |
| 201            | RECOVERY-001   | 인증번호 생성                   |
| 200            | RECOVERY-002   | 인증번호 검증 성공              |

---

### 로그인 

#### **요청**
- **URL**: `/member/login`
- **Method**: `POST`
- **Headers**:
    - `Content-Type: application/json`

**Request Body**:
```json
{
  "username": "user",
  "password": "password"
}
```

#### **응답**
- 성공
```json
{
  "status": 200,
  "code": "USER-001",
  "message": "로그인 성공",
  "value": "userId"
}
```

- 실패
```json
{
  "status": 404,
  "code": "USER-001",
  "message": "회원정보가 없습니다",
  "value": null
}
```

---

### 로그아웃

#### **요청**
- **URL**: `/member/logout`
- **Method**: `POST`

---

### 회원가입

#### **요청**
- **URL**: `/member/sign-up`
- **Method**: `POST`
- **Headers**:
    - `Content-Type: application/json`

**Request Body**:
```json
{
  "userId" : "userId",
  "email" : "email",
  "password" : "password",
  "name" : "name",
  "phoneNumber" : "phonNumber"
}
```

#### **응답**
- 성공
```json
{
  "status": 201,
  "code": "USER-002",
  "message": "회원가입 성공",
  "value": "userId"
}
```

- 실패
```json
{
  "status": 400,
  "code": "USER-003",
  "message": "이미 존재하는 이메일입니다.",
  "value": "email"
}
```

---

### 아이디 찾기 - 인증번호 생성

#### **요청**
- **URL**: `/member/forgot-id`
- **Method**: `POST`
- **Headers**:
    - `Content-Type: application/json`

**Request Body**:
```json
{
  "email" : "pcjban22@gmail.com"
}
```

#### **응답**
- 성공
```json
{
  "status": 201,
  "code": "RECOVERY-001",
  "message": "인증번호 생성",
  "value": null
}
```

- 실패
```json
{
  "status": 404,
  "code": "USER-001",
  "message": "회원정보가 없습니다",
  "value": "email"
}
```

---

### 아이디 찾기 - 인증번호 검증

#### **요청**
- **URL**: `/member/forgot-id`
- **Method**: `GET`
- **Headers**:
    - `Content-Type: application/json`

**Query Parameters**:
- `email` 사용자 이메일
- `verificationCode` 인증번호

#### **응답**
- 성공
```json
{
  "status": 200,
  "code": "RECOVERY-002",
  "message": "인증번호 검증 성공",
  "value": "pass*o**"
}
```

- 실패
```json
{
  "status": 404,
  "code": "RECOVERY-001",
  "message": "인증번호가 올바르지 않습니다.",
  "value": "email"
}
```

---