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

import com.sdm.gestion_escolar_backend.dto.request.CrearDocenteDTO;
import com.sdm.gestion_escolar_backend.dto.response.DocenteResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Docente;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.service.DocenteService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("hasAnyRole('DOCENTE','ADMIN')")
@RequestMapping("/docentes")
@RequiredArgsConstructor
@CrossOrigin
public class DocenteController {

    private final DocenteService docenteService;

    private DocenteResponseDTO convertirADTO(Docente docente) {
        return DocenteResponseDTO.builder()
                .idDocente(docente.getIdDocente())
                .nombres(docente.getNombres())
                .apellidos(docente.getApellidos())
                .fechaNacimiento(docente.getFechaNacimiento())
                .especialidad(docente.getEspecialidad())
                .direccion(docente.getDireccion())
                .telefono(docente.getTelefono())
                .idUsuario(docente.getUsuario() != null ? docente.getUsuario().getIdUsuario() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<DocenteResponseDTO>> obtenerTodosLosDocentes() {
        List<DocenteResponseDTO> docentes = docenteService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(docentes);
    }

    @GetMapping("/{idDocente}")
    public ResponseEntity<DocenteResponseDTO> obtenerDocentePorId(
            @Parameter(description = "ID del docente a buscar", required = true) @PathVariable Integer idDocente) {
        return ResponseEntity.ok(convertirADTO(docenteService.obtenerPorId(idDocente)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DocenteResponseDTO> crearDocente(@Valid @RequestBody CrearDocenteDTO dto) {
        Docente docente = Docente.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .especialidad(dto.getEspecialidad())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .usuario(Usuario.builder().idUsuario(dto.getIdUsuario()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(docenteService.crear(docente)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idDocente}")
    public ResponseEntity<DocenteResponseDTO> actualizarDocente(
            @Parameter(description = "ID del docente a actualizar", required = true) @PathVariable Integer idDocente,
            @Valid @RequestBody CrearDocenteDTO dto) {
        Docente docente = Docente.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .especialidad(dto.getEspecialidad())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .usuario(Usuario.builder().idUsuario(dto.getIdUsuario()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(docenteService.actualizar(idDocente, docente)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idDocente}")
    public ResponseEntity<Void> eliminarDocente(
            @Parameter(description = "ID del docente a eliminar", required = true) @PathVariable Integer idDocente) {
        docenteService.eliminar(idDocente);
        return ResponseEntity.noContent().build();
    }
}