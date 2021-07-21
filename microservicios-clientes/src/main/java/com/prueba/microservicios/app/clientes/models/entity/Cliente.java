package com.prueba.microservicios.app.clientes.models.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prueba.microservicios.app.foto.models.entity.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "clientes")
public class Cliente {

	@Id
	@Column(name = "identificacion")
	@NotEmpty(message = "El número de identificación no debe ser vacio")
	private String identificacion;
	
	@NotNull(message = "La categoria no debe ser vacia")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private TipoId tipo;

	@Column(name = "nombre", length = 150)
	@NotEmpty(message = "El nombre no debe ser vacio")
	private String nombre;
	
	@Column(name = "apellido", length = 150)
	@NotEmpty(message = "El apellido no debe ser vacio")
	private String apellido;

	@Column(name = "edad")
	@PositiveOrZero(message = "La edad debe ser igual o mayor a cero")
	private int edad;
	
	@Column(name = "ciudad", length = 150)
	@NotEmpty(message = "La ciudad no debe ser vacia")
	private String ciudad;
	
	@Column(name = "id_foto")
	private String idFoto;
	
	@Transient
	private Foto foto;
	
}
