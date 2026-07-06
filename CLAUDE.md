# 살림(Salim) 프로젝트

한국 가계부 웹 애플리케이션. 이직용 포트폴리오이며, 백엔드 설계 역량과 JPA 데이터 모델링 능력을 보여주는 게 핵심 목적. 모든 기술적 결정은 면접에서 근거를 설명할 수 있어야 함 — "그냥 되니까"가 아니라 "왜 이렇게 했는지"가 중요.

---

## 개발 명령어

```bash
./gradlew bootRun                              # 로컬 서버 실행 (application-local.yml 프로필 사용)
./gradlew build                                # 전체 빌드 (테스트 포함)
./gradlew test                                 # 전체 테스트 실행
./gradlew test --tests "SalimApplicationTests" # 단일 테스트 클래스 실행
```

- PostgreSQL이 로컬에서 떠 있어야 함 (`application-local.yml`의 `jdbc:postgresql://localhost:5432/salim_db`, 스키마 `salim`, 계정 `salim`).
- IntelliJ에서 작업 시 Build tool을 `IntelliJ IDEA`로 설정해야 정적 리소스 핫리로드가 동작함 (Gradle 기본값 아님, 위 표 참고).
- `spring.jpa.hibernate.ddl-auto: update` — 마이그레이션 도구 없이 엔티티 변경사항이 곧바로 스키마에 반영됨. 컬럼 삭제/타입 변경처럼 update가 못 따라가는 변경은 수동 DDL 필요.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Backend | Spring Boot 4.1.x, JPA, PostgreSQL |
| Frontend | Thymeleaf SSR (Layout Dialect), Tabler UI + Bootstrap, vanilla JS (`fetch` / `async/await`), ApexCharts |
| Auth | Spring Security + JWT (jjwt 라이브러리), HttpOnly 쿠키 기반 |
| Font | Pretendard Variable |
| Build | Gradle, Lombok |
| IDE | IntelliJ IDEA (Build tool을 Gradle이 아닌 `IntelliJ IDEA`로 설정 — 정적 리소스 핫리로드에 필수) |

---

## 데이터베이스 컨벤션

- **Schema:** `salim` (public 아님)
- **PK 전략:** `GENERATED ALWAYS AS IDENTITY` (bigint) — `AUTO_INCREMENT` 쓰지 말 것
- **금액 타입:** `DECIMAL(15,0)` — 원화는 소수점 없음
- **계좌번호:** `VARCHAR(255)`, AES-256 암호화 후 저장 (아래 암호화 섹션 참고)
- **날짜/시간:** PostgreSQL에는 `datetime` 타입 없음 → `timestamp` 사용
- **검증:** 애플리케이션 레벨 + DB 레벨(제약조건) 이중 체크 — 동시성/race condition 방어

### 테이블 (6개)
```
MEMBER, BANK, ACCOUNT, CATEGORY, PAYMENT_METHOD, TRANSACTION
```
`MEMBER`가 허브 엔티티 — 모든 테이블이 member_id FK 보유.

---

## 패키지 구조 원칙

```
com.salim.[domain]
```
도메인별로 패키지를 나누고, 도메인에 속하지 않는 공통 인프라(JWT, Security, 유틸)는 `global` 하위에 배치.

```
global/
├── jwt        # JwtProvider, JwtAuthenticationFilter
├── security   # SecurityConfig
└── util       # AesUtil, Converter 등 (계좌번호 암호화 구현 시 추가 예정, 현재 미구현)
```

도메인 패키지 내부 레이어링 (모든 도메인 공통):
```
[domain]/
├── controller
├── repository
├── dto
└── service
```

### 현재 구현 상태

- **완성:** `member` (회원가입/로그인, JWT 발급·검증, `MemberRepository`/`AuthService`/`MemberService`)
- **스캐폴딩만 존재 (화면 라우팅용 `@Controller`만 있고 repository/dto/service는 빈 패키지):** `account`, `category`, `dashboard`, `payment`, `transaction` — 각 컨트롤러는 정적 페이지 뷰 이름만 반환하는 상태이며 실제 CRUD/비즈니스 로직은 미구현
- `global/util`(AesUtil, AccountNumberConverter)은 위 컨벤션 문서화만 되어 있고 아직 코드 없음 — 계좌 암호화 작업 시작 시 신규 작성
- `src/test/java/org/example/salim/SalimApplicationTests.java`는 Spring Initializr가 생성한 기본 패키지(`org.example.salim`)에 남아 있음. 나머지 코드는 전부 `com.salim` 하위이므로 새 테스트를 추가할 때 패키지 위치에 주의 (관례상 `com.salim` 하위에 작성할 것)

---

## 엔티티 작성 규칙

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "table_name", schema = "salim")
public class SomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    public SomeEntity(...) { ... }

    // 상태 변경은 의미 있는 이름의 메서드로 (setter 아님)
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
- Bean Validation 어노테이션은 record 컴포넌트에 직접 명시
- `@Valid`는 컨트롤러의 `@RequestBody`에 붙일 것 (서비스 계층 아님)

```java
public record SignupRequest(
    @NotBlank @Pattern(regexp = "...") String memberId,
    @NotBlank String password
) {}
```

---

## 컨트롤러 구분 원칙

| 타입 | 어노테이션 | 반환 | 용도 |
|------|-----------|------|------|
| 페이지 컨트롤러 | `@Controller` | HTML view | 화면 렌더링 (Thymeleaf) |
| API 컨트롤러 | `@RestController` 또는 `@Controller`+`@ResponseBody` | JSON | 데이터 처리, 비동기 액션 |

- **클래스 네이밍:** 뷰 렌더링 컨트롤러는 `XxxViewController`, API 컨트롤러는 `XxxApiController`로 역할을 이름에 명시 (예: `CategoryViewController`, `CategoryApiController`). 한 도메인에 두 역할이 공존하면 이렇게 클래스를 분리한다.
  - 예외: `AuthController`(로그인 페이지 렌더링 + 로그인/로그아웃 폼 처리)처럼 페이지 전환이 있는 폼 액션과 뷰 렌더링이 강하게 얽혀 있고 도메인 CRUD 성격이 아닌 경우는 분리하지 않고 유지.
- URL 네이밍: kebab-case 복수형 (예: `/payment-methods`, `/transactions`)
- `@Transactional`은 서비스 쓰기 메서드에 명시적으로 선언

### 폼 리다이렉트 vs fetch(JSON) 선택 기준
- **폼 리다이렉트**: 액션 완료 후 어차피 다른 페이지로 전환되는 경우 (로그인, 로그아웃). `RedirectAttributes`로 플래시 메시지 전달.
- **fetch/JSON**: 페이지 전환 없이 부분 갱신이 필요한 경우 (중복 체크, 필터링, 목록 조회 등)
- 모든 곳에 ajax를 강제로 쓰지 않는다 — 액션의 성격(페이지 전환 필요 여부)에 따라 판단.

---

## 인증 구조

- **방식:** JWT를 HttpOnly 쿠키(`token`)에 저장 — SSR 페이지 이동 시에도 자동으로 서버에 전달되어야 하므로 Bearer 헤더 방식이 아닌 쿠키 방식 채택
- `JwtAuthenticationFilter`(`OncePerRequestFilter` 상속)가 쿠키에서 토큰을 읽어 검증하고, `SecurityContextHolder`에 인증 정보를 채움. `UsernamePasswordAuthenticationFilter` 이전에 실행되도록 `addFilterBefore`로 등록.
- 인증 실패 시 처리는 `AuthenticationEntryPoint`에서 경로 기준으로 분기: `/api/**`는 401 응답, 그 외 SSR 페이지 요청은 로그인 페이지로 리다이렉트.
- `authorizeHttpRequests`에서 로그인/회원가입 등 인증 전 접근이 필요한 경로만 명시적으로 `permitAll` 처리하고, 나머지는 `anyRequest().authenticated()`. `/**` 와일드카드로 뭉뚱그려 열지 않는다 — 뒤따르는 `anyRequest()`가 무력화됨.
- 스프링 시큐리티의 기본 `LogoutFilter`가 `/logout` 경로를 선점하므로, 커스텀 로그아웃 로직을 쓰려면 `.logout(AbstractHttpConfigurer::disable)`로 기본 필터를 꺼야 함.
- 비밀번호: BCrypt. 테스트용 해시는 반드시 `@Test` 메서드로 직접 생성 (외부 해시 문자열 복붙 금지).

---

## Thymeleaf 레이아웃 컨벤션

```
templates/
├── layout.html    # 메인 레이아웃 (다크 사이드바 포함)
├── auth.html       # 로그인/회원가입 전용 (중앙 정렬 풀페이지)
└── [domain]/
    └── [page].html
```

- 템플릿 폴더명은 단수, 파일명은 URL과 매칭되는 복수형
- `page-css`, `page-js` 블록으로 페이지별 리소스 관리
- 풀폭 페이지는 `container-fluid` 사용
- 사이드바 collapse 상태는 `localStorage`로 유지
- 레이아웃 시프트 방지: `scrollbar-gutter: stable`

**주의:**
- Thymeleaf 3.1+에서 `#httpServletRequest`, `#request` 직접 접근 불가 → `CurrentUriInterceptor` + `WebMvcConfig` 패턴으로 현재 URI를 모델에 주입해서 사용
- JS inline expression `/*[[...]]*/` 패턴은 파서 에러 원인이 되므로 지양

---

## 브랜드 CSS 변수

```css
--color-income:  #2D6A4F;  /* 수입 딥그린 */
--color-expense: #C1604A;  /* 지출 테라코타 */
--color-balance: #1E2D3D;  /* 잔액 잉크 */
```

Tabler CSS 오버라이드 시 주의점:
- 단일 클래스 선택자는 Tabler specificity에 밀림 → 2단계 선택자 필요 (예: `.navbar-vertical .navbar-brand`)
- Bootstrap 5는 `hr` 요소에 기본 opacity를 적용하므로, 완전 불투명하게 하려면 `opacity: 1 !important` 필요

---

## Spring Security 정적 리소스 처리

```java
web.ignoring().requestMatchers("/fonts/**", "/css/**", "/js/**", "/images/**");
```
`SecurityFilterChain` 내 `permitAll`만으로는 부족한 경우 `WebSecurityCustomizer.ignoring()` 병행 고려.

---

## 계좌번호 암호화

**방식:** JPA `AttributeConverter` + AES-256

```
global/util/
├── AesUtil.java                  # AES-256 암/복호화 유틸
└── AccountNumberConverter.java   # AttributeConverter 구현
```

```java
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
```

- 암호화 키는 `application-local.yml`에서 `@Value`로 주입 (하드코딩 금지)
- DB/SQL 로그에는 항상 암호화된 값만 노출
- 엔티티 비즈니스 로직 내부에서는 평문 String 그대로 다룸 (Converter가 저장/조회 시점에 자동 처리)

---

## Git / 환경 분리

```
application.yml         # 공개 (GitHub에 올라감)
application-local.yml   # 로컬 전용 (gitignore)
```

- **브랜치 전략:** `main` ← `develop` ← `feat/*`. 모든 변경사항(코드/문서 포함)은 `develop` 이하에서 커밋하고, `main`은 검증된 결과물을 merge만 받는다 (직접 커밋 지양 — 두 브랜치가 갈라지는 것 방지).
- **커밋 메시지:** Conventional Commits (`feat:`, `fix:`, `docs:` 등, 콜론 뒤 공백 하나)
- **CLI 우선:** 기본 작업은 터미널로, 복잡한 브랜치 히스토리 시각 확인용으로만 Sourcetree 보조 사용
- GitHub 유저명: `yunho-dev` — 포트폴리오 목적으로 public 저장소 유지

---

## 코드 스타일 / AI 협업 원칙

- 주석: 최소화하되, "왜 이렇게 했는지"가 자명하지 않은 지점에는 근거를 남긴다. 단순 문법 설명("이건 반복문입니다")보다 "이 프로젝트에서 왜 이 방식을 택했는지"에 집중.
- 검증·비즈니스 로직은 직접 설계하고 이해한 뒤 작성. AI는 보일러플레이트 생성과 개념 설명에 활용하되, 이해 없이 그대로 복붙하지 않는다 ("딸깍 코딩" 지양).
- Spring Boot 4.1.x처럼 최신 버전은 AI의 학습 데이터 범위를 벗어날 수 있으므로, 실제 빌드 에러가 발생하지 않는 한 Spring Initializer 자동 생성 결과를 신뢰한다.
