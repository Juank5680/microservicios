package com.prueba.microservicios.app.clientes.advice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {

	private String nombre;
	
	private String descripcion;
	 
	private Integer codigo;
}
