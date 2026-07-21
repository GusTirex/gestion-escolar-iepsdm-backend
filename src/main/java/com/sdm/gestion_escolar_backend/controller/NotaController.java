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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdm.gestion_escolar_backend.dto.request.CrearNotaDTO;
import com.sdm.gestion_escolar_backend.dto.response.NotaResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.entity.Evaluacion;
import com.sdm.gestion_escolar_backend.entity.Nota;
import com.sdm.gestion_escolar_backend.security.AccesoService;
import com.sdm.gestion_escolar_backend.service.NotaService;
import com.sdm.gestion_escolar_backend.service.NotificacionService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notas")
@RequiredArgsConstructor
@CrossOrigin
public class NotaController {

    private final NotaService notaService;
    private final AccesoService acceso;
    private final NotificacionService notificacionService;

    private NotaResponseDTO convertirADTO(Nota nota) {
        return NotaResponseDTO.builder()
                .idNota(nota.getIdNota())
                .nota(nota.getNota())
                .observacion(nota.getObservacion())
                .idEvaluacion(nota.getEvaluacion() != null ? nota.getEvaluacion().getIdEvaluacion() : null)
                .idEstudiante(nota.getEstudiante() != null ? nota.getEstudiante().getIdEstudiante() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<NotaResponseDTO>> obtenerTodasLasNotas(
            @Parameter(description = "Filtra las notas de un estudiante (opcional)")
            @RequestParam(required = false) Integer idEstudiante) {
        // Un alumno solo puede ver las suyas y un padre las de sus hijos:
        // el id real sale del token, no del parametro.
        Integer permitido = acceso.estudiantePermitido(idEstudiante);
        List<Nota> base = (permitido != null)
                ? notaService.listarPorEstudiante(permitido)
                : notaService.listar();
        List<NotaResponseDTO> notas = base.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/{idNota}")
    public ResponseEntity<NotaResponseDTO> obtenerNotaPorId(
            @Parameter(description = "ID de la nota a buscar", required = true) @PathVariable Integer idNota) {
        return ResponseEntity.ok(convertirADTO(notaService.obtenerPorId(idNota)));
    }

    @PreAuthorize("hasAnyRole('DOCENTE','ADMIN')")
    @PostMapping
    public ResponseEntity<NotaResponseDTO> crearNota(@Valid @RequestBody CrearNotaDTO dto) {
        Nota nota = Nota.builder()
                .nota(dto.getNota())
                .observacion(dto.getObservacion())
                .evaluacion(Evaluacion.builder().idEvaluacion(dto.getIdEvaluacion()).build())
                .estudiante(Estudiante.builder().idEstudiante(dto.getIdEstudiante()).build())
                .registradoPor(acceso.actual().usuario()) // queda registrado quien la puso
                .build();
        Nota guardada = notaService.crear(nota);
        // Genera el aviso al estudiante y a sus padres. Si algo falla aqui,
        // NO debe afectar el registro de la nota (ya esta guardada).
        try {
            notificacionService.notificarNotaRegistrada(dto.getIdEstudiante(), dto.getIdEvaluacion(), dto.getNota());
        } catch (Exception ignorado) {
            // Se ignora a proposito: la nota es lo importante.
        }
        return ResponseEntity.status(201).body(convertirADTO(guardada));
    }

    @PreAuthorize("hasAnyRole('DOCENTE','ADMIN')")
    @PutMapping("/{idNota}")
    public ResponseEntity<NotaResponseDTO> actualizarNota(
            @Parameter(description = "ID de la nota a actualizar", required = true) @PathVariable Integer idNota,
            @Valid @RequestBody CrearNotaDTO dto) {
        Nota nota = Nota.builder()
                .nota(dto.getNota())
                .observacion(dto.getObservacion())
                .evaluacion(Evaluacion.builder().idEvaluacion(dto.getIdEvaluacion()).build())
                .estudiante(Estudiante.builder().idEstudiante(dto.getIdEstudiante()).build())
                .registradoPor(acceso.actual().usuario()) // queda registrado quien la cambio
                .build();
        return ResponseEntity.ok(convertirADTO(notaService.actualizar(idNota, nota)));
    }

    @PreAuthorize("hasAnyRole('DOCENTE','ADMIN')")
    @DeleteMapping("/{idNota}")
    public ResponseEntity<Void> eliminarNota(
            @Parameter(description = "ID de la nota a eliminar", required = true) @PathVariable Integer idNota) {
        notaService.eliminar(idNota);
        return ResponseEntity.noContent().build();
    }
}