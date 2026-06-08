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
public class CrearDocenteDTO {
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @Size(max = 255, message = "La especialidad no puede exceder 255 caracteres")
    private String especialidad;

    @Size(max = 255, message = "La direccion no puede exceder 255 caracteres")
    private String direccion;

    @Size(max = 20, message = "El telefono no puede exceder 20 caracteres")
    private String telefono;

    @NotNull(message = "El idUsuario es obligatorio")
    private Integer idUsuario;
}