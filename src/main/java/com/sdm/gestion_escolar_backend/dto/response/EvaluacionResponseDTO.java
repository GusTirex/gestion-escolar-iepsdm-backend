package com.sdm.gestion_escolar_backend.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionResponseDTO {
    private Integer idEvaluacion;
    private String nombre;
    private Double porcentaje;
    private LocalDate fecha;
    private Integer idCurso;
}