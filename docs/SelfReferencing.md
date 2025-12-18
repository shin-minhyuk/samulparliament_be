# 자기 참조 (Self-Referencing) 패턴 🔄

## 개념

**"같은 테이블의 다른 행을 가리키는 관계"**

하나의 테이블에서 부모-자식 관계를 표현할 때 사용

---

## 예시: 대댓글 구조

```
comments 테이블
┌────┬───────────┬───────────────────┐
│ id │ parent_id │ content           │
├────┼───────────┼───────────────────┤
│ 1  │ NULL      │ "좋은 글이네요"   │  ← 최상위 댓글
│ 2  │ NULL      │ "감사합니다"      │  ← 최상위 댓글
│ 3  │ 1         │ "저도 동의해요"   │  ← 1번의 대댓글
│ 4  │ 1         │ "완전 공감"       │  ← 1번의 대댓글
└────┴───────────┴───────────────────┘
     ↑
     자기 참조 (comments.parent_id → comments.id)
```

---

## JPA 코드

```java
@Entity
public class Comment {
    @Id
    private Long id;
    
    // 부모 댓글 (null이면 최상위 댓글)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    
    // 자식 댓글들 (대댓글 목록)
    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();
}
```

---

## 깊이 제한

2단계 중첩만 허용하는 경우 (LinkedIn 스타일):

```java
// 대댓글 가능 여부 체크
public boolean canHaveReply() {
    return this.parent == null;  // 최상위 댓글만 대댓글 가능
}
```

---

## 다른 활용 예시

### 조직도 (직원-상사)
```java
@Entity
public class Employee {
    @ManyToOne
    private Employee manager;
    
    @OneToMany(mappedBy = "manager")
    private List<Employee> subordinates;
}
```

### 카테고리 (상위-하위)
```java
@Entity
public class Category {
    @ManyToOne
    private Category parent;
    
    @OneToMany(mappedBy = "parent")
    private List<Category> children;
}
```

---

## 장점

| 장점 | 설명 |
|------|------|
| 단일 테이블 | 여러 테이블 불필요 |
| 확장성 | N단계 중첩 지원 가능 |
| 단순한 코드 | 같은 엔티티라 재사용 용이 |

## 주의사항

| 주의 | 설명 |
|------|------|
| 무한 중첩 방지 | 깊이 제한 로직 필요 |
| N+1 문제 | 조회 시 fetch 전략 고려 |
| 삭제 처리 | 자식 댓글 처리 정책 결정 필요 |
