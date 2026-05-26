package com.sdm.gestion_escolar_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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

import com.sdm.gestion_escolar_backend.dto.request.CrearCursoDTO;
import com.sdm.gestion_escolar_backend.dto.response.CursoResponseDTO;
import com.sdm.gestion_escolar_backend.entity.Curso;
import com.sdm.gestion_escolar_backend.service.CursoService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
@CrossOrigin
public class CursoController {
    
    private final CursoService cursoService;

    // Método de conversión
    private CursoResponseDTO convertirADTO(Curso curso) {
        return CursoResponseDTO.builder()
            .idCurso(curso.getIdCurso())
            .nombre(curso.getNombre())
            .descripcion(curso.getDescripcion())
            .build();
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> obtenerTodosLosCursos() {
        List<Curso> cursos = cursoService.listar();
        List<CursoResponseDTO> cursosDTO = cursos.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(cursosDTO);
    }

    @GetMapping("/{idCurso}")
    public ResponseEntity<CursoResponseDTO> obtenerCursoPorId(
            @Parameter(description = "ID del curso a buscar", required = true)
            @PathVariable Integer idCurso) {
        Curso curso = cursoService.obtenerPorId(idCurso);
        return ResponseEntity.ok(convertirADTO(curso));
    }

    @PostMapping
    public ResponseEntity<CursoResponseDTO> crearCurso(@Valid @RequestBody CrearCursoDTO crearCursoDTO) {
        try {
            Curso curso = Curso.builder()
                .nombre(crearCursoDTO.getNombre())
                .descripcion(crearCursoDTO.getDescripcion())
                .build();
            
            Curso cursoCreado = cursoService.crear(curso);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(cursoCreado));
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{idCurso}")
    public ResponseEntity<CursoResponseDTO> actualizarCurso(
            @Parameter(description = "ID del curso a actualizar", required = true) @PathVariable Integer idCurso, 
            @Valid @RequestBody CrearCursoDTO cursoDTO) {
        try {
            Curso cursoActualizado = Curso.builder()
                .nombre(cursoDTO.getNombre())
                .descripcion(cursoDTO.getDescripcion())
                .build();
            
            Curso curso = cursoService.actualizar(idCurso, cursoActualizado);
            return ResponseEntity.ok(convertirADTO(curso));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idCurso}")
    public ResponseEntity<Void> eliminarCurso(
            @Parameter(description = "ID del curso a eliminar", required = true) @PathVariable Integer idCurso) {
        try {
            cursoService.eliminar(idCurso);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    //SIN DTO
    
    // @GetMapping
    // public List<Curso> listar() {
    //     return cursoService.listar();
    // }

    // @GetMapping("/{id}")
    // public Curso obtenerPorId(@PathVariable Integer idCurso) {
    //     return cursoService.obtenerPorId(idCurso);
    // }

    // @PostMapping
    // public Curso crear(@RequestBody Curso curso) {
    //     return cursoService.crear(curso);
    // }

    // @PutMapping("/{id}")
    // public Curso actualizar(@PathVariable Integer idCurso, @RequestBody Curso curso) {
    //     return cursoService.actualizar(idCurso, curso);
    // }

    // @DeleteMapping("/{id}")
    // public void eliminar(@PathVariable Integer idCurso) {
    //     cursoService.eliminar(idCurso);
    // }
}
