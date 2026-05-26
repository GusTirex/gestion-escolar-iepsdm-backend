package com.sdm.gestion_escolar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
    
}
