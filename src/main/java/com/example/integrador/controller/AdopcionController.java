package com.example.integrador.controller;

import com.example.integrador.model.Adopcion;
import com.example.integrador.model.PreguntaFormulario;
import com.example.integrador.model.RespuestaFormulario;
import com.example.integrador.model.User;
import com.example.integrador.repository.AdopcionRepository;
import com.example.integrador.repository.MascotaRepository;
import com.example.integrador.repository.PreguntaFormularioRepository;
import com.example.integrador.repository.RespuestaFormularioRepository;
import com.example.integrador.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adopcion")
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

    @GetMapping("/{idPerro}/formulario")
    public String mostrarFormularioAdopcion(@PathVariable Integer idPerro, Model model) {

        var mascota = mascotaRepository.findById(idPerro)
                .orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada"));


        var albergue = mascota.getAlbergue();


        List<PreguntaFormulario> preguntas = preguntaFormularioRepository.findByAlbergueId(albergue.getIdAlbergue());


        model.addAttribute("preguntas", preguntas);
        model.addAttribute("idPerro", idPerro);

        return "formularioAdopcion";
    }

    @PostMapping("/{idPerro}/guardar")
    public String guardarRespuestasAdopcion(
            @PathVariable Integer idPerro,
            @RequestParam Map<String, String> respuestas,
            Principal principal) {


        String correo = obtenerCorreoAutenticado(principal);

        if (correo == null || correo.isEmpty()) {
            throw new IllegalArgumentException("No se pudo determinar el correo del usuario autenticado.");
        }


        User user = userRepository.findByCorreo(correo);

        if (user == null) {
            throw new IllegalArgumentException("Usuario con correo " + correo + " no encontrado.");
        }


        Adopcion adopcion = new Adopcion();
        adopcion.setUsuario(user);
        adopcion.setMascota(mascotaRepository.findById(idPerro)
                .orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada.")));
        adopcion.setEstadoSolicitud(Adopcion.EstadoSolicitud.PENDIENTE);
        adopcion.setFechaSolicitud(new Date());


        adopcion = adopcionRepository.save(adopcion);


        for (Map.Entry<String, String> entry : respuestas.entrySet()) {
            try {
                Integer idPregunta = Integer.parseInt(entry.getKey().replace("respuesta_", ""));
                RespuestaFormulario respuesta = new RespuestaFormulario();
                respuesta.setAdopcion(adopcion);
                respuesta.setPregunta(preguntaFormularioRepository.findById(idPregunta)
                        .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada.")));
                respuesta.setRespuesta(entry.getValue());
                respuestaFormularioRepository.save(respuesta);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El formato de las claves de las respuestas es incorrecto.", e);
            }
        }

        return "adopcionExitosa";
    }

    private String obtenerCorreoAutenticado(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {

            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
            Map<String, Object> attributes = oauth2Token.getPrincipal().getAttributes();
            return (String) attributes.get("email");
        } else {
            return principal.getName();
        }
    }


}
