package com.sdm.gestion_escolar_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private Integer idUsuario;
    private String usuario;
    private String email;
    private String rol;       // ESTUDIANTE | DOCENTE | ADMIN | PADRE
    private String nombre;    // nombre completo de la persona
    private Integer idEntidad; // idEstudiante / idDocente / idPadre segun el rol
    private String token;     // JWT que autentica las siguientes peticiones
}
