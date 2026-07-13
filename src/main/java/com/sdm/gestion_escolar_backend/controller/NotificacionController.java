package com.sdm.gestion_escolar_backend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdm.gestion_escolar_backend.dto.response.NotificacionResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Notificacion;
import com.sdm.gestion_escolar_backend.service.NotificacionService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
@CrossOrigin
public class NotificacionController {

    private final NotificacionService notificacionService;

    private NotificacionResponseDTO convertirADTO(Notificacion n) {
        return NotificacionResponseDTO.builder()
                .idNotificacion(n.getIdNotificacion())
                .tipo(n.getTipo())
                .mensaje(n.getMensaje())
                .leida(n.getLeida())
                .fecha(n.getFecha())
                .build();
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listar(
            @Parameter(description = "ID del usuario", required = true)
            @RequestParam Integer idUsuario) {
        List<NotificacionResponseDTO> lista = notificacionService.listarDeUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<Map<String, Long>> contarNoLeidas(
            @RequestParam Integer idUsuario) {
        return ResponseEntity.ok(Map.of("noLeidas", notificacionService.contarNoLeidas(idUsuario)));
    }

    @PutMapping("/{idNotificacion}/leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable Integer idNotificacion) {
        notificacionService.marcarLeida(idNotificacion);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/leer-todas")
    public ResponseEntity<Void> marcarTodasLeidas(@RequestParam Integer idUsuario) {
        notificacionService.marcarTodasLeidas(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
