package com.sdm.gestion_escolar_backend.controller;

import com.sdm.gestion_escolar_backend.dto.request.ChatRequest;
import com.sdm.gestion_escolar_backend.dto.response.ChatResponse;
import com.sdm.gestion_escolar_backend.service.GeminiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final GeminiService geminiService;

    public ChatController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping
    public ChatResponse responder(@RequestBody ChatRequest request) {
        String respuesta = geminiService.responder(request.getMensaje());
        return new ChatResponse(respuesta);
    }
}