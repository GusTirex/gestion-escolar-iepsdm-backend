package com.sdm.gestion_escolar_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "docentes_cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_docente_curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente")
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso")
    private Curso curso;

    @Column(nullable = false)
    private Integer id_seccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seccion")
    private Seccion seccion;
}
