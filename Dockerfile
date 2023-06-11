FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-17-jdk

VOLUME /tmp
COPY /target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# FROM ubuntu:latest AS build
# COPY src /home/app/src
# COPY pom.xml /home/app
# RUN mvn -f /home/app/pom.xml clean package
