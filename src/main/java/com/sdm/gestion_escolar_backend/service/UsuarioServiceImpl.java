package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Rol;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ConflictException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.RolRepository;
import com.sdm.gestion_escolar_backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + idUsuario));
    }

    @Override
    @Transactional
    public Usuario crear(Usuario usuario) {
        validarUsuario(usuario);

        if (usuarioRepository.findByUsuario(usuario.getUsuario().trim()) != null) {
            throw new ConflictException("Ya existe un usuario con el nombre de usuario: " + usuario.getUsuario().trim());
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail().trim())) {
            throw new ConflictException("Ya existe un usuario con el email: " + usuario.getEmail().trim());
        }

        usuario.setUsuario(usuario.getUsuario().trim());
        usuario.setEmail(usuario.getEmail().trim());
        usuario.setRol(obtenerRol(usuario.getRol().getIdRol()));
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizar(Integer idUsuario, Usuario usuario) {
        validarUsuario(usuario);
        Usuario existente = obtenerPorId(idUsuario);

        Usuario encontradoPorUsuario = usuarioRepository.findByUsuario(usuario.getUsuario().trim());
        if (encontradoPorUsuario != null && !encontradoPorUsuario.getIdUsuario().equals(idUsuario)) {
            throw new ConflictException("Ya existe otro usuario con el nombre de usuario: " + usuario.getUsuario().trim());
        }

        usuarioRepository.findByEmail(usuario.getEmail().trim()).ifPresent(encontradoPorEmail -> {
            if (!encontradoPorEmail.getIdUsuario().equals(idUsuario)) {
                throw new ConflictException("Ya existe otro usuario con el email: " + usuario.getEmail().trim());
            }
        });

        existente.setUsuario(usuario.getUsuario().trim());
        existente.setEmail(usuario.getEmail().trim());
        existente.setPassword(usuario.getPassword());
        existente.setEstado(usuario.getEstado());
        existente.setRol(obtenerRol(usuario.getRol().getIdRol()));

        return usuarioRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idUsuario) {
        Usuario existente = obtenerPorId(idUsuario);
        usuarioRepository.delete(existente);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getUsuario() == null || usuario.getUsuario().trim().isEmpty()) {
            throw new BadRequestException("El nombre de usuario es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new BadRequestException("El email es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new BadRequestException("La password es obligatoria");
        }
        if (usuario.getEstado() == null) {
            throw new BadRequestException("El estado del usuario es obligatorio");
        }
        if (usuario.getRol() == null || usuario.getRol().getIdRol() == null) {
            throw new BadRequestException("El idRol es obligatorio");
        }
    }

    private Rol obtenerRol(Integer idRol) {
        return rolRepository.findById(idRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + idRol));
    }
}