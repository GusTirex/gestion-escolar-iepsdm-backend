package com.sdm.gestion_escolar_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "estudiantes_padres")
public class EstudiantePadre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante_padre")
    private Integer idEstudiantePadre;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_padre", nullable = false)
    private Padre padre;

    @Column(name = "parentesco", nullable = false, length = 50)
    private String parentesco;

    public Integer getIdEstudiantePadre() {
        return idEstudiantePadre;
    }

    public void setIdEstudiantePadre(Integer idEstudiantePadre) {
        this.idEstudiantePadre = idEstudiantePadre;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Padre getPadre() {
        return padre;
    }

    public void setPadre(Padre padre) {
        this.padre = padre;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}