FROM eclipse-temurin:22-alpine

COPY target/match-api-gateway-0.0.1.jar app.jar
EXPOSE 9000

ENV MATCH_API_URI=http://host.docker.internal:8088
ENTRYPOINT ["java", "-jar", "/app.jar"]
