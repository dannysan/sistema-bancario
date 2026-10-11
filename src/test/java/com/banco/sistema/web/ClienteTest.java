package com.banco.sistema.web;

import org.junit.jupiter.api.Test;

import com.banco.sistema.dominio.Cliente;
import com.banco.sistema.dominio.Persona;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void testCrearClienteConDatosValidos() {
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

        assertNotNull(cliente);
        assertEquals("Jose Lema", cliente.getNombre());
        assertEquals("1234567890", cliente.getIdentificacion());
        assertTrue(cliente.getEstado());
    }

    @Test
    void testEstadoPorDefectoEsTrue() {
        Cliente cliente = new Cliente();
        assertTrue(cliente.getEstado());
    }
}