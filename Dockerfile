FROM maven:3.9.11-eclipse-temurin-25-alpine AS build
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-alpine AS central-api
WORKDIR /app

COPY --from=build /build/app/app-central-api/target/club.ss220.app-central-api.jar app.jar

EXPOSE 8000
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]

FROM eclipse-temurin:25-alpine AS manager
WORKDIR /app

COPY --from=build /build/app/app-manager-discord/target/club.ss220.app-manager-discord.jar app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
