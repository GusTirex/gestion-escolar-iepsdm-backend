package com.sdm.gestion_escolar_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

/**
 * Migra las contraseñas guardadas en texto plano a BCrypt al iniciar la aplicación.
 * Es idempotente: si la contraseña ya está cifrada (empieza con $2), no la toca.
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class PasswordMigrationRunner implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        int migradas = 0;
        for (Usuario u : usuarioRepository.findAll()) {
            String p = u.getPassword();
            if (p != null && !(p.startsWith("$2a$") || p.startsWith("$2b$") || p.startsWith("$2y$"))) {
                u.setPassword(passwordEncoder.encode(p));
                usuarioRepository.save(u);
                migradas++;
            }
        }
        if (migradas > 0) {
            System.out.println("[PasswordMigration] Contraseñas cifradas con BCrypt: " + migradas);
        }
    }
}
