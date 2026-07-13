package com.sdm.gestion_escolar_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.EntregaTrabajo;
import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.entity.Evaluacion;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.EntregaTrabajoRepository;
import com.sdm.gestion_escolar_backend.repository.EstudianteRepository;
import com.sdm.gestion_escolar_backend.repository.EvaluacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EntregaTrabajoServiceImpl implements EntregaTrabajoService {

    private final EntregaTrabajoRepository entregaRepository;
    private final EstudianteRepository estudianteRepository;
    private final EvaluacionRepository evaluacionRepository;

    @Override
    public List<EntregaTrabajo> listarPorEstudiante(Integer idEstudiante) {
        return entregaRepository.findConEvaluacionByEstudiante(idEstudiante);
    }

    @Override
    @Transactional
    public EntregaTrabajo registrar(Integer idEstudiante, Integer idEvaluacion, String comentario) {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + idEstudiante));
        Evaluacion evaluacion = evaluacionRepository.findById(idEvaluacion)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluacion no encontrada con id: " + idEvaluacion));

        if (entregaRepository.existsByEstudianteIdEstudianteAndEvaluacionIdEvaluacion(idEstudiante, idEvaluacion)) {
            throw new BadRequestException("El trabajo ya fue entregado");
        }

        EntregaTrabajo entrega = EntregaTrabajo.builder()
                .estudiante(estudiante)
                .evaluacion(evaluacion)
                .fechaEntrega(LocalDate.now())
                .comentario(comentario)
                .build();

        return entregaRepository.save(entrega);
    }
}
