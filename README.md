# Project Management System

Hệ thống Backend RESTful API quản lý dự án, công việc và thành viên.

---

## Công nghệ sử dụng
- Java 21, Spring Boot
- Spring Security (JWT Stateless)
- MySQL 8.0, Spring Data JPA, Flyway
- Redis (Cache)
- Docker & Docker Compose
- Spring Mail

---

## Tính năng chính
- Xác thực: Đăng ký, đăng nhập với JWT (Access Token, Refresh Token).
- Quản lý dự án: Thêm, sửa, xóa (soft delete), xem danh sách thành viên và công việc. Người tạo dự án là Project Manager.
- Quản lý thành viên: Mời thành viên qua email (gửi mail tự động), xóa thành viên khỏi dự án.
- Quản lý công việc: Tạo task, phân công thành viên, cập nhật trạng thái (TODO, IN_PROGRESS, DONE), phân trang.
- Nộp bài & Tệp đính kèm: Tải lên tài liệu minh chứng (đổi tên UUID), nộp task và lưu lịch sử log.

---

## Cách chạy dự án

### 1. Chạy bằng Docker (Khuyên dùng)
Di chuyển vào thư mục backend và chạy lệnh:
```bash
cd backend
docker compose up --build -d
```
- API: http://localhost:8080
- MySQL: localhost:3307
- Redis: localhost:6379

Dừng dịch vụ:
```bash
docker compose down
```

### 2. Chạy thủ công (Local)
- Cần có sẵn MySQL (database `project_management`) và Redis đang chạy.
- Khởi chạy ứng dụng:
```bash
cd backend
./mvnw spring-boot:run
```
*(Trên Windows dùng: `.\mvnw.cmd spring-boot:run`)*

---

## Danh sách API Endpoints

Lưu ý: Các endpoint (trừ Auth) yêu cầu header `Authorization: Bearer <accessToken>`.

### 1. Authentication
| Method | Endpoint | Mô tả |
|---|---|---|
| POST | /api/auth/register | Đăng ký tài khoản (email, password, name, phoneNumber) |
| POST | /api/auth/login | Đăng nhập lấy JWT Token (email, password) |

### 2. Users
| Method | Endpoint | Mô tả |
|---|---|---|
| GET | /api/users/profile | Xem thông tin tài khoản hiện tại |
| GET | /api/users/{id} | Lấy danh sách dự án của user |

### 3. Projects
| Method | Endpoint | Mô tả |
|---|---|---|
| POST | /api/projects | Tạo dự án (name, description, ownerId) |
| GET | /api/projects/{id} | Xem danh sách task của dự án |
| GET | /api/projects/{id}/members | Xem danh sách thành viên dự án |
| PUT | /api/projects/{id} | Cập nhật dự án (name, description) |
| DELETE | /api/projects/{id} | Xóa mềm dự án |

### 4. Members
| Method | Endpoint | Mô tả |
|---|---|---|
| POST | /api/members | Thêm thành viên vào dự án (projectId, email) |
| DELETE | /api/members/{id} | Xóa thành viên khỏi dự án |

### 5. Tasks
| Method | Endpoint | Mô tả |
|---|---|---|
| POST | /api/tasks | Tạo task (projectId, title, priority, assigneeId, description) |
| POST | /api/tasks/uploadFile | Upload file đính kèm (form-data: file) |
| PUT | /api/tasks | Nộp bài / Hoàn thành task (taskId, id, filePath) |
| GET | /api/tasks/{userId}?page=1&size=10 | Lấy danh sách task của user (phân trang) |

---
