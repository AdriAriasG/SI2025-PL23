-- Initial data for testing the Report Management System

-- AgenciaPrensa
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (1, 'Agencia Norte', 'contacto@agencianorte.com');
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (2, 'Prensa Global', 'info@prensaglobal.com');
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (3, 'Medios Centro', 'info@medioscentro.com');

-- Reporteros (nombres genéricos)
-- Agencia 1
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (1, 'Carlos Martinez', 1);
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (2, 'Laura Garcia', 1);
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (3, 'Miguel Fernandez', 1);
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (4, 'Ana Rodriguez', 1);
-- Agencia 2
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (5, 'Pedro Sanchez', 2);
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (6, 'Elena Lopez', 2);
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (7, 'Javier Torres', 2);
-- Agencia 3
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (8, 'Sofia Ruiz', 3);
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (9, 'Diego Moreno', 3);

-- Eventos
-- Agencia 1: Eventos variados con diferentes estados
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (10, 'Final Copa Local', '2026-03-01', 1);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (11, 'Inauguracion Museo Arte', '2026-03-05', 1);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (12, 'Conferencia Tecnologica', '2026-03-10', 1);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (13, 'Maraton Ciudad', '2026-03-15', 1);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (14, 'Festival de Cine', '2026-03-20', 1);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (15, 'Concierto Sinfonico', '2026-04-01', 1);
-- Agencia 2
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (20, 'Concierto Rock Festival', '2026-03-01', 2);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (21, 'Feria de Abril', '2026-04-15', 2);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (22, 'Cumbre Empresarial', '2026-03-25', 2);
-- Agencia 3
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (30, 'Exposicion Internacional', '2026-03-12', 3);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (31, 'Campeonato Natacion', '2026-03-18', 3);

-- Asignaciones (HU #33537)
-- Agencia 1: Varias asignaciones para probar
-- Evento 10 (Final Copa Local) - tiene 2 reporteros asignados
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (10, 1);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (10, 2);
-- Evento 11 (Inauguracion Museo) - tiene 1 reportero asignado
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (11, 3);
-- Evento 12 (Conferencia Tecnologica) - SIN asignar (para probar HU #33537)
-- Evento 13 (Maraton Ciudad) - tiene 1 reportero asignado
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (13, 4);
-- Evento 14 (Festival de Cine) - tiene 2 reporteros asignados
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (14, 1);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (14, 2);
-- Evento 15 (Concierto Sinfonico) - SIN asignar (misma fecha que evento 22 para probar disponibilidad)

-- Agencia 2
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (20, 5);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (20, 6);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (22, 7);

-- Agencia 3
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (30, 8);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (31, 9);

-- EmpresaComunicacion
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (1, 'TeleCable');
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (2, 'El Diario');
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (3, 'Radio Voz');
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (4, 'Canal Noticias');
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (5, 'Prensa Digital');
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (6, 'Emisora Central');

-- Ofrecimientos (HU #33539, #33540)
-- Evento 10 (Final Copa Local) - varios ofrecimientos
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (1, 10, 1, 'PENDIENTE', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (2, 10, 2, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (3, 10, 3, 'RECHAZADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (4, 10, 4, 'ACEPTADO', TRUE);

-- Evento 11 (Inauguracion Museo)
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (5, 11, 1, 'PENDIENTE', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (6, 11, 5, 'ACEPTADO', FALSE);

-- Evento 14 (Festival de Cine)
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (7, 14, 2, 'PENDIENTE', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (8, 14, 6, 'PENDIENTE', FALSE);

-- Evento 20 (Concierto Rock Festival)
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (9, 20, 2, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (10, 20, 3, 'PENDIENTE', FALSE);

-- Reportajes (HU #33538)
-- Evento 10 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo) VALUES (1, 10, 1, 'Victoria historica en la Final Copa Local');
-- Evento 11 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo) VALUES (2, 11, 3, 'Nuevo museo abre sus puertas');
-- Evento 14 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo) VALUES (3, 14, 1, 'Estrellas del cine en el Festival');
-- Evento 20 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo) VALUES (4, 20, 5, 'Noche inolvidable de rock');

-- Versiones de Reportajes (HU #33545, #33547)
-- Reportaje 1 - 3 versiones
INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) 
VALUES (1, 1, 'Resultado sorprendente', 'El equipo local consiguio una victoria inesperada tras un partido muy intenso que mantuvo a todos al borde de sus asientos.', '2026-03-01 22:00:00', 'Version inicial', 1);

INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) 
VALUES (2, 1, 'Resultado sorprendente en tiempo extra', 'El equipo local consiguio una victoria inesperada tras un partido muy intenso que se decidio en el tiempo extra. Los aficionados celebraron por las calles.', '2026-03-02 10:30:00', 'Actualizacion de subtitulo y cuerpo', 1);

INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) 
VALUES (3, 1, 'Triunfo epico en tiempo extra', 'El equipo local consiguio una victoria inesperada tras un partido muy intenso que se decidio en el tiempo extra. Los aficionados celebraron por las calles durante toda la noche. El entrenador dedico el triunfo a la aficion.', '2026-03-02 18:00:00', 'Actualizacion de subtitulo y cuerpo con mas detalles', 2);

-- Reportaje 2 - 2 versiones
INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) 
VALUES (4, 2, 'Arte contemporaneo para todos', 'El nuevo museo de arte abre sus puertas con una coleccion impresionante de obras contemporaneas de artistas locales e internacionales.', '2026-03-05 16:00:00', 'Version inicial', 3);

INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) 
VALUES (5, 2, 'Arte contemporaneo para todos los publicos', 'El nuevo museo de arte abre sus puertas con una coleccion impresionante de obras contemporaneas de artistas locales e internacionales. La entrada sera gratuita durante el primer mes.', '2026-03-06 09:00:00', 'Actualizacion de subtitulo y cuerpo', 3);

-- Reportaje 3 - 1 version
INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) 
VALUES (6, 3, 'Celebrities en la alfombra roja', 'Las grandes estrellas del cine internacional llegaron al Festival de Cine para presentar sus ultimas producciones.', '2026-03-20 23:00:00', 'Version inicial', 1);

-- Reportaje 4 - 1 version
INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) 
VALUES (7, 4, 'Mil personas vibraron con el rock', 'El Concierto Rock Festival reunio a mil personas que disfrutaron de una noche inolvidable con las mejores bandas del momento.', '2026-03-02 01:00:00', 'Version inicial', 5);