package com.prueba.microservicios.app.clientes.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.prueba.microservicios.app.clientes.clients.FotoFeign;
import com.prueba.microservicios.app.clientes.models.entity.Cliente;
import com.prueba.microservicios.app.clientes.models.repository.ClienteRepository;
import com.prueba.microservicios.app.foto.models.entity.Foto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository repository;

	private final FotoFeign fotoFeign;

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {

		if (repository.findById(cliente.getIdentificacion()).isEmpty()) {
			if (cliente.getFoto() != null) {
				Foto foto = fotoFeign.crear(cliente.getFoto()).getBody();
				cliente.setIdFoto(foto.getId());
				cliente.setFoto(foto);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente ya se encuentra registrado");
		}
		try {
			return repository.save(cliente);
		} catch (Exception e) {
			if (cliente.getIdFoto() != null) {
				fotoFeign.eliminar(cliente.getIdFoto());
			}
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Cliente> findAll() {
		Iterable<Cliente> clientes = repository.findAll();
		return buscarFotos(clientes);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Cliente> findByEdadGreaterThanEqual(int edad) {
		Iterable<Cliente> clientes = repository.findByEdadGreaterThanEqual(edad);
		return buscarFotos(clientes);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Cliente> findByIdentificacion(String identificacion) {

		Optional<Cliente> cliente = repository.findById(identificacion);
		if (cliente.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El n??mero de identificaci??n " + identificacion + " no se encuentre registrado.");
		}
		if (cliente.get().getIdFoto() != null) {
			Foto foto = fotoFeign.obtenerFotoPorId(cliente.get().getIdFoto()).getBody();
			cliente.get().setFoto(foto);
		}
		return cliente;
	}

	@Override
	@Transactional
	public void deleteByIdentificacion(String identificacion) {
		Optional<Cliente> cliente = repository.findById(identificacion);
		Foto foto = null;

		if (cliente.get().getIdFoto() != null) {
			foto = fotoFeign.obtenerFotoPorId(cliente.get().getIdFoto()).getBody();
			fotoFeign.eliminar(cliente.get().getIdFoto());
		}
		try {
			repository.deleteByIdentificacion(identificacion);
		} catch (Exception e) {
			if (cliente.get().getIdFoto() != null) {
				fotoFeign.crear(foto);
			}
			throw e;
		}
	}

	@Override
	@Transactional
	public Cliente update(Cliente cliente) {

		Optional<Cliente> o = findByIdentificacion(cliente.getIdentificacion());

		Cliente clienteDb = o.get();
		clienteDb.setNombre(cliente.getNombre());
		clienteDb.setApellido(cliente.getApellido());
		clienteDb.setEdad(cliente.getEdad());
		clienteDb.setCiudad(cliente.getCiudad());
		clienteDb.setTipo(cliente.getTipo());

		Foto fotoActual = null;
		Foto fotoNueva = null;

		if (cliente.getFoto() != null) {
			if (clienteDb.getIdFoto() == null) {
				fotoNueva = fotoFeign.crear(cliente.getFoto()).getBody();
				clienteDb.setFoto(fotoNueva);
				clienteDb.setIdFoto(fotoNueva.getId());
			} else {
				fotoActual = fotoFeign.obtenerFotoPorId(clienteDb.getIdFoto()).getBody();
				fotoNueva = Foto.builder().id(fotoActual.getId()).datosBase64(fotoActual.getDatosBase64()).build();
				fotoNueva = fotoFeign.actualizar(fotoNueva).getBody();
				clienteDb.setFoto(fotoNueva);
			}
		}
		try {
			return repository.save(clienteDb);
		} catch (Exception e) {
			if (clienteDb.getFoto() != null) {
				if (fotoActual == null) {
					fotoFeign.eliminar(fotoNueva.getId());
				}
				fotoFeign.actualizar(fotoActual);
			}
			throw e;
		}
	}

	private Iterable<Cliente> buscarFotos(Iterable<Cliente> clientes) {
		List<Cliente> clientesFoto = null;
		if (clientes != null) {
			clientesFoto = (List<Cliente>) clientes;
			List<String> fotosId = obtenerFotosId(clientesFoto);
			if (!fotosId.isEmpty()) {
				List<Foto> fotos = (List<Foto>) fotoFeign.obtenerFotosPorListaIds(fotosId).getBody();
				clientesFoto.forEach((Cliente cliente) -> {
					fotos.forEach((Foto foto) -> {
						if (cliente.getIdFoto() != null) {
							if (cliente.getIdFoto().equalsIgnoreCase(foto.getId())) {
								cliente.setFoto(foto);
							}
						}
					});
				});
			}
		}
		return clientesFoto;
	}

	private List<String> obtenerFotosId(List<Cliente> clientes) {
		List<String> fotosId = new ArrayList<String>();

		clientes.forEach((final Cliente cliente) -> {
			if (cliente.getIdFoto() != null)
				fotosId.add(cliente.getIdFoto());
		});
		return fotosId;
	}

}
