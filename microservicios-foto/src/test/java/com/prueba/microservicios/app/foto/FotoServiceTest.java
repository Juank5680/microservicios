package com.prueba.microservicios.app.foto;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import com.prueba.microservicios.app.foto.models.entity.Foto;
import com.prueba.microservicios.app.foto.models.repository.FotoRepository;
import com.prueba.microservicios.app.foto.services.FotoServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class FotoServiceTest {

	@Mock
	private FotoRepository repository;

	@InjectMocks
	private FotoServiceImpl service;

	private static final String ID = "1";
	private static final String BASE64 = "TWFuIGlzIGRpc3Rpbmd1aX";

	@SuppressWarnings("deprecation")
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		service = new FotoServiceImpl(repository);
	}

	@Test
	public void consultarFotoPorIdTest() {
		// Preparación
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Mockito.when(repository.findById(ID)).thenReturn(Optional.of(foto));

		// Prueba
		Foto fotoPrueba = service.findFotoById(ID).get();

		// Verificacion
		Assertions.assertThat(fotoPrueba.getDatosBase64()).isEqualTo(BASE64);
	}

	@Test
	public void consultarFotoPorIdErrorTest() {
		// Preparación
		Foto foto = null;

		Mockito.when(repository.findById(ID)).thenReturn(Optional.ofNullable(foto));

		// Preuba - verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.findFotoById(ID);
		});
	}

	@Test
	public void guardarFotoTest() {
		// Preparación
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Mockito.when(repository.save(foto)).thenReturn(foto);

		// Prueba
		Foto fotoPrueba = service.save(foto);

		// Verificación
		Assertions.assertThat(fotoPrueba.getId()).isEqualTo(ID);
	}

	@Test
	public void obtenerFotosTest() {
		// Preparación
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		List<Foto> fotos = new ArrayList<>();
		fotos.add(foto);
		Mockito.when(repository.findAll()).thenReturn(fotos);

		// Prueba
		Iterable<Foto> fotosPrueba = service.findAll();

		// Verificación
		Assertions.assertThat(fotosPrueba).isNotEmpty();
	}

	@Test
	public void eliminarFotoPorIdTest() {
		// Preparación
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Mockito.when(repository.findById(ID)).thenReturn(Optional.of(foto));
		Mockito.doNothing().when(repository).delete(foto);
		
		//Prueba
		service.deleteById(ID);
	}

	@Test
	public void eliminarFotoPorIdErrorTest() {
		// Preparación
		Foto foto = null;

		Mockito.when(repository.findById(ID)).thenReturn(Optional.ofNullable(foto));

		// Preuba - verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.deleteById(ID);
		});
	}

	@Test
	public void actualizarFotoTest() {
		// Preparación
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Mockito.when(repository.save(foto)).thenReturn(foto);

		foto.setDatosBase64("HMYuIGlzIGRpc3");

		// Prueba
		Foto fotoActualizada = service.update(foto);

		// Verificación
		Assertions.assertThat(fotoActualizada.getDatosBase64()).isEqualTo("HMYuIGlzIGRpc3");
	}

	@Test
	public void obtenerFotosPorIdTest() {
		// Preparación
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Foto foto2 = Foto.builder().id("2").datosBase64(BASE64).build();
		List<Foto> fotos = new ArrayList<>();
		fotos.add(foto);
		fotos.add(foto2);

		List<String> fotosId = new ArrayList<>();
		fotosId.add(ID);
		fotosId.add("2");
		Mockito.when(repository.findAllById(fotosId)).thenReturn(fotos);

		// Prueba
		Iterable<Foto> fotosPrueba = service.findAllByIds(fotosId);

		// Verificación
		Assertions.assertThat(fotosPrueba).isNotEmpty();
	}
}
