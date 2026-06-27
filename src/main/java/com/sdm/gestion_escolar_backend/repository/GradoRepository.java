package com.sdm.gestion_escolar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.entity.Grado;

public interface GradoRepository extends JpaRepository<Grado, Integer> {
}
