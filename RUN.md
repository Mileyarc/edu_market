# Hướng dẫn chạy ứng dụng EduMarket

## Yêu cầu:
1. Java JDK 17 đã được cài đặt
2. MySQL đang chạy
3. Database `edumarket` đã được tạo

## Cách chạy:

### Cách 1: Sử dụng Maven Wrapper (Khuyến nghị)
```bash
# Windows PowerShell
.\mvnw.cmd spring-boot:run

# Hoặc nếu đã có Maven cài đặt
mvn spring-boot:run
```

### Cách 2: Build và chạy JAR
```bash
# Build project
.\mvnw.cmd clean package

# Chạy JAR file
java -jar target\edumarket-0.0.1-SNAPSHOT.jar
```

### Cách 3: Sử dụng Cursor/VS Code Extensions
1. Cài Extension Pack for Java
2. Mở file `EdumarketApplication.java`
3. Click vào nút "Run" hoặc nhấn F5

## Kiểm tra:
- Ứng dụng sẽ chạy tại: http://localhost:8080
- Admin login: admin@edumarket.com / Admin@123
