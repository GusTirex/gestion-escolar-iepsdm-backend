package com.sdm.gestion_escolar_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteCursoResponseDTO {
    private Integer idDocenteCurso;
    private Integer idDocente;
    private Integer idCurso;
    private Integer idSeccion;
}