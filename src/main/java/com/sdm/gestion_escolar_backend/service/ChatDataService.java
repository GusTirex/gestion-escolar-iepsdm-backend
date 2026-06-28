package com.sdm.gestion_escolar_backend.service;

import com.sdm.gestion_escolar_backend.dto.request.ChatRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatDataService {

    private final JdbcTemplate jdbcTemplate;

    public ChatDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String obtenerContextoPermitido(ChatRequest request) {
        String rol = request.getRol();

        if (rol == null) {
            return "No se recibió el rol del usuario.";
        }

        if ("ADMIN".equalsIgnoreCase(rol)) {
            return obtenerContextoAdmin();
        }

        if ("ESTUDIANTE".equalsIgnoreCase(rol)) {
            return obtenerContextoEstudiante(request.getIdEntidad());
        }

        if ("DOCENTE".equalsIgnoreCase(rol)) {
            return obtenerContextoDocente(request.getIdEntidad());
        }

        if ("PADRE".equalsIgnoreCase(rol)) {
            return obtenerContextoPadre(request.getIdEntidad());
        }

        return "Rol no reconocido. No hay información disponible.";
    }

    private String obtenerContextoAdmin() {
        StringBuilder contexto = new StringBuilder();

        contexto.append("CONTEXTO PERMITIDO PARA ADMINISTRADOR:\n");
        contexto.append("El administrador puede consultar toda la información del sistema.\n\n");

        agregarTabla(contexto, "usuarios", "SELECT * FROM usuarios LIMIT 100");
        agregarTabla(contexto, "estudiantes", "SELECT * FROM estudiantes LIMIT 100");
        agregarTabla(contexto, "padres", "SELECT * FROM padres LIMIT 100");
        agregarTabla(contexto, "docentes", "SELECT * FROM docentes LIMIT 100");
        agregarTabla(contexto, "cursos", "SELECT * FROM cursos LIMIT 100");
        agregarTabla(contexto, "docentes_cursos", "SELECT * FROM docentes_cursos LIMIT 100");
        agregarTabla(contexto, "matriculas", "SELECT * FROM matriculas LIMIT 100");
        agregarTabla(contexto, "notas", "SELECT * FROM notas LIMIT 100");
        agregarTabla(contexto, "asistencias", "SELECT * FROM asistencias LIMIT 100");
        agregarTabla(contexto, "evaluaciones", "SELECT * FROM evaluaciones LIMIT 100");
        agregarTabla(contexto, "secciones", "SELECT * FROM secciones LIMIT 100");
        agregarTabla(contexto, "grados", "SELECT * FROM grados LIMIT 100");
        agregarTabla(contexto, "niveles", "SELECT * FROM niveles LIMIT 100");
        agregarTabla(contexto, "anuncios", "SELECT * FROM anuncios LIMIT 100");

        return contexto.toString();
    }

    private String obtenerContextoEstudiante(Long idEstudiante) {
        StringBuilder contexto = new StringBuilder();

        if (idEstudiante == null) {
            return "No se recibió el ID del estudiante. No se puede consultar información académica.";
        }

        contexto.append("CONTEXTO PERMITIDO PARA ESTUDIANTE:\n");
        contexto.append("Solo puede consultar información propia del estudiante con ID ")
                .append(idEstudiante)
                .append(".\n");
        contexto.append("No puede consultar información de otros estudiantes.\n\n");

        agregarTabla(contexto, "datos_del_estudiante",
                "SELECT * FROM estudiantes WHERE id_estudiante = " + idEstudiante);

        agregarTabla(contexto, "matriculas_del_estudiante",
                "SELECT * FROM matriculas WHERE id_estudiante = " + idEstudiante);

        agregarTabla(contexto, "cursos_del_estudiante",
                """
                SELECT c.id_curso, c.nombre, c.descripcion, s.nombre AS seccion, g.nombre AS grado
                FROM matriculas m
                JOIN secciones s ON m.id_seccion = s.id_seccion
                JOIN grados g ON s.id_grado = g.id_grado
                JOIN docentes_cursos dc ON dc.id_seccion = s.id_seccion
                JOIN cursos c ON dc.id_curso = c.id_curso
                WHERE m.id_estudiante =
                """ + idEstudiante);

        agregarTabla(contexto, "notas_del_estudiante",
                """
                SELECT c.nombre AS curso, ev.nombre AS evaluacion, ev.fecha,
                       n.nota, n.observacion
                FROM notas n
                JOIN evaluaciones ev ON n.id_evaluacion = ev.id_evaluacion
                JOIN cursos c ON ev.id_curso = c.id_curso
                WHERE n.id_estudiante =
                """ + idEstudiante);

        agregarTabla(contexto, "asistencias_del_estudiante",
                "SELECT fecha, estado FROM asistencias WHERE id_estudiante = " + idEstudiante);

        agregarTabla(contexto, "anuncios_generales",
                "SELECT titulo, contenido, fecha FROM anuncios ORDER BY fecha DESC LIMIT 20");

        return contexto.toString();
    }

    private String obtenerContextoDocente(Long idDocente) {
        StringBuilder contexto = new StringBuilder();

        if (idDocente == null) {
            return "No se recibió el ID del docente. No se puede consultar información académica.";
        }

        contexto.append("CONTEXTO PERMITIDO PARA DOCENTE:\n");
        contexto.append("Solo puede consultar cursos, secciones, estudiantes y notas relacionados a sus propios cursos.\n");
        contexto.append("ID del docente: ").append(idDocente).append("\n\n");

        agregarTabla(contexto, "cursos_del_docente",
                """
                SELECT dc.id_docente_curso, c.id_curso, c.nombre AS curso,
                       c.descripcion, s.id_seccion, s.nombre AS seccion
                FROM docentes_cursos dc
                JOIN cursos c ON dc.id_curso = c.id_curso
                JOIN secciones s ON dc.id_seccion = s.id_seccion
                WHERE dc.id_docente =
                """ + idDocente);

        agregarTabla(contexto, "estudiantes_de_sus_cursos",
                """
                SELECT DISTINCT est.id_estudiante, est.nombres, est.apellidos,
                       c.nombre AS curso, s.nombre AS seccion
                FROM docentes_cursos dc
                JOIN secciones s ON dc.id_seccion = s.id_seccion
                JOIN matriculas m ON m.id_seccion = s.id_seccion
                JOIN estudiantes est ON m.id_estudiante = est.id_estudiante
                JOIN cursos c ON dc.id_curso = c.id_curso
                WHERE dc.id_docente =
                """ + idDocente);

        agregarTabla(contexto, "notas_de_sus_cursos",
                """
                SELECT est.nombres, est.apellidos, c.nombre AS curso,
                       ev.nombre AS evaluacion, ev.fecha, n.nota, n.observacion
                FROM docentes_cursos dc
                JOIN cursos c ON dc.id_curso = c.id_curso
                JOIN evaluaciones ev ON ev.id_curso = c.id_curso
                JOIN notas n ON n.id_evaluacion = ev.id_evaluacion
                JOIN estudiantes est ON est.id_estudiante = n.id_estudiante
                JOIN matriculas m ON m.id_estudiante = est.id_estudiante
                WHERE dc.id_docente =
                """ + idDocente + """
                AND m.id_seccion = dc.id_seccion
                """);

        agregarTabla(contexto, "evaluaciones_de_sus_cursos",
                """
                SELECT c.nombre AS curso, ev.nombre AS evaluacion,
                       ev.fecha, ev.porcentaje
                FROM docentes_cursos dc
                JOIN cursos c ON dc.id_curso = c.id_curso
                JOIN evaluaciones ev ON ev.id_curso = c.id_curso
                WHERE dc.id_docente =
                """ + idDocente);

        return contexto.toString();
    }

private String obtenerContextoPadre(Long idPadre) {
    StringBuilder contexto = new StringBuilder();

    if (idPadre == null) {
        return "No se recibió el ID del padre. No se puede consultar información académica.";
    }

    contexto.append("CONTEXTO PERMITIDO PARA PADRE/APODERADO:\n");
    contexto.append("Solo puede consultar información de sus hijos registrados.\n");
    contexto.append("ID del padre: ").append(idPadre).append("\n\n");

    agregarTabla(contexto, "hijos_del_padre",
            """
            SELECT est.id_estudiante, est.nombres, est.apellidos, ep.parentesco
            FROM estudiantes_padres ep
            JOIN estudiantes est ON ep.id_estudiante = est.id_estudiante
            WHERE ep.id_padre =
            """ + idPadre);

    agregarTabla(contexto, "notas_de_sus_hijos",
            """
            SELECT est.nombres, est.apellidos, c.nombre AS curso,
                   ev.nombre AS evaluacion, ev.fecha, n.nota, n.observacion
            FROM estudiantes_padres ep
            JOIN estudiantes est ON ep.id_estudiante = est.id_estudiante
            JOIN notas n ON n.id_estudiante = est.id_estudiante
            JOIN evaluaciones ev ON n.id_evaluacion = ev.id_evaluacion
            JOIN cursos c ON ev.id_curso = c.id_curso
            WHERE ep.id_padre =
            """ + idPadre);

    agregarTabla(contexto, "asistencias_de_sus_hijos",
            """
            SELECT est.nombres, est.apellidos, a.fecha, a.estado
            FROM estudiantes_padres ep
            JOIN estudiantes est ON ep.id_estudiante = est.id_estudiante
            JOIN asistencias a ON a.id_estudiante = est.id_estudiante
            WHERE ep.id_padre =
            """ + idPadre);

    return contexto.toString();
}

    private void agregarTabla(StringBuilder contexto, String nombreTabla, String sql) {
        try {
            List<Map<String, Object>> datos = jdbcTemplate.queryForList(sql);

            contexto.append("Tabla: ").append(nombreTabla).append("\n");

            if (datos.isEmpty()) {
                contexto.append("Sin registros.\n\n");
                return;
            }

            for (Map<String, Object> fila : datos) {
                contexto.append(fila).append("\n");
            }

            contexto.append("\n");

        } catch (Exception e) {
            contexto.append("No se pudo leer la tabla ")
                    .append(nombreTabla)
                    .append(": ")
                    .append(e.getMessage())
                    .append("\n\n");
        }
    }
}