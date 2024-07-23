# MemberController API 문서

## 개요
`MemberController`는 회원 등록, 로그인, 회원 정보 수정, 회원 탈퇴, 사용자 정보 조회를 위한 엔드포인트를 제공합니다. 이 API는 HTTP 메서드를 사용하여 회원 데이터와 상호작용합니다.

## 기본 URL
/api/member

markdown
코드 복사

## 엔드포인트

### 1. 회원 등록
- **URL:** `/api/member`
- **Method:** `POST`
- **요청 본문:**
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string"
  }
응답:
상태 코드: 200 OK
본문:
json
{
"username": "string",
"email": "string"
}
설명: 제공된 세부 정보로 새로운 회원을 등록합니다.
2. 로그인
   URL: /api/member/login
   Method: POST
   요청 본문:
   json
   {
   "username": "string",
   "password": "string"
   }
   응답:
   상태 코드: 200 OK
   본문:
   json
   {
   "username": "string",
   "email": "string"
   }
   상태 코드: 403 FORBIDDEN
   본문:
   json
   "이미 로그인되어 있습니다"
   상태 코드: 401 UNAUTHORIZED
   본문:
   json
   "유효하지 않은 사용자 이름 또는 비밀번호"
   설명: 회원을 로그인시킵니다. 이미 로그인된 경우나 자격 증명이 유효하지 않은 경우 에러를 반환합니다.
3. 회원 정보 수정
   URL: /api/member
   Method: PUT
   요청 본문:
   json
   {
   "username": "string",
   "password": "string",
   }
   응답:
   상태 코드: 200 OK
   본문:
   json
   {
   "username": "string",
   "email": "string"
   }
   상태 코드: 401 UNAUTHORIZED
   본문:
   json
   "로그인되어 있지 않습니다"
   설명: 회원 정보를 업데이트합니다. 사용자가 로그인되어 있어야 합니다.
4. 회원 탈퇴
   URL: /api/member
   Method: DELETE
   응답:
   상태 코드: 204 NO CONTENT
   설명: 회원의 가입을 철회합니다. 사용자가 로그인되어 있어야 합니다.
   상태 코드: 401 UNAUTHORIZED
   본문:
   json
   "로그인되어 있지 않습니다"
5. 사용자 정보 조회
   URL: /api/member/info
   Method: GET
   응답:
   상태 코드: 200 OK
   본문:
   json
   {
   "username": "string",
   "email": "string"
   }
   상태 코드: 401 UNAUTHORIZED
   본문:
   json
   "로그인되어 있지 않습니다"
   설명: 로그인된 회원의 정보를 조회합니다.
   참고 사항
   로그인 요구 엔드포인트는 "loginUser" 속성을 HttpSession에서 확인합니다.
   클라이언트 측 코드에서 세션 관리를 적절히 처리해야 합니다.