# 살림(Salrim) 프로젝트

한국 가계부 웹 애플리케이션. 포트폴리오 목적이며 백엔드 설계 역량과 JPA 데이터 모델링 능력을 보여주는 게 핵심.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Backend | Spring Boot 4.1.x, JPA, PostgreSQL |
| Frontend | Thymeleaf SSR, Tabler UI (jsDelivr CDN `@latest`), vanilla JS (`fetch` / `async/await`), ApexCharts |
| Auth | Spring Security + JWT (jjwt 라이브러리) |
| Font | Pretendard Variable |
| Build | Gradle, Lombok |
| IDE | IntelliJ IDEA |

---

## 데이터베이스

- **Schema:** `salim` (public 아님)
- **금액 타입:** `DECIMAL(15,0)` — 원화는 소수점 없음
- **PK 전략:** `GENERATED ALWAYS AS IDENTITY` (bigint) — `AUTO_INCREMENT` 쓰지 말 것

### 테이블 목록 (6개)

```
MEMBER, BANK, ACCOUNT, CATEGORY, PAYMENT_METHOD, TRANSACTION
```

- `MEMBER`가 허브 엔티티 (모든 테이블이 member_id FK 보유)
- `TRANSACTION`: `transaction_date` + nullable `settlement_date` 이중 날짜 설계
- `ACCOUNT`: 계좌번호 AES-256 암호화 필수

---

## 패키지 구조

```
com.salim
├── global
│   ├── jwt          # JwtProvider, JwtFilter 등
│   └── config       # SecurityConfig, WebMvcConfig
├── member
├── dashboard
├── account          # account + payment 묶음
├── transaction
├── category
└── report           # 읽기 전용 집계 레이어
```

---

## 엔티티 작성 규칙

```java
// 반드시 이 패턴 따를 것
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "table_name", schema = "salim")
public class SomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 변경 메서드는 명시적으로
    public void changeStatus(Status status) {
        this.status = status;
    }
}
```

**금지 사항:**
- `@Setter` 사용 금지
- `@Data` 사용 금지
- `@Builder`는 클래스가 아닌 생성자에 붙일 것

---

## DTO 규칙

- **record** 사용 (class 아님)
- 입력 DTO: `XxxRequest`, 출력 DTO: `XxxResponse`
- Bean Validation 어노테이션은 record 컴포넌트에 직접

```java
public record SignupRequest(
    @NotBlank @Pattern(regexp = "...") String email,
    @NotBlank String password
) {}
```

---

## 컨트롤러 구분

| 타입 | 어노테이션 | URL 패턴 | 반환 |
|------|-----------|---------|------|
| 페이지 컨트롤러 | `@Controller` | `/xxx` | HTML view |
| API 컨트롤러 | `@RestController` | `/api/xxx` | JSON |

- `AuthController`: 로그인/로그아웃/회원가입 (`/auth/**`)
- `MemberController`: 프로필 등 인증 후 액션
- `@Valid`는 컨트롤러 `@RequestBody`에 붙일 것
- `@Transactional`은 서비스 쓰기 메서드에 명시적으로

---

## 인증 구조

- JWT → `Authorization: Bearer` 헤더 방식
- **CSRF 비활성화** (쿠키 기반 아니므로 올바름 — 되돌리지 말 것)
- 비밀번호: BCrypt
- 테스트 해시는 반드시 `@Test` 메서드로 직접 생성 (외부 해시 문자열 복붙 금지)

```
완료된 파일:
- AuthController, AuthService, JwtProvider (com.salim.global.jwt)
- MemberRepository
- SecurityConfig (CSRF disabled, /fonts/** 허용)
- WebMvcConfig (CurrentUriInterceptor 등록)
```

---

## Thymeleaf 레이아웃

```
templates/
├── layout/
│   ├── base.html      # 사이드바 대시보드 레이아웃
│   └── auth.html      # 중앙 정렬 풀페이지 (로그인/회원가입)
├── dashboard.html
├── category.html
└── auth/
    ├── login.html
    └── signup.html
```

**주의:**
- Thymeleaf 3.1+에서 `#httpServletRequest`, `#request` 사용 불가
- 현재 URI 주입은 `CurrentUriInterceptor` + `WebMvcConfig` 패턴으로 처리 중
- JS inline expression: `/*[[...]]*/` 패턴 제거할 것 (파서 에러 원인)

---

## 브랜드 CSS 변수

```css
--color-income:  #2D6A4F;  /* 수입 딥그린 */
--color-expense: #C1604A;  /* 지출 테라코타 */
--color-balance: #1E2D3D;  /* 잔액 잉크 */
```

Tabler CSS 오버라이드 시:
- 단일 클래스 선택자는 Tabler specificity에 밀림 → 2단계 선택자 필요 (`.navbar-vertical .navbar-brand`)
- `opacity: 1 !important` — Bootstrap 5 기본값이 hr 요소에 opacity 적용하므로

---

## Spring Security 정적 리소스

```java
// SecurityFilterChain만으로는 부족함
// WebSecurityCustomizer.ignoring() 반드시 사용
web.ignoring().requestMatchers("/fonts/**", "/css/**", "/js/**", "/images/**");
```

---

## IntelliJ 설정

- **Build tool:** `IntelliJ IDEA`로 설정 (Gradle 아님)
- CSS/정적 파일 변경 시 서버 재시작 없이 반영되려면 이게 필수

---

## Git / 환경 분리

```
application.yml         # 공개 (GitHub에 올라감)
application-local.yml   # 로컬 전용 (gitignore)
```

- GitHub 유저명: `yunho-dev`
- 브랜치 전략: `main / develop / feat` (예정)
- CI/CD: GitHub Actions (예정)

---

## 완료된 페이지

| 페이지 | 파일 | 비고 |
|--------|------|------|
| 로그인 | `auth/login.html` | |
| 회원가입 | `auth/signup.html` | 클라이언트 사이드 validation, fetch/async |
| 대시보드 | `dashboard.html` | ApexCharts 바차트 + 도넛, KPI 카드, 최근 거래 |
| 카테고리 | `category.html` | 탭 전환 JS, 타입별 색상, container-fluid |

---

## 계좌번호 암호화

**방식: JPA AttributeConverter + AES-256**

```
global/
└── util/
    ├── AesUtil.java              # AES-256 암/복호화 유틸
    └── AccountNumberConverter.java  # JPA AttributeConverter 구현
```

```java
// AccountNumberConverter.java
@Converter
public class AccountNumberConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String plain) {
        return AesUtil.encrypt(plain);
    }
    @Override
    public String convertToEntityAttribute(String cipher) {
        return AesUtil.decrypt(cipher);
    }
}

// Account.java 엔티티에서
@Convert(converter = AccountNumberConverter.class)
private String accountNumber;
```

- 암호화 키는 `application-local.yml`에서 `@Value`로 주입 (절대 하드코딩 금지)
- DB/SQL 로그에는 항상 암호화된 값만 찍힘
- 엔티티 비즈니스 로직은 평문 String 그대로 사용

---

## 남은 작업

- [ ] UI 페이지: 결제수단, 거래내역, 계좌관리
- [ ] 백엔드 CRUD: Category → PaymentMethod → Transaction 순
- [ ] AWS 첫 배포 (로그인 + Transaction CRUD 완성 시점)
- [ ] GitHub Actions CI/CD
- [ ] 랜딩 페이지 (대시보드 스크린샷을 hero 이미지로)
- [ ] Flyway/Liquibase 마이그레이션 (초기 개발 이후)

---

## 코드 스타일

- 주석: 최소화. 명백한 로직엔 달지 않음. 비자명한 통합 포인트에만 TODO 허용
- 검증·비즈니스 로직은 직접 설계, AI는 보일러플레이트에 활용
