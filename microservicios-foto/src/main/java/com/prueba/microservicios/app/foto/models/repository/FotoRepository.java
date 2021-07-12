package com.prueba.microservicios.app.foto.models.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.prueba.microservicios.app.foto.models.entity.Foto;
@Repository
public interface FotoRepository extends MongoRepository<Foto, String>{

}

