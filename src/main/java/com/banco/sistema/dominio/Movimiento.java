package com.banco.sistema.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "movimiento")
public class Movimiento {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "movimiento_id")
	    private Long movimientoId;

	    @Column(nullable = false)
	    private LocalDateTime fecha;

	    @NotBlank(message = "El tipo de movimiento es obligatorio")
	    @Column(name = "tipo_movimiento", nullable = false, length = 30)
	    private String tipoMovimiento; // DEPOSITO, RETIRO

	    @NotNull(message = "El valor es obligatorio")
	    @Column(nullable = false, precision = 15, scale = 2)
	    private BigDecimal valor;

	    @Column(nullable = false, precision = 15, scale = 2)
	    private BigDecimal saldo;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "cuenta_id", nullable = false)
	    private Cuenta cuenta;

	    // Getters y Setters
	    public Long getMovimientoId() { return movimientoId; }
	    public void setMovimientoId(Long movimientoId) { this.movimientoId = movimientoId; }

	    public LocalDateTime getFecha() { return fecha; }
	    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

	    public String getTipoMovimiento() { return tipoMovimiento; }
	    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

	    public BigDecimal getValor() { return valor; }
	    public void setValor(BigDecimal valor) { this.valor = valor; }

	    public BigDecimal getSaldo() { return saldo; }
	    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

	    public Cuenta getCuenta() { return cuenta; }
	    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Movimiento)) return false;
	        Movimiento that = (Movimiento) o;
	        return Objects.equals(movimientoId, that.movimientoId);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(movimientoId);
	    }
}