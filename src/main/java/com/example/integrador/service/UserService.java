package com.example.integrador.service;

import com.example.integrador.model.User;
import com.example.integrador.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param correo Correo del usuario.
     * @return Usuario encontrado.
     * @throws IllegalArgumentException si no se encuentra el usuario.
     */
    public User obtenerPorCorreo(String correo) {
        User user = userRepository.findByCorreo(correo);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado con correo: " + correo);
        }
        return user;
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado.
     * @throws IllegalArgumentException si no se encuentra el usuario.
     */
    public User obtenerPorId(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Guarda o actualiza un usuario en la base de datos.
     *
     * @param user Usuario a guardar o actualizar.
     * @return Usuario guardado.
     */
    public User guardarUsuario(User user) {
        return userRepository.save(user);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @throws IllegalArgumentException si el usuario no existe.
     */
    public void eliminarUsuario(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Lista todos los usuarios en la base de datos.
     *
     * @return Iterable con todos los usuarios.
     */
    public Iterable<User> listarUsuarios() {
        return userRepository.findAll();
    }

    /**
     * Obtiene el correo electrónico desde un Principal (autenticación).
     *
     * @param principal Principal del usuario autenticado.
     * @return Correo electrónico del usuario.
     * @throws IllegalArgumentException si el correo no se puede obtener.
     */
    public String getCorreoFromPrincipal(Principal principal) {
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
        throw new IllegalArgumentException("No se pudo obtener el correo del Principal");
    }
}
