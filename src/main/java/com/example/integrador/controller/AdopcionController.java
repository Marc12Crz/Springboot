package com.example.integrador.controller;

import com.example.integrador.model.*;
import com.example.integrador.repository.*;
import com.example.integrador.service.AdopcionService;
import com.example.integrador.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/adopcion")
public class AdopcionController {

    @Autowired
    private PreguntaFormularioRepository preguntaFormularioRepository;

    @Autowired
    private RespuestaFormularioRepository respuestaFormularioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private AlbergueRepository albergueRepository;
    @Autowired
    private AdopcionService adopcionService;
    @Autowired
    private UserService userService;

    @GetMapping("/{idPerro}/formulario")
    public ResponseEntity<?> obtenerPreguntasFormulario(@PathVariable Integer idPerro) {
        Mascota mascota = mascotaRepository.findById(idPerro)
                .orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada"));

        Albergue albergue = mascota.getAlbergue();

        List<PreguntaFormulario> preguntas = preguntaFormularioRepository.findByAlbergueId(albergue.getIdAlbergue());

        return ResponseEntity.ok(Map.of(
                "preguntas", preguntas,
                "idPerro", idPerro
        ));
    }

    @PostMapping("/{idPerro}/guardar")
    public ResponseEntity<?> guardarRespuestasAdopcion(
            @PathVariable Integer idPerro,
            @RequestBody Map<String, String> respuestas,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("El usuario no está autenticado.");
        }

        User usuario = userRepository.findByCorreo(principal.getName());
        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        Mascota mascota = mascotaRepository.findById(idPerro)
                .orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada."));

        Adopcion adopcion = new Adopcion();
        adopcion.setUsuario(usuario);
        adopcion.setMascota(mascota);
        adopcion.setEstadoSolicitud(Adopcion.EstadoSolicitud.PENDIENTE);
        adopcion.setFechaSolicitud(new Date());
        adopcionRepository.save(adopcion);

        try {
            for (Map.Entry<String, String> entry : respuestas.entrySet()) {
                Integer idPregunta = Integer.parseInt(entry.getKey().replace("respuesta_", ""));
                PreguntaFormulario pregunta = preguntaFormularioRepository.findById(idPregunta)
                        .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada."));

                RespuestaFormulario respuesta = new RespuestaFormulario();
                respuesta.setAdopcion(adopcion);
                respuesta.setPregunta(pregunta);
                respuesta.setRespuesta(entry.getValue());

                respuestaFormularioRepository.save(respuesta);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Error en el formato de las respuestas.");
        }

        return ResponseEntity.ok("Formulario de adopción guardado con éxito.");
    }
    @GetMapping("/solicitudes")
    public ResponseEntity<?> obtenerSolicitudesDelUsuario(Principal principal) {
        try {
            String correo = userService.getCorreoFromPrincipal(principal);
            User usuario = userService.obtenerPorCorreo(correo);

            // Verifica el idUsuario
            System.out.println("Usuario autenticado: " + usuario.getId() + " - " + usuario.getCorreo());

            List<Adopcion> solicitudes = adopcionService.obtenerSolicitudesAdopcion(usuario.getId());
            return ResponseEntity.ok(solicitudes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener las solicitudes: " + e.getMessage());
        }
    }



}
