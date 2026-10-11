package com.banco.sistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banco.sistema.dominio.Persona;

public interface PersonaRepositorio extends JpaRepository<Persona, Long> {
}