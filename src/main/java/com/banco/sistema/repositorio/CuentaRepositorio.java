package com.banco.sistema.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banco.sistema.dominio.Cuenta;

public interface CuentaRepositorio extends JpaRepository<Cuenta, Long> {
	Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

	boolean existsByNumeroCuenta(String numeroCuenta);

	@Query("SELECT c FROM Cuenta c JOIN FETCH c.cliente cl JOIN FETCH cl.persona " + "WHERE cl.clienteId = :clienteId")
	List<Cuenta> findByClienteIdConPersona(@Param("clienteId") Long clienteId);

	List<Cuenta> findByClienteClienteId(Long clienteId);

	boolean existsByClienteClienteId(Long clienteId);
}