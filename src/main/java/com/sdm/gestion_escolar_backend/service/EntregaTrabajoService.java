package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.EntregaTrabajo;

public interface EntregaTrabajoService {
    List<EntregaTrabajo> listarPorEstudiante(Integer idEstudiante);
    EntregaTrabajo registrar(Integer idEstudiante, Integer idEvaluacion, String comentario);
}
