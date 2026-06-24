package com.usco.convocatorias.service.interfaces;

import com.usco.convocatorias.model.Usuario;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {

    List<Usuario> listarTodos();

    Usuario buscarPorId(UUID id);

    Usuario crear(Usuario usuario);

    Usuario actualizar(UUID id, Usuario datosActualizados);

    void eliminar(UUID id);
}
