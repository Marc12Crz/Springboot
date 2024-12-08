package com.example.integrador.repository;

import com.example.integrador.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {

    @Query("SELECT m FROM Mascota m WHERE m.albergue.departamento = :departamento")
    List<Mascota> findByDepartamento(@Param("departamento") String departamento);

    List<Mascota> findByAlbergueIdAlbergue(Integer idAlbergue);
}

