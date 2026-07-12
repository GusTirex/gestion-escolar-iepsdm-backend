package com.sdm.gestion_escolar_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdm.gestion_escolar_backend.entity.Nota;

public interface NotaRepository extends JpaRepository<Nota, Integer> {

    List<Nota> findByEstudianteIdEstudiante(Integer idEstudiante);
}
