package com.sdm.gestion_escolar_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sdm.gestion_escolar_backend.entity.Docente;
import com.sdm.gestion_escolar_backend.entity.DocenteCurso;
import com.sdm.gestion_escolar_backend.entity.EstudiantePadre;
import com.sdm.gestion_escolar_backend.entity.Estudiante;
import com.sdm.gestion_escolar_backend.entity.Evaluacion;
import com.sdm.gestion_escolar_backend.entity.Notificacion;
import com.sdm.gestion_escolar_backend.entity.Padre;
import com.sdm.gestion_escolar_backend.repository.DocenteCursoRepository;
import com.sdm.gestion_escolar_backend.repository.EstudiantePadreRepository;
import com.sdm.gestion_escolar_backend.repository.EstudianteRepository;
import com.sdm.gestion_escolar_backend.repository.EvaluacionRepository;
import com.sdm.gestion_escolar_backend.repository.NotificacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final EstudiantePadreRepository estudiantePadreRepository;
    private final DocenteCursoRepository docenteCursoRepository;

    @Override
    public List<Notificacion> listarDeUsuario(Integer idUsuario) {
        return notificacionRepository.findTop30ByIdUsuarioOrderByFechaDesc(idUsuario);
    }

    @Override
    public long contarNoLeidas(Integer idUsuario) {
        return notificacionRepository.countByIdUsuarioAndLeidaFalse(idUsuario);
    }

    @Override
    @Transactional
    public void marcarLeida(Integer idNotificacion) {
        notificacionRepository.findById(idNotificacion).ifPresent(n -> {
            n.setLeida(true);
            notificacionRepository.save(n);
        });
    }

    @Override
    @Transactional
    public void marcarLeidaDeUsuario(Integer idNotificacion, Integer idUsuario) {
        notificacionRepository.findById(idNotificacion)
                .filter(n -> n.getIdUsuario().equals(idUsuario)) // no toca las de otros
                .ifPresent(n -> {
                    n.setLeida(true);
                    notificacionRepository.save(n);
                });
    }

    @Override
    @Transactional
    public void marcarTodasLeidas(Integer idUsuario) {
        List<Notificacion> lista = notificacionRepository.findTop30ByIdUsuarioOrderByFechaDesc(idUsuario);
        lista.forEach(n -> n.setLeida(true));
        notificacionRepository.saveAll(lista);
    }

    @Override
    @Transactional
    public void notificarNotaRegistrada(Integer idEstudiante, Integer idEvaluacion, Double nota) {
        Estudiante est = estudianteRepository.findById(idEstudiante).orElse(null);
        Evaluacion ev = evaluacionRepository.findById(idEvaluacion).orElse(null);
        if (est == null || ev == null) return;

        String curso = (ev.getCurso() != null) ? ev.getCurso().getNombre() : ev.getNombre();
        String nn = String.valueOf(Math.round(nota));

        if (est.getUsuario() != null) {
            crear(est.getUsuario().getIdUsuario(), "NOTA",
                    "Te calificaron en " + curso + ": " + nn + "/20");
        }
        for (EstudiantePadre ep : estudiantePadreRepository.findConPadreUsuarioByEstudiante(idEstudiante)) {
            Padre p = ep.getPadre();
            if (p != null && p.getUsuario() != null) {
                crear(p.getUsuario().getIdUsuario(), "NOTA",
                        "Calificaron a " + est.getNombres() + " en " + curso + ": " + nn + "/20");
            }
        }
    }

    @Override
    @Transactional
    public void notificarTrabajoEntregado(Integer idEstudiante, Integer idEvaluacion) {
        Estudiante est = estudianteRepository.findById(idEstudiante).orElse(null);
        Evaluacion ev = evaluacionRepository.findById(idEvaluacion).orElse(null);
        if (est == null || ev == null) return;

        String titulo = ev.getNombre();

        for (EstudiantePadre ep : estudiantePadreRepository.findConPadreUsuarioByEstudiante(idEstudiante)) {
            Padre p = ep.getPadre();
            if (p != null && p.getUsuario() != null) {
                crear(p.getUsuario().getIdUsuario(), "ENTREGA",
                        est.getNombres() + " entregó el trabajo \"" + titulo + "\"");
            }
        }
        if (ev.getCurso() != null) {
            for (DocenteCurso dc : docenteCursoRepository.findConDocenteUsuarioByCurso(ev.getCurso().getIdCurso())) {
                Docente d = dc.getDocente();
                if (d != null && d.getUsuario() != null) {
                    crear(d.getUsuario().getIdUsuario(), "ENTREGA",
                            est.getNombres() + " entregó \"" + titulo + "\"");
                }
            }
        }
    }

    private void crear(Integer idUsuario, String tipo, String mensaje) {
        if (idUsuario == null) return;
        Notificacion n = Notificacion.builder()
                .idUsuario(idUsuario)
                .tipo(tipo)
                .mensaje(mensaje)
                .leida(false)
                .fecha(LocalDateTime.now())
                .build();
        notificacionRepository.save(n);
    }
}
