package com.banco.sistema.controlador;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banco.sistema.dominio.Cuenta;
import com.banco.sistema.servicio.CuentaServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaControlador {

    private final CuentaServicio cuentaServicio;

    public CuentaControlador(CuentaServicio cuentaServicio) {
        this.cuentaServicio = cuentaServicio;
    }

    @PostMapping
    public ResponseEntity<Cuenta> crear(@Valid @RequestBody Cuenta cuenta,
                                        @RequestParam Long clienteId) {
        return new ResponseEntity<>(cuentaServicio.crear(cuenta, clienteId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cuenta>> listar() {
        return ResponseEntity.ok(cuentaServicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaServicio.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> actualizar(@PathVariable Long id, @Valid @RequestBody Cuenta cuenta) {
        return ResponseEntity.ok(cuentaServicio.actualizar(id, cuenta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuentaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
