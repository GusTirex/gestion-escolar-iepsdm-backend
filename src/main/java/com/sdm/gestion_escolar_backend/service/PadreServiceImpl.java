package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Padre;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.PadreRepository;
import com.sdm.gestion_escolar_backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PadreServiceImpl implements PadreService {

    private final PadreRepository padreRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Padre> listar() {
        return padreRepository.findAll();
    }

    @Override
    public Padre obtenerPorId(Integer idPadre) {
        return padreRepository.findById(idPadre)
                .orElseThrow(() -> new ResourceNotFoundException("Padre no encontrado con id: " + idPadre));
    }

    @Override
    @Transactional
    public Padre crear(Padre padre) {
        validarPadre(padre);
        padre.setUsuario(obtenerUsuario(padre.getUsuario().getIdUsuario()));
        padre.setNombres(padre.getNombres().trim());
        padre.setApellidos(padre.getApellidos().trim());
        return padreRepository.save(padre);
    }

    @Override
    @Transactional
    public Padre actualizar(Integer idPadre, Padre padre) {
        validarPadre(padre);
        Padre existente = obtenerPorId(idPadre);

        existente.setNombres(padre.getNombres().trim());
        existente.setApellidos(padre.getApellidos().trim());
        existente.setFechaNacimiento(padre.getFechaNacimiento());
        existente.setDireccion(padre.getDireccion());
        existente.setTelefono(padre.getTelefono());
        existente.setUsuario(obtenerUsuario(padre.getUsuario().getIdUsuario()));

        return padreRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idPadre) {
        Padre existente = obtenerPorId(idPadre);
        padreRepository.delete(existente);
    }

    private void validarPadre(Padre padre) {
        if (padre.getNombres() == null || padre.getNombres().trim().isEmpty()) {
            throw new BadRequestException("Los nombres del padre son obligatorios");
        }
        if (padre.getApellidos() == null || padre.getApellidos().trim().isEmpty()) {
            throw new BadRequestException("Los apellidos del padre son obligatorios");
        }
        if (padre.getFechaNacimiento() == null) {
            throw new BadRequestException("La fecha de nacimiento del padre es obligatoria");
        }
        if (padre.getUsuario() == null || padre.getUsuario().getIdUsuario() == null) {
            throw new BadRequestException("El idUsuario es obligatorio");
        }
    }

    private Usuario obtenerUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + idUsuario));
    }
}