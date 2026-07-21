package com.sdm.gestion_escolar_backend.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sdm.gestion_escolar_backend.exception.TooManyRequestsException;

/**
 * Limita cuantas preguntas por minuto puede hacer cada usuario al asistente.
 * Evita que alguien agote (y encarezca) la cuota de la API de Gemini.
 */
@Service
public class LimiteChatService {

    private final int maxPorMinuto;
    private final Map<String, Deque<Long>> peticiones = new ConcurrentHashMap<>();

    public LimiteChatService(@Value("${chat.max-por-minuto:12}") int maxPorMinuto) {
        this.maxPorMinuto = maxPorMinuto;
    }

    public void verificar(String usuario) {
        String clave = (usuario != null) ? usuario : "anonimo";
        long ahora = System.currentTimeMillis();
        Deque<Long> marcas = peticiones.computeIfAbsent(clave, k -> new ArrayDeque<>());

        synchronized (marcas) {
            // Descarta lo que ya salio de la ventana de 1 minuto.
            while (!marcas.isEmpty() && ahora - marcas.peekFirst() > 60_000L) {
                marcas.pollFirst();
            }
            if (marcas.size() >= maxPorMinuto) {
                throw new TooManyRequestsException(
                        "Has hecho muchas preguntas seguidas. Espera un momento e intenta de nuevo.");
            }
            marcas.addLast(ahora);
        }

        // Limpieza basica para que el mapa no crezca indefinidamente.
        if (peticiones.size() > 500) {
            peticiones.entrySet().removeIf(e -> {
                synchronized (e.getValue()) {
                    return e.getValue().isEmpty()
                            || ahora - e.getValue().peekLast() > 300_000L;
                }
            });
        }
    }
}
