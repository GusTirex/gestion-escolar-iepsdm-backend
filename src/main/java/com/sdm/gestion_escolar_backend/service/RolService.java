package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Rol;

public interface RolService {
    Rol crear(Rol rol);
    Rol actualizar(Integer idRol, Rol rol);
    void eliminar(Integer idRol);
    List<Rol> listar();
    Rol obtenerPorId(Integer idRol);
}