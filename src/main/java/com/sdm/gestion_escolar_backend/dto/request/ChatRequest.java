package com.sdm.gestion_escolar_backend.dto.request;

public class ChatRequest {

    private Long alumnoId;
    private String mensaje;

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}