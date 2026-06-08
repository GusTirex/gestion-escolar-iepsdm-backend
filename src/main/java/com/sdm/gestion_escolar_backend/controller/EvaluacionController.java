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

import com.sdm.gestion_escolar_backend.dto.request.CrearEvaluacionDTO;
import com.sdm.gestion_escolar_backend.dto.response.EvaluacionResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Curso;
import com.sdm.gestion_escolar_backend.entity.Evaluacion;
import com.sdm.gestion_escolar_backend.service.EvaluacionService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/evaluaciones")
@RequiredArgsConstructor
@CrossOrigin
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    private EvaluacionResponseDTO convertirADTO(Evaluacion evaluacion) {
        return EvaluacionResponseDTO.builder()
                .idEvaluacion(evaluacion.getIdEvaluacion())
                .nombre(evaluacion.getNombre())
                .porcentaje(evaluacion.getPorcentaje())
                .fecha(evaluacion.getFecha())
                .idCurso(evaluacion.getCurso() != null ? evaluacion.getCurso().getIdCurso() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerTodasLasEvaluaciones() {
        List<EvaluacionResponseDTO> evaluaciones = evaluacionService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(evaluaciones);
    }

    @GetMapping("/{idEvaluacion}")
    public ResponseEntity<EvaluacionResponseDTO> obtenerEvaluacionPorId(
            @Parameter(description = "ID de la evaluacion a buscar", required = true)
            @PathVariable Integer idEvaluacion) {
        return ResponseEntity.ok(convertirADTO(evaluacionService.obtenerPorId(idEvaluacion)));
    }

    @PostMapping
    public ResponseEntity<EvaluacionResponseDTO> crearEvaluacion(@Valid @RequestBody CrearEvaluacionDTO dto) {
        Evaluacion evaluacion = Evaluacion.builder()
                .nombre(dto.getNombre())
                .porcentaje(dto.getPorcentaje())
                .fecha(dto.getFecha())
                .curso(Curso.builder().idCurso(dto.getIdCurso()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(evaluacionService.crear(evaluacion)));
    }

    @PutMapping("/{idEvaluacion}")
    public ResponseEntity<EvaluacionResponseDTO> actualizarEvaluacion(
            @Parameter(description = "ID de la evaluacion a actualizar", required = true)
            @PathVariable Integer idEvaluacion,
            @Valid @RequestBody CrearEvaluacionDTO dto) {
        Evaluacion evaluacion = Evaluacion.builder()
                .nombre(dto.getNombre())
                .porcentaje(dto.getPorcentaje())
                .fecha(dto.getFecha())
                .curso(Curso.builder().idCurso(dto.getIdCurso()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(evaluacionService.actualizar(idEvaluacion, evaluacion)));
    }

    @DeleteMapping("/{idEvaluacion}")
    public ResponseEntity<Void> eliminarEvaluacion(
            @Parameter(description = "ID de la evaluacion a eliminar", required = true)
            @PathVariable Integer idEvaluacion) {
        evaluacionService.eliminar(idEvaluacion);
        return ResponseEntity.noContent().build();
    }
}