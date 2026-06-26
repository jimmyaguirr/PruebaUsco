package com.usco.convocatorias.service.interfaces;

import com.usco.convocatorias.model.Usuario;
import com.usco.convocatorias.model.dto.UsuarioRequestDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {

    List<Usuario> listarTodos();

    Usuario buscarPorId(UUID id);

    Usuario crear(UsuarioRequestDTO usuario);

    Usuario actualizar(UUID id, UsuarioRequestDTO datosActualizados);

    void eliminar(UUID id);
}
