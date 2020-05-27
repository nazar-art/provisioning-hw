# For Java 8, try this
#FROM openjdk:8-jdk-alpine
FROM adoptopenjdk/openjdk11:alpine-jre

# For Java 11, try this
#FROM adoptopenjdk/openjdk11:alpine-jre

# Refer to Maven build -> finalName
ARG JAR_FILE=target/provisioning.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080