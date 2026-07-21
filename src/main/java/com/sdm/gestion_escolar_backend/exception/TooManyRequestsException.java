package com.sdm.gestion_escolar_backend.exception;

/** Se lanza cuando un usuario hace demasiadas peticiones en poco tiempo. */
public class TooManyRequestsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TooManyRequestsException(String message) {
        super(message);
    }
}
