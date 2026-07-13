package com.sdm.gestion_escolar_backend.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponseDTO {
    private Integer idNotificacion;
    private String tipo;
    private String mensaje;
    private Boolean leida;
    private LocalDateTime fecha;
}
