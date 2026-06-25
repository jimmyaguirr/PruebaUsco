package com.usco.convocatorias.config;

import com.usco.convocatorias.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ---------------------------------------------------
                        // Público: login
                        // ---------------------------------------------------
                        .requestMatchers("/api/auth/**").permitAll()

                        // ---------------------------------------------------
                        // Usuarios: gestión completa solo ADMINISTRADOR
                        // ---------------------------------------------------
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")

                        // ---------------------------------------------------
                        // Categorías: lectura para cualquier autenticado,
                        // escritura solo ADMINISTRADOR
                        // ---------------------------------------------------
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMINISTRADOR")

                        // ---------------------------------------------------
                        // Convocatorias: lectura para cualquier autenticado
                        // (los estudiantes deben verlas para postularse),
                        // escritura solo ADMINISTRADOR
                        // ---------------------------------------------------
                        .requestMatchers(HttpMethod.GET, "/api/convocatorias/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/convocatorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/convocatorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/convocatorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/convocatorias/**").hasRole("ADMINISTRADOR")

                        // ---------------------------------------------------
                        // Postulaciones: crear y leer para cualquier
                        // autenticado (estudiante postula y consulta las
                        // suyas); cambiar estado (aprobar/rechazar) solo
                        // ADMINISTRADOR
                        // ---------------------------------------------------
                        .requestMatchers(HttpMethod.POST, "/api/postulaciones").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/postulaciones/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/postulaciones/*/estado").hasAnyRole("ADMINISTRADOR", "DOCENTE")

                        // ---------------------------------------------------
                        // Reportes: solo ADMINISTRADOR
                        // ---------------------------------------------------
                        .requestMatchers("/api/reportes/**").hasRole("ADMINISTRADOR")

                        // ---------------------------------------------------
                        // Cualquier otra ruta no listada: requiere estar
                        // autenticado como mínimo
                        // ---------------------------------------------------
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}