package com.banco.sistema.servicio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistema.dominio.Cuenta;
import com.banco.sistema.dominio.Movimiento;
import com.banco.sistema.dto.MovimientoRequestDTO;
import com.banco.sistema.excepcion.SaldoNoDisponibleException;
import com.banco.sistema.excepcion.RecursoNoEncontradoException;
import com.banco.sistema.repositorio.CuentaRepositorio;
import com.banco.sistema.repositorio.MovimientoRepositorio;


@Service
public class MovimientoServicio {

    private final MovimientoRepositorio movimientoRepositorio;
    private final CuentaRepositorio cuentaRepositorio;

    public MovimientoServicio(MovimientoRepositorio movimientoRepositorio,
                              CuentaRepositorio cuentaRepositorio) {
        this.movimientoRepositorio = movimientoRepositorio;
        this.cuentaRepositorio = cuentaRepositorio;
    }

    @Transactional
    public Movimiento registrar(Long cuentaId, MovimientoRequestDTO request) {
        Cuenta cuenta = cuentaRepositorio.findById(cuentaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con id: " + cuentaId));

        BigDecimal valor = request.getValor();

        if ("RETIRO".equalsIgnoreCase(request.getTipoMovimiento())) {
            valor = valor.abs().negate();
        } else if ("DEPOSITO".equalsIgnoreCase(request.getTipoMovimiento())) {
            valor = valor.abs();
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no válido: " + request.getTipoMovimiento());
        }

        BigDecimal nuevoSaldo = cuenta.getSaldoDisponible().add(valor);

        // F3: validar saldo
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException("Saldo no disponible");
        }

        cuenta.setSaldoDisponible(nuevoSaldo);
        cuentaRepositorio.save(cuenta);

        // Construir el Movimiento con los campos calculados
        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(LocalDateTime.now());     
        movimiento.setTipoMovimiento(request.getTipoMovimiento());
        movimiento.setValor(valor);
        movimiento.setSaldo(nuevoSaldo);              

        return movimientoRepositorio.save(movimiento);
    }

    @Transactional(readOnly = true)
    public List<Movimiento> listar() {
        return movimientoRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Movimiento> listarPorClienteYRango(Long clienteId,
                                                    LocalDateTime fechaInicio,
                                                    LocalDateTime fechaFin) {
        return movimientoRepositorio.findByClienteAndRangoFechas(clienteId, fechaInicio, fechaFin);
    }
    
    @Transactional(readOnly = true)
    public Movimiento obtenerPorId(Long id) {
        return movimientoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Movimiento no encontrado con id: " + id));
    }
    @Transactional
    public void eliminar(Long id) {
        Movimiento existente = obtenerPorId(id);
        movimientoRepositorio.delete(existente);
    }
}