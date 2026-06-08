package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Docente;

public interface DocenteService {
    Docente crear(Docente docente);
    Docente actualizar(Integer idDocente, Docente docente);
    void eliminar(Integer idDocente);
    List<Docente> listar();
    Docente obtenerPorId(Integer idDocente);
}