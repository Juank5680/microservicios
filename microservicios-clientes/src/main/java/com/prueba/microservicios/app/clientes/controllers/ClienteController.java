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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.prueba.microservicios.app.clientes.models.entity.Cliente;
import com.prueba.microservicios.app.clientes.services.ClienteService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/clientes")
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
	@ApiOperation(value = "Crear cliente.", notes = "Ingrese los datos del cliente para ser creado.")
	@ApiResponses(value = {@ApiResponse(code = 201, message = "Cliente creado"),
			@ApiResponse(code = 400, message = "Cliente existente en la base de datos")})
	public ResponseEntity<?> crear(@Valid @RequestBody Cliente cliente){
		Cliente clienteDb = service.save(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteDb);
	}

	@GetMapping
	@ApiOperation(value = "Listar clientes.", notes = "Se consultan todos los clientes de la base de datos.")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Cliente encontrados en la based de datos")})
	public ResponseEntity<?> listar() {
		Iterable<Cliente> clientes = service.findAll();
		return ResponseEntity.ok().body(clientes);
	}

	@GetMapping("/listar-por-edad/{edad}")
	@ApiOperation(value = "Consultar clientes mayores de cierta edad.", notes = "Ingrese la edad para consultar los clientes.")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Clientes encontrados")})
	public ResponseEntity<?> listarPorEdad(@PathVariable int edad) {
		Iterable<Cliente> clientes = service.findByEdadGreaterThanEqual(edad);
		return ResponseEntity.ok().body(clientes);
	}

	@GetMapping("/{identificacion}")
	@ApiOperation(value = "Consultar cliente.", notes = "Ingrese el número de identificación del cliente a ser consultado.")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Cliente encontrado en la base de datos"),
			@ApiResponse(code = 404, message = "Cliente no encontrado en la base de datos")})
	public ResponseEntity<Cliente> ver(@PathVariable String identificacion) {
		Optional<Cliente> cliente = service.findByIdentificacion(identificacion);
		return ResponseEntity.ok(cliente.get());
	}

	@PutMapping
	@ApiOperation(value = "Actualizar cliente.", notes = "Ingrese los datos del cliente para ser actualizado.")
	@ApiResponses(value = {@ApiResponse(code = 201, message = "Cliente actualizado"),
			@ApiResponse(code = 404, message = "Cliente no encontrado en la base de datos")})
	public ResponseEntity<?> editar(@Valid @RequestBody Cliente cliente) {
		
		Cliente clienteActualizado = service.update(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteActualizado);
	}

	@DeleteMapping("/{identificacion}")
	@ApiOperation(value = "Eliminar cliente.", notes = "Ingrese el número de identificación del cliente a ser eliminado.")
	@ApiResponses(value = {@ApiResponse(code = 204, message = "Cliente eliminado"),
			@ApiResponse(code = 404, message = "Cliente no encontrado en la base de datos")})
	public ResponseEntity<?> eliminar(@PathVariable String identificacion) {
		service.deleteByIdentificacion(identificacion);
		return ResponseEntity.noContent().build();
	}

}