package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.entity.Matricula;
import com.sdm.gestion_escolar_backend.entity.Seccion;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.EstudianteRepository;
import com.sdm.gestion_escolar_backend.repository.MatriculaRepository;
import com.sdm.gestion_escolar_backend.repository.SeccionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final SeccionRepository seccionRepository;

    @Override
    public List<Matricula> listar() {
        return matriculaRepository.findAll();
    }

    @Override
    public Matricula obtenerPorId(Integer idMatricula) {
        return matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new ResourceNotFoundException("Matricula no encontrada con id: " + idMatricula));
    }

    @Override
    @Transactional
    public Matricula crear(Matricula matricula) {
        validarMatricula(matricula);
        matricula.setEstudiante(obtenerEstudiante(matricula.getEstudiante().getIdEstudiante()));
        matricula.setSeccion(obtenerSeccion(matricula.getSeccion().getIdSeccion()));
        return matriculaRepository.save(matricula);
    }

    @Override
    @Transactional
    public Matricula actualizar(Integer idMatricula, Matricula matricula) {
        validarMatricula(matricula);
        Matricula existente = obtenerPorId(idMatricula);

        existente.setAnio(matricula.getAnio());
        existente.setFecha(matricula.getFecha());
        existente.setEstado(matricula.getEstado());
        existente.setEstudiante(obtenerEstudiante(matricula.getEstudiante().getIdEstudiante()));
        existente.setSeccion(obtenerSeccion(matricula.getSeccion().getIdSeccion()));

        return matriculaRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idMatricula) {
        Matricula existente = obtenerPorId(idMatricula);
        matriculaRepository.delete(existente);
    }

    private void validarMatricula(Matricula matricula) {
        if (matricula.getAnio() == null) {
            throw new BadRequestException("El anio de la matricula es obligatorio");
        }
        if (matricula.getFecha() == null) {
            throw new BadRequestException("La fecha de la matricula es obligatoria");
        }
        if (matricula.getEstado() == null) {
            throw new BadRequestException("El estado de la matricula es obligatorio");
        }
        if (matricula.getEstudiante() == null || matricula.getEstudiante().getIdEstudiante() == null) {
            throw new BadRequestException("El idEstudiante es obligatorio");
        }
        if (matricula.getSeccion() == null || matricula.getSeccion().getIdSeccion() == null) {
            throw new BadRequestException("El idSeccion es obligatorio");
        }
    }

    private Estudiante obtenerEstudiante(Integer idEstudiante) {
        return estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + idEstudiante));
    }

    private Seccion obtenerSeccion(Integer idSeccion) {
        return seccionRepository.findById(idSeccion)
                .orElseThrow(() -> new ResourceNotFoundException("Seccion no encontrada con id: " + idSeccion));
    }
}