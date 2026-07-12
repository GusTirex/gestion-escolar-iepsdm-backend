package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Asistencia;

public interface AsistenciaService {
    Asistencia crear(Asistencia asistencia);
    Asistencia actualizar(Integer idAsistencia, Asistencia asistencia);
    void eliminar(Integer idAsistencia);
    List<Asistencia> listar();
    List<Asistencia> listarPorEstudiante(Integer idEstudiante);
    Asistencia obtenerPorId(Integer idAsistencia);
}