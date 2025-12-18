# JPA 엔티티 생명주기와 Dirty Checking 📦

## 엔티티 상태 4가지

```
[비영속] → save() → [영속] → clear() → [준영속]
                       ↓
                   remove()
                       ↓
                    [삭제]
```

---

## 1. 비영속 (New/Transient)

**"JPA가 모르는 상태"** - DB에도 없고, JPA도 관리 안 함

```java
// new로 생성만 한 상태
Comment comment = new Comment();
comment.setContent("안녕하세요");
// 아직 JPA가 관리하지 않음, DB에도 없음
```

| 특징 | 값 |
|------|-----|
| JPA 관리 | ❌ |
| DB 존재 | ❌ |
| Dirty Checking | ❌ |

---

## 2. 영속 (Managed/Persistent)

**"JPA가 관리하는 상태"** - DB와 동기화됨

```java
// 방법 1: save() 호출
Comment comment = new Comment();
commentRepository.save(comment);  // 👈 영속 상태!

// 방법 2: 조회
Comment comment = commentRepository.findById(1L);  // 👈 영속 상태!
```

| 특징 | 값 |
|------|-----|
| JPA 관리 | ✅ |
| DB 존재 | ✅ |
| Dirty Checking | ✅ |

---

## Dirty Checking (변경 감지)

영속 상태 엔티티를 수정하면 **자동으로 UPDATE 쿼리 실행**

```java
@Transactional
public void delete(Long commentId) {
    // 조회 → 영속 상태
    Comment comment = commentRepository.findById(commentId);
    
    // 값 변경
    comment.softDelete();
    
    // save() 없어도 트랜잭션 끝날 때 자동 UPDATE!
}
```

### 동작 원리
```
[조회] → JPA가 원본 스냅샷 저장
   ↓
[수정] → 엔티티 값 변경
   ↓
[트랜잭션 종료] → 원본과 비교
   ↓
[다르면] → UPDATE 쿼리 자동 실행
```

---

## 비영속 케이스 (save() 필수!)

### 케이스 1: new로 생성만 한 경우
```java
Comment comment = new Comment();
comment.setContent("내용");
// ❌ save() 안 하면 DB에 저장 안 됨
commentRepository.save(comment);  // ✅ 필수
```

### 케이스 2: @Transactional 없는 경우
```java
// @Transactional 없음!
public void update(Long id) {
    Comment comment = commentRepository.findById(id);
    comment.setContent("수정");
    // ❌ Dirty Checking 안 됨, save() 필수!
}
```

---

## 상태별 save() 필요 여부

| 상황 | 상태 | save() 필요? |
|------|------|-------------|
| `new Comment()` | 비영속 | ✅ 필수 |
| `repository.findById()` | 영속 | ❌ 불필요 |
| `repository.save(new)` 후 수정 | 영속 | ❌ 불필요 |
| `@Transactional` 없을 때 | - | ✅ 필수 |

---

## 비유 정리

| 상태 | 비유 |
|------|------|
| 비영속 | 학교에 입학 안 한 학생 |
| 영속 | 학교에 재학 중인 학생 |
| save() | 입학 신청서 제출 |
| Dirty Checking | 학교가 학생 정보 자동 동기화 |

---

## 핵심 정리

> **@Transactional + Repository 조회 엔티티 = save() 없이도 자동 저장!**
