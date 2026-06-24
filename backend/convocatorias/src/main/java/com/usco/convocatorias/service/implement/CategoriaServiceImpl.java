package com.usco.convocatorias.service.implement;

import com.usco.convocatorias.excepcion.ReglaNegocioException;
import com.usco.convocatorias.excepcion.RecursoNoEncontradoException;

import com.usco.convocatorias.model.Categoria;
import com.usco.convocatorias.respository.CategoriaRepository;
import com.usco.convocatorias.service.interfaces.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria buscarPorId(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la categoría con id " + id));
    }

    @Override
    public Categoria crear(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new ReglaNegocioException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria actualizar(UUID id, Categoria datosActualizados) {
        Categoria existente = buscarPorId(id);

        categoriaRepository.findByNombre(datosActualizados.getNombre()).ifPresent(c -> {
            if (!c.getId().equals(id)) {
                throw new ReglaNegocioException(
                        "Ya existe una categoría con el nombre: " + datosActualizados.getNombre());
            }
        });

        existente.setNombre(datosActualizados.getNombre());
        existente.setDescripcion(datosActualizados.getDescripcion());
        return categoriaRepository.save(existente);
    }

    @Override
    public void eliminar(UUID id) {
        Categoria existente = buscarPorId(id);
        categoriaRepository.delete(existente);
    }
}