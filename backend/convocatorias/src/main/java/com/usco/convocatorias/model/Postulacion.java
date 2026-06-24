package com.usco.convocatorias.model;

import com.usco.convocatorias.model.enums.EstadoPostulacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "postulaciones",
        schema = "convocatorias",
        uniqueConstraints = @UniqueConstraint(
                name = "UQ_postulaciones_usuario_convocatoria",
                columnNames = {"usuario_id", "convocatoria_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"usuario", "convocatoria"})
public class Postulacion {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_postulaciones_usuario"))
    private Usuario usuario;

    @NotNull(message = "La convocatoria es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convocatoria_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_postulaciones_convocatoria"))
    private Convocatoria convocatoria;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPostulacion estado = EstadoPostulacion.PENDIENTE;

    @Column(name = "fecha_postulacion", nullable = false, updatable = false)
    private LocalDateTime fechaPostulacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Size(max = 500)
    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @PrePersist
    protected void alPersistir() {
        if (fechaPostulacion == null) {
            fechaPostulacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoPostulacion.PENDIENTE;
        }
    }

    public void resolver(EstadoPostulacion nuevoEstado) {
        this.estado = nuevoEstado;
        this.fechaResolucion = LocalDateTime.now();
    }
}