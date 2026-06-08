package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Nivel;

public interface NivelService {
    Nivel crear(Nivel nivel);
    Nivel actualizar(Integer idNivel, Nivel nivel);
    void eliminar(Integer idNivel);
    List<Nivel> listar();
    Nivel obtenerPorId(Integer idNivel);
}
