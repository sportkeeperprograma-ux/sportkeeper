# 1) Build
FROM gradle:8.8-jdk17 AS builder
WORKDIR /app
COPY . /app
RUN gradle bootJar --no-daemon

# 2) Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
