package com.usco.convocatorias.service.interfaces;


import com.usco.convocatorias.model.Convocatoria;
import com.usco.convocatorias.model.dto.ConvocatoriaPorCategoriaDTO;
import com.usco.convocatorias.model.dto.ConvocatoriaRequestDTO;
import com.usco.convocatorias.model.enums.EstadoConvocatoria;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ConvocatoriaService {

    List<Convocatoria> listarTodas();

    Convocatoria buscarPorId(UUID id);

    Convocatoria crear(ConvocatoriaRequestDTO convocatoria, Set<UUID> idsCategorias);

    Convocatoria actualizar(UUID id, ConvocatoriaRequestDTO datosActualizados, Set<UUID> idsCategorias);

    void eliminar(UUID id);

    Convocatoria cambiarEstado(UUID id, EstadoConvocatoria nuevoEstado);

    List<ConvocatoriaPorCategoriaDTO> reporteConvocatoriasPorCategoria();
}
