package com.sdm.gestion_escolar_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String usuario;
    private String email;
    private Boolean estado;
    private Integer idRol;
}