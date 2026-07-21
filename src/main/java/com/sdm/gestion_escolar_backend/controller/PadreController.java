package com.sdm.gestion_escolar_backend.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

import com.sdm.gestion_escolar_backend.dto.request.CrearPadreDTO;
import com.sdm.gestion_escolar_backend.dto.response.PadreResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Padre;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.exception.ForbiddenException;
import com.sdm.gestion_escolar_backend.security.AccesoService;
import com.sdm.gestion_escolar_backend.repository.EstudiantePadreRepository;
import com.sdm.gestion_escolar_backend.service.PadreService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/padres")
@RequiredArgsConstructor
@CrossOrigin
public class PadreController {

    private final PadreService padreService;
    private final EstudiantePadreRepository estudiantePadreRepository;
    private final AccesoService acceso;

    // Hijos vinculados a un padre (relación estudiantes_padres).
    @GetMapping("/{idPadre}/hijos")
    public ResponseEntity<List<Map<String, Object>>> obtenerHijos(
            @Parameter(description = "ID del padre") @PathVariable Integer idPadre) {
        // Un padre solo puede pedir SUS hijos; docentes y admin pueden consultar.
        var u = acceso.actual();
        if (u.esPadre() && !u.idEntidad().equals(idPadre)) {
            throw new ForbiddenException("Solo puedes consultar a tus propios hijos.");
        }
        if (u.esEstudiante()) {
            throw new ForbiddenException("No tienes permiso para consultar esta informacion.");
        }
        List<Map<String, Object>> hijos = estudiantePadreRepository.findByPadreIdPadre(idPadre).stream()
                .filter(ep -> ep.getEstudiante() != null)
                .map(ep -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("idEstudiante", ep.getEstudiante().getIdEstudiante());
                    m.put("nombres", ep.getEstudiante().getNombres());
                    m.put("apellidos", ep.getEstudiante().getApellidos());
                    m.put("parentesco", ep.getParentesco());
                    return m;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(hijos);
    }

    private PadreResponseDTO convertirADTO(Padre padre) {
        return PadreResponseDTO.builder()
                .idPadre(padre.getIdPadre())
                .nombres(padre.getNombres())
                .apellidos(padre.getApellidos())
                .fechaNacimiento(padre.getFechaNacimiento())
                .direccion(padre.getDireccion())
                .telefono(padre.getTelefono())
                .idUsuario(padre.getUsuario() != null ? padre.getUsuario().getIdUsuario() : null)
                .build();
    }

    @PreAuthorize("hasAnyRole('DOCENTE','ADMIN')")
    @GetMapping
    public ResponseEntity<List<PadreResponseDTO>> obtenerTodosLosPadres() {
        List<PadreResponseDTO> padres = padreService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(padres);
    }

    @PreAuthorize("hasAnyRole('DOCENTE','ADMIN')")
    @GetMapping("/{idPadre}")
    public ResponseEntity<PadreResponseDTO> obtenerPadrePorId(
            @Parameter(description = "ID del padre a buscar", required = true) @PathVariable Integer idPadre) {
        return ResponseEntity.ok(convertirADTO(padreService.obtenerPorId(idPadre)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PadreResponseDTO> crearPadre(@Valid @RequestBody CrearPadreDTO dto) {
        Padre padre = Padre.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .usuario(Usuario.builder().idUsuario(dto.getIdUsuario()).build())
                .build();
        return ResponseEntity.status(201).body(convertirADTO(padreService.crear(padre)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idPadre}")
    public ResponseEntity<PadreResponseDTO> actualizarPadre(
            @Parameter(description = "ID del padre a actualizar", required = true) @PathVariable Integer idPadre,
            @Valid @RequestBody CrearPadreDTO dto) {
        Padre padre = Padre.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .usuario(Usuario.builder().idUsuario(dto.getIdUsuario()).build())
                .build();
        return ResponseEntity.ok(convertirADTO(padreService.actualizar(idPadre, padre)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idPadre}")
    public ResponseEntity<Void> eliminarPadre(
            @Parameter(description = "ID del padre a eliminar", required = true) @PathVariable Integer idPadre) {
        padreService.eliminar(idPadre);
        return ResponseEntity.noContent().build();
    }
}