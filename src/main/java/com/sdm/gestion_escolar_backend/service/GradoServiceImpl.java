package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Grado;
import com.sdm.gestion_escolar_backend.entity.Nivel;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.GradoRepository;
import com.sdm.gestion_escolar_backend.repository.NivelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradoServiceImpl implements GradoService {

    private final GradoRepository gradoRepository;
    private final NivelRepository nivelRepository;

    @Override
    public List<Grado> listar() {
        return gradoRepository.findAll();
    }

    @Override
    public Grado obtenerPorId(Integer idGrado) {
        return gradoRepository.findById(idGrado)
                .orElseThrow(() -> new ResourceNotFoundException("Grado no encontrado con id: " + idGrado));
    }

    @Override
    @Transactional
    public Grado crear(Grado grado) {
        validarGrado(grado);
        grado.setNombre(grado.getNombre().trim());
        grado.setNivel(obtenerNivel(grado.getNivel().getIdNivel()));
        return gradoRepository.save(grado);
    }

    @Override
    @Transactional
    public Grado actualizar(Integer idGrado, Grado grado) {
        validarGrado(grado);
        Grado existente = obtenerPorId(idGrado);

        existente.setNombre(grado.getNombre().trim());
        existente.setNivel(obtenerNivel(grado.getNivel().getIdNivel()));

        return gradoRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idGrado) {
        Grado existente = obtenerPorId(idGrado);
        gradoRepository.delete(existente);
    }

    private void validarGrado(Grado grado) {
        if (grado.getNombre() == null || grado.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del grado es obligatorio");
        }
        if (grado.getNivel() == null || grado.getNivel().getIdNivel() == null) {
            throw new BadRequestException("El idNivel es obligatorio");
        }
    }

    private Nivel obtenerNivel(Integer idNivel) {
        return nivelRepository.findById(idNivel)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel no encontrado con id: " + idNivel));
    }
}