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

import com.sdm.gestion_escolar_backend.dto.request.CrearAnuncioDTO;
import com.sdm.gestion_escolar_backend.dto.response.AnuncioResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Anuncio;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.service.AnuncioService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/anuncios")
@RequiredArgsConstructor
@CrossOrigin
public class AnuncioController {

    private final AnuncioService anuncioService;

    private AnuncioResponseDTO convertirADTO(Anuncio anuncio) {
        return AnuncioResponseDTO.builder()
                .idAnuncio(anuncio.getIdAnuncio())
                .titulo(anuncio.getTitulo())
                .contenido(anuncio.getContenido())
                .fecha(anuncio.getFecha())
                .idUsuario(anuncio.getUsuario() != null ? anuncio.getUsuario().getIdUsuario() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<AnuncioResponseDTO>> obtenerTodosLosAnuncios() {
        List<AnuncioResponseDTO> anuncios = anuncioService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(anuncios);
    }

    @GetMapping("/{idAnuncio}")
    public ResponseEntity<AnuncioResponseDTO> obtenerAnuncioPorId(
            @Parameter(description = "ID del anuncio a buscar", required = true) @PathVariable Integer idAnuncio) {
        return ResponseEntity.ok(convertirADTO(anuncioService.obtenerPorId(idAnuncio)));
    }

    @PostMapping
    public ResponseEntity<AnuncioResponseDTO> crearAnuncio(@Valid @RequestBody CrearAnuncioDTO dto) {
        Anuncio anuncio = Anuncio.builder()
                .titulo(dto.getTitulo())
                .contenido(dto.getContenido())
                .fecha(dto.getFecha())
                .usuario(Usuario.builder().idUsuario(dto.getIdUsuario()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(anuncioService.crear(anuncio)));
    }

    @PutMapping("/{idAnuncio}")
    public ResponseEntity<AnuncioResponseDTO> actualizarAnuncio(
            @Parameter(description = "ID del anuncio a actualizar", required = true) @PathVariable Integer idAnuncio,
            @Valid @RequestBody CrearAnuncioDTO dto) {
        Anuncio anuncio = Anuncio.builder()
                .titulo(dto.getTitulo())
                .contenido(dto.getContenido())
                .fecha(dto.getFecha())
                .usuario(Usuario.builder().idUsuario(dto.getIdUsuario()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(anuncioService.actualizar(idAnuncio, anuncio)));
    }

    @DeleteMapping("/{idAnuncio}")
    public ResponseEntity<Void> eliminarAnuncio(
            @Parameter(description = "ID del anuncio a eliminar", required = true) @PathVariable Integer idAnuncio) {
        anuncioService.eliminar(idAnuncio);
        return ResponseEntity.noContent().build();
    }
}