package com.usco.convocatorias.controller;


import com.usco.convocatorias.model.Usuario;
import com.usco.convocatorias.model.dto.UsuarioRequestDTO;
import com.usco.convocatorias.service.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = Usuario.builder()
                .identificacion(dto.identificacion())
                .nombre(dto.nombre())
                .correo(dto.correo())
                // NOTA: sin Spring Security/BCrypt aún, se guarda temporalmente
                // en texto plano. Reemplazar por passwordEncoder.encode(dto.password())
                // cuando se integre seguridad.
                .passwordHash(dto.password())
                .rol(dto.rol())
                .estado(dto.estado())
                .build();

        Usuario creado = usuarioService.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable UUID id,
                                              @Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario datosActualizados = Usuario.builder()
                .identificacion(dto.identificacion())
                .nombre(dto.nombre())
                .correo(dto.correo())
                .passwordHash(dto.password())
                .rol(dto.rol())
                .estado(dto.estado())
                .build();

        return ResponseEntity.ok(usuarioService.actualizar(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
