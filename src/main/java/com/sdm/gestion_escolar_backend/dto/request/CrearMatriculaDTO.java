package com.sdm.gestion_escolar_backend.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearMatriculaDTO {
    @NotNull(message = "El anio es obligatorio")
    @Min(value = 1900, message = "El anio no puede ser menor a 1900")
    @Max(value = 2100, message = "El anio no puede ser mayor a 2100")
    private Integer anio;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El idEstudiante es obligatorio")
    private Integer idEstudiante;

    @NotNull(message = "El idSeccion es obligatorio")
    private Integer idSeccion;
}