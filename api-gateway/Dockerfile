FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
# Aquí exponemos el puerto 8080 hacia la red de Docker
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]