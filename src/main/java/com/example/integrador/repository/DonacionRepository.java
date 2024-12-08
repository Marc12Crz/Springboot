package com.example.integrador.repository;

import com.example.integrador.model.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonacionRepository extends JpaRepository<Donacion, Integer> {
}
