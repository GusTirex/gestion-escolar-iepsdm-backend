package com.sdm.gestion_escolar_backend.service;

import com.sdm.gestion_escolar_backend.dto.request.ChatRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    private final RestClient restClient;
    private final ChatDataService chatDataService;

    public GeminiService(ChatDataService chatDataService) {
        this.chatDataService = chatDataService;
        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String responder(ChatRequest request) {

        if (apiKey == null || apiKey.isBlank()) {
            return "No se encontró la API Key de Gemini.";
        }

        String contextoPermitido = chatDataService.obtenerContextoPermitido(request);

        String prompt = """
                Eres un asistente académico de un campus virtual escolar.

                REGLAS DE PRIVACIDAD OBLIGATORIAS:
                - Usa únicamente la información del CONTEXTO PERMITIDO.
                - No inventes datos.
                - No reveles información de alumnos, padres, docentes o usuarios que no aparezcan en el contexto permitido.
                - Si el usuario pide información de otra persona que no aparece en el contexto permitido, responde:
                  "No puedo darte esa información porque es privada."
                - Si el usuario pregunta por sus notas, cursos, asistencia, tareas o evaluaciones, responde solo con la información disponible en el contexto permitido.
                - Si el rol es ADMIN, puede consultar toda la información disponible.

                REGLAS DE ANÁLISIS ACADÉMICO:
                - Las notas van de 0 a 20.
                - Una nota baja es entre 0 y 10.99.
                - Un curso está EN RIESGO DE JALAR si su promedio es menor a 11.
                - Un curso NECESITA REFUERZO si su promedio está entre 11 y 13.99.
                - Un curso está ESTABLE si su promedio es 14 o más.
                - Si el estudiante pregunta qué curso debe reforzar, usa las tablas:
                  analisis_promedio_por_curso, notas_bajas_del_estudiante, curso_mas_debil_del_estudiante, temas_recomendados_para_reforzar y notas_bajas_en_tareas.
                - Si pregunta qué curso está por jalar, en peligro de jalar o qué curso tiene baja nota, responde con los cursos que tengan promedio menor a 11.
                - Si no hay cursos con promedio menor a 11, responde que no tiene cursos en riesgo y que siga manteniendo su rendimiento.
                - Si existen notas bajas en evaluaciones específicas, menciona el curso, la evaluación y la nota.
                - Si pregunta por tareas pendientes, usa la tabla tareas_pendientes_del_estudiante.
                - Si pregunta qué tareas tiene, usa tareas_del_estudiante y distingue entre PENDIENTE y CALIFICADA.
                - Si pregunta qué temas debe mejorar, usa temas_recomendados_para_reforzar y notas_bajas_en_tareas.
                - Si pregunta qué temas vienen en el siguiente examen, usa proximos_examenes_y_temas.
                - Si una tarea tiene nota entre 0 y 10.99, recomiéndale reforzar el tema relacionado.
                - Si no tiene tareas pendientes, responde que no tiene tareas pendientes registradas.
                - Si no hay temas con promedio bajo, responde que sus temas están estables y que siga repasando.
                - Da recomendaciones breves y útiles: repasar temas, practicar ejercicios, pedir apoyo al docente y priorizar el curso con menor promedio.
                - No exageres. Sé claro, directo y amable.
                

                - Si el rol es PADRE y pregunta por tareas, notas de tareas o pendientes, usa tareas_de_sus_hijos.
                - Si el rol es DOCENTE y pregunta qué alumnos dejaron tarea, quién tiene tareas pendientes o notas de tareas, usa tareas_de_sus_alumnos.
                - Si el rol es ADMIN y pregunta por tareas, pendientes o notas de tareas, usa reporte_general_tareas.
                - Para tareas, indica curso, nombre de tarea, estado, nota si existe y fecha de entrega.

                
                FORMATO DE RESPUESTA:
                - Responde en español.
                - Usa respuestas breves.
                - Si hay riesgo académico, menciona primero el curso más débil.
                - Si todo está bien, felicita brevemente y recomienda mantener el ritmo.

                ROL DEL USUARIO:
                %s

                ID DE ENTIDAD DEL USUARIO:
                %s

                CONTEXTO PERMITIDO:
                %s

                PREGUNTA DEL USUARIO:
                %s
                """.formatted(
                request.getRol(),
                request.getIdEntidad(),
                contextoPermitido,
                request.getMensaje()
        );

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        Map<String, Object> response = restClient.post()
                .uri("/v1beta/models/" + model + ":generateContent?key=" + apiKey)
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        return extraerTexto(response);
    }

    @SuppressWarnings("unchecked")
    private String extraerTexto(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> candidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) candidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            Map<String, Object> part = parts.get(0);

            return part.get("text").toString();

        } catch (Exception e) {
            return "Gemini respondió, pero no pude interpretar la respuesta.";
        }
    }
}