package com.banco.sistema.controlador;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banco.sistema.dominio.Movimiento;
import com.banco.sistema.dto.ReporteEstadoCuentaDTO;
import com.banco.sistema.servicio.MovimientoServicio;

@RestController
@RequestMapping("/api/reportes")
public class ReporteControlador {

    private final MovimientoServicio movimientoServicio;

    public ReporteControlador(MovimientoServicio movimientoServicio) {
        this.movimientoServicio = movimientoServicio;
    }

    @GetMapping
    public ResponseEntity<List<ReporteEstadoCuentaDTO>> generarReporte(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        List<Movimiento> movimientos = movimientoServicio.listarPorClienteYRango(clienteId, fechaInicio, fechaFin);

        List<ReporteEstadoCuentaDTO> reporte = movimientos.stream()
                .map(m -> new ReporteEstadoCuentaDTO(
                        m.getFecha(),
                        m.getCuenta().getCliente().getPersona().getNombre(),
                        m.getCuenta().getNumeroCuenta(),
                        m.getCuenta().getTipoCuenta(),
                        m.getCuenta().getSaldoInicial(),
                        m.getCuenta().getEstado(),
                        m.getValor(),
                        m.getSaldo()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reporte);
    }
}
