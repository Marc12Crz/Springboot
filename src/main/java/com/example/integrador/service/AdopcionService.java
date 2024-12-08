package com.example.integrador.service;

import com.example.integrador.model.Adopcion;
import com.example.integrador.repository.AdopcionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdopcionService {

    private final AdopcionRepository adopcionRepository;

    public AdopcionService(AdopcionRepository adopcionRepository) {
        this.adopcionRepository = adopcionRepository;
    }

    public List<Adopcion> obtenerSolicitudesAdopcion(Long idUsuario) {
        // Aquí se asegura de que solo devuelva solicitudes para el usuario específico
        return adopcionRepository.findByUsuarioId(idUsuario);
    }
}
