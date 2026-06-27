package com.sdm.gestion_escolar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.entity.Nivel;

public interface NivelRepository extends JpaRepository<Nivel, Integer> {
}
