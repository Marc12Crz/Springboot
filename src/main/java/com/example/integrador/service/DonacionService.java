package com.example.integrador.service;

import com.example.integrador.model.Donacion;
import com.example.integrador.repository.DonacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonacionService {
    @Autowired
    private DonacionRepository donacionRepository;

    public Donacion guardarDonacion(Donacion donacion) {
        return donacionRepository.save(donacion);
    }
}
