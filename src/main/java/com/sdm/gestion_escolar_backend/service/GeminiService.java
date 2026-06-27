package com.sdm.gestion_escolar_backend.service;

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

    public String responder(String preguntaAlumno) {

        if (apiKey == null || apiKey.isBlank()) {
            return "No se encontró la API Key de Gemini.";
        }

        String contextoBD = chatDataService.obtenerContextoAcademico();

        String prompt = """
                Eres un asistente académico de un campus virtual escolar.

                Responde únicamente utilizando la información que proviene de la base de datos.
                Si la información no existe, responde que no hay registros.

                Base de datos:

                %s

                Pregunta:

                %s
                """.formatted(contextoBD, preguntaAlumno);

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