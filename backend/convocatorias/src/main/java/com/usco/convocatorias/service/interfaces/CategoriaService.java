package com.usco.convocatorias.service.interfaces;

import com.usco.convocatorias.model.Categoria;
import com.usco.convocatorias.model.dto.CategoriaRequestDTO;

import java.util.List;
import java.util.UUID;

public interface CategoriaService {

    List<Categoria> listarTodas();

    Categoria buscarPorId(UUID id);

    Categoria crear(CategoriaRequestDTO categoria);

    Categoria actualizar(UUID id, CategoriaRequestDTO datosActualizados);

    void eliminar(UUID id);
}