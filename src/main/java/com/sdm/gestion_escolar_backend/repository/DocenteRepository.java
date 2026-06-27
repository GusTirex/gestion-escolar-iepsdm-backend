package com.sdm.gestion_escolar_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.entity.Docente;

public interface DocenteRepository extends JpaRepository<Docente, Integer> {

    Optional<Docente> findByUsuarioIdUsuario(Integer idUsuario);
}
