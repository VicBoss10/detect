# Etapa de compilación
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el código fuente y compila
COPY src ./src
RUN mvn package -DskipTests

# Etapa final
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/detec-0.0.1-SNAPSHOT.jar java-app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "java-app.jar"]
