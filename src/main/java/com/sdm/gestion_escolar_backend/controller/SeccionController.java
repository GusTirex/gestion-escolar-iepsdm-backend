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

import com.sdm.gestion_escolar_backend.dto.request.CrearSeccionDTO;
import com.sdm.gestion_escolar_backend.dto.response.SeccionResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Grado;
import com.sdm.gestion_escolar_backend.entity.Seccion;
import com.sdm.gestion_escolar_backend.service.SeccionService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("hasAnyRole('DOCENTE','ADMIN')")
@RequestMapping("/secciones")
@RequiredArgsConstructor
@CrossOrigin
public class SeccionController {

    private final SeccionService seccionService;

    private SeccionResponseDTO convertirADTO(Seccion seccion) {
        return SeccionResponseDTO.builder()
                .idSeccion(seccion.getIdSeccion())
                .nombre(seccion.getNombre())
                .idGrado(seccion.getGrado() != null ? seccion.getGrado().getIdGrado() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<SeccionResponseDTO>> obtenerTodasLasSecciones() {
        List<SeccionResponseDTO> secciones = seccionService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(secciones);
    }

    @GetMapping("/{idSeccion}")
    public ResponseEntity<SeccionResponseDTO> obtenerSeccionPorId(
            @Parameter(description = "ID de la seccion a buscar", required = true) @PathVariable Integer idSeccion) {
        return ResponseEntity.ok(convertirADTO(seccionService.obtenerPorId(idSeccion)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SeccionResponseDTO> crearSeccion(@Valid @RequestBody CrearSeccionDTO dto) {
        Seccion seccion = Seccion.builder()
                .nombre(dto.getNombre())
                .grado(Grado.builder().idGrado(dto.getIdGrado()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(seccionService.crear(seccion)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idSeccion}")
    public ResponseEntity<SeccionResponseDTO> actualizarSeccion(
            @Parameter(description = "ID de la seccion a actualizar", required = true)
            @PathVariable Integer idSeccion,
            @Valid @RequestBody CrearSeccionDTO dto) {
        Seccion seccion = Seccion.builder()
                .nombre(dto.getNombre())
                .grado(Grado.builder().idGrado(dto.getIdGrado()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(seccionService.actualizar(idSeccion, seccion)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idSeccion}")
    public ResponseEntity<Void> eliminarSeccion(
            @Parameter(description = "ID de la seccion a eliminar", required = true) @PathVariable Integer idSeccion) {
        seccionService.eliminar(idSeccion);
        return ResponseEntity.noContent().build();
    }
}