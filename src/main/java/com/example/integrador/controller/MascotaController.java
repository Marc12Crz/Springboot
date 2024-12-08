package com.example.integrador.controller;

import com.example.integrador.model.Mascota;
import com.example.integrador.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    /**
     * Obtiene el detalle de una mascota por su ID.
     * @param id ID de la mascota.
     * @return Detalle de la mascota o un mensaje de error si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetalleMascota(@PathVariable String id) {
        try {
            // Validar que el ID sea un número
            Integer idNumerico = Integer.parseInt(id);

            // Buscar la mascota por su ID
            Mascota mascota = mascotaService.obtenerPorId(idNumerico);
            return ResponseEntity.ok(mascota);
        } catch (NumberFormatException e) {
            // Manejar error si el ID no es numérico
            return ResponseEntity.badRequest().body("El ID proporcionado no es válido.");
        } catch (RuntimeException e) {
            // Manejar caso de mascota no encontrada
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            // Manejo genérico de errores
            return ResponseEntity.status(500).body("Error interno del servidor.");
        }
    }

    /**
     * Lista todas las mascotas.
     * @return Lista de mascotas o un mensaje si no hay mascotas disponibles.
     */
    @GetMapping
    public ResponseEntity<?> listarTodasLasMascotas() {
        try {
            List<Mascota> mascotas = mascotaService.listarTodas();
            if (mascotas.isEmpty()) {
                return ResponseEntity.status(404).body("No hay mascotas disponibles.");
            }
            return ResponseEntity.ok(mascotas);
        } catch (Exception e) {
            // Manejo genérico de errores
            return ResponseEntity.status(500).body("Error interno del servidor.");
        }
    }
}
