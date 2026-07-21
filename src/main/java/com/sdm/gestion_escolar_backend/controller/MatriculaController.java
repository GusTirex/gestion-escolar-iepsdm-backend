package com.sdm.gestion_escolar_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdm.gestion_escolar_backend.dto.request.CrearMatriculaDTO;
import com.sdm.gestion_escolar_backend.dto.response.MatriculaResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.entity.Matricula;
import com.sdm.gestion_escolar_backend.entity.Seccion;
import com.sdm.gestion_escolar_backend.service.MatriculaService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("hasAnyRole('DOCENTE','ADMIN')")
@RequestMapping("/matriculas")
@RequiredArgsConstructor
@CrossOrigin
public class MatriculaController {

    private final MatriculaService matriculaService;

    private MatriculaResponseDTO convertirADTO(Matricula matricula) {
        return MatriculaResponseDTO.builder()
                .idMatricula(matricula.getIdMatricula())
                .anio(matricula.getAnio())
                .fecha(matricula.getFecha())
                .estado(matricula.getEstado())
                .idEstudiante(matricula.getEstudiante() != null ? matricula.getEstudiante().getIdEstudiante() : null)
                .idSeccion(matricula.getSeccion() != null ? matricula.getSeccion().getIdSeccion() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> obtenerTodasLasMatriculas() {
        List<MatriculaResponseDTO> matriculas = matriculaService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(matriculas);
    }

    @GetMapping("/{idMatricula}")
    public ResponseEntity<MatriculaResponseDTO> obtenerMatriculaPorId(
            @Parameter(description = "ID de la matricula a buscar", required = true)
            @PathVariable Integer idMatricula) {
        return ResponseEntity.ok(convertirADTO(matriculaService.obtenerPorId(idMatricula)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> crearMatricula(@Valid @RequestBody CrearMatriculaDTO dto) {
        Matricula matricula = Matricula.builder()
                .anio(dto.getAnio())
                .fecha(dto.getFecha())
                .estado(dto.getEstado())
                .estudiante(Estudiante.builder().idEstudiante(dto.getIdEstudiante()).build())
                .seccion(Seccion.builder().idSeccion(dto.getIdSeccion()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(matriculaService.crear(matricula)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idMatricula}")
    public ResponseEntity<MatriculaResponseDTO> actualizarMatricula(
            @Parameter(description = "ID de la matricula a actualizar", required = true)
            @PathVariable Integer idMatricula,
            @Valid @RequestBody CrearMatriculaDTO dto) {
        Matricula matricula = Matricula.builder()
                .anio(dto.getAnio())
                .fecha(dto.getFecha())
                .estado(dto.getEstado())
                .estudiante(Estudiante.builder().idEstudiante(dto.getIdEstudiante()).build())
                .seccion(Seccion.builder().idSeccion(dto.getIdSeccion()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(matriculaService.actualizar(idMatricula, matricula)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idMatricula}")
    public ResponseEntity<Void> eliminarMatricula(
            @Parameter(description = "ID de la matricula a eliminar", required = true)
            @PathVariable Integer idMatricula) {
        matriculaService.eliminar(idMatricula);
        return ResponseEntity.noContent().build();
    }
}