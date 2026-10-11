package com.banco.sistema.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistema.dominio.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
	Optional<Cliente> findByPersonaIdentificacion(String identificacion);

	boolean existsByPersonaIdentificacion(String identificacion);
}