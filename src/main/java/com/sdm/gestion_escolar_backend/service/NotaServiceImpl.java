package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.entity.Evaluacion;
import com.sdm.gestion_escolar_backend.entity.Nota;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.EstudianteRepository;
import com.sdm.gestion_escolar_backend.repository.EvaluacionRepository;
import com.sdm.gestion_escolar_backend.repository.NotaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotaServiceImpl implements NotaService {

    private final NotaRepository notaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final EstudianteRepository estudianteRepository;

    @Override
    public List<Nota> listar() {
        return notaRepository.findAll();
    }

    @Override
    public List<Nota> listarPorEstudiante(Integer idEstudiante) {
        return notaRepository.findByEstudianteIdEstudiante(idEstudiante);
    }

    @Override
    public Nota obtenerPorId(Integer idNota) {
        return notaRepository.findById(idNota)
                .orElseThrow(() -> new ResourceNotFoundException("Nota no encontrada con id: " + idNota));
    }

    @Override
    @Transactional
    public Nota crear(Nota nota) {
        validarNota(nota);
        nota.setEvaluacion(obtenerEvaluacion(nota.getEvaluacion().getIdEvaluacion()));
        nota.setEstudiante(obtenerEstudiante(nota.getEstudiante().getIdEstudiante()));
        return notaRepository.save(nota);
    }

    @Override
    @Transactional
    public Nota actualizar(Integer idNota, Nota nota) {
        validarNota(nota);
        Nota existente = obtenerPorId(idNota);

        existente.setNota(nota.getNota());
        existente.setObservacion(nota.getObservacion());
        existente.setEvaluacion(obtenerEvaluacion(nota.getEvaluacion().getIdEvaluacion()));
        existente.setEstudiante(obtenerEstudiante(nota.getEstudiante().getIdEstudiante()));
        if (nota.getRegistradoPor() != null) {
            existente.setRegistradoPor(nota.getRegistradoPor()); // auditoria: ultimo que la modifico
        }

        return notaRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idNota) {
        Nota existente = obtenerPorId(idNota);
        notaRepository.delete(existente);
    }

    private void validarNota(Nota nota) {
        if (nota.getNota() == null || nota.getNota() < 0 || nota.getNota() > 20) {
            throw new BadRequestException("La nota debe estar entre 0 y 20");
        }
        if (nota.getEvaluacion() == null || nota.getEvaluacion().getIdEvaluacion() == null) {
            throw new BadRequestException("El idEvaluacion es obligatorio");
        }
        if (nota.getEstudiante() == null || nota.getEstudiante().getIdEstudiante() == null) {
            throw new BadRequestException("El idEstudiante es obligatorio");
        }
    }

    private Evaluacion obtenerEvaluacion(Integer idEvaluacion) {
        return evaluacionRepository.findById(idEvaluacion)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluacion no encontrada con id: " + idEvaluacion));
    }

    private Estudiante obtenerEstudiante(Integer idEstudiante) {
        return estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + idEstudiante));
    }
}