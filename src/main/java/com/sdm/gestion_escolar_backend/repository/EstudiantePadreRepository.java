package com.sdm.gestion_escolar_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.entity.EstudiantePadre;

public interface EstudiantePadreRepository extends JpaRepository<EstudiantePadre, Integer> {

    List<EstudiantePadre> findByPadreIdPadre(Integer idPadre);
}
