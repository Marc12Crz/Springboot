package com.example.integrador.controller;

import com.example.integrador.model.Mascota;
import com.example.integrador.model.User;
import com.example.integrador.repository.MascotaRepository;
import com.example.integrador.repository.UserRepository;
import com.example.integrador.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @GetMapping("/{id}")
    public String detalleMascota(@PathVariable Integer id, Model model) {
        Mascota mascota = mascotaService.obtenerPorId(id);


        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no fue encontrada.");
        }

        model.addAttribute("mascota", mascota);
        return "detalleMascota";
    }

}
