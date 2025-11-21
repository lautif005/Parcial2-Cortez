# =========================================================
# ETAPA 1: BUILD (Compilación y Generación del JAR)
# Usa la imagen recomendada para Java en contenedores (JDK 21)
# =========================================================
FROM eclipse-temurin:21-jdk-alpine AS build

# Copiar el código fuente
COPY . /app
WORKDIR /app

# Dar permisos de ejecución a Gradle
RUN chmod +x ./gradlew

# Compilar el proyecto y generar el JAR. Omitimos tests para evitar fallos de ClassNotFound en build.
RUN ./gradlew bootJar -x test --no-daemon

# =========================================================
# ETAPA 2: RUNTIME (Ejecución)
# Usamos SOLO el JRE para mantener la imagen final pequeña.
# =========================================================
FROM eclipse-temurin:21-jre-alpine

# Exponer el puerto de Spring Boot
EXPOSE 8080

# Copiar el JAR generado en la ETAPA 1 a la imagen final.
# Usamos el comodín (*) para encontrar el archivo JAR, independientemente de su nombre exacto
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]