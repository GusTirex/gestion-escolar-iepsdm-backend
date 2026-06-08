package com.sdm.gestion_escolar_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdm.gestion_escolar_backend.dto.request.CrearNotaDTO;
import com.sdm.gestion_escolar_backend.dto.response.NotaResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.entity.Evaluacion;
import com.sdm.gestion_escolar_backend.entity.Nota;
import com.sdm.gestion_escolar_backend.service.NotaService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notas")
@RequiredArgsConstructor
@CrossOrigin
public class NotaController {

    private final NotaService notaService;

    private NotaResponseDTO convertirADTO(Nota nota) {
        return NotaResponseDTO.builder()
                .idNota(nota.getIdNota())
                .nota(nota.getNota())
                .observacion(nota.getObservacion())
                .idEvaluacion(nota.getEvaluacion() != null ? nota.getEvaluacion().getIdEvaluacion() : null)
                .idEstudiante(nota.getEstudiante() != null ? nota.getEstudiante().getIdEstudiante() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<NotaResponseDTO>> obtenerTodasLasNotas() {
        List<NotaResponseDTO> notas = notaService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/{idNota}")
    public ResponseEntity<NotaResponseDTO> obtenerNotaPorId(
            @Parameter(description = "ID de la nota a buscar", required = true) @PathVariable Integer idNota) {
        return ResponseEntity.ok(convertirADTO(notaService.obtenerPorId(idNota)));
    }

    @PostMapping
    public ResponseEntity<NotaResponseDTO> crearNota(@Valid @RequestBody CrearNotaDTO dto) {
        Nota nota = Nota.builder()
                .nota(dto.getNota())
                .observacion(dto.getObservacion())
                .evaluacion(Evaluacion.builder().idEvaluacion(dto.getIdEvaluacion()).build())
                .estudiante(Estudiante.builder().idEstudiante(dto.getIdEstudiante()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(notaService.crear(nota)));
    }

    @PutMapping("/{idNota}")
    public ResponseEntity<NotaResponseDTO> actualizarNota(
            @Parameter(description = "ID de la nota a actualizar", required = true) @PathVariable Integer idNota,
            @Valid @RequestBody CrearNotaDTO dto) {
        Nota nota = Nota.builder()
                .nota(dto.getNota())
                .observacion(dto.getObservacion())
                .evaluacion(Evaluacion.builder().idEvaluacion(dto.getIdEvaluacion()).build())
                .estudiante(Estudiante.builder().idEstudiante(dto.getIdEstudiante()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(notaService.actualizar(idNota, nota)));
    }

    @DeleteMapping("/{idNota}")
    public ResponseEntity<Void> eliminarNota(
            @Parameter(description = "ID de la nota a eliminar", required = true) @PathVariable Integer idNota) {
        notaService.eliminar(idNota);
        return ResponseEntity.noContent().build();
    }
}