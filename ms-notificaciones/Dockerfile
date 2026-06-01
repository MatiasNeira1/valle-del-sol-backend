# Usamos una imagen ligera de Java 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el archivo JAR generado
COPY target/*.jar app.jar

# Exponemos el puerto del microservicio
EXPOSE 8083

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
