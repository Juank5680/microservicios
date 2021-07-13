package com.prueba.microservicios.app.clientes.clients;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.prueba.microservicios.app.foto.models.entity.Foto;

@Component
public class FotoHystrixFallback implements FotoFeign{

	@Override
	public ResponseEntity<Foto> obtenerFotoPorId(String id) {
		Foto foto = Foto.builder().id(null).datosBase64("Error en el microservicio foto").build();
		return ResponseEntity.ok(foto);
	}

	@Override
	public ResponseEntity<Foto> eliminar(String id) {
		Foto foto = Foto.builder().id(null).datosBase64("Error eliminando la foto").build();
		return ResponseEntity.ok(foto);
	}

	@Override
	public ResponseEntity<Iterable<Foto>> obtenerFotosPorListaIds(List<String> fotosId) {
		Foto foto = Foto.builder().id(null).datosBase64("Error obtenido las fotos").build();
		List<Foto> fotos = new ArrayList<>();
		fotos.add(foto);
		return ResponseEntity.ok(fotos);
	}
	
	@Override
	public ResponseEntity<Foto> crear(Foto fotoNueva) {
		Foto foto = Foto.builder().id(null).datosBase64("Error creando la foto").build();
		return ResponseEntity.ok(foto);
	}

	@Override
	public ResponseEntity<Foto> actualizar(Foto fotoNueva) {
		Foto foto = Foto.builder().id(null).datosBase64("Error actualizando la foto").build();
		return ResponseEntity.ok(foto);
	}

}
