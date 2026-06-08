package com.sdm.gestion_escolar_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearDocenteCursoDTO {
    @NotNull(message = "El idDocente es obligatorio")
    private Integer idDocente;

    @NotNull(message = "El idCurso es obligatorio")
    private Integer idCurso;

    @NotNull(message = "El idSeccion es obligatorio")
    private Integer idSeccion;
}