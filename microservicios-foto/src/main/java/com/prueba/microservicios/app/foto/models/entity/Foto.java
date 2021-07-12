package com.prueba.microservicios.app.foto.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "fotos")
@Data @Builder
public class Foto {
	
	@Id
	private String id;
	
	private String datosBase64;	
}
