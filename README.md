# 사물의의회 API 서버 🏛️

사물(事物)들의 의견을 모아 토론하고 의결하는 커뮤니티 플랫폼 백엔드 서버

## 📌 프로젝트 현황

> **🔄 마이그레이션 진행 중**  
> 기존 Supabase 기반 서버를 Spring Boot로 마이그레이션하고 있습니다.

---

## 🛠 기술 스택

| 기술 | 버전 | 설명 |
|------|------|------|
| Java | 17 | 언어 |
| Spring Boot | 4.0.0 | 프레임워크 |
| Spring Security | - | 인증/인가 |
| Spring Data JPA | - | ORM |
| PostgreSQL | - | 데이터베이스 |
| JWT (jjwt) | 0.11.5 | 토큰 인증 |
| Swagger (springdoc) | 2.8.6 | API 문서 |

---

## 📂 프로젝트 구조

```
src/main/java/com/samulparliament_be/
├── domain/                     # 도메인별 모듈
│   ├── posts/                  # 게시글
│   ├── comments/               # 댓글 (대댓글 지원)
│   ├── users/                  # 사용자
│   ├── schedules/              # 일정
│   ├── notices/                # 공지사항
│   ├── faqs/                   # FAQ
│   ├── archives/               # 아카이브
│   └── common/                 # 공통 엔티티 (BaseEntity)
│
└── global/                     # 전역 설정
    ├── auth/                   # 인증 (JWT, 커스텀 어노테이션)
    ├── config/                 # Security, CORS 등
    ├── exception/              # 예외 처리
    └── oauth/                  # OAuth 클라이언트 (Kakao)
```

---

## 🚀 실행 방법

```bash
./gradlew bootRun
```

- 서버: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui/index.html`

---

## 🔐 인증 방식

- **OAuth 2.0**: 카카오 로그인
- **JWT**: Access Token + Refresh Token
- **권한**: `USER`, `ADMIN`

### API 접근 제어 어노테이션

| 어노테이션 | 설명 |
|-----------|------|
| `@ALL` | 누구나 접근 가능 |
| `@USER` | 로그인 필수 |
| `@ADMIN` | 관리자만 접근 가능 |

---

## 📖 개발 문서

자세한 개발 문서는 `docs/` 폴더에서 확인:

- [Spring Security 인증](docs/SpringSecurityAuth.md)
- [JPA 엔티티 생명주기](docs/JpaEntityLifecycle.md)
- [BaseEntity 설계](docs/BaseEntity.md)
- [자기 참조 패턴](docs/SelfReferencing.md)
- [에러 핸들링](docs/ERROR_HANDLING.md)

---

## 📋 마이그레이션 진행 상황

- [x] 프로젝트 초기 설정
- [x] 사용자 인증 (OAuth, JWT)
- [x] 게시글 CRUD
- [x] 댓글/대댓글 기능
- [x] API 접근 제어 (커스텀 어노테이션)
- [ ] 일정 관리 API
- [ ] 공지사항 API
- [ ] FAQ API
- [ ] 아카이브 API
