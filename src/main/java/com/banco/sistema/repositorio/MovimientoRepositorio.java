package com.banco.sistema.repositorio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banco.sistema.dominio.Movimiento;

public interface MovimientoRepositorio extends JpaRepository<Movimiento, Long> {
	@Query("SELECT m FROM Movimiento m WHERE m.cuenta.cliente.clienteId = :clienteId " +
	           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha DESC")
	    List<Movimiento> findByClienteAndRangoFechas(
	            @Param("clienteId") Long clienteId,
	            @Param("fechaInicio") LocalDateTime fechaInicio,
	            @Param("fechaFin") LocalDateTime fechaFin);
	 boolean existsByCuentaCuentaId(Long cuentaId);
}