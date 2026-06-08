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

import com.sdm.gestion_escolar_backend.dto.request.CrearNivelDTO;
import com.sdm.gestion_escolar_backend.dto.response.NivelResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Nivel;
import com.sdm.gestion_escolar_backend.service.NivelService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/niveles")
@RequiredArgsConstructor
@CrossOrigin
public class NivelController {

    private final NivelService nivelService;

    private NivelResponseDTO convertirADTO(Nivel nivel) {
        return NivelResponseDTO.builder()
                .idNivel(nivel.getIdNivel())
                .nombre(nivel.getNombre())
                .build();
    }

    @GetMapping
    public ResponseEntity<List<NivelResponseDTO>> obtenerTodosLosNiveles() {
        List<NivelResponseDTO> niveles = nivelService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(niveles);
    }

    @GetMapping("/{idNivel}")
    public ResponseEntity<NivelResponseDTO> obtenerNivelPorId(
            @Parameter(description = "ID del nivel a buscar", required = true) @PathVariable Integer idNivel) {
        return ResponseEntity.ok(convertirADTO(nivelService.obtenerPorId(idNivel)));
    }

    @PostMapping
    public ResponseEntity<NivelResponseDTO> crearNivel(@Valid @RequestBody CrearNivelDTO dto) {
        Nivel nivel = Nivel.builder().nombre(dto.getNombre()).build();
        return ResponseEntity.status(201).body(convertirADTO(nivelService.crear(nivel)));
    }

    @PutMapping("/{idNivel}")
    public ResponseEntity<NivelResponseDTO> actualizarNivel(
            @Parameter(description = "ID del nivel a actualizar", required = true) @PathVariable Integer idNivel,
            @Valid @RequestBody CrearNivelDTO dto) {
        Nivel nivel = Nivel.builder().nombre(dto.getNombre()).build();
        return ResponseEntity.ok(convertirADTO(nivelService.actualizar(idNivel, nivel)));
    }

    @DeleteMapping("/{idNivel}")
    public ResponseEntity<Void> eliminarNivel(
            @Parameter(description = "ID del nivel a eliminar", required = true) @PathVariable Integer idNivel) {
        nivelService.eliminar(idNivel);
        return ResponseEntity.noContent().build();
    }
}