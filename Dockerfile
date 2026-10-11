# =====================================================================
# Dockerfile - Sistema Bancario (Spring Boot)
# Build multi-stage: compila con Maven y ejecuta solo el JAR
# =====================================================================

# ---------- Etapa 1: BUILD ----------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar primero el pom.xml para aprovechar el cache de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Etapa 2: RUNTIME ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar solo el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]