# API 계약

기본 주소: `http://127.0.0.1:18080`. JSON 요청은 `Content-Type: application/json`, 인증 요청은 `Authorization: Bearer <token>`을 사용합니다. DB 조회 응답의 필드는 snake_case이며 입력 DTO 필드는 camelCase입니다.

## 엔드포인트

| Method | 경로 | 설명 |
|---|---|---|
| GET | `/api/health` | 공개 상태 확인 |
| POST | `/api/auth/login` | 이메일·비밀번호로 토큰 발급 |
| GET | `/api/auth/me` | 현재 사용자 |
| POST | `/api/auth/logout` | 현재 토큰 폐기 |
| GET | `/api/organization` | 현재 조직 |
| GET | `/api/organization/members` | 현재 조직 구성원 |
| GET | `/api/dashboard` | 청구·정산 집계 |
| GET | `/api/catalog?q=` | 품목 이름 검색 |
| GET | `/api/suppliers` | 거래처 목록 |
| POST | `/api/suppliers` | 거래처 등록 |
| GET | `/api/suppliers/{uuid}` | 거래처 상세 |
| GET | `/api/suppliers/export.csv` | 거래처 CSV |
| GET | `/api/purchases` | 발주 목록 |
| POST | `/api/purchases` | 발주 생성 |
| POST | `/api/purchases/{uuid}/approve` | 발주 승인 |
| GET | `/api/invoices` | 청구서 목록 |
| GET | `/api/invoices/{uuid}` | 청구서 상세 |
| POST | `/api/invoices/{uuid}/reconcile` | 청구서 정산 |
| GET | `/api/documents` | 문서 목록 |
| GET | `/api/documents/{number}` | 문서 상세 |
| GET | `/api/documents/{number}/preview` | text/plain 미리보기 |
| GET | `/api/reports` | 보고서 목록 |
| POST | `/api/reports` | 보고서 정의 저장 |
| GET | `/api/reports/{uuid}/preview` | 집계 미리보기 |
| GET | `/api/reports/{uuid}/export.csv` | 집계 CSV |
| GET | `/api/portal/rfqs` | 공개 입찰 목록 |
| POST | `/api/portal/rfqs/{code}/quotes` | 공개 견적 접수 |
| GET | `/api/portal/receipts/{uuid}` | 접수증과 처리 상태 |
| GET | `/api/quotes` | 조직의 접수 견적 목록 |
| GET | `/api/audit` | 최근 업무 감사 이벤트 |
| GET | `/api/support/bundle` | 지원 담당용 진단 번들 |

권한은 [구조 문서](ARCHITECTURE.md)의 표를 참조하세요. 모든 목록은 실습용 상한(100~200건)이 있으며 페이지네이션은 제공하지 않습니다.

## 입력 예시

로그인:
```json
{"email":"buyer@hanul.example","password":"Buyer-lab-2026!"}
```

거래처 등록:
```json
{"name":"온유 사무용품","contactEmail":"sales@example.test","category":"OFFICE"}
```

발주 생성: `supplierId`에는 거래처 목록의 UUID를 입력합니다.
```json
{"supplierId":"<거래처 UUID>","title":"개발팀 사무용품 구매","department":"Engineering","amount":250000}
```

보고서 저장:
```json
{"name":"부서별 청구 합계","dimension":"department"}
```

공개 견적 접수: 입찰 목록에서 `public_code`를 확인합니다.
```json
{"supplierName":"온유 사무용품","contactEmail":"sales@example.test","total":180000,"preferences":{"locale":"ko-KR"}}
```

견적 응답의 `receipt`로 접수증을 조회합니다. 즉시 조회하면 `PENDING/PROCESSING`일 수 있고, 후속 조회에서 `READY`와 생성된 표시 문구를 확인할 수 있습니다.

## 응답 오류

| 상태 | 의미 |
|---|---|
| 400 | 입력 유효성 또는 업무 상태 오류 |
| 401 | 인증 누락·만료·잘못된 로그인 |
| 403 | 역할 또는 승인 정책 위반 |
| 404 | 자원이 없거나 접근 범위 밖 |
| 500 | 처리 실패; 클라이언트에는 내부 스택을 반환하지 않음 |
