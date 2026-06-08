package com.sdm.gestion_escolar_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradoResponseDTO {
    private Integer idGrado;
    private String nombre;
    private Integer idNivel;
}