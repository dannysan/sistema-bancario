package com.banco.sistema.web;

import com.banco.sistema.dominio.Cliente;
import com.banco.sistema.dominio.Cuenta;
import com.banco.sistema.dominio.Persona;
import com.banco.sistema.repositorio.ClienteRepositorio;
import com.banco.sistema.repositorio.CuentaRepositorio;
import com.banco.sistema.repositorio.MovimientoRepositorio;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovimientoControladorIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private MovimientoRepositorio movimientoRepositorio;

    @Autowired
    private ObjectMapper objectMapper;

    private Long cuentaId;

    @BeforeEach
    void setUp() {
        movimientoRepositorio.deleteAll();
        cuentaRepositorio.deleteAll();
        clienteRepositorio.deleteAll();

        // 1. Crear Persona + Cliente
        Persona persona = new Persona();
        persona.setNombre("Jose Lema");
        persona.setGenero("M");
        persona.setEdad(30);
        persona.setIdentificacion("1234567890");
        persona.setDireccion("Otavalo sn y principal");
        persona.setTelefono("098254785");

        Cliente cliente = new Cliente();
        cliente.setPersona(persona);
        cliente.setContrasena("1234");
        cliente.setEstado(true);
        cliente = clienteRepositorio.save(cliente);

        // 2. Crear Cuenta con saldo inicial 2000
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta("Ahorro");
        cuenta.setSaldoInicial(new BigDecimal("2000.00"));
        cuenta.setSaldoDisponible(new BigDecimal("2000.00"));
        cuenta.setEstado(true);
        cuenta.setCliente(cliente);
        cuenta = cuentaRepositorio.save(cuenta);

        cuentaId = cuenta.getCuentaId();
    }

    /**
     * F6: Prueba de integración end-to-end del flujo de un movimiento.
     * Valida: controlador → servicio → repositorio → BD.
     * Cubre F2 (registro con actualización de saldo) y F3 (saldo no disponible).
     */
    @Test
    void testRegistrarMovimientoYActualizarSaldo() throws Exception {
        // ---------- CASO 1: Retiro válido ----------
        String bodyRetiro = objectMapper.writeValueAsString(
                Map.of("tipoMovimiento", "RETIRO", "valor", new BigDecimal("575.00")));

        mockMvc.perform(post("/api/movimientos/cuenta/{cuentaId}", cuentaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRetiro))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movimientoId").exists())
                .andExpect(jsonPath("$.tipoMovimiento").value("RETIRO"))
                .andExpect(jsonPath("$.valor").value(-575.00))
                .andExpect(jsonPath("$.saldo").value(1425.00));

        // Verificar que la cuenta se actualizó en BD
        Cuenta cuentaActualizada = cuentaRepositorio.findById(cuentaId).orElseThrow();
        assertEquals(0, new BigDecimal("1425.00").compareTo(cuentaActualizada.getSaldoDisponible()));

        // ---------- CASO 2: Retiro sin saldo suficiente (F3) ----------
        String bodyRetiroExcesivo = objectMapper.writeValueAsString(
                Map.of("tipoMovimiento", "RETIRO", "valor", new BigDecimal("99999.00")));

        mockMvc.perform(post("/api/movimientos/cuenta/{cuentaId}", cuentaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRetiroExcesivo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Saldo no disponible"));

        // El saldo NO debe haber cambiado
        Cuenta cuentaFinal = cuentaRepositorio.findById(cuentaId).orElseThrow();
        assertEquals(0, new BigDecimal("1425.00").compareTo(cuentaFinal.getSaldoDisponible()));

        // ---------- CASO 3: Depósito válido ----------
        String bodyDeposito = objectMapper.writeValueAsString(
                Map.of("tipoMovimiento", "DEPOSITO", "valor", new BigDecimal("600.00")));

        mockMvc.perform(post("/api/movimientos/cuenta/{cuentaId}", cuentaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyDeposito))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor").value(600.00))
                .andExpect(jsonPath("$.saldo").value(2025.00));

        // Verificar saldo final en BD
        Cuenta cuentaPostDeposito = cuentaRepositorio.findById(cuentaId).orElseThrow();
        assertEquals(0, new BigDecimal("2025.00").compareTo(cuentaPostDeposito.getSaldoDisponible()));
    }
}