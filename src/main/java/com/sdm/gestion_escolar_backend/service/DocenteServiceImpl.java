package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Docente;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.DocenteRepository;
import com.sdm.gestion_escolar_backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Docente> listar() {
        return docenteRepository.findAll();
    }

    @Override
    public Docente obtenerPorId(Integer idDocente) {
        return docenteRepository.findById(idDocente)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con id: " + idDocente));
    }

    @Override
    @Transactional
    public Docente crear(Docente docente) {
        validarDocente(docente);
        docente.setUsuario(obtenerUsuario(docente.getUsuario().getIdUsuario()));
        docente.setNombres(docente.getNombres().trim());
        docente.setApellidos(docente.getApellidos().trim());
        return docenteRepository.save(docente);
    }

    @Override
    @Transactional
    public Docente actualizar(Integer idDocente, Docente docente) {
        validarDocente(docente);
        Docente existente = obtenerPorId(idDocente);

        existente.setNombres(docente.getNombres().trim());
        existente.setApellidos(docente.getApellidos().trim());
        existente.setFechaNacimiento(docente.getFechaNacimiento());
        existente.setEspecialidad(docente.getEspecialidad());
        existente.setDireccion(docente.getDireccion());
        existente.setTelefono(docente.getTelefono());
        existente.setUsuario(obtenerUsuario(docente.getUsuario().getIdUsuario()));

        return docenteRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idDocente) {
        Docente existente = obtenerPorId(idDocente);
        docenteRepository.delete(existente);
    }

    private void validarDocente(Docente docente) {
        if (docente.getNombres() == null || docente.getNombres().trim().isEmpty()) {
            throw new BadRequestException("Los nombres del docente son obligatorios");
        }
        if (docente.getApellidos() == null || docente.getApellidos().trim().isEmpty()) {
            throw new BadRequestException("Los apellidos del docente son obligatorios");
        }
        if (docente.getFechaNacimiento() == null) {
            throw new BadRequestException("La fecha de nacimiento del docente es obligatoria");
        }
        if (docente.getUsuario() == null || docente.getUsuario().getIdUsuario() == null) {
            throw new BadRequestException("El idUsuario es obligatorio");
        }
    }

    private Usuario obtenerUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + idUsuario));
    }
}