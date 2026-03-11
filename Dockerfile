# Build stage
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app
COPY nutra/pom.xml .
COPY nutra/.mvn .mvn
COPY nutra/mvnw .
COPY nutra/mvnw.cmd .
COPY nutra/src src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8079
ENTRYPOINT ["java", "-jar", "app.jar"]
