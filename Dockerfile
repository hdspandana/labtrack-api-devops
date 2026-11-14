FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/labtrack-api.jar /app/labtrack-api.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD wget -qO- http://localhost:8080/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/labtrack-api.jar"]
