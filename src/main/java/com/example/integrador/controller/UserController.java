package com.example.integrador.controller;

import com.example.integrador.model.Mascota;
import com.example.integrador.model.User;
import com.example.integrador.repository.MascotaRepository;
import com.example.integrador.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
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
    public String showRegistrationForm(Model model, Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            Map<String, Object> attributes = authToken.getPrincipal().getAttributes();


            model.addAttribute("nombre", attributes.get("name"));
            model.addAttribute("correo", attributes.get("email"));
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(String nombre, String correo, String contraseña, String direccion,
                               String telefono, String tipo_usuario, String departamento, String distrito, Model model) {
        if (userRepository.findByCorreo(correo) != null) {
            model.addAttribute("error", "El correo ya está registrado.");
            return "register";
        }

        User newUser = new User();
        newUser.setNombre(nombre);
        newUser.setCorreo(correo);
        newUser.setContraseña(passwordEncoder.encode(contraseña));
        newUser.setDireccion(direccion);
        newUser.setTelefono(telefono);
        newUser.setTipoUsuario(tipo_usuario);
        newUser.setDepartamento(departamento);
        newUser.setDistrito(distrito);

        userRepository.save(newUser);


        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(String correo, String contraseña, Model model) {
        User user = userRepository.findByCorreo(correo);
        if (user == null || !passwordEncoder.matches(contraseña, user.getContraseña())) {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }


        model.addAttribute("nombre", user.getNombre());
        return "welcome";
    }


    @GetMapping("/welcome")
    public String welcome(Model model, Principal principal) {
        String correo = null;
        User user = null;


        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
            correo = (String) attributes.get("email");
            user = userRepository.findByCorreo(correo);
        } else if (principal != null) {
            correo = principal.getName();
            user = userRepository.findByCorreo(correo);
        }


        if (user != null) {

            if ("albergue".equalsIgnoreCase(user.getTipoUsuario())) {
                String djangoUrl = "http://127.0.0.1:8000/albergue/mascotas/?correo=" + user.getCorreo();
                return "redirect:" + djangoUrl;
            }


            List<Mascota> mascotas = mascotaRepository.findByDepartamento(user.getDepartamento());
            model.addAttribute("mascotas", mascotas);
            model.addAttribute("nombre", user.getNombre());
        } else {

            model.addAttribute("error", "Usuario no encontrado. Por favor, inicie sesión nuevamente.");
            return "redirect:/login";
        }

        return "welcome";
    }







}