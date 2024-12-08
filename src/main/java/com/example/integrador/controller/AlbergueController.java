package com.example.integrador.controller;

import com.example.integrador.model.Albergue;
import com.example.integrador.model.Mascota;
import com.example.integrador.service.AlbergueService;
import com.example.integrador.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albergues")
public class AlbergueController {

    @Autowired
    private AlbergueService albergueService;
    @Autowired
    private MascotaService mascotaService;

    @GetMapping
    public ResponseEntity<List<Albergue>> listarAlbergues() {
        List<Albergue> albergues = albergueService.listarAlbergues();
        return ResponseEntity.ok(albergues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAlbergue(@PathVariable Long id) {
        Albergue albergue = albergueService.obtenerPorId(id);
        if (albergue == null) {
            return ResponseEntity.status(404).body("Albergue no encontrado.");
        }
        return ResponseEntity.ok(albergue);
    }

    @PostMapping
    public ResponseEntity<?> guardarAlbergue(@RequestBody Albergue albergue) {
        Albergue albergueGuardado = albergueService.guardarAlbergue(albergue);
        return ResponseEntity.ok(albergueGuardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAlbergue(@PathVariable Long id) {
        boolean eliminado = albergueService.eliminarAlbergue(id);
        if (!eliminado) {
            return ResponseEntity.status(404).body("El albergue no existe.");
        }
        return ResponseEntity.ok("Albergue eliminado correctamente.");
    }

    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<?> buscarPorDepartamento(@PathVariable String departamento) {
        List<Albergue> albergues = albergueService.buscarPorDepartamento(departamento);
        if (albergues.isEmpty()) {
            return ResponseEntity.status(404).body("No se encontraron albergues en el departamento especificado.");
        }
        return ResponseEntity.ok(albergues);
    }

    @GetMapping("/distrito/{distrito}")
    public ResponseEntity<?> buscarPorDistrito(@PathVariable String distrito) {
        List<Albergue> albergues = albergueService.buscarPorDistrito(distrito);
        if (albergues.isEmpty()) {
            return ResponseEntity.status(404).body("No se encontraron albergues en el distrito especificado.");
        }
        return ResponseEntity.ok(albergues);
    }
    @GetMapping("/{idAlbergue}/mascotas")
    public ResponseEntity<?> obtenerMascotasPorAlbergue(@PathVariable Integer idAlbergue) {
        try {
            List<Mascota> mascotas = mascotaService.obtenerMascotasPorAlbergue(idAlbergue);
            if (mascotas.isEmpty()) {
                return ResponseEntity.status(404).body("No hay mascotas disponibles para este albergue.");
            }
            return ResponseEntity.ok(mascotas);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener mascotas.");
        }
    }

}
