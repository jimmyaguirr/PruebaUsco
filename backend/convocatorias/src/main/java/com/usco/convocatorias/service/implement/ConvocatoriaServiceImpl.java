package com.usco.convocatorias.service.implement;


import com.usco.convocatorias.excepcion.ReglaNegocioException;
import com.usco.convocatorias.excepcion.RecursoNoEncontradoException;

import com.usco.convocatorias.model.Categoria;
import com.usco.convocatorias.model.Convocatoria;
import com.usco.convocatorias.model.dto.ConvocatoriaPorCategoriaDTO;
import com.usco.convocatorias.model.enums.EstadoConvocatoria;
import com.usco.convocatorias.respository.CategoriaRepository;
import com.usco.convocatorias.respository.ConvocatoriaRepository;
import com.usco.convocatorias.service.interfaces.ConvocatoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConvocatoriaServiceImpl implements ConvocatoriaService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Convocatoria> listarTodas() {
        return convocatoriaRepository.buscarTodasConCategorias();
    }

    @Override
    @Transactional(readOnly = true)
    public Convocatoria buscarPorId(UUID id) {
        return convocatoriaRepository.buscarConCategoriasPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la convocatoria con id " + id));
    }

    @Override
    public Convocatoria crear(Convocatoria convocatoria, Set<UUID> idsCategorias) {
        validarFechas(convocatoria);
        asociarCategorias(convocatoria, idsCategorias);
        return convocatoriaRepository.save(convocatoria);
    }

    @Override
    public Convocatoria actualizar(UUID id, Convocatoria datosActualizados, Set<UUID> idsCategorias) {
        Convocatoria existente = buscarPorId(id);
        validarFechas(datosActualizados);

        existente.setNombre(datosActualizados.getNombre());
        existente.setDescripcion(datosActualizados.getDescripcion());
        existente.setFechaInicio(datosActualizados.getFechaInicio());
        existente.setFechaFin(datosActualizados.getFechaFin());
        existente.setCuposDisponibles(datosActualizados.getCuposDisponibles());
        existente.setEstado(datosActualizados.getEstado());

        if (idsCategorias != null) {
            existente.getCategorias().forEach(cat -> cat.getConvocatorias().remove(existente));
            existente.getCategorias().clear();
            asociarCategorias(existente, idsCategorias);
        }

        return convocatoriaRepository.save(existente);
    }

    @Override
    public void eliminar(UUID id) {
        Convocatoria existente = buscarPorId(id);
        convocatoriaRepository.delete(existente);
    }

    @Override
    public Convocatoria cambiarEstado(UUID id, EstadoConvocatoria nuevoEstado) {
        Convocatoria existente = buscarPorId(id);
        existente.setEstado(nuevoEstado);
        return convocatoriaRepository.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConvocatoriaPorCategoriaDTO> reporteConvocatoriasPorCategoria() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> new ConvocatoriaPorCategoriaDTO(
                        categoria.getId(),
                        categoria.getNombre(),
                        convocatoriaRepository.buscarPorCategoriaId(categoria.getId()).size()
                ))
                .collect(Collectors.toList());
    }

    private void validarFechas(Convocatoria convocatoria) {
        if (convocatoria.getFechaFin().isBefore(convocatoria.getFechaInicio())) {
            throw new ReglaNegocioException("La fecha fin no puede ser anterior a la fecha de inicio");
        }
    }

    private void asociarCategorias(Convocatoria convocatoria, Set<UUID> idsCategorias) {
        if (idsCategorias == null || idsCategorias.isEmpty()) {
            return;
        }
        for (UUID idCategoria : idsCategorias) {
            Categoria categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontró la categoría con id " + idCategoria));
            convocatoria.agregarCategoria(categoria);
        }
    }
}
