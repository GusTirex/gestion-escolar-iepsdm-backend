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
public class DocenteResponseDTO {
    private Integer idDocente;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String especialidad;
    private String direccion;
    private String telefono;
    private Integer idUsuario;
}