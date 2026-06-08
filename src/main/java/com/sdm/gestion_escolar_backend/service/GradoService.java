package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Grado;

public interface GradoService {
    Grado crear(Grado grado);
    Grado actualizar(Integer idGrado, Grado grado);
    void eliminar(Integer idGrado);
    List<Grado> listar();
    Grado obtenerPorId(Integer idGrado);
}