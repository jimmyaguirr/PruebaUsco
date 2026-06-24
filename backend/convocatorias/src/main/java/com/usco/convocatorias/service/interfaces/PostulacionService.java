package com.usco.convocatorias.service.interfaces;



import com.usco.convocatorias.model.Postulacion;
import com.usco.convocatorias.model.dto.PostulacionesPorConvocatoriaDTO;
import com.usco.convocatorias.model.dto.ResultadoPostulacionesDTO;
import com.usco.convocatorias.model.enums.EstadoPostulacion;

import java.util.List;
import java.util.UUID;

public interface PostulacionService {

    Postulacion postular(UUID usuarioId, UUID convocatoriaId);

    List<Postulacion> listarTodas();

    Postulacion buscarPorId(UUID id);

    List<Postulacion> listarPorConvocatoria(UUID convocatoriaId);

    List<Postulacion> listarPorUsuario(UUID usuarioId);

    Postulacion cambiarEstado(UUID id, EstadoPostulacion nuevoEstado);

    List<PostulacionesPorConvocatoriaDTO> reportePostulacionesPorConvocatoria();

    List<ResultadoPostulacionesDTO> reporteResultadoPostulaciones();
}