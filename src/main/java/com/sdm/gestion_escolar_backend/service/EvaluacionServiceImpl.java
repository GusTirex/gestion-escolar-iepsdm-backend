package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Curso;
import com.sdm.gestion_escolar_backend.entity.Evaluacion;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.CursoRepository;
import com.sdm.gestion_escolar_backend.repository.EvaluacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final CursoRepository cursoRepository;

    @Override
    public List<Evaluacion> listar() {
        return evaluacionRepository.findAll();
    }

    @Override
    public Evaluacion obtenerPorId(Integer idEvaluacion) {
        return evaluacionRepository.findById(idEvaluacion)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluacion no encontrada con id: " + idEvaluacion));
    }

    @Override
    @Transactional
    public Evaluacion crear(Evaluacion evaluacion) {
        validarEvaluacion(evaluacion);
        evaluacion.setCurso(obtenerCurso(evaluacion.getCurso().getIdCurso()));
        evaluacion.setNombre(evaluacion.getNombre().trim());
        return evaluacionRepository.save(evaluacion);
    }

    @Override
    @Transactional
    public Evaluacion actualizar(Integer idEvaluacion, Evaluacion evaluacion) {
        validarEvaluacion(evaluacion);
        Evaluacion existente = obtenerPorId(idEvaluacion);

        existente.setNombre(evaluacion.getNombre().trim());
        existente.setPorcentaje(evaluacion.getPorcentaje());
        existente.setFecha(evaluacion.getFecha());
        existente.setCurso(obtenerCurso(evaluacion.getCurso().getIdCurso()));

        return evaluacionRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idEvaluacion) {
        Evaluacion existente = obtenerPorId(idEvaluacion);
        evaluacionRepository.delete(existente);
    }

    private void validarEvaluacion(Evaluacion evaluacion) {
        if (evaluacion.getNombre() == null || evaluacion.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre de la evaluacion es obligatorio");
        }
        if (evaluacion.getPorcentaje() == null || evaluacion.getPorcentaje() < 0 || evaluacion.getPorcentaje() > 100) {
            throw new BadRequestException("El porcentaje de la evaluacion debe estar entre 0 y 100");
        }
        if (evaluacion.getFecha() == null) {
            throw new BadRequestException("La fecha de la evaluacion es obligatoria");
        }
        if (evaluacion.getCurso() == null || evaluacion.getCurso().getIdCurso() == null) {
            throw new BadRequestException("El idCurso es obligatorio");
        }
    }

    private Curso obtenerCurso(Integer idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + idCurso));
    }
}