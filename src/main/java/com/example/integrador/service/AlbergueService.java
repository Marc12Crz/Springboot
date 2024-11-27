package com.example.integrador.service;

import com.example.integrador.model.Albergue;
import com.example.integrador.repository.AlbergueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbergueService {

    @Autowired
    private AlbergueRepository albergueRepository;

    // Método para listar todos los albergues
    public List<Albergue> listarAlbergues() {
        return albergueRepository.findAll();
    }
}