package com.sdm.gestion_escolar_backend.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearAnuncioDTO {
    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 100, message = "El titulo no puede exceder 100 caracteres")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(max = 255, message = "El contenido no puede exceder 255 caracteres")
    private String contenido;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El idUsuario es obligatorio")
    private Integer idUsuario;
}