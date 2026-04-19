FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S debuglog && adduser -S debuglog -G debuglog
COPY --from=builder /app/target/*.jar app.jar
RUN chown debuglog:debuglog app.jar
USER debuglog
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]