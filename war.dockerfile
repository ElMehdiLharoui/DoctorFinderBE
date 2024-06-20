# Use a Maven image to build the WAR file
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM tomcat:9.0.55-jdk17-openjdk

RUN rm -rf /usr/local/tomcat/webapps

COPY --from=build /app/target/DOCTER_FINDER-1.0-SNAPSHOT-war.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]

