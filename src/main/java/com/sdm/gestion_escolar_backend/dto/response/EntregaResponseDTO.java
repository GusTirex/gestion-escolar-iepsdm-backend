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
public class EntregaResponseDTO {
    private Integer idEntrega;
    private Integer idEstudiante;
    private Integer idEvaluacion;
    private String titulo;   // nombre de la evaluacion / trabajo
    private String curso;    // nombre del curso
    private LocalDate vence; // fecha de la evaluacion
    private LocalDate fechaEntrega;
    private String comentario;
}
