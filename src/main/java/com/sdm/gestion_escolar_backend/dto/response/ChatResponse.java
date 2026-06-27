package com.sdm.gestion_escolar_backend.dto.response;

public class ChatResponse {

    private String respuesta;

    public ChatResponse() {
    }

    public ChatResponse(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}