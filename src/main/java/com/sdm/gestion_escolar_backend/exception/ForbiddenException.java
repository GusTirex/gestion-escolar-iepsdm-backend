package com.sdm.gestion_escolar_backend.exception;

/** Se lanza cuando el usuario esta autenticado pero no tiene permiso sobre el recurso. */
public class ForbiddenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ForbiddenException(String message) {
        super(message);
    }
}
