package com.usco.convocatorias.model.dto;

import com.usco.convocatorias.model.enums.EstadoConvocatoria;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoConvocatoriaDTO(
        @NotNull(message = "El estado es obligatorio") EstadoConvocatoria estado
) {}