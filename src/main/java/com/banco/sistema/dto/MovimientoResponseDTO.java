package com.banco.sistema.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoResponseDTO {
	private Long movimientoId;
    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private Long cuentaId;
    private String numeroCuenta;

    public MovimientoResponseDTO(Long movimientoId, LocalDateTime fecha,
                                 String tipoMovimiento, BigDecimal valor,
                                 BigDecimal saldo, Long cuentaId,
                                 String numeroCuenta) {
        this.movimientoId = movimientoId;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
    }

    // Getters
    public Long getMovimientoId() { return movimientoId; }
    public LocalDateTime getFecha() { return fecha; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public BigDecimal getValor() { return valor; }
    public BigDecimal getSaldo() { return saldo; }
    public Long getCuentaId() { return cuentaId; }
    public String getNumeroCuenta() { return numeroCuenta; }
}
