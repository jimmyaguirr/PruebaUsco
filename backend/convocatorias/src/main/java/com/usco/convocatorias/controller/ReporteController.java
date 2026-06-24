package com.usco.convocatorias.controller;


import com.usco.convocatorias.model.dto.ConvocatoriaPorCategoriaDTO;
import com.usco.convocatorias.model.dto.PostulacionesPorConvocatoriaDTO;
import com.usco.convocatorias.model.dto.ResultadoPostulacionesDTO;
import com.usco.convocatorias.service.interfaces.ConvocatoriaService;
import com.usco.convocatorias.service.interfaces.PostulacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller dedicado a los reportes exigidos por el documento de la prueba
 * técnica, expuestos bajo el namespace /api/reportes tal como se especifica
 * en la sección 10 (Backend) del documento.
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ConvocatoriaService convocatoriaService;
    private final PostulacionService postulacionService;

    /**
     * Reporte 1: Convocatorias por categoría.
     */
    @GetMapping("/convocatorias-categoria")
    public ResponseEntity<List<ConvocatoriaPorCategoriaDTO>> convocatoriasPorCategoria() {
        return ResponseEntity.ok(convocatoriaService.reporteConvocatoriasPorCategoria());
    }

    /**
     * Reporte 2: Cantidad de postulaciones por convocatoria.
     */
    @GetMapping("/postulaciones-convocatoria")
    public ResponseEntity<List<PostulacionesPorConvocatoriaDTO>> postulacionesPorConvocatoria() {
        return ResponseEntity.ok(postulacionService.reportePostulacionesPorConvocatoria());
    }

    /**
     * Reporte 3: Postulaciones aprobadas y rechazadas.
     */
    @GetMapping("/resultado-postulaciones")
    public ResponseEntity<List<ResultadoPostulacionesDTO>> resultadoPostulaciones() {
        return ResponseEntity.ok(postulacionService.reporteResultadoPostulaciones());
    }
}