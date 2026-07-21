package com.sdm.gestion_escolar_backend.service;

import java.util.List;

import com.sdm.gestion_escolar_backend.entity.Notificacion;

public interface NotificacionService {

    List<Notificacion> listarDeUsuario(Integer idUsuario);
    long contarNoLeidas(Integer idUsuario);
    void marcarLeida(Integer idNotificacion);
    /** Marca como leida solo si la notificacion pertenece a ese usuario. */
    void marcarLeidaDeUsuario(Integer idNotificacion, Integer idUsuario);
    void marcarTodasLeidas(Integer idUsuario);

    // Generacion automatica de avisos (se llama tras calificar o entregar).
    void notificarNotaRegistrada(Integer idEstudiante, Integer idEvaluacion, Double nota);
    void notificarTrabajoEntregado(Integer idEstudiante, Integer idEvaluacion);
}
