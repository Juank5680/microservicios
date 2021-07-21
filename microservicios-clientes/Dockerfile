FROM openjdk:16
VOLUME /tmp
EXPOSE 8002
ADD ./target/microservicios-clientes-0.0.1-SNAPSHOT.jar microservicios-clientes.jar
ENTRYPOINT ["java","-jar","/microservicios-clientes.jar"]