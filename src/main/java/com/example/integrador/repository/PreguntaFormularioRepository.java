package com.example.integrador.repository;

import com.example.integrador.model.PreguntaFormulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaFormularioRepository extends JpaRepository<PreguntaFormulario, Integer> {

    // Buscar preguntas por el ID del albergue
    @Query("SELECT p FROM PreguntaFormulario p WHERE p.albergue.idAlbergue = :idAlbergue")
    List<PreguntaFormulario> findByAlbergueId(@Param("idAlbergue") Long idAlbergue);
}
