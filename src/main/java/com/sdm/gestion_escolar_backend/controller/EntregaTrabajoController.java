package com.sdm.gestion_escolar_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdm.gestion_escolar_backend.dto.request.CrearEntregaDTO;
import com.sdm.gestion_escolar_backend.dto.response.EntregaResponseDTO;
import com.sdm.gestion_escolar_backend.entity.EntregaTrabajo;
import com.sdm.gestion_escolar_backend.service.EntregaTrabajoService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
@CrossOrigin
public class EntregaTrabajoController {

    private final EntregaTrabajoService entregaService;

    private EntregaResponseDTO convertirADTO(EntregaTrabajo e) {
        var ev = e.getEvaluacion();
        return EntregaResponseDTO.builder()
                .idEntrega(e.getIdEntrega())
                .idEstudiante(e.getEstudiante() != null ? e.getEstudiante().getIdEstudiante() : null)
                .idEvaluacion(ev != null ? ev.getIdEvaluacion() : null)
                .titulo(ev != null ? ev.getNombre() : null)
                .curso(ev != null && ev.getCurso() != null ? ev.getCurso().getNombre() : null)
                .vence(ev != null ? ev.getFecha() : null)
                .fechaEntrega(e.getFechaEntrega())
                .comentario(e.getComentario())
                .build();
    }

    @GetMapping
    public ResponseEntity<List<EntregaResponseDTO>> listar(
            @Parameter(description = "ID del estudiante", required = true)
            @RequestParam Integer idEstudiante) {
        List<EntregaResponseDTO> entregas = entregaService.listarPorEstudiante(idEstudiante).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(entregas);
    }

    @PostMapping
    public ResponseEntity<EntregaResponseDTO> registrar(@Valid @RequestBody CrearEntregaDTO dto) {
        EntregaTrabajo entrega = entregaService.registrar(
                dto.getIdEstudiante(), dto.getIdEvaluacion(), dto.getComentario());
        return ResponseEntity.status(201).body(convertirADTO(entrega));
    }
}
