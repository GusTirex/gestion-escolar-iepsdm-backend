package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Nota;

public interface NotaService {
    Nota crear(Nota nota);
    Nota actualizar(Integer idNota, Nota nota);
    void eliminar(Integer idNota);
    List<Nota> listar();
    List<Nota> listarPorEstudiante(Integer idEstudiante);
    Nota obtenerPorId(Integer idNota);
}