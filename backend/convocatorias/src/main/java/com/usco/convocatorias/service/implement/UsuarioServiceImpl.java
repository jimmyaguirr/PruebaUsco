package com.usco.convocatorias.service.implement;


import com.usco.convocatorias.excepcion.ReglaNegocioException;
import com.usco.convocatorias.excepcion.RecursoNoEncontradoException;

import com.usco.convocatorias.model.Usuario;
import com.usco.convocatorias.respository.UsuarioRepository;
import com.usco.convocatorias.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario con id " + id));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        validarDuplicados(usuario.getCorreo(), usuario.getIdentificacion(), null);
        // El hash de la contraseña se asume calculado antes de llegar aquí
        // (se resolverá cuando se integre Spring Security/BCrypt).
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(UUID id, Usuario datosActualizados) {
        Usuario existente = buscarPorId(id);
        validarDuplicados(datosActualizados.getCorreo(), datosActualizados.getIdentificacion(), id);

        existente.setNombre(datosActualizados.getNombre());
        existente.setCorreo(datosActualizados.getCorreo());
        existente.setIdentificacion(datosActualizados.getIdentificacion());
        existente.setRol(datosActualizados.getRol());
        existente.setEstado(datosActualizados.getEstado());

        return usuarioRepository.save(existente);
    }

    @Override
    public void eliminar(UUID id) {
        Usuario existente = buscarPorId(id);
        usuarioRepository.delete(existente);
    }

    private void validarDuplicados(String correo, String identificacion, UUID idAExcluir) {
        usuarioRepository.findByCorreo(correo).ifPresent(u -> {
            if (idAExcluir == null || !u.getId().equals(idAExcluir)) {
                throw new ReglaNegocioException("Ya existe un usuario con el correo: " + correo);
            }
        });
        usuarioRepository.findByIdentificacion(identificacion).ifPresent(u -> {
            if (idAExcluir == null || !u.getId().equals(idAExcluir)) {
                throw new ReglaNegocioException("Ya existe un usuario con la identificación: " + identificacion);
            }
        });
    }
}