package com.example.integrador.service;

import com.example.integrador.model.Mascota;
import com.example.integrador.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    /**
     * Busca una mascota por su ID.
     * @param id ID de la mascota.
     * @return La mascota encontrada o lanza una excepción si no existe.
     */
    public Mascota obtenerPorId(int id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La mascota con ID " + id + " no fue encontrada."));
    }
    public List<Mascota> obtenerMascotasPorAlbergue(Integer idAlbergue) {
        return mascotaRepository.findByAlbergueIdAlbergue(idAlbergue);
    }
    /**
     * Lista todas las mascotas disponibles.
     * @return Lista de mascotas.
     */
    public List<Mascota> listarTodas() {
        return mascotaRepository.findAll();
    }
}
