package com.sdm.gestion_escolar_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sdm.gestion_escolar_backend.entity.EstudiantePadre;

public interface EstudiantePadreRepository extends JpaRepository<EstudiantePadre, Integer> {

    List<EstudiantePadre> findByPadreIdPadre(Integer idPadre);

    // Trae los vinculos de un estudiante con el padre y su usuario ya cargados,
    // para poder notificar a los padres sin problemas de carga perezosa.
    @Query("SELECT ep FROM EstudiantePadre ep "
            + "JOIN FETCH ep.padre p JOIN FETCH p.usuario "
            + "WHERE ep.estudiante.idEstudiante = :idEstudiante")
    List<EstudiantePadre> findConPadreUsuarioByEstudiante(Integer idEstudiante);
}
