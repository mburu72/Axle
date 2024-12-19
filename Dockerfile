FROM openjdk:21-jdk
WORKDIR /haul
COPY target/axle-0.0.1.jar .
LABEL authors="mburu"

ENTRYPOINT ["java", "-jar", "axle-0.0.1.jar"]