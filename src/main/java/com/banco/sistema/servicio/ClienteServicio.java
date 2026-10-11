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
public class ClienteServicio {

	private final ClienteRepositorio clienteRepositorio;
	private final CuentaRepositorio cuentaRepositorio;
	private final MovimientoRepositorio movimientoRepositorio;

	public ClienteServicio(ClienteRepositorio clienteRepositorio, CuentaRepositorio cuentaRepositorio,
			MovimientoRepositorio movimientoRepositorio) {
		this.clienteRepositorio = clienteRepositorio;
		this.cuentaRepositorio = cuentaRepositorio;
		this.movimientoRepositorio = movimientoRepositorio;
	}

	@Transactional
	public Cliente crear(Cliente cliente) {
		if (clienteRepositorio.existsByPersonaIdentificacion(cliente.getPersona().getIdentificacion())) {
			throw new IllegalArgumentException(
					"Ya existe un cliente con la identificación: " + cliente.getPersona().getIdentificacion());
		}
		return clienteRepositorio.save(cliente);
	}

	@Transactional(readOnly = true)
	public List<Cliente> listar() {
		return clienteRepositorio.findAll();
	}

	@Transactional(readOnly = true)
	public Cliente obtenerPorId(Long id) {
		return clienteRepositorio.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id: " + id));
	}

	@Transactional
	public Cliente actualizar(Long id, Cliente datos) {
		Cliente existente = obtenerPorId(id);
		existente.getPersona().setNombre(datos.getPersona().getNombre());
		existente.getPersona().setGenero(datos.getPersona().getGenero());
		existente.getPersona().setEdad(datos.getPersona().getEdad());
		existente.getPersona().setIdentificacion(datos.getPersona().getIdentificacion());
		existente.getPersona().setDireccion(datos.getPersona().getDireccion());
		existente.getPersona().setTelefono(datos.getPersona().getTelefono());
		existente.setContrasena(datos.getContrasena());
		existente.setEstado(datos.getEstado());
		return clienteRepositorio.save(existente);
	}

	@Transactional
	public void eliminar(Long id) {
		Cliente existente = obtenerPorId(id);

        List<Cuenta> cuentas = cuentaRepositorio.findByClienteClienteId(id);

        // Validar que NINGUNA cuenta tenga movimientos
        for (Cuenta cuenta : cuentas) {
            if (movimientoRepositorio.existsByCuentaCuentaId(cuenta.getCuentaId())) {
                throw new IllegalStateException(
                        "No se puede eliminar la cuenta porque tiene movimientos asociados. "
                        + "Elimine primero sus movimientos.");
            }
        }

        // Baja lógica de todas las cuentas
        for (Cuenta cuenta : cuentas) {
            cuenta.setEstado(false);
            cuentaRepositorio.save(cuenta);
        }

        // Baja lógica del cliente
        existente.setEstado(false);
        clienteRepositorio.save(existente);
	}
}