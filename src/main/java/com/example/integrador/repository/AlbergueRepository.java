package com.example.integrador.repository;

import com.example.integrador.model.Albergue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbergueRepository extends JpaRepository<Albergue, Long> {
}