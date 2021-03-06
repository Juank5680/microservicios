package com.prueba.microservicios.app.foto.services;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.prueba.microservicios.app.foto.models.entity.Foto;
import com.prueba.microservicios.app.foto.models.repository.FotoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FotoServiceImpl implements FotoService {

	private final FotoRepository repository;

	@Override
	public Foto save(Foto foto) {
		return repository.save(foto);
	}

	@Override
	public Optional<Foto> findFotoById(String id) {
		Optional<Foto> foto = repository.findById(id);
		
		if(foto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay ninguna foto con el id: " +id+ " en la base de datos.");
		}
		return foto;
	}
	
	@Override
	public Iterable<Foto> findAll() {
		return repository.findAll();
	}

	@Override
	public void deleteById(String id) {
		Optional<Foto> foto = repository.findById(id);
		
		if(foto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay ninguna foto con el id: " +id+ " en la base de datos.");
		}
		repository.deleteById(id);
	}
	
	@Override
	public Foto update(Foto foto) {
		return repository.save(foto);
	}
	
	@Override
	public Iterable<Foto> findAllByIds(List<String> fotosId) {
		return repository.findAllById(fotosId);
	}

}
