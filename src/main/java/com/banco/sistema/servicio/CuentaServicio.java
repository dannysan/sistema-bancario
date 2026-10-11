package com.banco.sistema.servicio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistema.dominio.Cliente;
import com.banco.sistema.dominio.Cuenta;
import com.banco.sistema.excepcion.RecursoNoEncontradoException;
import com.banco.sistema.repositorio.ClienteRepositorio;
import com.banco.sistema.repositorio.CuentaRepositorio;
import com.banco.sistema.repositorio.MovimientoRepositorio;

@Service
public class CuentaServicio {

	private final CuentaRepositorio cuentaRepositorio;
	private final ClienteRepositorio clienteRepositorio;
	private final MovimientoRepositorio movimientoRepositorio;

	public CuentaServicio(CuentaRepositorio cuentaRepositorio, ClienteRepositorio clienteRepositorio,
			MovimientoRepositorio movimientoRepositorio) {
		this.cuentaRepositorio = cuentaRepositorio;
		this.clienteRepositorio = clienteRepositorio;
		this.movimientoRepositorio = movimientoRepositorio;
	}

	@Transactional
	public Cuenta crear(Cuenta cuenta, Long clienteId) {
		Cliente cliente = clienteRepositorio.findById(clienteId)
				.orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id: " + clienteId));
		cuenta.setCliente(cliente);
		cuenta.setSaldoDisponible(cuenta.getSaldoInicial());
		return cuentaRepositorio.save(cuenta);
	}

	@Transactional(readOnly = true)
	public List<Cuenta> listar() {
		return cuentaRepositorio.findAll();
	}

	@Transactional(readOnly = true)
	public Cuenta obtenerPorId(Long id) {
		return cuentaRepositorio.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con id: " + id));
	}

	@Transactional
	public Cuenta actualizar(Long id, Cuenta datos) {
		Cuenta existente = obtenerPorId(id);
		existente.setNumeroCuenta(datos.getNumeroCuenta());
		existente.setTipoCuenta(datos.getTipoCuenta());
		existente.setSaldoInicial(datos.getSaldoInicial());
		existente.setEstado(datos.getEstado());
		return cuentaRepositorio.save(existente);
	}

	@Transactional
	public void eliminar(Long id) {
		Cuenta existente = obtenerPorId(id);

		if (movimientoRepositorio.existsByCuentaCuentaId(id)) {
			throw new IllegalStateException("No se puede eliminar la cuenta porque tiene movimientos asociados. "
					+ "Elimine primero sus movimientos.");
		}

		// Baja lógica: cambia estado a false
		existente.setEstado(false);
		cuentaRepositorio.save(existente);
	}
}