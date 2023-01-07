FROM maven:3.6.3-jdk-11 AS build-project
ADD . ./docker-spring-boot
WORKDIR /docker-spring-boot
RUN mvn clean install


FROM openjdk:11.0.6-jre
EXPOSE 8080

ENV TZ=UTC
ENV DB_IP=ec2-63-32-248-14.eu-west-1.compute.amazonaws.com
ENV DB_PORT=5432
ENV DB_USER=elhibdeidgrfre
ENV DB_PASSWORD=elhibdeidgrfre
ENV DB_DBNAME=d942bqtpg0fsvf

COPY --from=build-project /docker-spring-boot/target/ds-2020-0.0.1-SNAPSHOT.jar ./docker-spring-boot.jar
CMD ["java", "-jar", "docker-spring-boot.jar"]
