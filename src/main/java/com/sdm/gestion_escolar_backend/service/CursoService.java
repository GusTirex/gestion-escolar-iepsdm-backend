package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Curso;

public interface CursoService {
    Curso crear(Curso curso);
    Curso actualizar(Integer idCurso, Curso curso);
    void eliminar(Integer idCurso);
    List<Curso> listar();
    Curso obtenerPorId(Integer idCurso);
}
