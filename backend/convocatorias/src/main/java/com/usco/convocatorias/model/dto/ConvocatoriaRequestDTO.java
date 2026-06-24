package com.usco.convocatorias.model.dto;

import com.usco.convocatorias.model.enums.EstadoConvocatoria;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record ConvocatoriaRequestDTO(
        @NotNull String nombre,
        String descripcion,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotNull Integer cuposDisponibles,
        EstadoConvocatoria estado,
        Set<UUID> idsCategorias
) {}