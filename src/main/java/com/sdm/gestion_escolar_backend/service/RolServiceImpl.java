package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Rol;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.RolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public List<Rol> listar() {
        return rolRepository.findAll();
    }

    @Override
    public Rol obtenerPorId(Integer idRol) {
        return rolRepository.findById(idRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + idRol));
    }

    @Override
    @Transactional
    public Rol crear(Rol rol) {
        validarRol(rol);
        rol.setRol(rol.getRol().trim());
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public Rol actualizar(Integer idRol, Rol rol) {
        validarRol(rol);
        Rol existente = obtenerPorId(idRol);

        existente.setRol(rol.getRol().trim());
        return rolRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idRol) {
        Rol existente = obtenerPorId(idRol);
        rolRepository.delete(existente);
    }

    private void validarRol(Rol rol) {
        if (rol.getRol() == null || rol.getRol().trim().isEmpty()) {
            throw new BadRequestException("El nombre del rol es obligatorio");
        }
    }
}