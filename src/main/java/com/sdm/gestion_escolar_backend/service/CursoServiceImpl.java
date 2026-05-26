package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Curso;
import com.sdm.gestion_escolar_backend.repository.CursoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    @Override
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    @Override
    public Curso obtenerPorId(Integer idCurso) {
        return cursoRepository.findById(idCurso).orElseThrow(() -> new RuntimeException("Curso no encontrado: " + idCurso));
    } 
    
    @Override
    public Curso crear(Curso curso) {
        if (cursoRepository.existsByNombre(curso.getNombre())) {
            throw new RuntimeException("Ya existe un curso con ese nombre");
        }
        return cursoRepository.save(curso);
    }

    @Override
    public Curso actualizar(Integer idCurso, Curso curso) {
        Curso existente = obtenerPorId(idCurso);
        existente.setNombre(curso.getNombre());
        existente.setDescripcion(curso.getDescripcion());
        return cursoRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idCurso) {
        Curso existente = obtenerPorId(idCurso);
        cursoRepository.delete(existente);
    }
}

