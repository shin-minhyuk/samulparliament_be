# BaseEntity와 JPA 콜백 어노테이션 이해하기 🎯

## BaseEntity 개요

모든 엔티티가 공통으로 가지는 필드(생성일, 수정일, 삭제일)를 관리하는 추상 클래스

```java
@MappedSuperclass  // 👈 이 클래스는 테이블로 생성되지 않고, 상속받는 엔티티에 필드만 추가됨
@Getter
public abstract class BaseEntity {
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected LocalDateTime deletedAt;
}
```

---

## @MappedSuperclass

| 개념 | 설명 |
|------|------|
| 역할 | 상속받는 엔티티에 필드를 공유 |
| 테이블 생성 | ❌ BaseEntity 테이블은 생성되지 않음 |
| 상속 | Post, Comment 등이 상속받아 사용 |

```
BaseEntity (테이블 없음)
    ├── Post (posts 테이블) - createdAt, updatedAt, deletedAt 컬럼 포함
    └── Comment (comments 테이블) - createdAt, updatedAt, deletedAt 컬럼 포함
```

---

## JPA 콜백 어노테이션

### @PrePersist - INSERT 직전 실행

```java
@PrePersist
public void onCreate() {
    this.createdAt = LocalDateTime.now();
}
```

**동작 시점:**
```
Post post = new Post(...);
postRepository.save(post);  // 👈 여기서
       ↓
@PrePersist → onCreate() 자동 실행 (createdAt 설정)
       ↓
INSERT INTO posts ... 쿼리 실행
```

---

### @PreUpdate - UPDATE 직전 실행

```java
@PreUpdate
public void onUpdate() {
    if (this.deletedAt == null) {
        this.updatedAt = LocalDateTime.now();
    }
}
```

**동작 시점:**
```
Post post = postRepository.findById(1L);
post.update("새 제목", "새 내용");
postRepository.save(post);  // 👈 여기서
       ↓
@PreUpdate → onUpdate() 자동 실행 (updatedAt 설정)
       ↓
UPDATE posts SET ... 쿼리 실행
```

**왜 `deletedAt == null` 체크?**
- `softDelete()` 호출 시에도 `@PreUpdate`가 실행됨
- 삭제 시에는 `updatedAt`을 갱신하지 않으려고

---

## 전체 JPA 콜백 어노테이션 목록

| 어노테이션 | 시점 | 용도 |
|-----------|------|------|
| `@PrePersist` | INSERT 전 | 생성일 자동 기록 |
| `@PostPersist` | INSERT 후 | 생성 후 로깅 등 |
| `@PreUpdate` | UPDATE 전 | 수정일 자동 기록 |
| `@PostUpdate` | UPDATE 후 | 수정 후 로깅 등 |
| `@PreRemove` | DELETE 전 | 삭제 전 검증 등 |
| `@PostRemove` | DELETE 후 | 삭제 후 정리 작업 |
| `@PostLoad` | SELECT 후 | 조회 시 추가 처리 |

---

## Soft Delete (논리 삭제)

```java
public void softDelete() {
    this.deletedAt = LocalDateTime.now();
}

public boolean isDeleted() {
    return this.deletedAt != null;
}
```

### Hard Delete vs Soft Delete

| 방식 | SQL | 복구 |
|------|-----|------|
| Hard Delete | `DELETE FROM posts WHERE id = 1` | ❌ 불가능 |
| Soft Delete | `UPDATE posts SET deleted_at = NOW()` | ✅ 가능 |

### Repository에서 조회 시

```java
// deletedAt이 null인 것만 조회 (삭제되지 않은 것만)
Optional<Post> findByIdAndDeletedAtIsNull(Long id);
Page<Post> findByDeletedAtIsNull(Pageable pageable);
```

---

## 사용 예시

```java
// Post 엔티티
@Entity
public class Post extends BaseEntity {
    // createdAt, updatedAt, deletedAt 자동으로 상속받음
}

// 생성
Post post = Post.create(author, request);
postRepository.save(post);  // → @PrePersist로 createdAt 자동 설정

// 수정
post.update("새 제목", "새 내용");
postRepository.save(post);  // → @PreUpdate로 updatedAt 자동 설정

// 삭제 (soft delete)
post.softDelete();
postRepository.save(post);  // → deletedAt만 설정, updatedAt은 그대로
```

---

## 장점

| 수동 관리 | 자동 관리 (현재 방식) |
|----------|---------------------|
| `post.setCreatedAt(LocalDateTime.now());` | 필요 없음, 자동! |
| 매번 작성해야 함 | 까먹을 일 없음 |
| 실수 가능성 높음 | 일관성 보장 |
