package com.sdm.gestion_escolar_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sdm.gestion_escolar_backend.entity.Grado;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Integer> {
}
