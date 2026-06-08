package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Evaluacion;

public interface EvaluacionService {
    Evaluacion crear(Evaluacion evaluacion);
    Evaluacion actualizar(Integer idEvaluacion, Evaluacion evaluacion);
    void eliminar(Integer idEvaluacion);
    List<Evaluacion> listar();
    Evaluacion obtenerPorId(Integer idEvaluacion);
}