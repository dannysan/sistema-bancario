package com.banco.sistema.dto;

import java.math.BigDecimal;

public class CuentaResponseDTO {
	private Long cuentaId;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private Long clienteId;
    private String clienteNombre;

    public CuentaResponseDTO(Long cuentaId, String numeroCuenta, String tipoCuenta,
                             BigDecimal saldoInicial, Boolean estado,
                             Long clienteId, String clienteNombre) {
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
    }

    // Getters
    public Long getCuentaId() { return cuentaId; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipoCuenta() { return tipoCuenta; }
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public Boolean getEstado() { return estado; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNombre() { return clienteNombre; }
}