package com.usco.convocatorias.controller;


import com.usco.convocatorias.model.Categoria;
import com.usco.convocatorias.model.dto.CategoriaRequestDTO;
import com.usco.convocatorias.service.interfaces.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaRequestDTO dto) {

        Categoria creada = categoriaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable UUID id,
                                                @Valid @RequestBody CategoriaRequestDTO dto) {

        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}