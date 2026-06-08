package com.sdm.gestion_escolar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sdm.gestion_escolar_backend.entity.DocenteCurso;

@Repository
public interface DocenteCursoRepository extends JpaRepository<DocenteCurso, Integer> {
}
