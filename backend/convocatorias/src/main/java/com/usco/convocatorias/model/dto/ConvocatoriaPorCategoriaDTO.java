package com.usco.convocatorias.model.dto;

import java.util.UUID;

public record ConvocatoriaPorCategoriaDTO(
        UUID categoriaId,
        String categoriaNombre,
        long totalConvocatorias
) {}