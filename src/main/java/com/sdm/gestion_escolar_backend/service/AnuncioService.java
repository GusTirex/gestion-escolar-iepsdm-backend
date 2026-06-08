package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Anuncio;

public interface AnuncioService {
    Anuncio crear(Anuncio anuncio);
    Anuncio actualizar(Integer idAnuncio, Anuncio anuncio);
    void eliminar(Integer idAnuncio);
    List<Anuncio> listar();
    Anuncio obtenerPorId(Integer idAnuncio);
}