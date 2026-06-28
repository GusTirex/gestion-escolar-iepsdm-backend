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
                - Responde en español, claro y breve.

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