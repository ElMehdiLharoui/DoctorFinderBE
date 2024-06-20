# Use a Maven image to build the WAR file
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package
RUN chmod 777 /app/target/DOCTER_FINDER-1.0-SNAPSHOT-jar.jar

CMD ["java", "-jar", "/app/target/DOCTER_FINDER-1.0-SNAPSHOT-jar.jar"]


