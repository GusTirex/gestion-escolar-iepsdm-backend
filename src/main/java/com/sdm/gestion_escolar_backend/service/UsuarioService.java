package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Usuario;

public interface UsuarioService {
    Usuario crear(Usuario usuario);
    Usuario actualizar(Integer idUsuario, Usuario usuario);
    void eliminar(Integer idUsuario);
    List<Usuario> listar();
    Usuario obtenerPorId(Integer idUsuario);
}