package com.sdm.gestion_escolar_backend.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearAsistenciaDTO {
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El idEstudiante es obligatorio")
    private Integer idEstudiante;
}