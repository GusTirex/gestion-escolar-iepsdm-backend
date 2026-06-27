package com.sdm.gestion_escolar_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sdm.gestion_escolar_backend.entity.Docente;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {

    Optional<Docente> findByUsuarioIdUsuario(Integer idUsuario);
}
