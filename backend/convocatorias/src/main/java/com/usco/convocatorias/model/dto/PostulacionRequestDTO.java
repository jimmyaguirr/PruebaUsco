package com.usco.convocatorias.model.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PostulacionRequestDTO(
        @NotNull(message = "El usuario es obligatorio") UUID usuarioId,
        @NotNull(message = "La convocatoria es obligatoria") UUID convocatoriaId
) {}