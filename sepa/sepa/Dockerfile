FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/sepa-0.0.1.jar sepa.jar
EXPOSE 80
ENTRYPOINT ["java","-jar", "sepa.jar"]
