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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdm.gestion_escolar_backend.dto.request.CrearAsistenciaDTO;
import com.sdm.gestion_escolar_backend.dto.response.AsistenciaResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Asistencia;
import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.service.AsistenciaService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/asistencias")
@RequiredArgsConstructor
@CrossOrigin
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    private AsistenciaResponseDTO convertirADTO(Asistencia asistencia) {
        return AsistenciaResponseDTO.builder()
                .idAsistencia(asistencia.getIdAsistencia())
                .fecha(asistencia.getFecha())
                .estado(asistencia.getEstado())
                .idEstudiante(asistencia.getEstudiante() != null ? asistencia.getEstudiante().getIdEstudiante() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerTodasLasAsistencias(
            @Parameter(description = "Filtra las asistencias de un estudiante (opcional)")
            @RequestParam(required = false) Integer idEstudiante) {
        List<Asistencia> base = (idEstudiante != null)
                ? asistenciaService.listarPorEstudiante(idEstudiante)
                : asistenciaService.listar();
        List<AsistenciaResponseDTO> asistencias = base.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(asistencias);
    }

    @GetMapping("/{idAsistencia}")
    public ResponseEntity<AsistenciaResponseDTO> obtenerAsistenciaPorId(
            @Parameter(description = "ID de la asistencia a buscar", required = true) @PathVariable Integer idAsistencia) {
        return ResponseEntity.ok(convertirADTO(asistenciaService.obtenerPorId(idAsistencia)));
    }

    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> crearAsistencia(@Valid @RequestBody CrearAsistenciaDTO dto) {
        Asistencia asistencia = Asistencia.builder()
                .fecha(dto.getFecha())
                .estado(dto.getEstado())
                .estudiante(Estudiante.builder().idEstudiante(dto.getIdEstudiante()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(asistenciaService.crear(asistencia)));
    }

    @PutMapping("/{idAsistencia}")
    public ResponseEntity<AsistenciaResponseDTO> actualizarAsistencia(
            @Parameter(description = "ID de la asistencia a actualizar", required = true)
            @PathVariable Integer idAsistencia,
            @Valid @RequestBody CrearAsistenciaDTO dto) {
        Asistencia asistencia = Asistencia.builder()
                .fecha(dto.getFecha())
                .estado(dto.getEstado())
                .estudiante(Estudiante.builder().idEstudiante(dto.getIdEstudiante()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(asistenciaService.actualizar(idAsistencia, asistencia)));
    }

    @DeleteMapping("/{idAsistencia}")
    public ResponseEntity<Void> eliminarAsistencia(
            @Parameter(description = "ID de la asistencia a eliminar", required = true)
            @PathVariable Integer idAsistencia) {
        asistenciaService.eliminar(idAsistencia);
        return ResponseEntity.noContent().build();
    }
}