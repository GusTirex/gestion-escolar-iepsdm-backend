package com.sdm.gestion_escolar_backend.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearNotaDTO {
    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "La nota no puede ser menor a 0")
    @DecimalMax(value = "20.0", inclusive = true, message = "La nota no puede ser mayor a 20")
    private Double nota;

    @Size(max = 255, message = "La observacion no puede exceder 255 caracteres")
    private String observacion;

    @NotNull(message = "El idEvaluacion es obligatorio")
    private Integer idEvaluacion;

    @NotNull(message = "El idEstudiante es obligatorio")
    private Integer idEstudiante;
}