package com.usco.convocatorias.service.interfaces;

import com.usco.convocatorias.model.Categoria;

import java.util.List;
import java.util.UUID;

public interface CategoriaService {

    List<Categoria> listarTodas();

    Categoria buscarPorId(UUID id);

    Categoria crear(Categoria categoria);

    Categoria actualizar(UUID id, Categoria datosActualizados);

    void eliminar(UUID id);
}