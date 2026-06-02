FROM openjdk:17-ea-jdk-alpine3.14

WORKDIR /app

COPY . .

CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
