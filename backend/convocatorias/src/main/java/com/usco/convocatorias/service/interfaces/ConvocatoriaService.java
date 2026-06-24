package com.usco.convocatorias.service.interfaces;


import com.usco.convocatorias.model.Convocatoria;
import com.usco.convocatorias.model.dto.ConvocatoriaPorCategoriaDTO;
import com.usco.convocatorias.model.enums.EstadoConvocatoria;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ConvocatoriaService {

    List<Convocatoria> listarTodas();

    Convocatoria buscarPorId(UUID id);

    Convocatoria crear(Convocatoria convocatoria, Set<UUID> idsCategorias);

    Convocatoria actualizar(UUID id, Convocatoria datosActualizados, Set<UUID> idsCategorias);

    void eliminar(UUID id);

    Convocatoria cambiarEstado(UUID id, EstadoConvocatoria nuevoEstado);

    List<ConvocatoriaPorCategoriaDTO> reporteConvocatoriasPorCategoria();
}
