package com.sdm.gestion_escolar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.model.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    
}
