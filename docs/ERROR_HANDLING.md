# 에러 처리 및 로깅 시스템 가이드

## 📁 파일 구조

```
global/
├── config/
│   └── LoggingAspect.java      # API 자동 로깅 (AOP)
└── exception/
    ├── ErrorCode.java          # 에러 코드 정의
    ├── BusinessException.java  # 비즈니스 예외 클래스
    ├── ErrorResponse.java      # API 에러 응답 DTO
    └── GlobalExceptionHandler.java  # 전역 예외 처리
```

---

## 🔄 동작 순서

```
1. 클라이언트 → API 요청
        ↓
2. LoggingAspect가 요청 가로챔 → [API] 호출 로그 출력
        ↓
3. Controller → Service 실행
        ↓
   ┌─────────────────────────────────────┐
   │ 정상 처리 시                          │
   │   → LoggingAspect가 완료 로그 출력    │
   │   → 성공 응답 반환                    │
   └─────────────────────────────────────┘
   ┌─────────────────────────────────────┐
   │ 예외 발생 시                          │
   │   → GlobalExceptionHandler가 잡음    │
   │   → 에러 로그 출력                    │
   │   → ErrorResponse 반환               │
   └─────────────────────────────────────┘
```

---

## 📄 파일별 상세 설명

### 1. ErrorCode.java (에러 코드 정의)

**역할**: 모든 에러 코드를 한 곳에서 관리

```java
public enum ErrorCode {
    USER_NOT_FOUND("USER001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    //              ↑ 코드        ↑ 메시지                    ↑ HTTP 상태
}
```

| 필드 | 설명 | 예시 |
|------|------|------|
| `code` | 고유 에러 식별자 | `AUTH001`, `USER002` |
| `message` | 사용자에게 보여줄 메시지 | `유효하지 않은 토큰입니다` |
| `status` | HTTP 상태 코드 | `401`, `404`, `500` |

**카테고리별 에러 코드**:

| 카테고리 | 접두사 | 예시 |
|----------|--------|------|
| 인증 | `AUTH` | `AUTH001`, `AUTH002` |
| 유저 | `USER` | `USER001`, `USER002` |
| OAuth | `OAUTH` | `OAUTH001`, `OAUTH002` |
| 공통 | `COMMON` | `COMMON001`, `COMMON002` |

---

### 2. BusinessException.java (비즈니스 예외)

**역할**: 비즈니스 로직에서 발생하는 예외를 표현

```java
// 사용 예시
throw new BusinessException(ErrorCode.USER_NOT_FOUND);

// 커스텀 메시지와 함께
throw new BusinessException(ErrorCode.INVALID_INPUT, "이메일 형식이 올바르지 않습니다");
```

**언제 사용하나요?**
- 사용자를 찾을 수 없을 때
- 권한이 없을 때
- 입력값이 잘못되었을 때
- 외부 API 호출이 실패했을 때

---

### 3. ErrorResponse.java (에러 응답 DTO)

**역할**: 클라이언트에게 전달할 에러 응답 형식 정의

```json
{
    "code": "USER001",
    "message": "사용자를 찾을 수 없습니다",
    "timestamp": "2024-12-17T20:00:00"
}
```

| 필드 | 설명 |
|------|------|
| `code` | 프론트엔드에서 에러 구분에 사용 |
| `message` | 사용자에게 표시할 메시지 |
| `timestamp` | 에러 발생 시각 |

---

### 4. GlobalExceptionHandler.java (전역 예외 처리)

**역할**: 애플리케이션 전체에서 발생하는 예외를 한 곳에서 처리

**처리하는 예외 종류**:

| 예외 타입 | 상황 | 로그 레벨 |
|-----------|------|-----------|
| `BusinessException` | 비즈니스 로직 예외 | `WARN` |
| `MethodArgumentNotValidException` | @Valid 검증 실패 | `WARN` |
| `IllegalArgumentException` | 잘못된 인자 | `WARN` |
| `Exception` | 예상치 못한 오류 | `ERROR` |

**로그 출력 예시**:

```
// 비즈니스 예외
WARN  [USER001] 사용자를 찾을 수 없습니다 | URI: /api/users/999 | Method: GET

// 예상치 못한 예외 (스택트레이스 포함)
ERROR [UNEXPECTED] URI: /api/orders | Method: POST | Error: Connection refused
java.sql.SQLTransientConnectionException: Connection refused
    at com.mysql.cj.jdbc.ConnectionImpl...
```

---

### 5. LoggingAspect.java (API 자동 로깅)

**역할**: 모든 Controller 메서드의 호출/완료/실패를 자동으로 로깅

**동작 방식**:
1. `@Around` 어노테이션으로 Controller 메서드 실행 전후를 가로챔
2. 메서드 실행 전: 호출 로그 출력
3. 메서드 실행 후: 완료 로그 + 실행 시간 출력
4. 예외 발생 시: 실패 로그 출력 후 예외 다시 던짐

**로그 출력 예시**:

```
// 성공 케이스
INFO  [API] AuthController.login 호출 | args=[LoginRequest(email=user@test.com)]
INFO  [API] AuthController.login 완료 | 156ms

// 실패 케이스
INFO  [API] UserController.getUser 호출 | args=[999]
WARN  [API] UserController.getUser 실패 | 23ms | error=사용자를 찾을 수 없습니다
```

**적용 범위**:
```java
// 이 패턴에 해당하는 모든 메서드에 자동 적용
execution(* com.samulparliament_be..controller..*(..))
//         ↑ 모든 반환타입           ↑ controller 패키지 하위 모든 클래스의 모든 메서드
```

---

## 🛠 사용 방법

### 새로운 에러 코드 추가하기

```java
// 1. ErrorCode.java에 추가
public enum ErrorCode {
    // ... 기존 코드들
    
    // 새로운 에러 코드
    PARLIAMENT_NOT_FOUND("PARLIAMENT001", "의회를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
}
```

### Service에서 예외 던지기

```java
@Service
public class ParliamentService {
    
    public Parliament getParliament(Long id) {
        return parliamentRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PARLIAMENT_NOT_FOUND));
    }
}
```

### 수동으로 비즈니스 로그 추가하기 (선택사항)

```java
@Service
@Slf4j  // Lombok 어노테이션
public class AuthService {
    
    public void login(LoginRequest request) {
        // 중요한 비즈니스 이벤트는 수동으로 로깅
        log.info("[AUTH] OAuth 토큰 검증 중 | provider={}", request.getProvider());
        
        // ... 로직
        
        log.info("[AUTH] 로그인 성공 | userId={}", user.getId());
    }
}
```

---

## 📋 로그 레벨 가이드

| 레벨 | 용도 | 예시 |
|------|------|------|
| `ERROR` | 즉시 조치 필요한 심각한 오류 | DB 연결 실패, 결제 실패 |
| `WARN` | 비정상이지만 처리된 상황 | 로그인 실패, 권한 없음, 리소스 없음 |
| `INFO` | 중요한 비즈니스 이벤트 | 회원가입, 로그인 성공, 주문 완료 |
| `DEBUG` | 개발/디버깅용 상세 정보 | 변수 값, API 응답 내용 |

---

## ⚠️ 주의사항

1. **민감정보 로깅 금지**
   - 비밀번호, 토큰 원본, 개인정보는 절대 로그에 남기지 않기

2. **ErrorCode 추가 시**
   - 카테고리에 맞는 접두사 사용 (AUTH, USER, OAUTH 등)
   - 번호는 순차적으로 증가

3. **예외 처리 계층**
   - Controller에서 try-catch 하지 않기 (GlobalExceptionHandler가 처리)
   - Service에서 BusinessException 던지기
