package com.banco.sistema.controlador;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banco.sistema.dominio.Movimiento;
import com.banco.sistema.dto.MovimientoRequestDTO;
import com.banco.sistema.servicio.MovimientoServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoControlador {

	private final MovimientoServicio movimientoServicio;

	public MovimientoControlador(MovimientoServicio movimientoServicio) {
		this.movimientoServicio = movimientoServicio;
	}

	@PostMapping("/cuenta/{cuentaId}")
	public ResponseEntity<Movimiento> registrar(@PathVariable Long cuentaId,
			@Valid @RequestBody MovimientoRequestDTO request) {
		return new ResponseEntity<>(movimientoServicio.registrar(cuentaId, request), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<Movimiento>> listar() {
		return ResponseEntity.ok(movimientoServicio.listar());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		movimientoServicio.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
