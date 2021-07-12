package com.prueba.microservicios.app.clientes.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.prueba.microservicios.app.clientes.models.entity.Cliente;
import com.prueba.microservicios.app.clientes.services.ClienteService;

@RestController
public class ClienteController {

	@Autowired
	private ClienteService service;

	@Value("${config.balanceador.test}")
	private String balanceadorTest;

	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("balanceador", balanceadorTest);
		response.put("clientes", service.findAll());
		return ResponseEntity.ok(response);
	}
	
	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody Cliente cliente){
		Cliente clienteDb = service.save(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteDb);
	}

	@GetMapping
	public ResponseEntity<?> listar() {
		Iterable<Cliente> clientes = service.findAll();
		return ResponseEntity.ok().body(clientes);
	}

	@GetMapping("/listar-por-edad/{edad}")
	public ResponseEntity<?> listarPorEdad(@PathVariable int edad) {
		Iterable<Cliente> clientes = service.findByEdadGreaterThanEqual(edad);
		return ResponseEntity.ok().body(clientes);
	}

	@GetMapping("/{identificacion}")
	public ResponseEntity<Cliente> ver(@PathVariable String identificacion) {
		Optional<Cliente> cliente = service.findByIdentificacion(identificacion);
		return ResponseEntity.ok(cliente.get());
	}

	@PutMapping("/{identificacion}")
	public ResponseEntity<?> editar(@Valid @RequestBody Cliente cliente, @PathVariable String identificacion) {
		
		Cliente clienteActualizado = service.update(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteActualizado);
	}

	@DeleteMapping("/{identificacion}")
	public ResponseEntity<?> eliminar(@PathVariable String identificacion) {
		service.deleteByIdentificacion(identificacion);
		return ResponseEntity.noContent().build();
	}

}