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
import com.sdm.gestion_escolar_backend.security.AccesoService;
import com.sdm.gestion_escolar_backend.service.NotificacionService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
@CrossOrigin
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final AccesoService acceso;

    private NotificacionResponseDTO convertirADTO(Notificacion n) {
        return NotificacionResponseDTO.builder()
                .idNotificacion(n.getIdNotificacion())
                .tipo(n.getTipo())
                .mensaje(n.getMensaje())
                .leida(n.getLeida())
                .fecha(n.getFecha())
                .build();
    }

    // Nadie puede leer las notificaciones de otro: el usuario sale del token
    // y el parametro de la URL se ignora a proposito.
    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listar() {
        List<NotificacionResponseDTO> lista =
                notificacionService.listarDeUsuario(acceso.idUsuarioPropio()).stream()
                        .map(this::convertirADTO)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<Map<String, Long>> contarNoLeidas() {
        return ResponseEntity.ok(
                Map.of("noLeidas", notificacionService.contarNoLeidas(acceso.idUsuarioPropio())));
    }

    @PutMapping("/{idNotificacion}/leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable Integer idNotificacion) {
        notificacionService.marcarLeidaDeUsuario(idNotificacion, acceso.idUsuarioPropio());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/leer-todas")
    public ResponseEntity<Void> marcarTodasLeidas() {
        notificacionService.marcarTodasLeidas(acceso.idUsuarioPropio());
        return ResponseEntity.noContent().build();
    }
}
