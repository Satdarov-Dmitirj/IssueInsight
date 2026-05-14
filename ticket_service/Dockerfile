FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY ticket_service/pom.xml ticket_service/
COPY ticket_service/src ticket_service/src
COPY emailmodule/pom.xml emailmodule/
COPY emailmodule/src emailmodule/src

RUN mvn clean package -pl ticket_service -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/ticket_service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]