FROM gradle:jdk17-alpine as builder
WORKDIR /app
COPY ./ ./
RUN gradle clean build --no-daemon -x test

FROM openjdk:17-jdk-alpine
WORKDIR /app
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /app/${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/app.jar"]