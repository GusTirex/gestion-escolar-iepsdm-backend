package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Matricula;

public interface MatriculaService {
    Matricula crear(Matricula matricula);
    Matricula actualizar(Integer idMatricula, Matricula matricula);
    void eliminar(Integer idMatricula);
    List<Matricula> listar();
    Matricula obtenerPorId(Integer idMatricula);
}