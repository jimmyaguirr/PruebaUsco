package com.usco.convocatorias.respository;


import com.usco.convocatorias.model.Convocatoria;
import com.usco.convocatorias.model.enums.EstadoConvocatoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, UUID> {

    List<Convocatoria> findByEstado(EstadoConvocatoria estado);

    /**
     * Carga la convocatoria junto con sus categorías en una sola consulta
     * (evita el problema N+1 al acceder a categorias perezosamente).
     */
    @Query("SELECT c FROM Convocatoria c LEFT JOIN FETCH c.categorias WHERE c.id = :id")
    Optional<Convocatoria> buscarConCategoriasPorId(@Param("id") UUID id);

    @Query("SELECT DISTINCT c FROM Convocatoria c LEFT JOIN FETCH c.categorias")
    List<Convocatoria> buscarTodasConCategorias();

    /**
     * Soporte para el Reporte 1: convocatorias por categoría.
     */
    @Query("SELECT c FROM Convocatoria c JOIN c.categorias cat WHERE cat.id = :categoriaId")
    List<Convocatoria> buscarPorCategoriaId(@Param("categoriaId") UUID categoriaId);
}
