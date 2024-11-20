# Usa una imagen base para Java
FROM openjdk:17-jdk-alpine

# Configura el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el jar generado al contenedor
COPY target/microservicio-cliente-0.0.1-SNAPSHOT.jar app.jar


# Exponer el puerto en el que el microservicio escucha
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
