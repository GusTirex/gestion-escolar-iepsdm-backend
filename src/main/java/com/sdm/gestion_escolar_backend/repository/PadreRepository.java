package com.sdm.gestion_escolar_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sdm.gestion_escolar_backend.entity.Padre;

@Repository
public interface PadreRepository extends JpaRepository<Padre, Integer> {

    Optional<Padre> findByUsuarioIdUsuario(Integer idUsuario);
}
