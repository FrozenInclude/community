# Member API 문서

## 개요
`Member API`는 회원 등록, 로그인, 회원 정보 수정, 회원 탈퇴, 사용자 정보 조회를 위한 엔드포인트를 제공합니다. 이 API는 HTTP 메서드를 사용하여 회원 데이터와 상호작용합니다.

## 기본 URL
`/api/member`

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
  ```
- **응답:**
   - **상태 코드:** `200 OK`
   - **본문:**
     ```json
     {
       "username": "string",
       "email": "string"
     }
     ```
     - **상태 코드:** `400 Bad Request`
     - **본문:**
       이메일 중복이 발생한 경우
       ```json
       {
         "email": "이미 가입된 이메일 입니다."
       }
       ```
       
       유효성 검사에 실패한경우
       ```json
       {
         "username": "이름을 입력해주세요.",
         "username": "이름은 2자 이상, 30자 이하로 입력해주세요.",
         "email": "올바른 이메일 주소를 입력해주세요.",
         "password": "비밀번호는 최소 8자 이상이어야 합니다.",
         "password": "비밀번호에는 최소 하나의 대문자가 포함되어야 합니다.",
         "password": "비밀번호에는 최소 하나의 소문자가 포함되어야 합니다.",
         "password": "비밀번호에는 최소 하나의 숫자가 포함되어야 합니다.",
         "password": "비밀번호에는 최소 하나의 특수 문자가 포함되어야 합니다."
       }
       ```
- **설명:** 제공된 세부 정보로 새로운 회원을 등록합니다.

### 2. 로그인
- **URL:** `/api/member/login`
- **Method:** `POST`
- **요청 본문:**
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **응답:**
   - **상태 코드:** `200 OK`
   - **본문:**
     ```json
     {
       "username": "string",
       "email": "string"
     }
     ```
   - **상태 코드:** `401 UNAUTHORIZED`
   - **본문:**
     ```json
     "유효하지 않은 이메일 또는 비밀번호"
     ```
- **설명:** 회원을 로그인시킵니다. 이미 로그인된 경우나 자격 증명이 유효하지 않은 경우 에러를 반환합니다.

### 3. 회원 정보 수정
- **URL:** `/api/member`
- **Method:** `PUT`
- **요청 본문:**
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **응답:**
   - **상태 코드:** `200 OK`
   - **본문:**
     ```json
     {
       "username": "string",
       "email": "string"
     }
     ```
   - **상태 코드:** `401 UNAUTHORIZED`
   - **본문:**
     ```json
     "로그인되어 있지 않습니다"
     ```
- **설명:** 회원 정보를 업데이트합니다. 사용자가 로그인되어 있어야 합니다.

### 4. 회원 탈퇴
- **URL:** `/api/member`
- **Method:** `DELETE`
- **응답:**
   - **상태 코드:** `204 NO CONTENT`
   - **설명:** 회원의 가입을 철회합니다. 사용자가 로그인되어 있어야 합니다.
   - **상태 코드:** `401 UNAUTHORIZED`
   - **본문:**
     ```json
     "로그인되어 있지 않습니다"
     ```

### 5. 사용자 정보 조회
- **URL:** `/api/member`
- **Method:** `GET`
- **응답:**
   - **상태 코드:** `200 OK`
   - **본문:**
     ```json
     {
       "id": "number",
       "username": "string",
       "email": "string",
       "createdAt": "string"
     }
     ```
   - **상태 코드:** `401 UNAUTHORIZED`
   - **본문:**
     ```json
     "로그인되어 있지 않습니다"
     ```
- **설명:** 로그인된 회원의 정보를 조회합니다.

### 6. 게시물 조회
- **URL:** `/api/member/articles`
- **Method:** GET
- **응답:**
    - **상태 코드:** 200 OK
    - **본문:**
      ```json
      [
        {
          "id": "number",
          "title": "string",
          "content": "string",
          "author": "string",
          "createdAt": "string",
          "updatedAt": "string"
        }
      ]
      ```
    - **상태 코드:** 401 UNAUTHORIZED
    - **본문:**
      ```json
      "로그인되어 있지 않습니다"
      ```
- **설명:** 로그인된 사용자의 게시물 목록을 조회합니다.

### 7. 게시판 조회
- **URL:** `/api/member/boards`
- **Method:** GET
- **응답:**
    - **상태 코드:** 200 OK
    - **본문:**
      ```json
      [
        {
          "id": "number",
          "boardName": "string",
          "description": "string",
          "author": "string",
          "createdAt": "string",
          "updatedAt": "string",
          "articles": [
            {
              "id": "number",
              "title": "string",
              "content": "string",
              "author": "string",
              "createdAt": "string",
              "updatedAt": "string"
            }
          ]
        }
      ]
      ```
    - **상태 코드:** 401 UNAUTHORIZED
    - **본문:**
      ```json
      "로그인되어 있지 않습니다"
      ```
- **설명:** 로그인된 사용자가 소속된 게시판 목록을 조회합니다.

## 참고 사항
- 로그인 요구 엔드포인트는 "loginUser" 속성을 `HttpSession`에서 확인합니다.
- 클라이언트 측 코드에서 세션 관리를 적절히 처리해야 합니다.