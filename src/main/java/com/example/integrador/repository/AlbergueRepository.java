package com.example.integrador.repository;

import com.example.integrador.model.Albergue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbergueRepository extends JpaRepository<Albergue, Long> {

    /**
     * Buscar albergues por departamento.
     * @param departamento Nombre del departamento.
     * @return Lista de albergues en el departamento.
     */
    List<Albergue> findByDepartamento(String departamento);

    /**
     * Buscar albergues por distrito.
     * @param distrito Nombre del distrito.
     * @return Lista de albergues en el distrito.
     */
    List<Albergue> findByDistrito(String distrito);
}
