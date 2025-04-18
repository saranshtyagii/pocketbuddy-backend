# Use an OpenJDK 21 image for Java 21 (adjust if you use a different version)
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper and source code
COPY . .

# Give execution permission to mvnw (Unix-based OS)
RUN chmod +x ./mvnw

# Build the application (skip tests for faster build)
RUN ./mvnw -B -DskipTests clean install

# Run the jar file (make sure the jar name matches your actual output)
CMD ["java", "-jar", "target/Pocket-Buddy-0.0.1-SNAPSHOT.jar"]

# Expose the port your Spring Boot app will run on (usually 8080)
EXPOSE 8080
