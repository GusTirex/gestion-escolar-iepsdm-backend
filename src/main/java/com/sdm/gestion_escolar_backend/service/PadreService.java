package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Padre;

public interface PadreService {
    Padre crear(Padre padre);
    Padre actualizar(Integer idPadre, Padre padre);
    void eliminar(Integer idPadre);
    List<Padre> listar();
    Padre obtenerPorId(Integer idPadre);
}