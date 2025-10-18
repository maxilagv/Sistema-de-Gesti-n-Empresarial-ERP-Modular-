FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar (build with: mvn -DskipTests package)
ARG JAR_FILE=target/erp-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

