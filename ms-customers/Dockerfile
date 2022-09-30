FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ms-customers.jar
ENTRYPOINT ["java","-jar","/ms-customers.jar"]