package com.sdm.gestion_escolar_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearEntregaDTO {

    @NotNull(message = "El idEstudiante es obligatorio")
    private Integer idEstudiante;

    @NotNull(message = "El idEvaluacion es obligatorio")
    private Integer idEvaluacion;

    @Size(max = 500, message = "El comentario no puede exceder 500 caracteres")
    private String comentario;
}
