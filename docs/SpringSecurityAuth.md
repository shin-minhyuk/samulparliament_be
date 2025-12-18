# Spring Security 인증 객체 완벽 이해하기 🔐

## 전체 그림

```
[로그인 성공] 
     ↓
[User 엔티티] → [UserDetailsImpl로 감싸기] → [SecurityContext에 저장]
     ↓
[API 요청 시]
     ↓
[@AuthenticationPrincipal] → [UserDetailsImpl 꺼내서 Controller에 주입]
```

---

## 1. UserDetails - Spring Security의 "사용자 규격서"

### 정의
```java
public interface UserDetails {
    // Spring Security가 정의한 인터페이스
    // "사용자 정보는 이런 형태여야 해!" 라는 규격
}
```

### 왜 필요한가?
Spring Security는 **어떤 프로젝트에서든** 동작해야 한다.
- 어떤 프로젝트는 `Member` 클래스를 사용
- 어떤 프로젝트는 `User` 클래스를 사용
- 어떤 프로젝트는 `Account` 클래스를 사용

**Spring Security:** "나는 `UserDetails` 형태만 받을게. 너희가 알아서 맞춰!"

### 필수 메서드들
| 메서드 | 설명 |
|--------|------|
| `getUsername()` | 사용자 식별자 (이메일, 아이디 등) |
| `getPassword()` | 비밀번호 |
| `getAuthorities()` | 권한 목록 (ROLE_USER, ROLE_ADMIN 등) |
| `isAccountNonExpired()` | 계정 만료 여부 |
| `isAccountNonLocked()` | 계정 잠김 여부 |
| `isCredentialsNonExpired()` | 비밀번호 만료 여부 |
| `isEnabled()` | 계정 활성화 여부 |

---

## 2. UserDetailsImpl - 우리 프로젝트 전용 구현체

### 정의
```java
public class UserDetailsImpl implements UserDetails {
    private final User user;  // 👈 우리 User 엔티티를 품고 있음!
    
    public User getUser() {
        return user;  // Controller에서 실제 User 엔티티를 꺼낼 때 사용
    }
}
```

### 역할
**UserDetailsImpl = User 엔티티를 Spring Security가 이해할 수 있게 번역해주는 어댑터**

| Spring Security 메서드 | 우리 프로젝트 매핑 |
|------------------------|-------------------|
| `getUsername()` | `user.getEmail()` |
| `getPassword()` | `null` (OAuth라서 없음) |
| `getAuthorities()` | `ROLE_USER` 또는 `ROLE_ADMIN` |

---

## 3. @AuthenticationPrincipal - 현재 로그인한 사용자 꺼내기

### 정의
```java
@AuthenticationPrincipal UserDetailsImpl userDetails
```
→ "현재 로그인한 사용자(Principal)를 자동으로 주입해줘!"

### 사용 비교

**@AuthenticationPrincipal 없이:**
```java
@PostMapping
public void create() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
    User user = userDetails.getUser();
}
```

**@AuthenticationPrincipal 사용:**
```java
@PostMapping
public void create(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
}
```

---

## 4. 전체 흐름

### 로그인 시
```
Kakao OAuth 로그인 성공
       ↓
User user = userRepository.findByEmail(...)
       ↓
UserDetailsImpl details = new UserDetailsImpl(user)
       ↓
JWT 토큰 생성 (userId 포함)
```

### API 요청 시
```
Authorization: Bearer eyJ...
       ↓
JwtAuthenticationFilter가 토큰 검증
       ↓
userId로 User 조회 → UserDetailsImpl 생성
       ↓
SecurityContextHolder에 저장
```

### Controller 진입
```
@AuthenticationPrincipal UserDetailsImpl userDetails
       ↓
SecurityContext에서 꺼내서 자동 주입
       ↓
userDetails.getUser() → 실제 User 엔티티 사용
```

---

## 5. JWT의 Stateless 특징

**매 요청마다 새로 검증됩니다!**

```
[요청 1] → SecurityContext에 저장 → 응답 → SecurityContext 초기화
[요청 2] → SecurityContext에 다시 저장 → 응답 → SecurityContext 초기화
```

서버는 "이전 요청에서 누가 로그인했는지" 전혀 기억하지 않고, 오직 **현재 요청의 토큰만** 봅니다.

---

## 6. 비유 정리

| 개념 | 비유 |
|------|------|
| `UserDetails` | 신분증 규격 (사진, 이름, 생년월일 필수) |
| `UserDetailsImpl` | 실제 신분증 (규격대로 만든 우리 회원의 신분증) |
| `@AuthenticationPrincipal` | 신분증 자동 제출 서비스 |
| `SecurityContext` | 현재 들고 있는 신분증 보관함 |
