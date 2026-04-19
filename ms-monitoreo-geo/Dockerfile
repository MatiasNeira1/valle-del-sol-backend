# Usamos una imagen de Java 21 ligera
FROM eclipse-temurin:21-jre-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el .jar generado por Maven
COPY target/*.jar app.jar

# Exponemos el puerto del ms-monitoreo-geo
EXPOSE 8082

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]