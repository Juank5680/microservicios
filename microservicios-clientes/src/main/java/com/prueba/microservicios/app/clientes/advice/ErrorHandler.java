package com.prueba.microservicios.app.clientes.advice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;


@ControllerAdvice
public class ErrorHandler {
	
	private static final ConcurrentHashMap<String, Integer> STATUS_CODES = new ConcurrentHashMap<>();
	

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValid (BindingResult result){
		
		Map<String, Object> errores = new HashMap<>();
		result.getFieldErrors().forEach(err -> {
			errores.put(err.getField(), err.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(errores);
	}	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleException (Exception exception){	
		
		String nombre = exception.getClass().getSimpleName();
		String descripcion = exception.getMessage();
		Integer codigo = STATUS_CODES.get(exception.getClass().getSimpleName());
		
		if(codigo == null) {
			codigo = HttpStatus.INTERNAL_SERVER_ERROR.value();
		}
		
		ErrorMessage error = ErrorMessage.builder().nombre(nombre).descripcion(descripcion).codigo(codigo).build();
		return new ResponseEntity<>(error, HttpStatus.valueOf(codigo));
	}
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<?> handleResponseStatus (ResponseStatusException responseStatusException){	
		return new ResponseEntity<String>(responseStatusException.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
}
