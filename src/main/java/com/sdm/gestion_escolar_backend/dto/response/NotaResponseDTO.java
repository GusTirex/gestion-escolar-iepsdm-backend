package com.sdm.gestion_escolar_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaResponseDTO {
    private Integer idNota;
    private Double nota;
    private String observacion;
    private Integer idEvaluacion;
    private Integer idEstudiante;
}