package com.banco.sistema.dominio;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cliente_id")
	private Long clienteId;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "persona_id", nullable = false, unique = true)
	@Valid
	private Persona persona;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false, length = 255)	
    private String contrasena;

    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false)
    private Boolean estado = true;

    // Getters y Setters
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    // 👇 Métodos de conveniencia para acceder a los datos de la Persona
    // Esto simula lo que harías si Cliente "heredara" de Persona
    @JsonProperty("nombre")
    public String getNombre() { return persona != null ? persona.getNombre() : null; }

    @JsonProperty("genero")
    public String getGenero() { return persona != null ? persona.getGenero() : null; }

    @JsonProperty("edad")
    public Integer getEdad() { return persona != null ? persona.getEdad() : null; }

    @JsonProperty("identificacion")
    public String getIdentificacion() { return persona != null ? persona.getIdentificacion() : null; }

    @JsonProperty("direccion")
    public String getDireccion() { return persona != null ? persona.getDireccion() : null; }

    @JsonProperty("telefono")
    public String getTelefono() { return persona != null ? persona.getTelefono() : null; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(clienteId, cliente.clienteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clienteId);
    }
}