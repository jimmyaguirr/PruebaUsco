package com.usco.convocatorias.controller;


import com.usco.convocatorias.model.Convocatoria;
import com.usco.convocatorias.model.dto.CambioEstadoConvocatoriaDTO;
import com.usco.convocatorias.model.dto.ConvocatoriaPorCategoriaDTO;
import com.usco.convocatorias.model.dto.ConvocatoriaRequestDTO;
import com.usco.convocatorias.service.interfaces.ConvocatoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/convocatorias")
@RequiredArgsConstructor
public class ConvocatoriaController {

    private final ConvocatoriaService convocatoriaService;

    @GetMapping
    public ResponseEntity<List<Convocatoria>> listar() {
        return ResponseEntity.ok(convocatoriaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Convocatoria> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(convocatoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Convocatoria> crear(@Valid @RequestBody ConvocatoriaRequestDTO dto) {
        Convocatoria convocatoria = Convocatoria.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .fechaInicio(dto.fechaInicio())
                .fechaFin(dto.fechaFin())
                .cuposDisponibles(dto.cuposDisponibles())
                .estado(dto.estado())
                .build();

        Convocatoria creada = convocatoriaService.crear(convocatoria, dto.idsCategorias());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Convocatoria> actualizar(@PathVariable UUID id,
                                                   @Valid @RequestBody ConvocatoriaRequestDTO dto) {
        Convocatoria datosActualizados = Convocatoria.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .fechaInicio(dto.fechaInicio())
                .fechaFin(dto.fechaFin())
                .cuposDisponibles(dto.cuposDisponibles())
                .estado(dto.estado())
                .build();

        Convocatoria actualizada = convocatoriaService.actualizar(id, datosActualizados, dto.idsCategorias());
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        convocatoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Convocatoria> cambiarEstado(@PathVariable UUID id,
                                                      @Valid @RequestBody CambioEstadoConvocatoriaDTO dto) {
        return ResponseEntity.ok(convocatoriaService.cambiarEstado(id, dto.estado()));
    }


}