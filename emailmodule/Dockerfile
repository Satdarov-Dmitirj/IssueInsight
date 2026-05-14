FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY ticket_service/pom.xml ticket_service/
COPY ticket_service/src ticket_service/src
COPY emailmodule/pom.xml emailmodule/
COPY emailmodule/src emailmodule/src

RUN mvn clean package -pl emailmodule -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/emailmodule/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]