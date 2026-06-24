package com.usco.convocatorias.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.usco.convocatorias.model.enums.EstadoUsuario;
import com.usco.convocatorias.model.enums.RolUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "usuarios",
        schema = "convocatorias",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_usuarios_identificacion", columnNames = "identificacion"),
                @UniqueConstraint(name = "UQ_usuarios_correo", columnNames = "correo")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "passwordHash")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 20)
    @Column(name = "identificacion", nullable = false, length = 20, unique = true)
    private String identificacion;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email
    @Size(max = 150)
    @Column(name = "correo", nullable = false, length = 150, unique = true)
    private String correo;

    @NotBlank
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private RolUsuario rol;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void alPersistir() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoUsuario.ACTIVO;
        }
    }

    // ===========================================================
    // UserDetails (Spring Security)
    // ===========================================================

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return passwordHash;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return correo;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return estado == EstadoUsuario.ACTIVO;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return estado == EstadoUsuario.ACTIVO;
    }
}