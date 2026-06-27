package com.sdm.gestion_escolar_backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdm.gestion_escolar_backend.dto.request.LoginRequest;
import com.sdm.gestion_escolar_backend.dto.response.LoginResponse;
import com.sdm.gestion_escolar_backend.entity.Usuario;
import com.sdm.gestion_escolar_backend.repository.DocenteRepository;
import com.sdm.gestion_escolar_backend.repository.EstudianteRepository;
import com.sdm.gestion_escolar_backend.repository.PadreRepository;
import com.sdm.gestion_escolar_backend.repository.UsuarioRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final DocenteRepository docenteRepository;
    private final PadreRepository padreRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Usuario u = usuarioRepository.findByUsuario(req.getUsuario());

        if (u == null || !passwordCoincide(req.getPassword(), u.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Usuario o contraseña incorrectos"));
        }
        if (!Boolean.TRUE.equals(u.getEstado())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "La cuenta está inactiva"));
        }

        String rol = (u.getRol() != null) ? u.getRol().getRol() : "ESTUDIANTE";

        String nombre = u.getUsuario();
        Integer idEntidad = null;

        switch (rol) {
            case "ESTUDIANTE" -> {
                var e = estudianteRepository.findByUsuarioIdUsuario(u.getIdUsuario());
                if (e.isPresent()) {
                    nombre = e.get().getNombres() + " " + e.get().getApellidos();
                    idEntidad = e.get().getIdEstudiante();
                }
            }
            case "DOCENTE" -> {
                var d = docenteRepository.findByUsuarioIdUsuario(u.getIdUsuario());
                if (d.isPresent()) {
                    nombre = d.get().getNombres() + " " + d.get().getApellidos();
                    idEntidad = d.get().getIdDocente();
                }
            }
            case "PADRE" -> {
                var p = padreRepository.findByUsuarioIdUsuario(u.getIdUsuario());
                if (p.isPresent()) {
                    nombre = p.get().getNombres() + " " + p.get().getApellidos();
                    idEntidad = p.get().getIdPadre();
                }
            }
            case "ADMIN" -> nombre = "Administrador";
            default -> { /* deja el nombre = usuario */ }
        }

        LoginResponse resp = LoginResponse.builder()
                .idUsuario(u.getIdUsuario())
                .usuario(u.getUsuario())
                .email(u.getEmail())
                .rol(rol)
                .nombre(nombre)
                .idEntidad(idEntidad)
                .build();

        return ResponseEntity.ok(resp);
    }

    // Soporta contraseñas en texto plano (dataset actual) o cifradas con BCrypt (futuro).
    private boolean passwordCoincide(String ingresada, String almacenada) {
        if (almacenada == null || ingresada == null) return false;
        if (almacenada.startsWith("$2a$") || almacenada.startsWith("$2b$") || almacenada.startsWith("$2y$")) {
            return passwordEncoder.matches(ingresada, almacenada);
        }
        return almacenada.equals(ingresada);
    }
}
