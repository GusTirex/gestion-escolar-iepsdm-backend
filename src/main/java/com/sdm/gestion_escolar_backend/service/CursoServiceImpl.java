package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Curso;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ConflictException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.CursoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    @Override
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    @Override
    public Curso obtenerPorId(Integer idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + idCurso));
    } 
    
    @Override
    @Transactional
    public Curso crear(Curso curso) {
        validarNombre(curso.getNombre());
        String nombreNormalizado = curso.getNombre().trim();

        if (cursoRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new ConflictException("Ya existe un curso con el nombre: " + nombreNormalizado);
        }

        curso.setNombre(nombreNormalizado);
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public Curso actualizar(Integer idCurso, Curso curso) {
        validarNombre(curso.getNombre());
        String nombreNormalizado = curso.getNombre().trim();

        Curso existente = obtenerPorId(idCurso);

        if (cursoRepository.existsByNombreIgnoreCaseAndIdCursoNot(nombreNormalizado, idCurso)) {
            throw new ConflictException("Ya existe otro curso con el nombre: " + nombreNormalizado);
        }

        existente.setNombre(nombreNormalizado);
        existente.setDescripcion(curso.getDescripcion());
        return cursoRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idCurso) {
        Curso existente = obtenerPorId(idCurso);
        cursoRepository.delete(existente);
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BadRequestException("El nombre del curso es obligatorio");
        }
    }
}

