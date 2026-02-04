#stage 1 Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Cache dependencies
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN mvn -B dependency:go-offline

# Copy source
COPY src ./src

# Build JAR
RUN mvn clean package -DskipTests



# Stage 2: Runtime (Java 21 JRE)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Security: non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR
COPY --from=builder /app/target/*.jar app.jar

# Expose app port
EXPOSE 5000

# JVM container tuning
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

# Run app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
