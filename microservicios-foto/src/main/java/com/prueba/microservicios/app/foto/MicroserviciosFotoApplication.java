package com.prueba.microservicios.app.foto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MicroserviciosFotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosFotoApplication.class, args);
	}

}
