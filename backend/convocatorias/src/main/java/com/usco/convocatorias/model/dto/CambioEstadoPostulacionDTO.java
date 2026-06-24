package com.usco.convocatorias.model.dto;


import com.usco.convocatorias.model.enums.EstadoPostulacion;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoPostulacionDTO(
        @NotNull(message = "El estado es obligatorio") EstadoPostulacion estado
) {}