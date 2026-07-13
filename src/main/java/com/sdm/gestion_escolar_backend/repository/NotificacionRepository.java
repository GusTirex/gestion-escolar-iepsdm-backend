package com.sdm.gestion_escolar_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.entity.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findTop30ByIdUsuarioOrderByFechaDesc(Integer idUsuario);

    long countByIdUsuarioAndLeidaFalse(Integer idUsuario);
}
