package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Grado;
import com.sdm.gestion_escolar_backend.entity.Seccion;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.GradoRepository;
import com.sdm.gestion_escolar_backend.repository.SeccionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeccionServiceImpl implements SeccionService {

    private final SeccionRepository seccionRepository;
    private final GradoRepository gradoRepository;

    @Override
    public List<Seccion> listar() {
        return seccionRepository.findAll();
    }

    @Override
    public Seccion obtenerPorId(Integer idSeccion) {
        return seccionRepository.findById(idSeccion)
                .orElseThrow(() -> new ResourceNotFoundException("Seccion no encontrada con id: " + idSeccion));
    }

    @Override
    @Transactional
    public Seccion crear(Seccion seccion) {
        validarSeccion(seccion);
        seccion.setNombre(seccion.getNombre().trim());
        seccion.setGrado(obtenerGrado(seccion.getGrado().getIdGrado()));
        return seccionRepository.save(seccion);
    }

    @Override
    @Transactional
    public Seccion actualizar(Integer idSeccion, Seccion seccion) {
        validarSeccion(seccion);
        Seccion existente = obtenerPorId(idSeccion);

        existente.setNombre(seccion.getNombre().trim());
        existente.setGrado(obtenerGrado(seccion.getGrado().getIdGrado()));

        return seccionRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idSeccion) {
        Seccion existente = obtenerPorId(idSeccion);
        seccionRepository.delete(existente);
    }

    private void validarSeccion(Seccion seccion) {
        if (seccion.getNombre() == null || seccion.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre de la seccion es obligatorio");
        }
        if (seccion.getGrado() == null || seccion.getGrado().getIdGrado() == null) {
            throw new BadRequestException("El idGrado es obligatorio");
        }
    }

    private Grado obtenerGrado(Integer idGrado) {
        return gradoRepository.findById(idGrado)
                .orElseThrow(() -> new ResourceNotFoundException("Grado no encontrado con id: " + idGrado));
    }
}