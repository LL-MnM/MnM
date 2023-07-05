# Build Stage
FROM gradle:jdk17-alpine as builder
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src/ ./src/
RUN gradle clean build --no-daemon -x test

# Run Stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
ARG JAR_FILE=/app/build/libs/*.jar
COPY --from=builder /${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/app.jar"]
