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
public class AnuncioResponseDTO {
    private Integer idAnuncio;
    private String titulo;
    private String contenido;
    private LocalDate fecha;
    private Integer idUsuario;
}