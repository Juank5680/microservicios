package com.prueba.microservicios.app.clientes.models.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prueba.microservicios.app.clientes.models.entity.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, String> {

	Iterable<Cliente> findByEdadGreaterThanEqual(int edad); 
	
	Optional<Cliente> findByIdentificacion(String identificacion);
	
	void deleteByIdentificacion(String identificacion);
	
}
