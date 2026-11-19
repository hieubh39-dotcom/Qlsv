# Phần mềm Quản lý sinh viên

Đồ án Java Swing kết nối MySQL, gồm 4 phân hệ:
- Quản lý sinh viên
- Quản lý giảng viên
- Quản lý môn học
- Quản lý điểm

## Công nghệ
- JDK 17
- Java Swing
- MySQL 8
- JDBC (MySQL Connector/J 9.3.0)
- Maven
- NetBeans

## 1. Tạo cơ sở dữ liệu
1. Mở XAMPP Control Panel hoặc MySQL Workbench.
2. Khởi động MySQL.
3. Mở phpMyAdmin hoặc MySQL Workbench.
4. Chạy toàn bộ nội dung file `database/quan_ly_sinh_vien.sql`.
5. Kiểm tra đã có database `quan_ly_sinh_vien` và 4 bảng.

## 2. Cấu hình tài khoản MySQL
Mặc định chương trình dùng:
- User: `root`
- Password: rỗng

Nếu máy bạn có mật khẩu khác, đặt biến môi trường:
- `DB_USER`: tên đăng nhập MySQL
- `DB_PASSWORD`: mật khẩu MySQL

Hoặc sửa hai giá trị mặc định trong `DBConnection.java`.

## 3. Mở bằng NetBeans
1. Giải nén project.
2. Mở NetBeans.
3. Chọn File > Open Project.
4. Chọn thư mục `QuanLySinhVienMySQL` có file `pom.xml`.
5. Chờ Maven tải thư viện MySQL Connector/J.
6. Mở `src/main/java/qlsv/Main.java`.
7. Nhấn Run Project (F6).

## 4. Cách sử dụng
- Chọn tab Sinh viên, Giảng viên, Môn học hoặc Điểm.
- Nhập thông tin rồi nhấn Thêm.
- Chọn một dòng trong bảng để đưa dữ liệu lên ô nhập.
- Sửa thông tin rồi nhấn Sửa.
- Chọn dòng và nhấn Xóa để xóa.
- Nhập từ khóa rồi nhấn Tìm.
- Nhấn Hiển thị tất cả để tải lại danh sách.

### Lưu ý nhập liệu
- Ngày sinh: `yyyy-MM-dd`, ví dụ `2005-03-12`.
- Điểm: từ 0 đến 10, dùng dấu chấm, ví dụ `8.5`.
- Mã sinh viên và mã môn học trong bảng Điểm phải tồn tại trước.
- Mã giảng viên trong Môn học phải tồn tại hoặc để trống.
- Khi thêm điểm, để trống mã điểm để MySQL tự tăng.

## 5. Đưa lên GitHub
1. Tạo repository mới trên GitHub, ví dụ `QuanLySinhVienJava`.
2. Không tải mật khẩu MySQL lên GitHub.
3. Mở Terminal tại thư mục project và chạy:

```bash
git init
git add .
git commit -m "Initial project Quan Ly Sinh Vien"
git branch -M main
git remote add origin https://github.com/USERNAME/QuanLySinhVienJava.git
git push -u origin main
```

Thay `USERNAME` bằng tên tài khoản GitHub của bạn.
Sau khi push thành công, sao chép link repository để nộp.

## Ghi chú
Đây là project nền tảng có CRUD và tìm kiếm trên cả 4 bảng. Trường khóa ngoại đang nhập bằng mã (ví dụ SV01, MH01, GV01); có thể nâng cấp thành JComboBox sau khi chạy ổn định.
