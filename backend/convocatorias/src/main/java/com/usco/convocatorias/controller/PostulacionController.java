package com.usco.convocatorias.controller;


import com.usco.convocatorias.model.Postulacion;
import com.usco.convocatorias.model.dto.CambioEstadoPostulacionDTO;
import com.usco.convocatorias.model.dto.PostulacionRequestDTO;
import com.usco.convocatorias.model.dto.PostulacionesPorConvocatoriaDTO;
import com.usco.convocatorias.model.dto.ResultadoPostulacionesDTO;
import com.usco.convocatorias.service.interfaces.PostulacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/postulaciones")
@RequiredArgsConstructor
public class PostulacionController {

    private final PostulacionService postulacionService;

    @PostMapping
    public ResponseEntity<Postulacion> postular(@Valid @RequestBody PostulacionRequestDTO dto) {
        Postulacion creada = postulacionService.postular(dto.usuarioId(), dto.convocatoriaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<Postulacion>> listar(
            @RequestParam(required = false) UUID convocatoriaId,
            @RequestParam(required = false) UUID usuarioId) {

        if (convocatoriaId != null) {
            return ResponseEntity.ok(postulacionService.listarPorConvocatoria(convocatoriaId));
        }
        if (usuarioId != null) {
            return ResponseEntity.ok(postulacionService.listarPorUsuario(usuarioId));
        }
        return ResponseEntity.ok(postulacionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Postulacion> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(postulacionService.buscarPorId(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Postulacion> cambiarEstado(@PathVariable UUID id,
                                                     @Valid @RequestBody CambioEstadoPostulacionDTO dto) {
        return ResponseEntity.ok(postulacionService.cambiarEstado(id, dto.estado()));
    }

    @GetMapping("/reportes/postulaciones-convocatoria")
    public ResponseEntity<List<PostulacionesPorConvocatoriaDTO>> reportePostulacionesPorConvocatoria() {
        return ResponseEntity.ok(postulacionService.reportePostulacionesPorConvocatoria());
    }

    @GetMapping("/reportes/resultado-postulaciones")
    public ResponseEntity<List<ResultadoPostulacionesDTO>> reporteResultadoPostulaciones() {
        return ResponseEntity.ok(postulacionService.reporteResultadoPostulaciones());
    }
}