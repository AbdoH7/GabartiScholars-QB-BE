FROM openjdk:21-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy pom.xml first for caching
COPY pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the app
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Run with Maven for auto-reloading (not JAR)
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-Dspring.profiles.active=dev"]
