package com.example.integrador.repository;

import com.example.integrador.model.Adopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdopcionRepository extends JpaRepository<Adopcion, Integer> {
}
