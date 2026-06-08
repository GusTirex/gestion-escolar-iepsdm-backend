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
public class MatriculaResponseDTO {
    private Integer idMatricula;
    private Integer anio;
    private LocalDate fecha;
    private Boolean estado;
    private Integer idEstudiante;
    private Integer idSeccion;
}