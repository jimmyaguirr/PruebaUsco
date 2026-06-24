package com.usco.convocatorias.model.dto;


import com.usco.convocatorias.model.enums.EstadoUsuario;
import com.usco.convocatorias.model.enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDTO(
        @NotBlank(message = "La identificación es obligatoria") String identificacion,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank @Email(message = "El correo debe tener un formato válido") String correo,
        @NotBlank(message = "La contraseña es obligatoria") String password,
        @NotNull(message = "El rol es obligatorio") RolUsuario rol,
        EstadoUsuario estado
) {}