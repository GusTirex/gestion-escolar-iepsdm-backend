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

import com.sdm.gestion_escolar_backend.dto.request.CrearRolDTO;
import com.sdm.gestion_escolar_backend.dto.response.RolResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Rol;
import com.sdm.gestion_escolar_backend.service.RolService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@CrossOrigin
public class RolController {

    private final RolService rolService;

    private RolResponseDTO convertirADTO(Rol rol) {
        return RolResponseDTO.builder()
                .idRol(rol.getIdRol())
                .rol(rol.getRol())
                .build();
    }

    @GetMapping
    public ResponseEntity<List<RolResponseDTO>> obtenerTodosLosRoles() {
        List<RolResponseDTO> roles = rolService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{idRol}")
    public ResponseEntity<RolResponseDTO> obtenerRolPorId(
            @Parameter(description = "ID del rol a buscar", required = true) @PathVariable Integer idRol) {
        return ResponseEntity.ok(convertirADTO(rolService.obtenerPorId(idRol)));
    }

    @PostMapping
    public ResponseEntity<RolResponseDTO> crearRol(@Valid @RequestBody CrearRolDTO dto) {
        Rol rol = Rol.builder().rol(dto.getRol()).build();
        return ResponseEntity.status(201).body(convertirADTO(rolService.crear(rol)));
    }

    @PutMapping("/{idRol}")
    public ResponseEntity<RolResponseDTO> actualizarRol(
            @Parameter(description = "ID del rol a actualizar", required = true) @PathVariable Integer idRol,
            @Valid @RequestBody CrearRolDTO dto) {
        Rol rol = Rol.builder().rol(dto.getRol()).build();
        return ResponseEntity.ok(convertirADTO(rolService.actualizar(idRol, rol)));
    }

    @DeleteMapping("/{idRol}")
    public ResponseEntity<Void> eliminarRol(
            @Parameter(description = "ID del rol a eliminar", required = true) @PathVariable Integer idRol) {
        rolService.eliminar(idRol);
        return ResponseEntity.noContent().build();
    }
}