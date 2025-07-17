FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/rinha-router-1.0.0.jar app.jar

EXPOSE 9999

ENTRYPOINT ["java","-jar","-Dserver.port=9999","app.jar"]

