package com.example.integrador.controller;

import com.example.integrador.model.Mascota;
import com.example.integrador.model.User;
import com.example.integrador.repository.MascotaRepository;
import com.example.integrador.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MascotaRepository mascotaRepository;

    public UserController(UserRepository userRepository, MascotaRepository mascotaRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.mascotaRepository = mascotaRepository;
    }
    @GetMapping("/register")
    public ResponseEntity<?> mostrarFormularioRegistro(@AuthenticationPrincipal OAuth2AuthenticationToken principal) {
        if (principal != null) {
            Map<String, Object> atributos = principal.getPrincipal().getAttributes();
            Map<String, String> usuario = Map.of(
                    "nombre", atributos.get("name").toString(),
                    "correo", atributos.get("email").toString()
            );
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.ok(Map.of());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> datosUsuario) {
        if (userRepository.findByCorreo(datosUsuario.get("correo")) != null) {
            return ResponseEntity.badRequest().body("El correo ya está registrado.");
        }
        User nuevoUsuario = new User();
        nuevoUsuario.setNombre(datosUsuario.get("nombre"));
        nuevoUsuario.setCorreo(datosUsuario.get("correo"));
        nuevoUsuario.setContraseña(passwordEncoder.encode(datosUsuario.get("contraseña")));
        nuevoUsuario.setDireccion(datosUsuario.get("direccion"));
        nuevoUsuario.setTelefono(datosUsuario.get("telefono"));
        nuevoUsuario.setTipoUsuario(datosUsuario.get("tipo_usuario"));
        nuevoUsuario.setDepartamento(datosUsuario.get("departamento"));
        nuevoUsuario.setDistrito(datosUsuario.get("distrito"));
        userRepository.save(nuevoUsuario);
        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }

    @GetMapping("/login")
    public ResponseEntity<?> mostrarFormularioLogin() {
        return ResponseEntity.ok("API de inicio de sesión listo");
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String correo = credentials.get("correo");
        String contraseña = credentials.get("contraseña");

        User user = userRepository.findByCorreo(correo);
        if (user == null || !passwordEncoder.matches(contraseña, user.getContraseña())) {
            System.out.println("Correo o contraseña incorrectos: " + correo);
            return ResponseEntity.status(401).body("Correo o contraseña incorrectos.");
        }


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(correo, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        System.out.println("Login exitoso: " + user.getCorreo());
        System.out.println("SecurityContext almacenado para: " +
                SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("ID de sesión: " + request.getSession().getId());

        return ResponseEntity.ok(Map.of(
                "usuario", user,
                "sessionId", request.getSession().getId()
        ));
    }



    @GetMapping("/welcome")
    public ResponseEntity<?> welcome(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = null;
        User user = null;

        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            email = oauthToken.getPrincipal().getAttribute("email");
        } else {
            email = principal.getName(); // For non-OAuth2 logins
        }

        user = userRepository.findByCorreo(email.toLowerCase());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<Mascota> mascotas = mascotaRepository.findByDepartamento(user.getDepartamento());
        if (mascotas.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyMap());
        }

        return ResponseEntity.ok(mascotas);
    }



    @GetMapping("/perfil")
    public ResponseEntity<?> verPerfil(Principal principal) {
        if (principal == null) {
            System.out.println("El usuario no está autenticado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No estás autenticado.");
        }

        // Verificar el contexto de seguridad
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println("Contexto activo: " +
                    SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            System.out.println("Contexto de seguridad no encontrado.");
        }

        String correo = principal.getName(); // Ahora obtendrá el correo correctamente
        System.out.println("Correo extraído del principal: " + correo);

        User usuario = userRepository.findByCorreo(correo);
        if (usuario == null) {
            System.out.println("Usuario no encontrado en la base de datos para el correo: " + correo);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        return ResponseEntity.ok(usuario);
    }



    @PutMapping("/editar-perfil")
    public ResponseEntity<?> editarPerfil(@RequestBody User usuarioActualizado, Principal principal) {
        String correo = getCorreoFromPrincipal(principal);

        if (correo == null) {
            return ResponseEntity.badRequest().body("No se pudo obtener el correo del usuario.");
        }

        User usuario = userRepository.findByCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado.");
        }

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        usuario.setDistrito(usuarioActualizado.getDistrito());
        usuario.setDepartamento(usuarioActualizado.getDepartamento());
        usuario.setImagen(usuarioActualizado.getImagen());

        userRepository.save(usuario);
        return ResponseEntity.ok("Perfil actualizado con éxito.");
    }

    private String getCorreoFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
            String email = (String) attributes.get("email");
            System.out.println("Correo extraído de OAuth2: " + email);
            return email;
        } else if (principal != null) {
            System.out.println("Correo extraído del Principal: " + principal.getName());
            return principal.getName();
        }
        System.out.println("Principal es nulo o no válido");
        return null;
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Invalidar la sesión
            request.getSession().invalidate();

            // Limpiar el SecurityContext
            SecurityContextHolder.clearContext();

            // Log de sesión cerrada
            System.out.println("Sesión cerrada correctamente");

            // Devolver URL para redireccionamiento del cliente
            return ResponseEntity.ok(Map.of("message", "Sesión cerrada con éxito.", "redirectUrl", "http://localhost:3000/login"));
        } catch (Exception e) {
            System.out.println("Error al cerrar la sesión: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cerrar sesión.");
        }
    }






}
