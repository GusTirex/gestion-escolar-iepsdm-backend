package com.sdm.gestion_escolar_backend.service;

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

    public String obtenerContextoAcademico() {
        StringBuilder contexto = new StringBuilder();

        contexto.append("Información actual de la base de datos del sistema escolar:\n\n");

        agregarTabla(contexto, "usuarios", "SELECT * FROM usuarios LIMIT 20");
        agregarTabla(contexto, "estudiantes", "SELECT * FROM estudiantes LIMIT 20");
        agregarTabla(contexto, "docentes", "SELECT * FROM docentes LIMIT 20");
        agregarTabla(contexto, "cursos", "SELECT * FROM cursos LIMIT 20");
        agregarTabla(contexto, "matriculas", "SELECT * FROM matriculas LIMIT 20");
        agregarTabla(contexto, "notas", "SELECT * FROM notas LIMIT 20");
        agregarTabla(contexto, "asistencias", "SELECT * FROM asistencias LIMIT 20");
        agregarTabla(contexto, "evaluaciones", "SELECT * FROM evaluaciones LIMIT 20");
        agregarTabla(contexto, "secciones", "SELECT * FROM secciones LIMIT 20");
        agregarTabla(contexto, "grados", "SELECT * FROM grados LIMIT 20");

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