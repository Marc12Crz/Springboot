package com.example.integrador.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Map;

@Configuration
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;

    public SecurityConfig(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/mascotas/**").permitAll()  // Permite acceso sin autenticación a todas las rutas de mascotas
                        .requestMatchers("/", "/api/users/register", "/api/users/login", "/api/albergues", "/oauth2/**", "/login", "/api/donaciones/**","/api/donaciones/solicitudes","/api/donaciones/guardar").permitAll()
                        .requestMatchers("/api/adopcion/**").authenticated()
                        .anyRequest().authenticated()
                )


                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")  // Página de login personalizada, si es necesario
                        .defaultSuccessUrl("http://localhost:5173/welcome", true)// Redirigir a la página de bienvenida después del login exitoso
                        .failureUrl("/login?error=true") // URL de error si la autenticación falla
                )
                .logout(logout -> logout
                        .logoutUrl("/api/users/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            SecurityContextHolder.clearContext();  // Clear the security context
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("{\"logout\":\"success\"}");
                            response.getWriter().flush();
                        })
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Change this from ALWAYS to IF_REQUIRED
                )

                .csrf().disable()
                .addFilterBefore((request, response, chain) -> {
                    if (SecurityContextHolder.getContext().getAuthentication() != null) {
                        System.out.println("Autenticado: " + SecurityContextHolder.getContext().getAuthentication().getName());
                    } else {
                        System.out.println("SecurityContext es nulo o anónimo.");
                    }
                    chain.doFilter(request, response);
                }, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("Solicitud no autorizada para URL: " + request.getRequestURI());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
                        })
                );
        return http.build();
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    public class CustomOAuth2UserService extends DefaultOAuth2UserService {
        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            OAuth2User user = super.loadUser(userRequest);
            Map<String, Object> attributes = user.getAttributes();

            // Puedes procesar atributos adicionales aquí, como el correo y nombre
            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");

            // Agrega lógica adicional si necesitas guardar datos o validar el usuario
            return new DefaultOAuth2User(
                    user.getAuthorities(),
                    attributes,
                    "sub" // O cualquier clave única proporcionada por Google
            );
        }
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            response.sendRedirect("/login?error=true&message=" + exception.getMessage());
        };
    }

}
