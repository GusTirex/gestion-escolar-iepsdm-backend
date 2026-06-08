package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Estudiante;

public interface EstudianteService {
    Estudiante crear(Estudiante estudiante);
    Estudiante actualizar(Integer idEstudiante, Estudiante estudiante);
    void eliminar(Integer idEstudiante);
    List<Estudiante> listar();
    Estudiante obtenerPorId(Integer idEstudiante);
}
