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

import com.sdm.gestion_escolar_backend.dto.request.CrearEstudianteDTO;
import com.sdm.gestion_escolar_backend.dto.response.EstudianteResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.service.EstudianteService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/estudiantes")
@RequiredArgsConstructor
@CrossOrigin
public class EstudianteController {

    private final EstudianteService estudianteService;

    private EstudianteResponseDTO convertirADTO(Estudiante estudiante) {
        return EstudianteResponseDTO.builder()
                .idEstudiante(estudiante.getIdEstudiante())
                .nombres(estudiante.getNombres())
                .apellidos(estudiante.getApellidos())
                .fechaNacimiento(estudiante.getFechaNacimiento())
                .direccion(estudiante.getDireccion())
                .telefono(estudiante.getTelefono())
                .idUsuario(estudiante.getUsuario() != null ? estudiante.getUsuario().getIdUsuario() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<EstudianteResponseDTO>> obtenerTodosLosEstudiantes() {
        List<EstudianteResponseDTO> estudiantes = estudianteService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/{idEstudiante}")
    public ResponseEntity<EstudianteResponseDTO> obtenerEstudiantePorId(
            @Parameter(description = "ID del estudiante a buscar", required = true) @PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(convertirADTO(estudianteService.obtenerPorId(idEstudiante)));
    }

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> crearEstudiante(@Valid @RequestBody CrearEstudianteDTO dto) {
        Estudiante estudiante = Estudiante.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .usuario(Usuario.builder().idUsuario(dto.getIdUsuario()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(estudianteService.crear(estudiante)));
    }

    @PutMapping("/{idEstudiante}")
    public ResponseEntity<EstudianteResponseDTO> actualizarEstudiante(
            @Parameter(description = "ID del estudiante a actualizar", required = true)
            @PathVariable Integer idEstudiante,
            @Valid @RequestBody CrearEstudianteDTO dto) {
        Estudiante estudiante = Estudiante.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .usuario(Usuario.builder().idUsuario(dto.getIdUsuario()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(estudianteService.actualizar(idEstudiante, estudiante)));
    }

    @DeleteMapping("/{idEstudiante}")
    public ResponseEntity<Void> eliminarEstudiante(
            @Parameter(description = "ID del estudiante a eliminar", required = true)
            @PathVariable Integer idEstudiante) {
        estudianteService.eliminar(idEstudiante);
        return ResponseEntity.noContent().build();
    }
}