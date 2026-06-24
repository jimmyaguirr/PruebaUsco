package com.usco.convocatorias.model.dto;

import java.util.UUID;

public record PostulacionesPorConvocatoriaDTO(
        UUID convocatoriaId,
        String convocatoriaNombre,
        long totalPostulaciones
) {}
