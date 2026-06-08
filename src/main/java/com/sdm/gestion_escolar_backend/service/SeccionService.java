package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Seccion;

public interface SeccionService {
    Seccion crear(Seccion seccion);
    Seccion actualizar(Integer idSeccion, Seccion seccion);
    void eliminar(Integer idSeccion);
    List<Seccion> listar();
    Seccion obtenerPorId(Integer idSeccion);
}