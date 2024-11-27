package com.example.integrador.service;

import com.example.integrador.model.Mascota;
import com.example.integrador.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    public Mascota obtenerPorId(int id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La mascota con ID " + id + " no fue encontrada."));
    }
}
