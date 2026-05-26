package com.sdm.gestion_escolar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.model.Docente;

public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    
}
