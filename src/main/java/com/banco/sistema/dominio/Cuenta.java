package com.banco.sistema.dominio;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "cuenta")
public class Cuenta {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "cuenta_id")
	    private Long cuentaId;

	    @NotBlank(message = "El número de cuenta es obligatorio")
	    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 30)
	    private String numeroCuenta;

	    @NotBlank(message = "El tipo de cuenta es obligatorio")
	    @Column(name = "tipo_cuenta", nullable = false, length = 30)
	    private String tipoCuenta;

	    @NotNull(message = "El saldo inicial es obligatorio")
	    @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo")
	    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
	    private BigDecimal saldoInicial;

	    @NotNull(message = "El saldo disponible es obligatorio")
	    @Column(name = "saldo_disponible", nullable = false, precision = 15, scale = 2)
	    private BigDecimal saldoDisponible;

	    @NotNull(message = "El estado es obligatorio")
	    @Column(nullable = false)
	    private Boolean estado = true;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "cliente_id", nullable = false)
	    private Cliente cliente;

	    // Getters y Setters
	    public Long getCuentaId() { return cuentaId; }
	    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

	    public String getNumeroCuenta() { return numeroCuenta; }
	    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

	    public String getTipoCuenta() { return tipoCuenta; }
	    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

	    public BigDecimal getSaldoInicial() { return saldoInicial; }
	    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

	    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
	    public void setSaldoDisponible(BigDecimal saldoDisponible) { this.saldoDisponible = saldoDisponible; }

	    public Boolean getEstado() { return estado; }
	    public void setEstado(Boolean estado) { this.estado = estado; }

	    public Cliente getCliente() { return cliente; }
	    public void setCliente(Cliente cliente) { this.cliente = cliente; }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Cuenta)) return false;
	        Cuenta cuenta = (Cuenta) o;
	        return Objects.equals(cuentaId, cuenta.cuentaId);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(cuentaId);
	    }
}
