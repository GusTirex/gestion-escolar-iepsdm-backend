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

import com.sdm.gestion_escolar_backend.dto.request.CrearGradoDTO;
import com.sdm.gestion_escolar_backend.dto.response.GradoResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Grado;
import com.sdm.gestion_escolar_backend.entity.Nivel;
import com.sdm.gestion_escolar_backend.service.GradoService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/grados")
@RequiredArgsConstructor
@CrossOrigin
public class GradoController {

    private final GradoService gradoService;

    private GradoResponseDTO convertirADTO(Grado grado) {
        return GradoResponseDTO.builder()
                .idGrado(grado.getIdGrado())
                .nombre(grado.getNombre())
                .idNivel(grado.getNivel() != null ? grado.getNivel().getIdNivel() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<GradoResponseDTO>> obtenerTodosLosGrados() {
        List<GradoResponseDTO> grados = gradoService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(grados);
    }

    @GetMapping("/{idGrado}")
    public ResponseEntity<GradoResponseDTO> obtenerGradoPorId(
            @Parameter(description = "ID del grado a buscar", required = true) @PathVariable Integer idGrado) {
        return ResponseEntity.ok(convertirADTO(gradoService.obtenerPorId(idGrado)));
    }

    @PostMapping
    public ResponseEntity<GradoResponseDTO> crearGrado(@Valid @RequestBody CrearGradoDTO dto) {
        Grado grado = Grado.builder()
                .nombre(dto.getNombre())
                .nivel(Nivel.builder().idNivel(dto.getIdNivel()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(gradoService.crear(grado)));
    }

    @PutMapping("/{idGrado}")
    public ResponseEntity<GradoResponseDTO> actualizarGrado(
            @Parameter(description = "ID del grado a actualizar", required = true) @PathVariable Integer idGrado,
            @Valid @RequestBody CrearGradoDTO dto) {
        Grado grado = Grado.builder()
                .nombre(dto.getNombre())
                .nivel(Nivel.builder().idNivel(dto.getIdNivel()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(gradoService.actualizar(idGrado, grado)));
    }

    @DeleteMapping("/{idGrado}")
    public ResponseEntity<Void> eliminarGrado(
            @Parameter(description = "ID del grado a eliminar", required = true) @PathVariable Integer idGrado) {
        gradoService.eliminar(idGrado);
        return ResponseEntity.noContent().build();
    }
}