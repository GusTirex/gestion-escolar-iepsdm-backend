package com.sdm.gestion_escolar_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Aviso dentro de la app dirigido a un usuario (por su idUsuario).
 * Se genera automaticamente al calificar o al entregar un trabajo.
 */
@Entity
// Se consulta siempre por usuario (la campana refresca cada 40 s),
// por eso el indice sobre id_usuario.
@Table(name = "notificaciones",
        indexes = @Index(name = "idx_notificaciones_usuario", columnList = "id_usuario"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNotificacion;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(nullable = false, length = 30)
    private String tipo; // NOTA | ENTREGA

    @Column(nullable = false, length = 300)
    private String mensaje;

    @Builder.Default
    @Column(nullable = false)
    private Boolean leida = false;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
