package com.prueba.microservicios.app.clientes.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.prueba.microservicios.app.foto.models.entity.*;

@FeignClient(name = "microservicio-foto", fallback = FotoHystrixFallback.class)
//@RequestMapping("/fotos")
public interface FotoFeign {
	
	static final String PREFIJO_URL = "/fotos";

	@GetMapping(PREFIJO_URL + "/{id}")
	public ResponseEntity<Foto> obtenerFotoPorId(@PathVariable String id);
	
	@PostMapping(PREFIJO_URL)
	public ResponseEntity<Foto> crear(@RequestBody Foto foto);
	
	@PutMapping(PREFIJO_URL)
	public ResponseEntity<Foto> actualizar(@RequestBody Foto foto);
	
	@DeleteMapping(PREFIJO_URL + "/{id}")
	public ResponseEntity<Foto> eliminar(@PathVariable String id);
	
	@GetMapping(PREFIJO_URL + "/lista-por-ids")
	public ResponseEntity<Iterable<Foto>> obtenerFotosPorListaIds(@RequestParam List<String> fotosId);

}