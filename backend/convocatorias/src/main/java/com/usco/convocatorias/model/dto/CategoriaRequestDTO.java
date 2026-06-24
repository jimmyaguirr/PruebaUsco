package com.usco.convocatorias.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @Size(max = 255) String descripcion
) {}
