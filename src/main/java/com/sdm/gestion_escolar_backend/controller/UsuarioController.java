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

import com.sdm.gestion_escolar_backend.dto.request.CrearUsuarioDTO;
import com.sdm.gestion_escolar_backend.dto.response.UsuarioResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Rol;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin
public class UsuarioController {

    private final UsuarioService usuarioService;

    private UsuarioResponseDTO convertirADTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .usuario(usuario.getUsuario())
                .email(usuario.getEmail())
                .estado(usuario.getEstado())
                .idRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario a buscar", required = true) @PathVariable Integer idUsuario) {
        return ResponseEntity.ok(convertirADTO(usuarioService.obtenerPorId(idUsuario)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody CrearUsuarioDTO dto) {
        Usuario usuario = Usuario.builder()
                .usuario(dto.getUsuario())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .estado(dto.getEstado())
                .rol(Rol.builder().idRol(dto.getIdRol()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(usuarioService.crear(usuario)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @Parameter(description = "ID del usuario a actualizar", required = true)
            @PathVariable Integer idUsuario,
            @Valid @RequestBody CrearUsuarioDTO dto) {
        Usuario usuario = Usuario.builder()
                .usuario(dto.getUsuario())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .estado(dto.getEstado())
                .rol(Rol.builder().idRol(dto.getIdRol()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(usuarioService.actualizar(idUsuario, usuario)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "ID del usuario a eliminar", required = true) @PathVariable Integer idUsuario) {
        usuarioService.eliminar(idUsuario);
        return ResponseEntity.noContent().build();
    }
}