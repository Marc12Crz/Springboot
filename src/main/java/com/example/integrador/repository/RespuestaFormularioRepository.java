package com.example.integrador.repository;

import com.example.integrador.model.RespuestaFormulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaFormularioRepository extends JpaRepository<RespuestaFormulario, Integer> {
}
