-- ============================================================
-- Datos de demostración para el sistema IEPSDM
-- Estudiante de ejemplo: Juan Pérez (idEstudiante = 1)
--
-- Uso (Windows, MySQL en el puerto local):
--   mysql --default-character-set=utf8mb4 -u root -p -h 127.0.0.1 -P 3307 iepsdm < scripts/seed-demo.sql
--
-- IMPORTANTE: usar --default-character-set=utf8mb4 para que los acentos
-- se guarden correctamente. La base se crea con: CREATE DATABASE iepsdm;
-- y las tablas las genera Hibernate al arrancar el backend (ddl-auto=update).
-- ============================================================
USE iepsdm;
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

-- ROLES (los 3 perfiles del sistema + admin)
INSERT INTO roles (id_rol, rol) VALUES
 (1,'ADMIN'),(2,'DOCENTE'),(3,'ESTUDIANTE'),(4,'PADRE');

-- USUARIOS (estado 1 = activo). Passwords demo (login aun no implementado).
INSERT INTO usuarios (id_usuario, email, estado, password, usuario, id_rol) VALUES
 (1,'admin@iepsdm.edu.pe',1,'demo123','admin',1),
 (2,'walter.ramos@iepsdm.edu.pe',1,'demo123','wramos',2),
 (3,'maria.torres@iepsdm.edu.pe',1,'demo123','mtorres',2),
 (4,'juan.briam@iepsdm.edu.pe',1,'demo123','jbriam',2),
 (5,'sofia.diaz@iepsdm.edu.pe',1,'demo123','sdiaz',2),
 (6,'juan.perez@iepsdm.edu.pe',1,'demo123','jperez',3),
 (7,'carlos.perez@iepsdm.edu.pe',1,'demo123','cperez',4);

-- NIVELES / GRADOS / SECCIONES
INSERT INTO niveles (id_nivel, nombre) VALUES (1,'Secundaria');
INSERT INTO grados (id_grado, nombre, id_nivel) VALUES (1,'3ro de Secundaria',1);
INSERT INTO secciones (id_seccion, nombre, id_grado) VALUES (1,'A',1),(2,'B',1);

-- CURSOS (6 cursos activos)
INSERT INTO cursos (id_curso, nombre, descripcion) VALUES
 (1,'Programación Web','Desarrollo de aplicaciones web con React y Node.js'),
 (2,'Cálculo I','Límites, derivadas e integrales'),
 (3,'Historia Universal','Procesos históricos de la humanidad'),
 (4,'Química General','Fundamentos de la química'),
 (5,'Comunicación','Comprensión y producción de textos'),
 (6,'Inglés','Inglés intermedio');

-- DOCENTES
INSERT INTO docentes (id_docente, nombres, apellidos, direccion, especialidad, fecha_nacimiento, telefono, id_usuario) VALUES
 (1,'Walter','Ramos','Av. Lima 123','Ingeniería de Sistemas','1985-03-12','987654321',2),
 (2,'María','Torres','Jr. Cusco 456','Matemáticas','1982-07-22','987111222',3),
 (3,'Juan','Briam','Av. Sol 789','Historia','1980-11-05','987333444',4),
 (4,'Sofía','Díaz','Calle Real 321','Química','1988-01-30','987555666',5);

-- ESTUDIANTE (Juan Pérez, el del portal)
INSERT INTO estudiantes (id_estudiante, nombres, apellidos, direccion, fecha_nacimiento, telefono, id_usuario) VALUES
 (1,'Juan','Pérez','Av. Los Andes 100','2009-05-12','999888777',6);

-- PADRE
INSERT INTO padres (id_padre, nombres, apellidos, direccion, fecha_nacimiento, telefono, id_usuario) VALUES
 (1,'Carlos','Pérez','Av. Los Andes 100','1983-09-18','999000111',7);

-- MATRICULA (Juan, seccion A, 2026)
INSERT INTO matriculas (id_matricula, anio, estado, fecha, id_estudiante, id_seccion) VALUES
 (1,2026,1,'2026-03-01',1,1);

-- DOCENTES_CURSOS (cada docente dicta su curso en seccion A)
INSERT INTO docentes_cursos (id_docente_curso, id_curso, id_docente, id_seccion) VALUES
 (1,1,1,1),(2,2,2,1),(3,3,3,1),(4,4,4,1);

-- EVALUACIONES por curso (las 4, 6 y 10 quedan sin nota = trabajos pendientes)
INSERT INTO evaluaciones (id_evaluacion, nombre, fecha, porcentaje, id_curso) VALUES
 (1,'Tarea 3','2026-05-10',20,1),
 (2,'Examen Parcial','2026-05-18',40,1),
 (3,'Examen Parcial','2026-05-18',40,2),
 (4,'Tarea de Derivadas','2026-07-01',20,2),
 (5,'Ensayo 2','2026-05-15',30,3),
 (6,'Ensayo Revolución Industrial','2026-07-04',30,3),
 (7,'Laboratorio Ácido-Base','2026-05-15',30,4),
 (8,'Práctica Calificada','2026-05-20',25,5),
 (9,'Speaking Test','2026-05-22',25,6),
 (10,'Proyecto Final - Aplicación Web','2026-07-10',40,1);

-- NOTAS de Juan
INSERT INTO notas (id_nota, nota, observacion, id_estudiante, id_evaluacion) VALUES
 (1,18,'Excelente trabajo',1,1),
 (2,17,'Buen dominio',1,2),
 (3,16,'Aprobado',1,3),
 (5,17,'Buen análisis',1,5),
 (7,18,'Muy buen laboratorio',1,7),
 (8,16,'Aprobado',1,8),
 (9,17,'Good speaking',1,9);

-- ASISTENCIAS (13 registros: 12 presente, 1 ausente ~ 92%)
INSERT INTO asistencias (id_asistencia, estado, fecha, id_estudiante) VALUES
 (1,1,'2026-05-04',1),(2,1,'2026-05-05',1),(3,1,'2026-05-06',1),
 (4,1,'2026-05-07',1),(5,0,'2026-05-08',1),(6,1,'2026-05-11',1),
 (7,1,'2026-05-12',1),(8,1,'2026-05-13',1),(9,1,'2026-05-14',1),
 (10,1,'2026-05-15',1),(11,1,'2026-05-18',1),(12,1,'2026-05-19',1),
 (13,1,'2026-05-20',1);

-- ANUNCIOS
INSERT INTO anuncios (id_anuncio, titulo, contenido, fecha, id_usuario) VALUES
 (1,'Inicio de exámenes parciales','Los exámenes parciales inician el 18 de mayo. Revisa el cronograma.','2026-05-10',1),
 (2,'Entrega de proyectos','Recuerda entregar el Proyecto Final de Programación Web hasta el 10 de julio.','2026-05-20',2);
