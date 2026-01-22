# Hướng dẫn chạy ứng dụng EduMarket

## ⚠️ QUAN TRỌNG: Phải vào đúng thư mục trước!

Bạn **PHẢI** vào thư mục `edumarket` trước khi chạy lệnh.

## Các bước chạy ứng dụng:

### Bước 1: Mở Terminal trong Cursor
- Nhấn `` Ctrl+` `` (Ctrl + dấu backtick) để mở Terminal
- Hoặc vào menu: Terminal → New Terminal

### Bước 2: Di chuyển vào thư mục edumarket
```powershell
cd "C:\Users\admin\Downloads\Apps\edumarket search Project 3\edumarket"
```

### Bước 3: Chạy ứng dụng
```powershell
.\mvnw.cmd spring-boot:run
```

## Lệnh đầy đủ (copy/paste vào terminal):

```powershell
cd "C:\Users\admin\Downloads\Apps\edumarket search Project 3\edumarket"; .\mvnw.cmd spring-boot:run
```

## Kiểm tra đã vào đúng thư mục:

Sau khi `cd`, bạn sẽ thấy prompt như này:
```
PS C:\Users\admin\Downloads\Apps\edumarket search Project 3\edumarket>
```

Nếu thấy `edumarket>` ở cuối prompt = ĐÚNG ✅

## Các lệnh khác hữu ích:

### Dừng ứng dụng:
- Nhấn `Ctrl+C` trong terminal

### Build project (không chạy):
```powershell
.\mvnw.cmd clean package
```

### Chạy JAR file (sau khi build):
```powershell
java -jar target\edumarket-0.0.1-SNAPSHOT.jar
```

### Kiểm tra Java version:
```powershell
java -version
```

## Sau khi ứng dụng chạy:

1. Đợi 30-60 giây để ứng dụng khởi động
2. Tìm dòng: `Started EdumarketApplication`
3. Mở trình duyệt: http://localhost:8080

## Thông tin đăng nhập Admin:
- Email: `admin@edumarket.com`
- Password: `Admin@123`

## Nếu vẫn lỗi:

1. Kiểm tra đã vào đúng thư mục chưa
2. Kiểm tra file `mvnw.cmd` có tồn tại không: `dir mvnw.cmd`
3. Kiểm tra Java đã cài: `java -version`
4. Kiểm tra MySQL đang chạy
