# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jdk-jammy as base
WORKDIR /build
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

# Giai đoạn chạy Test: Build sẽ dừng nếu Test thất bại
FROM base as test
WORKDIR /build
COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw test

# Giai đoạn tải thư viện (Dependencies)
FROM base as deps
WORKDIR /build
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -DskipTests

# Giai đoạn đóng gói file JAR
FROM deps as package
WORKDIR /build
COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/*.jar target/app.jar

# Giai đoạn giải nén để tối ưu hóa Layer
FROM package as extract
WORKDIR /build
RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

# Giai đoạn Producton
FROM extract as development
WORKDIR /build
COPY --from=extract /build/target/extracted/dependencies/. ./
COPY --from=extract /build/target/extracted/spring-boot-loader/. ./
COPY --from=extract /build/target/extracted/snapshot-dependencies/. ./
COPY --from=extract /build/target/extracted/application/. ./
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000"
CMD [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]

# Giai đoạn FINAL (Dành cho chạy thực tế trên Render)
FROM eclipse-temurin:21-jre-jammy AS final
WORKDIR /app

# 1. Sao chép các layer Spring Boot đã được giải nén
COPY --from=extract /build/target/extracted/dependencies/ ./
COPY --from=extract /build/target/extracted/spring-boot-loader/ ./
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract /build/target/extracted/application/ ./

# COPY frontend/ ./frontend/

EXPOSE 8080

# 3. Chạy với profile mặc định (đã tích hợp H2 database, sendgrid, openai)
ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]