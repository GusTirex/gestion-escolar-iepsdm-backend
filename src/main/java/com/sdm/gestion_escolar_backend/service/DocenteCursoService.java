package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.DocenteCurso;

public interface DocenteCursoService {
    DocenteCurso crear(DocenteCurso docenteCurso);
    DocenteCurso actualizar(Integer idDocenteCurso, DocenteCurso docenteCurso);
    void eliminar(Integer idDocenteCurso);
    List<DocenteCurso> listar();
    DocenteCurso obtenerPorId(Integer idDocenteCurso);
}