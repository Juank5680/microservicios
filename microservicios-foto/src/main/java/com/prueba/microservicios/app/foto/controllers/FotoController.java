package com.prueba.microservicios.app.foto.controllers;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.prueba.microservicios.app.foto.models.entity.Foto;
import com.prueba.microservicios.app.foto.services.FotoService;


@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, 
		RequestMethod.PUT, RequestMethod.DELETE})
public class FotoController {
	
	@Autowired
	private FotoService service;
	
	@PostMapping
	public ResponseEntity<?> crear(@RequestBody Foto foto){
		return ResponseEntity.ok().body(service.save(foto));
	}
	
	@GetMapping
	public ResponseEntity<?> obtenerFotos(){
		Iterable<Foto> fotos = service.findAll();
		return ResponseEntity.ok().body(fotos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerFotoPorId(@PathVariable String id){

		Optional<Foto> foto = service.findFotoById(id);
		return ResponseEntity.ok().body(foto.get());
	}
	
	@PutMapping
	public ResponseEntity<?> actualizar(@RequestBody Foto foto){	
		Foto fotoActualizada = service.update(foto);
		return ResponseEntity.status(HttpStatus.CREATED).body(fotoActualizada);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable String id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/lista-por-ids")
	public ResponseEntity<?> obtenerFotosPorListaIds(@RequestParam List<String> fotosId){
		Iterable<Foto> fotos = service.findAllByIds(fotosId);
		return ResponseEntity.ok().body(fotos);
	}
}