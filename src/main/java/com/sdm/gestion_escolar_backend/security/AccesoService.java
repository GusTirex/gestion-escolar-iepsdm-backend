package com.sdm.gestion_escolar_backend.security;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sdm.gestion_escolar_backend.entity.EstudiantePadre;
import com.sdm.gestion_escolar_backend.exception.ForbiddenException;
import com.sdm.gestion_escolar_backend.repository.EstudiantePadreRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

/**
 * Centraliza "quien esta pidiendo" y "que tiene permitido ver".
 *
 * La identidad SIEMPRE sale del token, nunca de un parametro de la URL:
 * asi un alumno no puede pedir los datos de otro cambiando el id.
 */
@Service
@RequiredArgsConstructor
public class AccesoService {

    private final EstudiantePadreRepository estudiantePadreRepository;

    public record Usuario(String usuario, String rol, Integer idUsuario, Integer idEntidad) {
        public boolean esAdmin() { return "ADMIN".equals(rol); }
        public boolean esDocente() { return "DOCENTE".equals(rol); }
        public boolean esEstudiante() { return "ESTUDIANTE".equals(rol); }
        public boolean esPadre() { return "PADRE".equals(rol); }
    }

    /** Datos del usuario que hizo la peticion (leidos del JWT). */
    public Usuario actual() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getDetails() instanceof Claims c)) {
            throw new ForbiddenException("No se pudo identificar al usuario.");
        }
        return new Usuario(
                c.getSubject(),
                c.get("rol", String.class),
                aEntero(c.get("idUsuario")),
                aEntero(c.get("idEntidad")));
    }

    /**
     * Devuelve el idEstudiante que el usuario tiene permitido consultar.
     * - ESTUDIANTE: siempre el suyo (se ignora lo que pida).
     * - PADRE: solo el de sus hijos.
     * - DOCENTE / ADMIN: cualquiera.
     */
    public Integer estudiantePermitido(Integer idSolicitado) {
        Usuario u = actual();

        if (u.esEstudiante()) {
            return u.idEntidad();
        }
        if (u.esPadre()) {
            List<Integer> hijos = estudiantePadreRepository.findByPadreIdPadre(u.idEntidad())
                    .stream()
                    .map(ep -> ep.getEstudiante() != null ? ep.getEstudiante().getIdEstudiante() : null)
                    .filter(java.util.Objects::nonNull)
                    .toList();
            if (idSolicitado == null) {
                return hijos.isEmpty() ? null : hijos.get(0);
            }
            if (!hijos.contains(idSolicitado)) {
                throw new ForbiddenException("Solo puedes consultar la informacion de tus hijos.");
            }
            return idSolicitado;
        }
        if (u.esDocente() || u.esAdmin()) {
            return idSolicitado;
        }
        throw new ForbiddenException("No tienes permiso para consultar esta informacion.");
    }

    /** El idUsuario propio: nadie puede leer las notificaciones de otro. */
    public Integer idUsuarioPropio() {
        return actual().idUsuario();
    }

    /** Exige rol de administrador. */
    public void exigirAdmin() {
        if (!actual().esAdmin()) {
            throw new ForbiddenException("Esta accion es solo para administradores.");
        }
    }

    /** Exige rol de docente o administrador (ej. registrar/modificar notas). */
    public void exigirDocenteOAdmin() {
        Usuario u = actual();
        if (!u.esDocente() && !u.esAdmin()) {
            throw new ForbiddenException("Esta accion es solo para docentes o administradores.");
        }
    }

    /** Verifica que el vinculo padre-hijo exista antes de dar datos del hijo. */
    public void exigirHijoDe(Integer idPadre, Integer idEstudiante) {
        boolean esSuHijo = estudiantePadreRepository.findByPadreIdPadre(idPadre).stream()
                .map(EstudiantePadre::getEstudiante)
                .filter(java.util.Objects::nonNull)
                .anyMatch(e -> e.getIdEstudiante().equals(idEstudiante));
        if (!esSuHijo) {
            throw new ForbiddenException("Ese estudiante no esta asociado a tu cuenta.");
        }
    }

    private Integer aEntero(Object valor) {
        if (valor == null) return null;
        if (valor instanceof Integer i) return i;
        if (valor instanceof Number n) return n.intValue();
        try {
            return Integer.valueOf(valor.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
