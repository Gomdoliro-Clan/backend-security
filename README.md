# ProcureFlow

Spring Boot와 PostgreSQL로 구현한 다중 조직 구매·정산 API 프로젝트입니다. 거래처 등록 → 발주 → 분리 승인 → 청구서 정산, 문서 열람, 보고서, 공급사 견적 접수와 문서 자동 생성 기능을 제공합니다.

교육용 애플리케이션 보안 검토 과제입니다. 합성 데이터만 사용하고 로컬 실습 환경에서 실행하세요. 기본 포트는 loopback에만 공개되며 Compose의 런타임 네트워크는 외부 통신이 차단됩니다. 외부 네트워크에 배포하지 마세요.

## 빠른 시작: Docker

Docker Engine/Desktop과 Compose v2가 필요합니다. 최초 빌드는 Maven 및 컨테이너 이미지를 내려받습니다.

```sh
docker compose --profile full up --build -d
docker compose logs -f app
```

앱 시작 및 데이터 생성 후 `http://127.0.0.1:18080/api/health`에서 `{"status":"UP"}`를 확인합니다. 별도 웹 UI 없이 REST API로 사용하는 프로젝트입니다.

```sh
curl -s http://127.0.0.1:18080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"analyst@hanul.example","password":"Analyst-lab-2026!"}'
```

응답의 `accessToken`을 이후 요청의 `Authorization: Bearer <token>` 헤더에 넣습니다. 토큰은 8시간 후 만료되며, 쿠키 인증은 사용하지 않습니다.

## 실습 계정

| 역할 | 이메일 | 초기 비밀번호 |
|---|---|---|
| 조회 담당 | `reader@hanul.example` | `Reader-lab-2026!` |
| 분석 담당 | `analyst@hanul.example` | `Analyst-lab-2026!` |
| 구매 담당 | `buyer@hanul.example` | `Buyer-lab-2026!` |
| 조직 관리자 | `manager@hanul.example` | `Manager-lab-2026!` |

네 계정 모두 **하늘 테크** 조직 소속입니다. 조직 관리자는 서비스 운영자나 DB 관리자가 아니며 다른 조직 데이터에 접근할 권한이 없습니다. 미래 스튜디오 계정 비밀번호는 초기화 시 무작위로 생성합니다. 기본 계정과 로컬 DB 정보는 과제 목표에 포함하지 않습니다.

## 개발 실행

Java 17 이상이 필요합니다.

```sh
docker compose up -d db
./mvnw verify
./mvnw spring-boot:run
```

Windows에서는 `mvnw.cmd`를 사용합니다. 전체 Docker 실행과 개발 실행은 동일한 앱 포트를 사용하므로 동시에 실행하지 않습니다.
macOS에서 `LC_ALL=C` 때문에 한글 경로가 `???`로 바뀌면 `LC_ALL=en_US.UTF-8 LANG=en_US.UTF-8 ./mvnw verify`로 실행합니다.

| 설정 | 기본값 | 용도 |
|---|---|---|
| `APP_PORT` | `18080` | 앱 포트 / Compose의 공개 포트 |
| `PG_PORT` | `55438` | Compose의 PostgreSQL 공개 포트 |
| `DB_URL` | `jdbc:postgresql://127.0.0.1:55438/procureflow` | 로컬 JVM의 JDBC 연결 |
| `DB_USER` | `procure_app` | 일반 DB 사용자 |
| `DB_PASSWORD` | `local-classroom-db` | 로컬 실습용 DB 비밀번호 |
| `WORKER_DELAY_MS` | `2000` | 견적 문서 작업 주기 |

## 종료와 초기화

```sh
docker compose --profile full down
```

아래 명령은 **이 Compose 프로젝트의 실습 데이터 볼륨을 삭제**합니다. 새 과제 실행이나 재현 결과 초기화가 필요할 때만 사용합니다.

```sh
docker compose --profile full down -v
docker compose --profile full up --build -d
```

## 구성

- Java 17, Spring Boot 3.5.16, Spring Security, Bean Validation
- Spring JDBC, PostgreSQL 17, Flyway 마이그레이션
- 2개 조직, 4개 역할, 14개 업무·인증 테이블, 96개 청구서 등 합성 데이터
- 기능별 패키지, Controller → Service → Repository 계층, 예약 작업
- 공개 견적 접수와 비동기 견적 문서 생성
- 정상 구매 승인 정책 단위 테스트

문서: [API 계약](docs/API.md), [구조와 업무 규칙](docs/ARCHITECTURE.md), [과제 안내](docs/ASSIGNMENT.md), [일반 요청 예시](requests/demo.http).
