package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Anuncio;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.exception.BadRequestException;
import com.sdm.gestion_escolar_backend.exception.ResourceNotFoundException;
import com.sdm.gestion_escolar_backend.repository.AnuncioRepository;
import com.sdm.gestion_escolar_backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnuncioServiceImpl implements AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Anuncio> listar() {
        return anuncioRepository.findAll();
    }

    @Override
    public Anuncio obtenerPorId(Integer idAnuncio) {
        return anuncioRepository.findById(idAnuncio)
                .orElseThrow(() -> new ResourceNotFoundException("Anuncio no encontrado con id: " + idAnuncio));
    }

    @Override
    @Transactional
    public Anuncio crear(Anuncio anuncio) {
        validarAnuncio(anuncio);
        anuncio.setUsuario(obtenerUsuario(anuncio.getUsuario().getIdUsuario()));
        return anuncioRepository.save(anuncio);
    }

    @Override
    @Transactional
    public Anuncio actualizar(Integer idAnuncio, Anuncio anuncio) {
        validarAnuncio(anuncio);
        Anuncio existente = obtenerPorId(idAnuncio);

        existente.setTitulo(anuncio.getTitulo().trim());
        existente.setContenido(anuncio.getContenido().trim());
        existente.setFecha(anuncio.getFecha());
        existente.setUsuario(obtenerUsuario(anuncio.getUsuario().getIdUsuario()));

        return anuncioRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer idAnuncio) {
        Anuncio existente = obtenerPorId(idAnuncio);
        anuncioRepository.delete(existente);
    }

    private void validarAnuncio(Anuncio anuncio) {
        if (anuncio.getTitulo() == null || anuncio.getTitulo().trim().isEmpty()) {
            throw new BadRequestException("El titulo del anuncio es obligatorio");
        }
        if (anuncio.getContenido() == null || anuncio.getContenido().trim().isEmpty()) {
            throw new BadRequestException("El contenido del anuncio es obligatorio");
        }
        if (anuncio.getFecha() == null) {
            throw new BadRequestException("La fecha del anuncio es obligatoria");
        }
        if (anuncio.getUsuario() == null || anuncio.getUsuario().getIdUsuario() == null) {
            throw new BadRequestException("El idUsuario es obligatorio");
        }
    }

    private Usuario obtenerUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + idUsuario));
    }
}