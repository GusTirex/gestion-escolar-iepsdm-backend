package com.sdm.gestion_escolar_backend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.dto.response.ResumenEstudianteDTO;
import com.sdm.gestion_escolar_backend.entity.Asistencia;
import com.sdm.gestion_escolar_backend.entity.Curso;
import com.sdm.gestion_escolar_backend.entity.Docente;
import com.sdm.gestion_escolar_backend.entity.DocenteCurso;
import com.sdm.gestion_escolar_backend.entity.Evaluacion;
import com.sdm.gestion_escolar_backend.entity.Nota;
import com.sdm.gestion_escolar_backend.repository.AsistenciaRepository;
import com.sdm.gestion_escolar_backend.repository.CursoRepository;
import com.sdm.gestion_escolar_backend.repository.DocenteCursoRepository;
import com.sdm.gestion_escolar_backend.repository.EvaluacionRepository;
import com.sdm.gestion_escolar_backend.repository.NotaRepository;

import lombok.RequiredArgsConstructor;

/**
 * Arma el resumen academico del estudiante en el servidor.
 * Devuelve solo lo que la pantalla necesita, en vez de mandar tablas enteras.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumenEstudianteService {

    private final CursoRepository cursoRepository;
    private final NotaRepository notaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final DocenteCursoRepository docenteCursoRepository;

    public ResumenEstudianteDTO obtener(Integer idEstudiante) {
        List<Curso> cursos = cursoRepository.findAll();
        List<Evaluacion> evaluaciones = evaluacionRepository.findAll();
        List<Nota> notas = notaRepository.findByEstudianteIdEstudiante(idEstudiante);
        List<Asistencia> asistencias = asistenciaRepository.findByEstudianteIdEstudiante(idEstudiante);

        Map<Integer, Curso> cursoPorId = cursos.stream()
                .collect(Collectors.toMap(Curso::getIdCurso, c -> c, (a, b) -> a));
        Map<Integer, Evaluacion> evalPorId = evaluaciones.stream()
                .collect(Collectors.toMap(Evaluacion::getIdEvaluacion, e -> e, (a, b) -> a));

        // Primer docente encontrado por curso (para mostrar el nombre).
        Map<Integer, Docente> docentePorCurso = new HashMap<>();
        for (DocenteCurso dc : docenteCursoRepository.findAll()) {
            if (dc.getCurso() == null || dc.getDocente() == null) continue;
            docentePorCurso.putIfAbsent(dc.getCurso().getIdCurso(), dc.getDocente());
        }

        // --- Notas del estudiante con su curso resuelto ---
        record NotaPlana(Integer idEvaluacion, String evaluacion, Double nota,
                         java.time.LocalDate fecha, Integer idCurso, String curso) {}

        List<NotaPlana> notasPlanas = new ArrayList<>();
        for (Nota n : notas) {
            Integer idEv = n.getEvaluacion() != null ? n.getEvaluacion().getIdEvaluacion() : null;
            Evaluacion ev = idEv != null ? evalPorId.get(idEv) : null;
            Integer idCurso = (ev != null && ev.getCurso() != null) ? ev.getCurso().getIdCurso() : null;
            Curso curso = idCurso != null ? cursoPorId.get(idCurso) : null;
            notasPlanas.add(new NotaPlana(
                    idEv,
                    ev != null ? ev.getNombre() : "Evaluacion",
                    n.getNota(),
                    ev != null ? ev.getFecha() : null,
                    idCurso,
                    curso != null ? curso.getNombre() : "Curso"));
        }

        double promedio = notasPlanas.isEmpty() ? 0.0
                : notasPlanas.stream().mapToDouble(NotaPlana::nota).average().orElse(0.0);

        long presentes = asistencias.stream().filter(a -> Boolean.TRUE.equals(a.getEstado())).count();
        int asistenciaPct = asistencias.isEmpty() ? 0
                : (int) Math.round((presentes * 100.0) / asistencias.size());

        // --- Pendientes = evaluaciones sin nota del estudiante ---
        Set<Integer> conNota = notasPlanas.stream()
                .map(NotaPlana::idEvaluacion).filter(Objects::nonNull).collect(Collectors.toSet());

        List<ResumenEstudianteDTO.Trabajo> pendientes = evaluaciones.stream()
                .filter(e -> !conNota.contains(e.getIdEvaluacion()))
                .sorted(Comparator.comparing(Evaluacion::getFecha,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(e -> {
                    Curso c = (e.getCurso() != null) ? cursoPorId.get(e.getCurso().getIdCurso()) : null;
                    return ResumenEstudianteDTO.Trabajo.builder()
                            .idEvaluacion(e.getIdEvaluacion())
                            .titulo(e.getNombre())
                            .curso(c != null ? c.getNombre() : "Curso")
                            .vence(e.getFecha())
                            .build();
                })
                .toList();

        List<ResumenEstudianteDTO.Trabajo> completados = notasPlanas.stream()
                .sorted(Comparator.comparing(NotaPlana::fecha,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(n -> ResumenEstudianteDTO.Trabajo.builder()
                        .idEvaluacion(n.idEvaluacion())
                        .titulo(n.evaluacion())
                        .curso(n.curso())
                        .vence(n.fecha())
                        .nota(n.nota())
                        .build())
                .toList();

        // --- Notas agrupadas por curso (solo cursos donde tiene notas) ---
        List<ResumenEstudianteDTO.CursoNotas> porCurso = new ArrayList<>();
        for (Curso c : cursos) {
            List<NotaPlana> items = notasPlanas.stream()
                    .filter(n -> c.getIdCurso().equals(n.idCurso()))
                    .toList();
            if (items.isEmpty()) continue;

            Docente d = docentePorCurso.get(c.getIdCurso());
            porCurso.add(ResumenEstudianteDTO.CursoNotas.builder()
                    .curso(c.getNombre())
                    .docente(d != null ? d.getNombres() + " " + d.getApellidos() : "—")
                    .promedio(items.stream().mapToDouble(NotaPlana::nota).average().orElse(0.0))
                    .items(items.stream()
                            .map(n -> ResumenEstudianteDTO.Item.builder()
                                    .idEvaluacion(n.idEvaluacion())
                                    .evaluacion(n.evaluacion())
                                    .nota(n.nota())
                                    .fecha(n.fecha())
                                    .build())
                            .toList())
                    .build());
        }

        return ResumenEstudianteDTO.builder()
                .promedio(promedio)
                .totalCursos(cursos.size())
                .asistenciaPct(asistenciaPct)
                .notasPorCurso(porCurso)
                .trabajosPendientes(pendientes)
                .trabajosCompletados(completados)
                .build();
    }
}
