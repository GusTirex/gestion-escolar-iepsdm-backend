package com.sdm.gestion_escolar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.model.Nota;

public interface NotaRepository extends JpaRepository<Nota, Integer> {
    
}
