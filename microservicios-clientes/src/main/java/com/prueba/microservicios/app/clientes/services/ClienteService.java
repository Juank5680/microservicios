package com.prueba.microservicios.app.clientes.services;

import java.util.Optional;

import com.prueba.microservicios.app.clientes.models.entity.Cliente;

public interface ClienteService {
	
	public Iterable<Cliente> findAll();
	
	public Iterable<Cliente> findByEdadGreaterThanEqual(int edad);
	
	public Optional<Cliente> findByIdentificacion(String identificacion);
	
	public Cliente save(Cliente cliente);
	
	public void deleteByIdentificacion(String identificacion);
	
	public Cliente update(Cliente cliente);

}
