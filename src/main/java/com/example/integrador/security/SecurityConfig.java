package com.example.integrador.security;

import com.example.integrador.model.User;
import com.example.integrador.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepository;

    public SecurityConfig(MyUserDetailsService myUserDetailsService, UserRepository userRepository) {
        this.myUserDetailsService = myUserDetailsService;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/", "/login", "/register", "/error", "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/welcome", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .successHandler(oAuth2SuccessHandler(userRepository))
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                .csrf().disable();
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2SuccessHandler(UserRepository userRepository) {
        return (request, response, authentication) -> {
            OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
            String email = auth.getPrincipal().getAttribute("email");

            // Verifica si el usuario existe en la base de datos
            User user = userRepository.findByCorreo(email);

            if (user == null || user.getDireccion() == null || user.getTelefono() == null) {
                // Si el usuario no está completamente registrado, redirige al registro
                response.sendRedirect("/register");
            } else {
                // Si ya está registrado, redirige a welcome
                response.sendRedirect("/welcome");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}
