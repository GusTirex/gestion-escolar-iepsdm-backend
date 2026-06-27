-- ============================================================
-- Datos del sistema IEPSDM (dataset oficial del equipo)
--
-- Uso (Windows, MySQL en el puerto local):
--   mysql --default-character-set=utf8mb4 -u root -p -h 127.0.0.1 -P 3307 iepsdm < scripts/seed-demo.sql
--
-- IMPORTANTE: usar --default-character-set=utf8mb4 para que los acentos
-- se guarden correctamente. La base se crea con: CREATE DATABASE iepsdm;
-- y las tablas las genera Hibernate al arrancar el backend (ddl-auto=update).
--
-- Roles: ESTUDIANTE=1, DOCENTE=2, ADMIN=3 (orden del dataset oficial).
-- ============================================================
USE iepsdm;

-- Limpieza para poder recargar el dataset desde cero
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE notas;
TRUNCATE TABLE evaluaciones;
TRUNCATE TABLE asistencias;
TRUNCATE TABLE matriculas;
TRUNCATE TABLE docentes_cursos;
TRUNCATE TABLE anuncios;
TRUNCATE TABLE estudiantes;
TRUNCATE TABLE docentes;
TRUNCATE TABLE padres;
TRUNCATE TABLE usuarios;
TRUNCATE TABLE secciones;
TRUNCATE TABLE grados;
TRUNCATE TABLE niveles;
TRUNCATE TABLE cursos;
TRUNCATE TABLE roles;
SET FOREIGN_KEY_CHECKS = 1;

-- ===================== DATASET OFICIAL =====================
INSERT INTO roles (rol) VALUES
('ESTUDIANTE'),
('DOCENTE'),
('ADMIN');

INSERT INTO usuarios (email, estado, password, usuario, id_rol) VALUES
('juan.perez@colegio.edu.pe', 1, '123456', 'juanperez', 1),
('maria.lopez@colegio.edu.pe', 1, '123456', 'marialopez', 1),
('carlos.ramos@colegio.edu.pe', 1, '123456', 'carlosramos', 2);

INSERT INTO niveles (nombre) VALUES
('Secundaria');

INSERT INTO grados (nombre, id_nivel) VALUES
('4to de Secundaria', 1);

INSERT INTO secciones (nombre, id_grado) VALUES
('A', 1);

INSERT INTO estudiantes (nombres, apellidos, fecha_nacimiento, telefono, direccion, id_usuario) VALUES
('Juan', 'Pérez', '2010-05-15', '999888777', 'Lima', 1),
('María', 'López', '2010-08-20', '988777666', 'Lima', 2);

INSERT INTO docentes (nombres, apellidos, fecha_nacimiento, telefono, direccion, especialidad, id_usuario) VALUES
('Carlos', 'Ramos', '1985-03-10', '977666555', 'Lima', 'Programación Web', 3);

INSERT INTO cursos (nombre, descripcion) VALUES
('Programación Web', 'Curso de desarrollo web con HTML, CSS, JavaScript y React'),
('Cálculo I', 'Curso de matemáticas básicas y funciones'),
('Historia Universal', 'Curso de historia general');

INSERT INTO docentes_cursos (id_curso, id_docente, id_seccion) VALUES
(1, 1, 1),
(2, 1, 1),
(3, 1, 1);

INSERT INTO matriculas (anio, estado, fecha, id_estudiante, id_seccion) VALUES
(2026, 1, '2026-03-01', 1, 1),
(2026, 1, '2026-03-01', 2, 1);

INSERT INTO evaluaciones (nombre, fecha, porcentaje, id_curso) VALUES
('Proyecto Final de Programación', '2026-07-01', 30, 1),
('Examen Parcial de Cálculo I', '2026-06-20', 25, 2),
('Ensayo de Historia Universal', '2026-06-25', 20, 3);

INSERT INTO notas (nota, observacion, id_estudiante, id_evaluacion) VALUES
(18, 'Buen trabajo en React', 1, 1),
(16, 'Debe reforzar ejercicios', 1, 2),
(17, 'Buen análisis histórico', 1, 3);

INSERT INTO asistencias (estado, fecha, id_estudiante) VALUES
(1, '2026-06-20', 1),
(1, '2026-06-21', 1),
(0, '2026-06-22', 1);

INSERT INTO anuncios (titulo, contenido, fecha, id_usuario) VALUES
('Semana de evaluaciones', 'Recordatorio: revisar el calendario de exámenes.', '2026-06-19', 3);

-- ===================== EXTRAS (opcional, demo) =====================
-- Solo AGREGAN datos para poder probar los portales Admin y Padre del frontend.
-- No modifican el dataset oficial. Bórralos si no los necesitas.
INSERT INTO roles (rol) VALUES ('PADRE');               -- id_rol = 4

INSERT INTO usuarios (email, estado, password, usuario, id_rol) VALUES
('admin@colegio.edu.pe', 1, '123456', 'admin', 3),       -- administrador
('padre.perez@colegio.edu.pe', 1, '123456', 'padrejuan', 4); -- padre de Juan

INSERT INTO padres (nombres, apellidos, fecha_nacimiento, telefono, direccion, id_usuario) VALUES
('Roberto', 'Pérez', '1982-02-10', '966555444', 'Lima',
 (SELECT id_usuario FROM usuarios WHERE usuario = 'padrejuan'));
