FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .

RUN sh ./mvnw -B clean package -DskipTests


FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

USER 10001:10001

EXPOSE 8080


ENTRYPOINT ["java","-jar","app.jar"]
