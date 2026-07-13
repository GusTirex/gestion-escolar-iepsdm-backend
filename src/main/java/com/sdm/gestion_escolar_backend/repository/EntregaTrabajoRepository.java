package com.sdm.gestion_escolar_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sdm.gestion_escolar_backend.entity.EntregaTrabajo;

public interface EntregaTrabajoRepository extends JpaRepository<EntregaTrabajo, Integer> {

    // Trae las entregas de un estudiante ya con la evaluacion y su curso cargados,
    // para poder armar la respuesta sin consultas extra.
    @Query("SELECT e FROM EntregaTrabajo e "
            + "JOIN FETCH e.evaluacion ev JOIN FETCH ev.curso "
            + "WHERE e.estudiante.idEstudiante = :idEstudiante")
    List<EntregaTrabajo> findConEvaluacionByEstudiante(Integer idEstudiante);

    boolean existsByEstudianteIdEstudianteAndEvaluacionIdEvaluacion(Integer idEstudiante, Integer idEvaluacion);
}
