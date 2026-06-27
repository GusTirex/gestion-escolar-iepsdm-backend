package com.sdm.gestion_escolar_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.entity.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    Optional<Estudiante> findByUsuarioIdUsuario(Integer idUsuario);
}
