package com.sdm.gestion_escolar_backend.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Todo lo que necesita el panel del estudiante en una sola respuesta.
 * Antes el navegador se descargaba tablas completas y calculaba esto a mano.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenEstudianteDTO {

    private Double promedio;
    private Integer totalCursos;
    private Integer asistenciaPct;
    private List<CursoNotas> notasPorCurso;
    private List<Trabajo> trabajosPendientes;
    private List<Trabajo> trabajosCompletados;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CursoNotas {
        private String curso;
        private String docente;
        private Double promedio;
        private List<Item> items;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {
        private Integer idEvaluacion;
        private String evaluacion;
        private Double nota;
        private LocalDate fecha;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Trabajo {
        private Integer idEvaluacion;
        private String titulo;
        private String curso;
        private LocalDate vence;
        private Double nota; // solo en los completados
    }
}
