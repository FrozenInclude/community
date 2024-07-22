## API Documentation
| Method | Endpoint       | Description           | Parameters          | Headers                        | Body                                              |
|--------|----------------|-----------------------|---------------------|--------------------------------|---------------------------------------------------|
| GET    | /member       | 회원 목록을 가져옵니다.         | -                   | -                              | -                                                 |
| GET    | /member/{id}  | 특정 회원의 세부 정보를 가져옵니다.  | `id` (path): 회원 ID  | -                              | -                                                 |
| DELETE | /member/{id}  | 특정 회원을 삭제합니다.         | `id` (path): 회원 ID  | -                              | -                                                 |
| POST   | /member       | 새 회원을 생성합니다.          | -                   | Content-Type: application/json | `{ "username": "", "email": "", "password": "" }` |
| PUT    | /member/{id}  | 기존 회원을 업데이트합니다.       | `id` (path): 회원 ID  | Content-Type: application/json | `{ "username": "", "email": "" }`                 |
