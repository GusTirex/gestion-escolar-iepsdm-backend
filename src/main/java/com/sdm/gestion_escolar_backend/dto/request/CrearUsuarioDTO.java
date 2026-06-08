package com.sdm.gestion_escolar_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearUsuarioDTO {
    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede exceder 50 caracteres")
    private String usuario;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es valido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @NotBlank(message = "La password es obligatoria")
    @Size(max = 255, message = "La password no puede exceder 255 caracteres")
    private String password;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El idRol es obligatorio")
    private Integer idRol;
}