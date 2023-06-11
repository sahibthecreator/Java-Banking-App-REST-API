# FROM ubuntu:latest AS build
# RUN apt-get update
# RUN apt-get install openjdk-17-jdk -y
# COPY . /app
# WORKDIR /app
# RUN ./mvnw clean install -U 
# EXPOSE 8080

# ENTRYPOINT ["./mvnw","spring-boot:run"]


FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-17-jdk

VOLUME /tmp
COPY /target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]