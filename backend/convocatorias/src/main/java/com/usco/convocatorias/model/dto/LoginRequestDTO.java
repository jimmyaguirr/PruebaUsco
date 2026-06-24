package com.usco.convocatorias.model.dto;

import jakarta.validation.constraints.NotBlank;
public record LoginRequestDTO(
        @NotBlank(message = "El correo es obligatorio") String correo,
        @NotBlank(message = "La contraseña es obligatoria") String password
) {}
