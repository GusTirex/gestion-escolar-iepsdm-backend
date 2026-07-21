package com.sdm.gestion_escolar_backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdm.gestion_escolar_backend.dto.request.ChatRequest;
import com.sdm.gestion_escolar_backend.dto.response.ChatResponse;
import com.sdm.gestion_escolar_backend.security.AccesoService;
import com.sdm.gestion_escolar_backend.service.GeminiService;
import com.sdm.gestion_escolar_backend.service.LimiteChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@CrossOrigin
public class ChatController {

    private final GeminiService geminiService;
    private final AccesoService acceso;
    private final LimiteChatService limite;

    @PostMapping
    public ChatResponse responder(@RequestBody ChatRequest request) {
        AccesoService.Usuario u = acceso.actual();

        // Limita las preguntas por minuto (protege la cuota de Gemini).
        limite.verificar(u.usuario());

        // El rol y la entidad se toman del token, NO de lo que envie el navegador:
        // asi nadie puede pedirle al asistente los datos de otra persona.
        request.setRol(u.rol());
        request.setIdEntidad(u.idEntidad() != null ? u.idEntidad().longValue() : null);
        request.setUsuarioId(u.idUsuario() != null ? u.idUsuario().longValue() : null);

        return new ChatResponse(geminiService.responder(request));
    }
}
