FROM openjdk:16
VOLUME /tmp
EXPOSE 8001
ADD ./target/microservicios-foto-0.0.1-SNAPSHOT.jar microservicios-foto.jar
ENTRYPOINT ["java","-jar","/microservicios-foto.jar"]