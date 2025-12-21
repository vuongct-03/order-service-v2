# Bước 1: Sử dụng image base OpenJDK (Java 17)
# FROM openjdk:17-jdk-slim AS build
FROM eclipse-temurin:17-jdk-jammy AS build

# Bước 2: Cài đặt Maven
RUN apt-get update && apt-get install -y maven

# Bước 3: Thiết lập thư mục làm việc trong container
WORKDIR /app

# Bước 4: Sao chép file pom.xml để tải các phụ thuộc
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Bước 5: Sao chép mã nguồn vào container
COPY src /app/src

# Bước 6: Build ứng dụng (để tạo file JAR)
RUN mvn clean package -DskipTests

# Bước 7: Chọn image chính thức của OpenJDK để chạy ứng dụng
# FROM openjdk:17-jdk-slim
FROM eclipse-temurin:17-jdk-jammy

# Bước 8: Sao chép file JAR đã build từ image trước vào container
COPY --from=build /app/target/*.jar app.jar

# Bước 9: Thiết lập môi trường để kết nối với MongoDB và Eureka
# Các giá trị này có thể được cấu hình qua biến môi trường hoặc trong `application.properties/application.yml`

ENV EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE=https://eureka-server-v1-1.onrender.com/eureka

ENV MYSQL_URI=jdbc:mysql://order-service-db-huy535137-23c2.f.aivencloud.com:25294/defaultdb?useSSL=true&serverTimezone=UTC&ssl-mode=REQUIRED
ENV JWT_SIGNER_KEY=1TjXchw5FloESb63Kc+DFhTARvpWL4jUGCwfGWxuG5SIf/1y/LgJxHnMqaF6A/ij

# Bước 10: Mở cổng 8080
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8083

# Bước 11: Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]
