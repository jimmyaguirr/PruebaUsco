package com.usco.convocatorias.model.dto;

import com.usco.convocatorias.model.enums.RolUsuario;

import java.util.UUID;

public record LoginResponseDTO(
        String token,
        UUID id,
        String nombre,
        String correo,
        RolUsuario rol
) {}