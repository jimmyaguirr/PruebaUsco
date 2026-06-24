package com.usco.convocatorias.service.implement;


import com.usco.convocatorias.excepcion.ReglaNegocioException;
import com.usco.convocatorias.excepcion.RecursoNoEncontradoException;

import com.usco.convocatorias.model.Convocatoria;
import com.usco.convocatorias.model.Postulacion;
import com.usco.convocatorias.model.Usuario;
import com.usco.convocatorias.model.dto.PostulacionesPorConvocatoriaDTO;
import com.usco.convocatorias.model.dto.ResultadoPostulacionesDTO;
import com.usco.convocatorias.model.enums.EstadoConvocatoria;
import com.usco.convocatorias.model.enums.EstadoPostulacion;
import com.usco.convocatorias.respository.ConvocatoriaRepository;
import com.usco.convocatorias.respository.PostulacionRepository;
import com.usco.convocatorias.respository.UsuarioRepository;
import com.usco.convocatorias.service.interfaces.PostulacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PostulacionServiceImpl implements PostulacionService {

    private final PostulacionRepository postulacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConvocatoriaRepository convocatoriaRepository;

    @Override
    public Postulacion postular(UUID usuarioId, UUID convocatoriaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario con id " + usuarioId));

        Convocatoria convocatoria = convocatoriaRepository.findById(convocatoriaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la convocatoria con id " + convocatoriaId));

        // Validación 1: no postularse dos veces a la misma convocatoria
        if (postulacionRepository.existsByUsuarioAndConvocatoria(usuario, convocatoria)) {
            throw new ReglaNegocioException(
                    "El usuario ya se encuentra postulado a esta convocatoria");
        }

        // Validación 2: no postularse a convocatorias cerradas (ni en borrador)
        if (convocatoria.getEstado() == EstadoConvocatoria.CERRADA) {
            throw new ReglaNegocioException(
                    "No es posible postularse: la convocatoria se encuentra cerrada");
        }
        if (convocatoria.getEstado() == EstadoConvocatoria.BORRADOR) {
            throw new ReglaNegocioException(
                    "No es posible postularse: la convocatoria aún no ha sido publicada");
        }

        // Validación 3: no exceder los cupos disponibles
        long aprobadas = postulacionRepository.countByConvocatoriaIdAndEstado(
                convocatoriaId, EstadoPostulacion.APROBADA);
        if (aprobadas >= convocatoria.getCuposDisponibles()) {
            throw new ReglaNegocioException(
                    "No es posible postularse: se han agotado los cupos disponibles");
        }

        Postulacion postulacion = Postulacion.builder()
                .usuario(usuario)
                .convocatoria(convocatoria)
                .estado(EstadoPostulacion.PENDIENTE)
                .build();

        return postulacionRepository.save(postulacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Postulacion> listarTodas() {
        return postulacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Postulacion buscarPorId(UUID id) {
        return postulacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la postulación con id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Postulacion> listarPorConvocatoria(UUID convocatoriaId) {
        return postulacionRepository.findByConvocatoriaId(convocatoriaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Postulacion> listarPorUsuario(UUID usuarioId) {
        return postulacionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Postulacion cambiarEstado(UUID id, EstadoPostulacion nuevoEstado) {
        Postulacion postulacion = buscarPorId(id);

        if (postulacion.getEstado() != EstadoPostulacion.PENDIENTE) {
            throw new ReglaNegocioException(
                    "Solo se pueden resolver postulaciones en estado PENDIENTE");
        }

        if (nuevoEstado == EstadoPostulacion.APROBADA) {
            long aprobadas = postulacionRepository.countByConvocatoriaIdAndEstado(
                    postulacion.getConvocatoria().getId(), EstadoPostulacion.APROBADA);
            if (aprobadas >= postulacion.getConvocatoria().getCuposDisponibles()) {
                throw new ReglaNegocioException(
                        "No es posible aprobar: se han agotado los cupos disponibles");
            }
        }

        postulacion.resolver(nuevoEstado);
        return postulacionRepository.save(postulacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostulacionesPorConvocatoriaDTO> reportePostulacionesPorConvocatoria() {
        return postulacionRepository.contarPostulacionesPorConvocatoria().stream()
                .map(fila -> new PostulacionesPorConvocatoriaDTO(
                        (UUID) fila[0],
                        (String) fila[1],
                        (long) fila[2]
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoPostulacionesDTO> reporteResultadoPostulaciones() {
        return postulacionRepository.contarPorResultado().stream()
                .map(fila -> new ResultadoPostulacionesDTO(
                        fila[0].toString(),
                        (long) fila[1]
                ))
                .toList();
    }
}