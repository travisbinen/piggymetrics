FROM eclipse-temurin:17-jre-alpine

ADD ./target/config.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/config.jar"]

HEALTHCHECK --interval=30s --timeout=30s CMD wget --spider -q http://localhost:8888/actuator/health || exit 1

EXPOSE 8888
