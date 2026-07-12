package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Asistencia;
import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.AsistenciaRepository;
import com.sdm.gestion_escolar_backend.repository.EstudianteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final EstudianteRepository estudianteRepository;

    @Override
    public List<Asistencia> listar() {
        return asistenciaRepository.findAll();
    }

    @Override
    public List<Asistencia> listarPorEstudiante(Integer idEstudiante) {
        return asistenciaRepository.findByEstudianteIdEstudiante(idEstudiante);
    }

    @Override
    public Asistencia obtenerPorId(Integer idAsistencia) {
        return asistenciaRepository.findById(idAsistencia)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia no encontrada con id: " + idAsistencia));
    }

    @Override
    @Transactional
    public Asistencia crear(Asistencia asistencia) {
        validarAsistencia(asistencia);
        asistencia.setEstudiante(obtenerEstudiante(asistencia.getEstudiante().getIdEstudiante()));
        return asistenciaRepository.save(asistencia);
    }

    @Override
    @Transactional
    public Asistencia actualizar(Integer idAsistencia, Asistencia asistencia) {
        validarAsistencia(asistencia);
        Asistencia existente = obtenerPorId(idAsistencia);

        existente.setFecha(asistencia.getFecha());
        existente.setEstado(asistencia.getEstado());
        existente.setEstudiante(obtenerEstudiante(asistencia.getEstudiante().getIdEstudiante()));

        return asistenciaRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idAsistencia) {
        Asistencia existente = obtenerPorId(idAsistencia);
        asistenciaRepository.delete(existente);
    }

    private void validarAsistencia(Asistencia asistencia) {
        if (asistencia.getFecha() == null) {
            throw new BadRequestException("La fecha de la asistencia es obligatoria");
        }
        if (asistencia.getEstado() == null) {
            throw new BadRequestException("El estado de la asistencia es obligatorio");
        }
        if (asistencia.getEstudiante() == null || asistencia.getEstudiante().getIdEstudiante() == null) {
            throw new BadRequestException("El idEstudiante es obligatorio");
        }
    }

    private Estudiante obtenerEstudiante(Integer idEstudiante) {
        return estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + idEstudiante));
    }
}