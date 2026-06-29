package com.sdm.gestion_escolar_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notas_tareas")
public class NotaTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota_tarea")
    private Integer idNotaTarea;

    @Column(nullable = false)
    private Double nota;

    @Column(length = 255)
    private String observacion;

    @Column(name = "fecha_calificacion")
    private LocalDate fechaCalificacion;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_tarea", nullable = false)
    private Tarea tarea;

    public Integer getIdNotaTarea() { return idNotaTarea; }
    public void setIdNotaTarea(Integer idNotaTarea) { this.idNotaTarea = idNotaTarea; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public LocalDate getFechaCalificacion() { return fechaCalificacion; }
    public void setFechaCalificacion(LocalDate fechaCalificacion) { this.fechaCalificacion = fechaCalificacion; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Tarea getTarea() { return tarea; }
    public void setTarea(Tarea tarea) { this.tarea = tarea; }
}