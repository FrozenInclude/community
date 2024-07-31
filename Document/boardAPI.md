# Board API 문서

## 개요
`board API`는 게시판의 생성, 수정, 삭제, 조회를 위한 엔드포인트를 제공합니다. 이 API는 HTTP 메서드를 사용하여 게시판 데이터와 상호작용합니다.


## 기본 URL
`/api/board`

## 엔드포인트

### 1. 게시판 상세 조회
- **URL:** `/api/board/{id}`
- **Method:** GET
- **URL 매개변수:**
    - `id` (Long) : 게시판의 ID
- **응답:**
    - **상태 코드:** 200 OK
    - **본문:**
      ```json
      {
        "id": "number",
        "boardName": "string",
        "description": "string",
        "author": "string",
        "createdAt": "string",
        "updatedAt": "string"
      }
      ```
- **설명:** 주어진 ID로 게시판의 세부 정보를 조회합니다.

### 2. 게시판 목록 조회
- **URL:** `/api/board`
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
          "updatedAt": "string"
        }
      ]
      ```
- **설명:** 모든 게시판의 목록을 조회합니다.

### 3. 게시판의 게시물 조회
- **URL:** `/api/board/{id}/articles`
- **Method:** GET
- **URL 매개변수:**
    - `id` (Long) : 게시판의 ID
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
- **설명:** 주어진 ID로 게시판의 모든 게시물 목록을 조회합니다.

### 4. 게시판의 작성자 조회
- **URL:** `/api/board/{id}/author`
- **Method:** GET
- **URL 매개변수:**
    - `id` (Long) : 게시판의 ID
- **응답:**
    - **상태 코드:** 200 OK
    - **본문:**
      ```json
      {
        "id": "number",
        "username": "string",
        "email": "string"
      }
      ```
- **설명:** 주어진 ID로 게시판의 작성자를 조회합니다.

### 5. 게시판 생성
- **URL:** `/api/board`
- **Method:** POST
- **URL 매개변수:**
  - `id` (Long) : 게시판의 ID
- **요청 본문:**
  ```json
  {
    "boardName": "string",
    "description": "string"
  }
- **응답:**
    - **상태 코드:** 200 OK
    - **본문:**
      ```json
      {
        "id": "number",
        "boardName": "string",
        "description": "string",
        "author": "string",
        "createdAt": "string",
      "updatedAt": "string"
      }
      ```
      - **상태 코드:** 401 UNAUTHORIZED
      - **본문:**
    ```json
     "로그인되어 있지 않습니다"
    ```
- **설명:** 로그인된 사용자가 새로운 게시판을 생성합니다.

### 6. 게시판 수정
- **URL:** `/api/board/{id}`
- **Method:** POST
- **URL 매개변수:**
  - `id` (Long) : 게시판의 ID
- **요청 본문:**
  ```json
  {
    "boardName": "string",
    "description": "string"
  }
- **응답:**
  - **상태 코드:** 200 OK
  - **본문:**
    ```json
    {
      "id": "number",
      "boardName": "string",
      "description": "string",
      "author": "string",
      "createdAt": "string",
    "updatedAt": "string"
    }
    ```
    
    - **상태 코드:** 401 UNAUTHORIZED
    - **본문:**
    ```json
     "로그인되어 있지 않습니다"
    ```
    - **상태 코드:** 403 FORBIDDEN
    - **본문:**
    ```json
     "권한이 없습니다"
    ```
- **설명:** 로그인된 사용자가 게시판 정보를 수정합니다. 게시판의 작성자만 수정할 수 있습니다.

### 7. 게시판 삭제
- **URL:** `/api/board/{id}`
- **Method:** DELETE
- **URL 매개변수:**
  - `id` (Long) : 게시판의 ID
- **요청 본문:**
  ```json
  {
    "boardName": "string",
    "description": "string"
  }
- **응답:**
  - **상태 코드:** 204 NO CONTENT
  - **본문:**
    ```json
    ```
- **설명:** 로그인된 사용자가 게시판 정보를 수정합니다. 게시판의 작성자만 수정할 수 있습니다.

  - **상태 코드:** 401 UNAUTHORIZED
  - **본문:**
  ```json
   "로그인되어 있지 않습니다"
  ```
  - **상태 코드:** 403 FORBIDDEN
  - **본문:**
  ```json
   "권한이 없습니다"
  ```
- **설명:** 로그인된 사용자가 게시판 정보를 수정합니다. 게시판의 작성자만 수정할 수 있습니다.
  
## 참고 사항
- 로그인 요구 엔드포인트는 "loginUser" 속성을 `HttpSession`에서 확인합니다.
- 클라이언트 측 코드에서 세션 관리를 적절히 처리해야 합니다.