FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
## docker buildx build --platform linux/amd64 -t springio/state-machines .
## docker run -d -p 8080:8080 springio/state-machines