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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.prueba.microservicios.app.foto.models.entity.Foto;
import com.prueba.microservicios.app.foto.services.FotoService;

import io.swagger.annotations.ApiOperation;


@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, 
		RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/fotos")
public class FotoController {
	
	@Autowired
	private FotoService service;
	
	@PostMapping
	@ApiOperation(value = "Crear foto.", notes = "Registra una foto en la base de datos.")
	public ResponseEntity<?> crear(@RequestBody Foto foto){
		return ResponseEntity.ok().body(service.save(foto));
	}
	
	@GetMapping
	@ApiOperation(value = "Listar todas las fotos.", notes = "Lista todas las fotos registradas en la base de datos.")
	public ResponseEntity<?> obtenerFotos(){
		Iterable<Foto> fotos = service.findAll();
		return ResponseEntity.ok().body(fotos);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Listar foto por Id.", notes = "Lista la foto correspondiente al Id registrado en la base de datos.")
	public ResponseEntity<?> obtenerFotoPorId(@PathVariable String id){

		Optional<Foto> foto = service.findFotoById(id);
		return ResponseEntity.ok().body(foto.get());
	}
	
	@PutMapping
	@ApiOperation(value = "Actualizar foto.", notes = "Actualiza la foto registrada en la base de datos.")
	public ResponseEntity<?> actualizar(@RequestBody Foto foto){	
		Foto fotoActualizada = service.update(foto);
		return ResponseEntity.status(HttpStatus.CREATED).body(fotoActualizada);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Elimina foto por Id.", notes = "Elimina la foto registrada en la base de datos.")
	public ResponseEntity<?> eliminar(@PathVariable String id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/lista-por-ids")
	@ApiOperation(value = "Listar las fotos por un listado de Id.", notes = "Lista las fotos registradas correspondientes al listado de Ids enviado.")
	public ResponseEntity<?> obtenerFotosPorListaIds(@RequestParam List<String> fotosId){
		Iterable<Foto> fotos = service.findAllByIds(fotosId);
		return ResponseEntity.ok().body(fotos);
	}
}
