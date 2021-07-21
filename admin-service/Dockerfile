FROM openjdk:16
VOLUME /tmp
EXPOSE 8086
ADD ./target/Admin-service-0.0.1-SNAPSHOT.jar Admin-service.jar
ENTRYPOINT ["java","-jar","/Admin-service.jar"]