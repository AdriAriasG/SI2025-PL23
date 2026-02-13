-- Initial data for testing the Report Management System

-- AgenciaPrensa
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (1, 'Agencia Norte', 'contacto@agencianorte.com');
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (2, 'Prensa Global', 'info@prensaglobal.com');

-- Reporteros
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (1, 'Adrian Reportero', 1);
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (2, 'Diego Escritor', 1);
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (3, 'Ivan Cronista', 2);
INSERT INTO Reportero (id, nombre, id_agencia) VALUES (4, 'Irene Periodista', 2);

-- Eventos
-- Agencia 1
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (10, 'Final Copa Local', '2026-03-01', 1);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (11, 'Inauguracion Museo', '2026-03-05', 1);
-- Agencia 2
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (20, 'Concierto Rock', '2026-03-01', 2);
INSERT INTO Evento (id, nombre, fecha, id_agencia) VALUES (21, 'Feria de Abril', '2026-04-15', 2);

-- Asignaciones (HU #33537)
-- Adrian (1) asignado a Copa Local (10)
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (10, 1);
-- Ivan (3) asignado a Concierto Rock (20)
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (20, 3);

-- EmpresaComunicacion
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (1, 'TeleCable');
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (2, 'El Diario');
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (3, 'Radio Voz');

-- Ofrecimientos (HU #33539, #33540)
-- Agencia 1 ofrece Copa Local (10) a TeleCable (1)
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (1, 10, 1, 'PENDIENTE', FALSE);
-- Agencia 2 ofrece Concierto Rock (20) a El Diario (2)
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (2, 20, 2, 'ACEPTADO', FALSE);

-- Reportajes (HU #33538)
-- Reportaje de la Copa Local por Adrian
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo) VALUES (1, 10, 1, 'Gran victoria en la Final');

-- Versiones (HU #33545)
INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) 
VALUES (1, 1, 'Resultado inesperado', 'El equipo local gano tras un partido muy intenso...', '2026-03-01 22:00:00', 'Version inicial', 1);
