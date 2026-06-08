package com.sdm.gestion_escolar_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearRolDTO {
    @NotBlank(message = "El rol es obligatorio")
    @Size(max = 100, message = "El rol no puede exceder 100 caracteres")
    private String rol;
}