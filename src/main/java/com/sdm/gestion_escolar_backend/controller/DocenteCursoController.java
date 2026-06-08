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

import com.sdm.gestion_escolar_backend.dto.request.CrearDocenteCursoDTO;
import com.sdm.gestion_escolar_backend.dto.response.DocenteCursoResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Curso;
import com.sdm.gestion_escolar_backend.entity.Docente;
import com.sdm.gestion_escolar_backend.entity.DocenteCurso;
import com.sdm.gestion_escolar_backend.entity.Seccion;
import com.sdm.gestion_escolar_backend.service.DocenteCursoService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/docentes-cursos")
@RequiredArgsConstructor
@CrossOrigin
public class DocenteCursoController {

    private final DocenteCursoService docenteCursoService;

    private DocenteCursoResponseDTO convertirADTO(DocenteCurso docenteCurso) {
        return DocenteCursoResponseDTO.builder()
                .idDocenteCurso(docenteCurso.getIdDocenteCurso())
                .idDocente(docenteCurso.getDocente() != null ? docenteCurso.getDocente().getIdDocente() : null)
                .idCurso(docenteCurso.getCurso() != null ? docenteCurso.getCurso().getIdCurso() : null)
                .idSeccion(docenteCurso.getSeccion() != null ? docenteCurso.getSeccion().getIdSeccion() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<DocenteCursoResponseDTO>> obtenerTodosLosDocenteCursos() {
        List<DocenteCursoResponseDTO> docenteCursos = docenteCursoService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(docenteCursos);
    }

    @GetMapping("/{idDocenteCurso}")
    public ResponseEntity<DocenteCursoResponseDTO> obtenerDocenteCursoPorId(
            @Parameter(description = "ID del registro docente-curso a buscar", required = true)
            @PathVariable Integer idDocenteCurso) {
        return ResponseEntity.ok(convertirADTO(docenteCursoService.obtenerPorId(idDocenteCurso)));
    }

    @PostMapping
    public ResponseEntity<DocenteCursoResponseDTO> crearDocenteCurso(@Valid @RequestBody CrearDocenteCursoDTO dto) {
        DocenteCurso docenteCurso = DocenteCurso.builder()
                .docente(Docente.builder().idDocente(dto.getIdDocente()).build())
                .curso(Curso.builder().idCurso(dto.getIdCurso()).build())
                .seccion(Seccion.builder().idSeccion(dto.getIdSeccion()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(docenteCursoService.crear(docenteCurso)));
    }

    @PutMapping("/{idDocenteCurso}")
    public ResponseEntity<DocenteCursoResponseDTO> actualizarDocenteCurso(
            @Parameter(description = "ID del registro docente-curso a actualizar", required = true)
            @PathVariable Integer idDocenteCurso,
            @Valid @RequestBody CrearDocenteCursoDTO dto) {
        DocenteCurso docenteCurso = DocenteCurso.builder()
                .docente(Docente.builder().idDocente(dto.getIdDocente()).build())
                .curso(Curso.builder().idCurso(dto.getIdCurso()).build())
                .seccion(Seccion.builder().idSeccion(dto.getIdSeccion()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(docenteCursoService.actualizar(idDocenteCurso, docenteCurso)));
    }

    @DeleteMapping("/{idDocenteCurso}")
    public ResponseEntity<Void> eliminarDocenteCurso(
            @Parameter(description = "ID del registro docente-curso a eliminar", required = true)
            @PathVariable Integer idDocenteCurso) {
        docenteCursoService.eliminar(idDocenteCurso);
        return ResponseEntity.noContent().build();
    }
}