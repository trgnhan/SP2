FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} sp2.jar

ENTRYPOINT ["java", "-jar", "sp2.jar"]

EXPOSE 8080