# # Dockerfile for api module
# FROM eclipse-temurin:17-jdk-alpine
# WORKDIR /app
# COPY mvnw mvnw.cmd pom.xml ./
# COPY .mvn .mvn
# COPY src src
# RUN ./mvnw clean package -DskipTests
# EXPOSE 8080
# CMD ["java", "-jar", "target/*.jar"]

# FROM eclipse-temurin:17-jre
# WORKDIR /app
# COPY target/*.jar /app/app.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","/app/app.jar"]

# ---------- build stage ----------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /workspace

# Copy POMs first to maximize cache
COPY pom.xml .
COPY api/pom.xml api/pom.xml

# Fetch dependencies
RUN mvn -B -q -DskipTests -U dependency:go-offline

# Copy sources needed
COPY api api

# Build only the api module (reactor builds 'common' too)
RUN mvn -B -q -DskipTests -pl api -am package

# ---------- runtime ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/api/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
