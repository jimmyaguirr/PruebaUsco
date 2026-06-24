package com.usco.convocatorias.respository;


import com.usco.convocatorias.model.Convocatoria;
import com.usco.convocatorias.model.Postulacion;
import com.usco.convocatorias.model.Usuario;
import com.usco.convocatorias.model.enums.EstadoPostulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostulacionRepository extends JpaRepository<Postulacion, UUID> {

    boolean existsByUsuarioAndConvocatoria(Usuario usuario, Convocatoria convocatoria);

    Optional<Postulacion> findByUsuarioAndConvocatoria(Usuario usuario, Convocatoria convocatoria);

    List<Postulacion> findByConvocatoriaId(UUID convocatoriaId);

    List<Postulacion> findByUsuarioId(UUID usuarioId);

    List<Postulacion> findByEstado(EstadoPostulacion estado);

    /**
     * Cuenta postulaciones APROBADAS de una convocatoria.
     * Útil para validar el cupo disponible antes de aprobar una nueva.
     */
    long countByConvocatoriaIdAndEstado(UUID convocatoriaId, EstadoPostulacion estado);

    /**
     * Soporte para el Reporte 2: cantidad de postulaciones por convocatoria.
     * Retorna pares [convocatoria, cantidad].
     */
    @Query("""
            SELECT p.convocatoria.id, p.convocatoria.nombre, COUNT(p)
            FROM Postulacion p
            GROUP BY p.convocatoria.id, p.convocatoria.nombre
            """)
    List<Object[]> contarPostulacionesPorConvocatoria();

    /**
     * Soporte para el Reporte 3: postulaciones aprobadas vs rechazadas.
     * Retorna pares [estado, cantidad].
     */
    @Query("""
            SELECT p.estado, COUNT(p)
            FROM Postulacion p
            WHERE p.estado IN ('APROBADA', 'RECHAZADA')
            GROUP BY p.estado
            """)
    List<Object[]> contarPorResultado();
}
