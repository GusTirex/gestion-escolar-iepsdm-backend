package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Curso;
import com.sdm.gestion_escolar_backend.entity.Docente;
import com.sdm.gestion_escolar_backend.entity.DocenteCurso;
import com.sdm.gestion_escolar_backend.entity.Seccion;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.CursoRepository;
import com.sdm.gestion_escolar_backend.repository.DocenteCursoRepository;
import com.sdm.gestion_escolar_backend.repository.DocenteRepository;
import com.sdm.gestion_escolar_backend.repository.SeccionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocenteCursoServiceImpl implements DocenteCursoService {

    private final DocenteCursoRepository docenteCursoRepository;
    private final DocenteRepository docenteRepository;
    private final CursoRepository cursoRepository;
    private final SeccionRepository seccionRepository;

    @Override
    public List<DocenteCurso> listar() {
        return docenteCursoRepository.findAll();
    }

    @Override
    public DocenteCurso obtenerPorId(Integer idDocenteCurso) {
        return docenteCursoRepository.findById(idDocenteCurso)
                .orElseThrow(() -> new ResourceNotFoundException("DocenteCurso no encontrado con id: " + idDocenteCurso));
    }

    @Override
    @Transactional
    public DocenteCurso crear(DocenteCurso docenteCurso) {
        validarDocenteCurso(docenteCurso);
        docenteCurso.setDocente(obtenerDocente(docenteCurso.getDocente().getIdDocente()));
        docenteCurso.setCurso(obtenerCurso(docenteCurso.getCurso().getIdCurso()));
        docenteCurso.setSeccion(obtenerSeccion(docenteCurso.getSeccion().getIdSeccion()));
        return docenteCursoRepository.save(docenteCurso);
    }

    @Override
    @Transactional
    public DocenteCurso actualizar(Integer idDocenteCurso, DocenteCurso docenteCurso) {
        validarDocenteCurso(docenteCurso);
        DocenteCurso existente = obtenerPorId(idDocenteCurso);

        existente.setDocente(obtenerDocente(docenteCurso.getDocente().getIdDocente()));
        existente.setCurso(obtenerCurso(docenteCurso.getCurso().getIdCurso()));
        existente.setSeccion(obtenerSeccion(docenteCurso.getSeccion().getIdSeccion()));

        return docenteCursoRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idDocenteCurso) {
        DocenteCurso existente = obtenerPorId(idDocenteCurso);
        docenteCursoRepository.delete(existente);
    }

    private void validarDocenteCurso(DocenteCurso docenteCurso) {
        if (docenteCurso.getDocente() == null || docenteCurso.getDocente().getIdDocente() == null) {
            throw new BadRequestException("El idDocente es obligatorio");
        }
        if (docenteCurso.getCurso() == null || docenteCurso.getCurso().getIdCurso() == null) {
            throw new BadRequestException("El idCurso es obligatorio");
        }
        if (docenteCurso.getSeccion() == null || docenteCurso.getSeccion().getIdSeccion() == null) {
            throw new BadRequestException("El idSeccion es obligatorio");
        }
    }

    private Docente obtenerDocente(Integer idDocente) {
        return docenteRepository.findById(idDocente)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con id: " + idDocente));
    }

    private Curso obtenerCurso(Integer idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + idCurso));
    }

    private Seccion obtenerSeccion(Integer idSeccion) {
        return seccionRepository.findById(idSeccion)
                .orElseThrow(() -> new ResourceNotFoundException("Seccion no encontrada con id: " + idSeccion));
    }
}