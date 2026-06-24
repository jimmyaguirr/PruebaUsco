package com.usco.convocatorias.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "categorias",
        schema = "convocatorias",
        uniqueConstraints = @UniqueConstraint(name = "UQ_categorias_nombre", columnNames = "nombre")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "convocatorias")
public class Categoria {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100, unique = true)
    private String nombre;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    /**
     * Lado inverso de la relación N:M. Se ignora en la serialización JSON
     * para evitar referencias circulares (Categoria -> Convocatoria -> Categoria...).
     * Si se necesita consultar las convocatorias de una categoría, usar el
     * reporte GET /api/reportes/convocatorias-categoria.
     */
    @ManyToMany(mappedBy = "categorias")
    @Builder.Default
    @JsonIgnore
    private Set<Convocatoria> convocatorias = new HashSet<>();
}