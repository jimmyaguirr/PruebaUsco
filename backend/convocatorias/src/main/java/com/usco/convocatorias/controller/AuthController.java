package com.usco.convocatorias.controller;


import com.usco.convocatorias.model.Usuario;
import com.usco.convocatorias.model.dto.LoginRequestDTO;
import com.usco.convocatorias.model.dto.LoginResponseDTO;
import com.usco.convocatorias.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.correo(), dto.password()));

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtService.generarToken(usuario);

        LoginResponseDTO respuesta = new LoginResponseDTO(
                token, usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getRol());

        return ResponseEntity.ok(respuesta);
    }
}