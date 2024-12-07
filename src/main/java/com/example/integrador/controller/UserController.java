package com.example.integrador.controller;

import com.example.integrador.model.Mascota;
import com.example.integrador.model.User;
import com.example.integrador.repository.MascotaRepository;
import com.example.integrador.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByCorreo(user.getCorreo()) != null) {
            return ResponseEntity.badRequest().body("El correo ya está registrado.");
        }

        user.setContraseña(passwordEncoder.encode(user.getContraseña()));
        userRepository.save(user);
        return ResponseEntity.ok("Usuario registrado con éxito.");
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

        // Configurar el SecurityContext con el correo como principal
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(correo, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Almacenar el contexto en la sesión
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
        String correo = null;

        // Verificar el tipo de autenticación y extraer el correo
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
            correo = (String) attributes.get("email");
            System.out.println("Correo extraído de OAuth2: " + correo);
        } else if (principal != null) {
            correo = principal.getName();
            System.out.println("Correo extraído del principal: " + correo);
        }

        if (correo == null) {
            System.out.println("El usuario no está autenticado.");
            return ResponseEntity.status(401).body("No estás autenticado.");
        }

        // Buscar el usuario en la base de datos
        User user = userRepository.findByCorreo(correo);
        if (user == null) {
            System.out.println("Usuario no encontrado en la base de datos para el correo: " + correo);
            return ResponseEntity.status(404).body("Usuario no encontrado.");
        }

        // Si es un albergue, redirigir a Django
        if ("albergue".equalsIgnoreCase(user.getTipoUsuario())) {
            String redireccion = "Redirigir a la gestión de mascotas en Django.";
            System.out.println("Redirigiendo para el usuario tipo albergue: " + correo);
            return ResponseEntity.ok(redireccion);
        }

        // Si es un adoptante, listar las mascotas según su departamento
        List<Mascota> mascotas = mascotaRepository.findByDepartamento(user.getDepartamento());
        System.out.println("Mascotas encontradas para el usuario: " + correo + ", cantidad: " + mascotas.size());
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

}
