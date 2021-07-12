package com.prueba.microservicios.app.clientes;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.prueba.microservicios.app.clientes.clients.FotoFeign;
import com.prueba.microservicios.app.clientes.models.entity.Cliente;
import com.prueba.microservicios.app.clientes.models.entity.TipoId;
import com.prueba.microservicios.app.clientes.models.repository.ClienteRepository;
import com.prueba.microservicios.app.clientes.services.ClienteServiceImpl;
import com.prueba.microservicios.app.foto.models.entity.Foto;


@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {

	@Mock
	private ClienteRepository repository;

	@Mock
	private FotoFeign fotoFeign;

	@InjectMocks
	private ClienteServiceImpl service;


	private static final String IDENTIFICACION = "1";
	private static final String NOMBRE = "Juan";
	private static final String APELLIDO = "Pinzón";
	private static final String CIUDAD = "Calarca";
	private static final int EDAD = 25;
	private static final TipoId TIPO_IDENTIFICACION = TipoId.builder().id(1L).nombre("Cedula de ciudadania").build();

	private static final String ID = "1";
	private static final String BASE64 = "TWFuIGlzIGRpc3Rpbmd1aX";

	@SuppressWarnings("deprecation")
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		service = new ClienteServiceImpl(repository, fotoFeign);
	}

	@Test
	public void guardarClienteConFotoTest() {
		// Preparación
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto(ID).foto(foto).build();

		Cliente clienteVacio = null;

		Optional<Cliente> c = Optional.ofNullable(clienteVacio);

		Mockito.when(repository.save(cliente)).thenReturn(cliente);
		Mockito.when(repository.findById(cliente.getIdentificacion())).thenReturn(c);
		Mockito.when(fotoFeign.crear(cliente.getFoto())).thenReturn(ResponseEntity.of(Optional.of(foto)));

		// Prueba
		Cliente clientePrueba = service.save(cliente);

		// Verificacion
		Assertions.assertThat(clientePrueba.getNombre()).isEqualTo(NOMBRE);
		Assertions.assertThat(clientePrueba.getIdFoto()).isEqualTo(ID);
	}

	@Test
	public void guardarClienteSinFotoTest() {
		// Preparación
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		Cliente clienteVacio = null;

		Optional<Cliente> c = Optional.ofNullable(clienteVacio);

		Mockito.when(repository.save(cliente)).thenReturn(cliente);
		Mockito.when(repository.findById(cliente.getIdentificacion())).thenReturn(c);

		// Prueba
		Cliente clientePrueba = service.save(cliente);

		// Verificacion
		Assertions.assertThat(clientePrueba.getNombre()).isEqualTo(NOMBRE);
		Assertions.assertThat(clientePrueba.getIdentificacion()).isEqualTo(IDENTIFICACION);
	}

	@Test
	public void guardarClienteExistenteErorTest() {

		// Preparación
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();
		Mockito.when(repository.findById(cliente.getIdentificacion())).thenReturn(Optional.of(cliente));

		// Preuba - verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.save(cliente);
		});
	}

	@Test
	public void guardarClienteConFotoExpcionTest() {
		// Preparación
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto(ID).build();

		Cliente clienteVacio = null;

		Mockito.doThrow(ResponseStatusException.class).when(repository).save(cliente);
		Mockito.when(repository.findById(cliente.getIdentificacion())).thenReturn(Optional.ofNullable(clienteVacio));

		// Preuba - verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.save(cliente);
		});
	}

	@Test
	public void guardarClienteSinFotoExpcionTest() {
		// Preparación
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		Cliente clienteVacio = null;

		Optional<Cliente> c = Optional.ofNullable(clienteVacio);

		Mockito.doThrow(ResponseStatusException.class).when(repository).save(cliente);
		Mockito.when(repository.findById(cliente.getIdentificacion())).thenReturn(c);

		// Preuba - verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.save(cliente);
		});
	}

	@Test
	public void consultarClientePorIdTest() {
		// Preparación
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto(ID).foto(foto).build();
		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));
		Mockito.when(fotoFeign.obtenerFotoPorId(ID)).thenReturn(ResponseEntity.of(Optional.of(foto)));

		// Prueba
		Cliente clientePrueba = service.findByIdentificacion(IDENTIFICACION).get();

		// Verificacion
		Assertions.assertThat(clientePrueba.getNombre()).isEqualTo(NOMBRE);
		Assertions.assertThat(clientePrueba.getIdFoto()).isEqualTo(ID);
	}

	@Test
	public void consultarClientePorIdentificacionErrorTest() {
		// Preparación
		Cliente clienteVacio = null;

		Optional<Cliente> c = Optional.ofNullable(clienteVacio);

		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(c);

		// Preuba - verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.findByIdentificacion(IDENTIFICACION);
		});
	}

	@Test
	public void consultarClientePorIdentificacionSinFotoTest() {
		// Preparacion
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto(null).build();
		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));

		// Prueba
		Cliente clientePrueba = service.findByIdentificacion(IDENTIFICACION).get();

		// Verificacion
		Assertions.assertThat(clientePrueba.getCiudad()).isEqualTo(CIUDAD);
	}

	@Test
	public void listarClientesTest() {
		// Preparación
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Foto foto2 = Foto.builder().id("2").datosBase64(BASE64).build();

		Cliente clienteA = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto(ID).foto(foto).build();

		Cliente clienteB = Cliente.builder().identificacion("2").nombre(NOMBRE).apellido(APELLIDO).ciudad(CIUDAD)
				.edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto("2").foto(foto2).build();

		Cliente clienteC = Cliente.builder().identificacion("3").nombre(NOMBRE).apellido(APELLIDO).ciudad(CIUDAD)
				.edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		List<Cliente> clientes = new ArrayList<>();
		clientes.add(clienteA);
		clientes.add(clienteB);
		clientes.add(clienteC);

		List<Foto> fotos = new ArrayList<>();
		fotos.add(foto);
		fotos.add(foto2);

		List<String> fotosId = new ArrayList<>();
		fotosId.add(ID);
		fotosId.add("2");

		Iterable<Foto> fotosPrueba = fotos;

		Mockito.when(repository.findAll()).thenReturn(clientes);
		Mockito.when(fotoFeign.obtenerFotosPorListaIds(fotosId)).thenReturn(ResponseEntity.ok(fotosPrueba));

		// Prueba
		Iterable<Cliente> clientesPrueba = service.findAll();

		// Verificacion
		assertNotNull(clientesPrueba);
	}

	@Test
	public void listarClientesVaciosTest() {
		// Preparación
		Mockito.when(repository.findAll()).thenReturn(null);

		// Prueba
		Iterable<Cliente> clientesPrueba = service.findAll();

		// Verificacion
		assertNull(clientesPrueba);
	}

	@Test
	public void listarClientesSinFotoTest() {
		// Preparación
		Cliente cliente = Cliente.builder().identificacion(ID).nombre(NOMBRE).apellido(APELLIDO).ciudad(CIUDAD)
				.edad(EDAD).tipo(TIPO_IDENTIFICACION).build();
		List<Cliente> clientes = new ArrayList<>();
		clientes.add(cliente);

		Mockito.when(repository.findAll()).thenReturn(clientes);

		// Prueba
		Iterable<Cliente> clientesPrueba = service.findAll();

		// Verificacion
		Assertions.assertThat(clientesPrueba).isNotNull();
	}

	@Test
	public void listarClientesPorEdadTest() {
		// Preparación

		Cliente cliente1 = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		Cliente cliente2 = Cliente.builder().identificacion("2").nombre(NOMBRE).apellido(APELLIDO).ciudad(CIUDAD)
				.edad(28).tipo(TIPO_IDENTIFICACION).build();

		List<Cliente> clientes = new ArrayList<>();
		clientes.add(cliente1);
		clientes.add(cliente2);

		Mockito.when(repository.findByEdadGreaterThanEqual(EDAD)).thenReturn(clientes);

		// Prueba
		Iterable<Cliente> clientesPrueba = service.findByEdadGreaterThanEqual(EDAD);

		// Verificacion
		assertNotNull(clientesPrueba);
	}

	@Test
	public void actualizarClienteSinFotoTest() {
		// Preparacion
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		cliente.setEdad(40);

		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));
		Mockito.when(repository.save(cliente)).thenReturn(cliente);

		// Prueba
		Cliente clienteActualizado = service.update(cliente);

		// Verificacion
		Assertions.assertThat(clienteActualizado.getEdad()).isEqualTo(40);
	}

	@Test
	public void actualizarFotoClienteSinFotoTest() {
		// Preparacion
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		Cliente clienteConFoto = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		clienteConFoto.setIdFoto(ID);
		clienteConFoto.setFoto(foto);

		Mockito.when(fotoFeign.crear(clienteConFoto.getFoto())).thenReturn(ResponseEntity.of(Optional.of(foto)));
		Mockito.when(fotoFeign.obtenerFotoPorId(ID)).thenReturn(ResponseEntity.of(Optional.of(foto)));
		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));
		Mockito.when(repository.save(clienteConFoto)).thenReturn(clienteConFoto);

		// Prueba
		Cliente clienteActualizado = service.update(clienteConFoto);

		// Verificación
		assertNotNull(clienteActualizado.getFoto());
	}

	@Test
	public void actualizarFotoClienteConFotoTest() {
		// Preparacion
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Foto fotoActualizada = Foto.builder().id(ID).datosBase64("TWFuIGlzI").build();
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto(ID).foto(foto).build();

		Mockito.when(fotoFeign.actualizar(cliente.getFoto()))
				.thenReturn(ResponseEntity.of(Optional.of(fotoActualizada)));
		Mockito.when(fotoFeign.obtenerFotoPorId(ID)).thenReturn(ResponseEntity.of(Optional.of(foto)));
		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));
		cliente.setIdFoto(ID);
		cliente.setFoto(fotoActualizada);
		Mockito.when(repository.save(cliente)).thenReturn(cliente);

		// Prueba
		Cliente clienteActualizado = service.update(cliente);

		// Verificación
		assertNotNull(clienteActualizado.getFoto());
		Assertions.assertThat(clienteActualizado.getFoto().getDatosBase64()).isEqualTo("TWFuIGlzI");
	}

	@Test
	public void actualizarClienteSinFotoExepcionTest() {
		// Preparacion
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		Mockito.doThrow(ResponseStatusException.class).when(repository).save(cliente);
		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));

		// Prueba - Verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.update(cliente);
		});
	}

	@Test
	public void actualizarFotoClienteSinFotoExepcionTest() {
		// Preparacion
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		Cliente cliente2 = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente2));
		Mockito.doThrow(ResponseStatusException.class).when(repository).save(cliente);
		Mockito.when(fotoFeign.crear(foto)).thenReturn(ResponseEntity.of(Optional.of(foto)));

		cliente.setIdFoto(ID);
		cliente.setFoto(foto);

		// Verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.update(cliente);
		});
	}

	@Test
	public void actualizarFotoClienteConFotoExepcionTest() {
		// Preparacion
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto(ID).foto(foto).build();

		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));
		Mockito.doThrow(ResponseStatusException.class).when(repository).save(cliente);
		Mockito.when(fotoFeign.obtenerFotoPorId(ID)).thenReturn(ResponseEntity.of(Optional.of(foto)));
		Mockito.when(fotoFeign.actualizar(foto)).thenReturn(ResponseEntity.of(Optional.of(foto)));
		Mockito.when(fotoFeign.eliminar(ID)).thenReturn(ResponseEntity.of(Optional.of(foto)));

		foto.setDatosBase64("TWFuIGlzI");
		cliente.setFoto(foto);

		// Verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.update(cliente);
		});
	}

	@Test
	public void eliminarClienteSinFotoTest() {
		// Preparacion
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();

		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));
		Mockito.doNothing().when(repository).deleteByIdentificacion(IDENTIFICACION);

		// Prueba
		service.deleteByIdentificacion(IDENTIFICACION);
	}

	@Test
	public void eliminarClienteConFotoTest() {
		// Preparacion
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto(ID).foto(foto).build();

		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));
		Mockito.doNothing().when(repository).deleteByIdentificacion(IDENTIFICACION);
		Mockito.when(fotoFeign.eliminar(ID)).thenReturn(ResponseEntity.of(Optional.of(foto)));
		Mockito.when(fotoFeign.obtenerFotoPorId(ID)).thenReturn(ResponseEntity.of(Optional.of(foto)));

		// Prueba
		service.deleteByIdentificacion(IDENTIFICACION);
	}
	
	@Test
	public void eliminarClienteSinFotoExcepcionTest() {
		// Preparacion
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).build();
		
		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));	
		Mockito.doThrow(ResponseStatusException.class).when(repository).deleteByIdentificacion(IDENTIFICACION);
		
		//Prueba - Verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.deleteByIdentificacion(IDENTIFICACION);
		});
	}
	
	@Test
	public void eliminarClienteConFotoExcepcionTest() {
		// Preparacion
		Foto foto = Foto.builder().id(ID).datosBase64(BASE64).build();
		Cliente cliente = Cliente.builder().identificacion(IDENTIFICACION).nombre(NOMBRE).apellido(APELLIDO)
				.ciudad(CIUDAD).edad(EDAD).tipo(TIPO_IDENTIFICACION).idFoto(ID).foto(foto).build();
		
		Mockito.when(repository.findById(IDENTIFICACION)).thenReturn(Optional.of(cliente));	
		Mockito.doThrow(ResponseStatusException.class).when(repository).deleteByIdentificacion(IDENTIFICACION);
		Mockito.when(fotoFeign.eliminar(ID)).thenReturn(ResponseEntity.of(Optional.of(foto)));
		Mockito.when(fotoFeign.obtenerFotoPorId(ID)).thenReturn(ResponseEntity.of(Optional.of(foto)));
		Mockito.when(fotoFeign.crear(foto)).thenReturn(ResponseEntity.of(Optional.of(foto)));
		
		//Prueba - Verificación
		assertThrows(ResponseStatusException.class, () -> {
			service.deleteByIdentificacion(IDENTIFICACION);
		});
	}

}
