package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.EstudianteRepository;
import com.sdm.gestion_escolar_backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Estudiante> listar() {
        return estudianteRepository.findAll();
    }

    @Override
    public Estudiante obtenerPorId(Integer idEstudiante) {
        return estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + idEstudiante));
    }

    @Override
    @Transactional
    public Estudiante crear(Estudiante estudiante) {
        validarEstudiante(estudiante);
        estudiante.setUsuario(obtenerUsuario(estudiante.getUsuario().getIdUsuario()));
        estudiante.setNombres(estudiante.getNombres().trim());
        estudiante.setApellidos(estudiante.getApellidos().trim());
        return estudianteRepository.save(estudiante);
    }

    @Override
    @Transactional
    public Estudiante actualizar(Integer idEstudiante, Estudiante estudiante) {
        validarEstudiante(estudiante);
        Estudiante existente = obtenerPorId(idEstudiante);

        existente.setNombres(estudiante.getNombres().trim());
        existente.setApellidos(estudiante.getApellidos().trim());
        existente.setFechaNacimiento(estudiante.getFechaNacimiento());
        existente.setDireccion(estudiante.getDireccion());
        existente.setTelefono(estudiante.getTelefono());
        existente.setUsuario(obtenerUsuario(estudiante.getUsuario().getIdUsuario()));

        return estudianteRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idEstudiante) {
        Estudiante existente = obtenerPorId(idEstudiante);
        estudianteRepository.delete(existente);
    }

    private void validarEstudiante(Estudiante estudiante) {
        if (estudiante.getNombres() == null || estudiante.getNombres().trim().isEmpty()) {
            throw new BadRequestException("Los nombres del estudiante son obligatorios");
        }
        if (estudiante.getApellidos() == null || estudiante.getApellidos().trim().isEmpty()) {
            throw new BadRequestException("Los apellidos del estudiante son obligatorios");
        }
        if (estudiante.getFechaNacimiento() == null) {
            throw new BadRequestException("La fecha de nacimiento del estudiante es obligatoria");
        }
        if (estudiante.getUsuario() == null || estudiante.getUsuario().getIdUsuario() == null) {
            throw new BadRequestException("El idUsuario es obligatorio");
        }
    }

    private Usuario obtenerUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + idUsuario));
    }
}