⚙️ Municipalidad Valle del Sol - Backend
Este repositorio contiene la arquitectura de microservicios diseñada para la gestión de emergencias forestales y urbanas. El sistema utiliza Spring Boot y Java para garantizar escalabilidad y robustez.

🏗️ Arquitectura de Microservicios
El proyecto está estructurado como un proyecto Maven multimodular:

api-gateway: Punto de entrada único que gestiona el enrutamiento y la seguridad de las peticiones.
ms-alerta-incendios: Microservicio encargado de la creación, gestión y persistencia de reportes de incendios.
ms-monitoreo-geo: Encargado del procesamiento de coordenadas y visualización geográfica.
ms-notificaciones: Servicio dedicado al envío de alertas a la comunidad por diversos canales oficiales.

🛠️ Tecnologías y Persistencia
Framework: Spring Boot 3 con Maven.
Persistencia: Oracle Database para el manejo de datos críticos y multimedia.  
Contenedores: Configuración de Docker y Docker Compose para orquestar los servicios localmente.

🚀 Instrucciones de Ejecución

1. Requisitos Previos
* Java 17, Maven y Oracle Database operativo.

2. Orden de Encendido (Crítico) Para garantizar el correcto registro de los servicios y la conectividad, se debe seguir este orden:
    1.  **Base de Datos (Oracle):** Debe estar activa para permitir las conexiones de persistencia.
    2.  **API Gateway:** Punto central de enrutamiento que debe estar listo para recibir tráfico.
    3.  **Microservicios:** Encender "ms-alerta-incendios", "ms-monitoreo-geo" y "ms-notificaciones" una vez que el Gateway esté operativo.

3. Compilación del Proyecto
Desde la raíz del proyecto, ejecuta:
Bash
./mvnw clean install

4. Ejecución de Microservicios
Puedes levantar un servicio específico utilizando el comando de Maven:
Bash
./mvnw.cmd spring-boot:run -pl (NOMBRE_MICROSERVICIO)

5. Orquestación con Docker (Opcional)
Si deseas levantar todo el ecosistema de microservicios:
Bash
docker-compose up --build
