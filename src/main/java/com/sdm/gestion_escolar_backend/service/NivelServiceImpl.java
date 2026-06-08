package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Nivel;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.NivelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NivelServiceImpl implements NivelService {

    private final NivelRepository nivelRepository;

    @Override
    public List<Nivel> listar() {
        return nivelRepository.findAll();
    }

    @Override
    public Nivel obtenerPorId(Integer idNivel) {
        return nivelRepository.findById(idNivel)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel no encontrado con id: " + idNivel));
    }

    @Override
    @Transactional
    public Nivel crear(Nivel nivel) {
        validarNivel(nivel);
        nivel.setNombre(nivel.getNombre().trim());
        return nivelRepository.save(nivel);
    }

    @Override
    @Transactional
    public Nivel actualizar(Integer idNivel, Nivel nivel) {
        validarNivel(nivel);
        Nivel existente = obtenerPorId(idNivel);

        existente.setNombre(nivel.getNombre().trim());
        return nivelRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idNivel) {
        Nivel existente = obtenerPorId(idNivel);
        nivelRepository.delete(existente);
    }

    private void validarNivel(Nivel nivel) {
        if (nivel.getNombre() == null || nivel.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del nivel es obligatorio");
        }
    }
}