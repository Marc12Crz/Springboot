package com.example.integrador.controller;

import com.example.integrador.model.Chat;
import com.example.integrador.model.User;
import com.example.integrador.model.Albergue;
import com.example.integrador.repository.ChatRepository;
import com.example.integrador.repository.AlbergueRepository;
import com.example.integrador.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatRepository chatRepository;
    private final AlbergueRepository albergueRepository;
    private final UserRepository userRepository;

    public ChatController(ChatRepository chatRepository, AlbergueRepository albergueRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.albergueRepository = albergueRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{idAlbergue}")
    public String mostrarChat(@PathVariable Long idAlbergue, Model model, Principal principal) {
        User user = validarUsuarioAutenticado(principal);
        
        Albergue albergue = albergueRepository.findById(idAlbergue)
                .orElseThrow(() -> new IllegalArgumentException("Albergue no encontrado."));

        List<Chat> mensajes = chatRepository.findByUsuarioAndAlbergue(user.getId(), idAlbergue);

        model.addAttribute("mensajes", mensajes);
        model.addAttribute("idAlbergue", idAlbergue);
        model.addAttribute("usuario", user);
        model.addAttribute("nombreAlbergue", albergue.getNombre()); // Añadir nombre del albergue

        return "chat";
    }

    @PostMapping("/{idAlbergue}/enviar")
    public String enviarMensaje(@PathVariable Long idAlbergue, @RequestParam String mensaje, Principal principal) {
        User user = validarUsuarioAutenticado(principal);

        // Validar que el albergue exista
        Albergue albergue = albergueRepository.findById(idAlbergue)
                .orElseThrow(() -> new IllegalArgumentException("Albergue no encontrado."));

        // Crear y guardar el nuevo mensaje
        Chat nuevoMensaje = new Chat();
        nuevoMensaje.setUsuario(user);
        nuevoMensaje.setAlbergue(albergue);
        nuevoMensaje.setMensaje(mensaje);
        nuevoMensaje.setFechaEnvio(LocalDateTime.now());
        nuevoMensaje.setEmisor(Chat.Emisor.USUARIO);

        chatRepository.save(nuevoMensaje);

        // Redirigir nuevamente al chat
        return "redirect:/chat/" + idAlbergue;
    }

    /**
     * Valida al usuario autenticado, ya sea mediante autenticación normal o OAuth2.
     */
    private User validarUsuarioAutenticado(Principal principal) {
        String correo = null;

        // Verificar si la autenticación es de tipo OAuth2
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            correo = (String) attributes.get("email"); // Obtener correo desde el token
        } else {
            // Autenticación estándar
            correo = principal.getName();
        }

        if (correo == null) {
            throw new IllegalArgumentException("No se pudo determinar el correo del usuario autenticado.");
        }

        // Buscar usuario en la base de datos
        User user = userRepository.findByCorreo(correo);

        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        return user;
    }
}
