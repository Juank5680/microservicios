package com.prueba.microservicios.app.foto.services;

import java.util.List;
import java.util.Optional;

import com.prueba.microservicios.app.foto.models.entity.Foto;

public interface FotoService {
	
	public Foto save(Foto foto);
	
	public Optional<Foto> findFotoById(String id);
	
	public Iterable<Foto> findAll();
	
	public Iterable<Foto> findAllByIds(List<String> fotosId);
	
	public void deleteById(String id);
	
	public Foto update(Foto foto);
}
