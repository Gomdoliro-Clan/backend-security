FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src src
RUN mvn -q verify

FROM eclipse-temurin:17-jre-jammy
RUN groupadd --system procure && useradd --system --gid procure --no-create-home procure
WORKDIR /app
COPY --from=build /build/target/procureflow-1.0.0.jar app.jar
USER procure
EXPOSE 18080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=65", "-jar", "/app/app.jar"]
