package com.sdm.gestion_escolar_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sdm.gestion_escolar_backend.entity.DocenteCurso;

public interface DocenteCursoRepository extends JpaRepository<DocenteCurso, Integer> {

    // Docentes de un curso con su usuario cargado, para notificarles.
    @Query("SELECT dc FROM DocenteCurso dc "
            + "JOIN FETCH dc.docente d JOIN FETCH d.usuario "
            + "WHERE dc.curso.idCurso = :idCurso")
    List<DocenteCurso> findConDocenteUsuarioByCurso(Integer idCurso);
}
