-- Initial data for testing the Report Management System

-- Pais
INSERT INTO Pais (id, nombre, precio_manutencion) VALUES (1, 'España', 30.0);
INSERT INTO Pais (id, nombre, precio_manutencion) VALUES (2, 'Francia', 40.0);
INSERT INTO Pais (id, nombre, precio_manutencion) VALUES (3, 'Alemania', 50.0);
INSERT INTO Pais (id, nombre, precio_manutencion) VALUES (4, 'Holanda', 60.0);
INSERT INTO Pais (id, nombre, precio_manutencion) VALUES (5, 'Suiza', 70.0);

-- Provincia
INSERT INTO Provincia (id, nombre, precio_alojamiento, id_pais) VALUES (1, 'Asturias', 60.0, 1);
INSERT INTO Provincia (id, nombre, precio_alojamiento, id_pais) VALUES (2, 'Madrid', 80.0, 1);
INSERT INTO Provincia (id, nombre, precio_alojamiento, id_pais) VALUES (3, 'Paris', 120.0, 2);
INSERT INTO Provincia (id, nombre, precio_alojamiento, id_pais) VALUES (4, 'Berlin', 110.0, 3);
INSERT INTO Provincia (id, nombre, precio_alojamiento, id_pais) VALUES (5, 'Amsterdam', 100.0, 4);
INSERT INTO Provincia (id, nombre, precio_alojamiento, id_pais) VALUES (6, 'Zurich', 140.0, 5);

-- AgenciaPrensa
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (1, 'Agencia Norte', 'contacto@agencianorte.com');
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (2, 'Prensa Global', 'info@prensaglobal.com');
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (3, 'Medios Centro', 'info@medioscentro.com');
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (16, 'Agencia Estrella', 'contacto@estrella.com');
INSERT INTO AgenciaPrensa(id, nombre, email) VALUES (39, 'Voz Global', 'voz@agenciavoz.com');
INSERT INTO AgenciaPrensa(id, nombre, email) VALUES (40, 'Agencia Gallega', 'voz@agenciagalleg.com');
INSERT INTO AgenciaPrensa(id, nombre, email) VALUES (41, 'Punto Prensa', 'puntoprensa.agencia@gmail.com');
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (42, 'Agencia Catalana', 'contacto@agenciacatalana.com');
INSERT INTO AgenciaPrensa (id, nombre, email) VALUES (50, 'Agencia Sur', 'contacto@agenciasur.com');

-- Reporteros (nombres genéricos)
-- Agencia 1
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (1, 'Carlos Martinez', 1, 1);
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (2, 'Laura Garcia', 1, 1);
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (3, 'Miguel Fernandez', 1, 1);
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (4, 'Ana Rodriguez', 1, 1);
-- Agencia 2
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (5, 'Pedro Sanchez', 3, 2);
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (6, 'Elena Lopez', 2, 2);
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (7, 'Javier Torres', 2, 2);
-- Agencia 3
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (8, 'Sofia Ruiz', 3, 3);
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (9, 'Diego Moreno', 3, 3);
-- Agencia 16
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (16, 'Javier Rodríguez', 16, 4);
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (17, 'Francisco Pérez', 16, 4);

-- Agencia 39
INSERT INTO Reportero(id, nombre, id_agencia, id_provincia) VALUES(90, 'Reportero Luis', 39, 4);
INSERT INTO Reportero(id, nombre, id_agencia, id_provincia) VALUES(91, 'Reportera Ana', 39, 4);
-- Agencia 40
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (200, 'Luis Castro', 40, 5);
INSERT INTO Reportero (id, nombre, id_agencia, id_provincia) VALUES (201, 'Josefa Varela', 40, 5);
-- Agencia 42
INSERT INTO Reportero (id, nombre, tipo, id_agencia, id_provincia) VALUES (420, 'Lucia Prueba', 'GRAFICO', 42, 6);
INSERT INTO Reportero (id, nombre, tipo, id_agencia, id_provincia) VALUES (421, 'Mario Prueba', 'CAMAROGRAFO', 42, 6);
-- Agencia 50
INSERT INTO Reportero (id, nombre, tipo, id_agencia, id_provincia) VALUES (510, 'Pablo', 'BASE', 50, 2);
INSERT INTO Reportero (id, nombre, tipo, id_agencia, id_provincia) VALUES (511, 'Lucia', 'GRAFICO', 50, 2);

-- Eventos
-- Agencia 1: Eventos variados con diferentes estados
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) 
	VALUES (10, 'Final Copa Local', '2026-03-01', '2026-03-01', '2026-05-01', 1, FALSE, 1);
INSERT INTO Evento VALUES (11, 'Inauguracion Museo Arte', '2026-03-01', '2026-03-01', '2026-05-01', 1, FALSE, 2);
INSERT INTO Evento VALUES (12, 'Conferencia Tecnologica', '2026-03-01', '2026-03-01', '2026-05-01', 1, FALSE, 3);
INSERT INTO Evento VALUES (13, 'Maraton Ciudad', '2026-03-15', '2026-03-15', '2026-05-15', 1, FALSE, 4);
INSERT INTO Evento VALUES (14, 'Festival de Cine', '2026-03-20', '2026-03-20', '2026-05-20', 1, FALSE, 5);
INSERT INTO Evento VALUES (15, 'Concierto Sinfonico', '2026-03-01', '2026-03-01', '2026-05-01', 1, FALSE, 6);

-- Agencia 2
INSERT INTO Evento VALUES (20, 'Concierto Rock Festival', '2026-03-01', '2026-03-01', '2026-05-01', 2, FALSE, 2);
INSERT INTO Evento VALUES (21, 'Feria de Abril', '2026-04-15', '2026-04-15', '2026-06-15', 2, FALSE, 2);
INSERT INTO Evento VALUES (22, 'Cumbre Empresarial', '2026-03-25', '2026-03-25', '2026-05-25', 2, FALSE, 3);

-- Agencia 3
INSERT INTO Evento VALUES (30, 'Exposicion Internacional', '2026-03-12', '2026-03-12', '2026-05-12', 3, FALSE, 4);
INSERT INTO Evento VALUES (31, 'Campeonato Natacion', '2026-03-18', '2026-03-18', '2026-05-18', 3, FALSE, 5);

-- Agencia 16
INSERT INTO Evento VALUES (76, 'Concierto Jazz', '2026-04-10', '2026-04-10', '2026-06-10', 16, FALSE, 6);
INSERT INTO Evento VALUES (77, 'Concierto Blues', '2026-04-12', '2026-04-12', '2026-06-12', 16, FALSE, 1);

-- Agencia 39
INSERT INTO Evento VALUES (92, 'Punto de Partida', '2026-05-08', '2026-05-08', '2026-07-08', 39, FALSE, 2);
INSERT INTO Evento VALUES (93, 'Impacto Cero', '2026-05-12', '2026-05-12', '2026-07-12', 39, FALSE, 3);
INSERT INTO Evento VALUES (94, 'Impacto Rápido', '2026-05-20', '2026-05-20', '2026-07-20', 39, FALSE, 4);

-- Agencia 40
INSERT INTO Evento VALUES (300, 'Fiesta del Mar', '2026-06-10', '2026-06-10', '2026-08-10', 40, FALSE, 5);
INSERT INTO Evento VALUES (301, 'Congreso Pesquero', '2026-06-15', '2026-06-15', '2026-08-15', 40, FALSE, 6);

-- Agencia 41
INSERT INTO Evento VALUES (410, 'Foro Digital', '2026-07-01', '2026-07-01', '2026-09-01', 41, FALSE, 1);
INSERT INTO Evento VALUES (411, 'Cumbre Innovación', '2026-07-10', '2026-07-10', '2026-09-10', 41, FALSE, 2);

-- Agencia 42
INSERT INTO Evento VALUES (420, 'Evento F', '2026-08-01', '2026-08-01', '2026-10-01', 42, TRUE, 3);
INSERT INTO Evento VALUES (421, 'EventoNF', '2026-08-02', '2026-08-02', '2026-10-02', 42, FALSE, 4);
INSERT INTO Evento VALUES (422, 'Evento Embargo Vigente', '2026-08-03', '2026-08-03', '2026-10-03', 42, TRUE, 5);
INSERT INTO Evento VALUES (423, 'Evento Embargo Caducado', '2026-08-04', '2026-08-04', '2026-10-04', 42, TRUE, 6);
INSERT INTO Evento VALUES (424, 'Evento Sin Embargo', '2026-08-05', '2026-08-05', '2026-10-05', 42, TRUE, 1);

-- Agencia 50
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) VALUES (700, 'Evento Embargo Vigente', '2026-10-01', '2026-10-01', '2026-12-01', 50, TRUE, 2);
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) VALUES (701, 'Evento Sin Embargo', '2026-10-02', '2026-10-02', '2026-12-02', 50, TRUE, 2);
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) VALUES (702, 'Evento Embargo Caducado', '2026-10-03', '2026-10-03', '2026-12-03', 50, TRUE, 2);

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
-- Evento 14 (Festival de Cine) - tiene 3 reporteros asignados (incluye BASE para probar finalización HU #34426)
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (14, 1);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (14, 2);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (14, 3);
-- Evento 15 (Concierto Sinfonico) - SIN asignar (misma fecha que evento 22 para probar disponibilidad)

-- Evento 420
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (420, 420, TRUE);
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (420, 421, FALSE);
-- Evento 421
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (421, 420, TRUE);
-- Evento 422
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (422, 420, TRUE);
-- Evento 423
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (423, 420, TRUE);
-- Evento 424
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (424, 420, TRUE);
-- Evento 700
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (700, 510, TRUE);
-- Evento 701
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (701, 510, TRUE);
-- Evento 702
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (702, 511, TRUE);

-- Agencia 2
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (20, 5);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (20, 6);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (22, 7);

-- Agencia 3
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (30, 8);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (31, 9);

-- Agencia 16
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (76,1);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (77,1);

-- Agencia 39
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (92, 90);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (93, 91);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (94, 90);

--Agencia 40
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (300, 200);
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (301, 201);

-- EmpresaComunicacion
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (1, 'TeleCable', 'contacto@telecable.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (2, 'El Diario', 'redaccion@eldiario.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (3, 'Radio Voz', 'info@radiovoz.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (4, 'Canal Noticias', 'noticias@canalnoticias.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (5, 'Prensa Digital', 'editorial@prensadigital.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (6, 'Emisora Central', 'contacto@emisoracentral.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (110, 'VozTV', 'contacto@voztv.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (111, 'Voz Diario', 'redaccion@vozdiario.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (112, 'Voz Radio', 'info@vozradio.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (113, 'Voz Digital', 'editor@vozdigital.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (114, 'Voz Streaming', 'streaming@vozstreaming.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (115, 'Voz Internacional', 'internacional@vozmedia.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (116, 'Voz Deportes', 'deportes@vozmedia.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (117, 'Voz Cultura', 'cultura@vozmedia.com');
INSERT INTO EmpresaComunicacion (id, nombre, email) VALUES (118, 'Galicia Noticias', 'redaccion@galicianoticias.com');
INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago) VALUES (119, 'Diario Metropolitano', 'redaccion@diariometropolitano.com', FALSE, FALSE);
INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago) VALUES (120, 'Canal Actual 24', 'contacto@canalactual24.com', TRUE, TRUE);
INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago) VALUES (121, 'Tecnologia al Dia', 'info@tecnologiaaldia.com', TRUE, FALSE);
INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago) VALUES (122, 'Radio Mediterranea', 'programacion@radiomediterranea.com', FALSE, FALSE);
INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago) VALUES (123, 'Noticias Urbanas', 'redaccion@noticiasurbanas.com', TRUE, TRUE);
INSERT INTO EmpresaComunicacion (id, nombre, email, tiene_tarifa_plana, al_corriente_pago) VALUES (124, 'Prensa del Litoral', 'editorial@prensadellitoral.com', FALSE, FALSE);
UPDATE EmpresaComunicacion SET acepta_embargo = TRUE  WHERE id IN (120, 123);
UPDATE EmpresaComunicacion SET acepta_embargo = FALSE WHERE id IN (119, 121, 122, 124);

-- Ofrecimientos (HU #33539, #33540)
-- Evento 10 (Final Copa Local) - varios ofrecimientos
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (1, 10, 1, 'PENDIENTE', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (2, 10, 2, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (3, 10, 3, 'RECHAZADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (4, 10, 4, 'ACEPTADO', TRUE);

-- Evento 11 (Inauguracion Museo)
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio) VALUES (5, 11, 1, 'PENDIENTE', FALSE, 50.0);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio) VALUES (6, 11, 5, 'ACEPTADO', FALSE, 400.0);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio) VALUES (1100, 11, 4, 'ACEPTADO', TRUE, 120.0);

-- Evento 14 (Festival de Cine)
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (7, 14, 2, 'PENDIENTE', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (8, 14, 6, 'PENDIENTE', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio) VALUES (1400, 14, 4, 'ACEPTADO', TRUE, 300.0);

-- Evento 20 (Concierto Rock Festival)
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (9, 20, 2, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (10, 20, 3, 'PENDIENTE', FALSE);

-- Evento 92
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (200, 92, 110, 'PENDIENTE', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (201, 92, 112, 'ACEPTADO', FALSE);

-- Evento 300
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (400, 300, 1, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (405, 300, 4, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (406, 300, 5, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (407, 300, 6, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (401, 300, 2, 'ACEPTADO', TRUE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (402, 300, 3, 'PENDIENTE', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, descargado, precio) VALUES (4100, 300, 118, 'ACEPTADO', TRUE, TRUE, 350.0);

-- Evento 301
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (403, 301, 4, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (404, 301, 5, 'RECHAZADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (408, 301, 1, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (409, 301, 2, 'ACEPTADO', FALSE);

-- Evento 410
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (500, 410, 1, 'PENDIENTE', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (501, 410, 2, 'ACEPTADO', FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (600, 410, 3, 'ACEPTADO', TRUE);

-- Evento 411
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido) VALUES (601, 411, 4, 'PENDIENTE', FALSE);

-- Evento 420
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) VALUES (700, 420, 123, 'PENDIENTE', FALSE, 0.0, FALSE, FALSE, FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado) VALUES (701, 420, 124, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE);

--Evento 119
INSERT INTO Ofrecimiento (id_evento, id_empresa, estado, acceso_concedido, pagado) VALUES (13, 119, 'ACEPTADO', FALSE, TRUE);
--Evento 120
INSERT INTO Ofrecimiento (id_evento, id_empresa, estado, acceso_concedido, pagado) VALUES (13, 120, 'ACEPTADO', FALSE, FALSE);
--Evento 121
INSERT INTO Ofrecimiento (id_evento, id_empresa, estado, acceso_concedido, pagado) VALUES (13, 121, 'ACEPTADO', FALSE, FALSE);

-- Reportajes (HU #33538)
-- Evento 10 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (1, 10, 1, 'Victoria historica en la Final Copa Local','2030-12-31');
-- Evento 11 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo) VALUES (2, 11, 3, 'Nuevo museo abre sus puertas');
-- Evento 14 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo) VALUES (3, 14, 1, 'Estrellas del cine en el Festival');
-- Evento 20 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo) VALUES (4, 20, 5, 'Noche inolvidable de rock');

-- Reportajes (HU #33541)
-- Evento 300 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo)
VALUES (50, 300, 200, 'Gran éxito en la Fiesta del Mar');
-- Evento 301 tiene reportaje
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo)
VALUES (51, 301, 201, 'Nuevas medidas en el Congreso Pesquero');

INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, estado, fecha_fin_embargo) VALUES (7000, 700, 510, 'Reportaje Sur con embargo vigente', 'TERMINADO', '2030-12-31');
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, estado, fecha_fin_embargo) VALUES (7001, 701, 510, 'Reportaje Sur sin embargo', 'TERMINADO', NULL);
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, estado, fecha_fin_embargo) VALUES (7002, 702, 511, 'Reportaje Sur con embargo caducado', 'TERMINADO', '2020-01-01');

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

-- Reportajes con fecha de embargo
-- Evento 422: Embargo vigente
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (60, 422, 420, 'Reportaje con embargo vigente', '2026-12-31');

-- Evento 423: Embargo caducado
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (61, 423, 420, 'Reportaje con embargo caducado', '2026-01-01');

-- Evento 424: Sin embargo
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (62, 424, 420, 'Reportaje sin embargo', NULL);

-- ---------------------------------------------------------
-- NUEVOS DATOS SPRINT 2
-- ---------------------------------------------------------

-- Tematicas (HU #34060, #34063, #34064, #34070)
INSERT INTO Tematica (id, nombre) VALUES (1, 'Deportes');
INSERT INTO Tematica (id, nombre) VALUES (2, 'Cultura');
INSERT INTO Tematica (id, nombre) VALUES (3, 'Tecnologia');
INSERT INTO Tematica (id, nombre) VALUES (4, 'Sociedad');
INSERT INTO Tematica (id, nombre) VALUES (5, 'Economia');

-- EventoTematica (Asignar temáticas a eventos)
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (10, 1);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (11, 2);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (12, 3);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (13, 1);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (14, 1);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (14, 2);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (20, 2);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (22, 5);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (300, 4);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (301, 5);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (410, 3); 
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (411, 5); 	
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (422, 3);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (423, 3);
INSERT INTO EventoTematica (id_evento, id_tematica) VALUES (424, 3);

-- ReporteroTematica (Asignar temáticas a reporteros)
INSERT INTO ReporteroTematica (id_reportero, id_tematica) VALUES (1, 1);
INSERT INTO ReporteroTematica (id_reportero, id_tematica) VALUES (2, 1);
INSERT INTO ReporteroTematica (id_reportero, id_tematica) VALUES (3, 2);
INSERT INTO ReporteroTematica (id_reportero, id_tematica) VALUES (4, 1);
INSERT INTO ReporteroTematica (id_reportero, id_tematica) VALUES (5, 2);
INSERT INTO ReporteroTematica (id_reportero, id_tematica) VALUES (7, 5);

-- EmpresaTematica (Asignar temáticas a empresas)
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (116, 1);
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (117, 2);
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (1, 1);
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (2, 2);
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (5, 3);
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (3, 3);   
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (4, 5);   
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (6, 5);  
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (119, 3);
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (120, 3);
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (121, 3);
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (123, 3); 
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (122, 5);
INSERT INTO EmpresaTematica (id_empresa, id_tematica) VALUES (124, 5);

-- Actualizar reporteros con tipos y Reporteros Freelance (HU #34068, #34070)
UPDATE Reportero SET tipo = 'GRAFICO' WHERE id = 1;
UPDATE Reportero SET tipo = 'CAMAROGRAFO' WHERE id = 2;
UPDATE Reportero SET tipo = 'BASE' WHERE id = 3;
UPDATE Reportero SET tipo = 'GRAFICO' WHERE id = 4;
UPDATE Reportero SET tipo = 'GRAFICO' WHERE id = 5;
UPDATE Reportero SET tipo = 'CAMAROGRAFO' WHERE id = 6;
UPDATE Reportero SET tipo = 'BASE' WHERE id = 7;
UPDATE Reportero SET tipo = 'GRAFICO' WHERE id = 8;
UPDATE Reportero SET tipo = 'BASE' WHERE id = 9;
UPDATE Reportero SET tipo = 'CAMAROGRAFO' WHERE id = 16;
UPDATE Reportero SET tipo = 'BASE' WHERE id = 17;
UPDATE Reportero SET tipo = 'GRAFICO' WHERE id = 90;
UPDATE Reportero SET tipo = 'CAMAROGRAFO' WHERE id = 91;
UPDATE Reportero SET tipo = 'BASE' WHERE id = 200;
UPDATE Reportero SET tipo = 'GRAFICO' WHERE id = 201;

-- Insertar reporteros Freelance (id_agencia es NULL)
INSERT INTO Reportero (id, nombre, tipo, id_agencia, id_provincia, email)
VALUES (500, 'Freelance Juan', 'BASE', NULL, 1, 'juan.freelance@correo.com');
INSERT INTO Reportero (id, nombre, tipo, id_agencia, id_provincia, email)
VALUES (501, 'Freelance Maria', 'GRAFICO', NULL, 2, 'maria.freelance@correo.com');
INSERT INTO Reportero (id, nombre, tipo, id_agencia, id_provincia, email)
VALUES (502, 'Freelance Roberto', 'CAMAROGRAFO', NULL, 3, 'roberto.freelance@correo.com');
INSERT INTO ReporteroTematica (id_reportero, id_tematica) VALUES (500, 1);
INSERT INTO ReporteroTematica (id_reportero, id_tematica) VALUES (501, 2);
INSERT INTO ReporteroTematica (id_reportero, id_tematica) VALUES (502, 1);

-- DecisionFreelance (HU #34070 y #34437)
-- Evento 10 (Final Copa Local, Deportes): Juan INTERESADO, Roberto INTERESADO, Maria INTERESADO
-- Maria (Cultura) está interesada pero su temática no coincide con Deportes → útil para TEST 3
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (10, 500, 'INTERESADO');
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (10, 502, 'INTERESADO');
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (10, 501, 'INTERESADO');
-- Evento 11 (Inauguracion Museo, Cultura): Maria DUDOSO
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (11, 501, 'DUDOSO');
-- Evento 13 (Maraton Ciudad, Deportes): Juan NO_INTERESADO, Roberto DUDOSO
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (13, 500, 'NO_INTERESADO');
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (13, 502, 'DUDOSO');
-- Evento 14 (Festival de Cine, Deportes+Cultura): Maria INTERESADO, Juan INTERESADO
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (14, 501, 'INTERESADO');
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (14, 500, 'INTERESADO');

-- Multimedia (HU #34061, #34062)
-- Reportaje 1 (Final Copa Local), autor 1
INSERT INTO Multimedia (id_reportaje, id_autor, ruta, tipo, estado) VALUES (1, 1, '/media/copa_foto1.jpg', 'IMAGEN', 'DEFINITIVO');
INSERT INTO Multimedia (id_reportaje, id_autor, ruta, tipo, estado) VALUES (1, 1, '/media/copa_video1.mp4', 'VIDEO', 'BORRADOR');
-- Reportaje 2, autor 3
INSERT INTO Multimedia (id_reportaje, id_autor, ruta, tipo, estado) VALUES (2, 3, '/media/museo.jpg', 'IMAGEN', 'DEFINITIVO');

-- RevisionReportaje y Cambio de Estado (HU #34065, #34066)
-- Creamos un reportaje nuevo en revisión
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, estado) VALUES (5, 13, 4, 'Maraton en la ciudad', 'EN_REVISION');
INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) VALUES (8, 5, 'Gran participacion', 'Miles de corredores', '2026-03-15 14:00:00', 'Version inicial', 4);
-- Asignamos otro reportero al evento 13 para que lo revise
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (13, 1);
INSERT INTO RevisionReportaje (id_reportaje, id_reportero, comentario, estado) VALUES (5, 1, 'Falta incluir resultados', 'PENDIENTE');

-- Embargo en Reportajes (HU #34062)
-- Escenario 1 → Reportaje 1 (evento 10, empresa 4): sin embargo → acceso completo
-- Escenario 2 → Reportaje 2 (evento 11, empresa 4): embargo vigente, sin acceso especial → solo mensaje
UPDATE Reportaje SET fecha_fin_embargo = '2030-01-01' WHERE id = 2;
-- Escenario 3 → Reportaje 3 (evento 14, empresa 4): embargo vigente, con acceso especial → texto sin multimedia
UPDATE Reportaje SET fecha_fin_embargo = '2030-01-01' WHERE id = 3;
UPDATE Ofrecimiento SET acceso_especial = TRUE WHERE id = 1400;

-- Precios y estado de descarga en Ofrecimientos (HU #34067, #34072, #34073)
UPDATE Ofrecimiento SET precio = 150.0, descargado = TRUE WHERE id = 2;
UPDATE Ofrecimiento SET precio = 200.0, descargado = FALSE WHERE id = 4;
UPDATE Ofrecimiento SET precio = 100.0 WHERE id = 1;
UPDATE Ofrecimiento SET precio = 300.0 WHERE id = 400;
UPDATE Ofrecimiento SET precio = 50.0 WHERE id = 5;
UPDATE Ofrecimiento SET precio = 400.0 WHERE id = 6;
UPDATE Ofrecimiento SET precio = 250.0 WHERE id = 9;

-- Nuevos Eventos para pruebas de embargo
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) 
VALUES (500, 'Descubrimiento Arqueológico', '2026-04-10', '2026-04-10', '2026-06-10', 1, TRUE, 1);
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) 
VALUES (501, 'Filtración Tecnológica', '2026-04-05', '2026-04-05', '2026-06-05', 2, TRUE, 2);
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) 
VALUES (502, 'Sentencia Judicial', '2026-04-14', '2026-04-14', '2026-06-14', 3, TRUE, 3);
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) 
VALUES (503, 'Adelanto Editorial', '2026-04-12', '2026-04-12', '2026-06-12', 1, TRUE, 4);

-- Asignaciones correspondientes
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (500, 1, TRUE);
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (501, 5, TRUE);
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (502, 8, TRUE);
INSERT INTO Asignacion (id_evento, id_reportero, es_responsable) VALUES (503, 2, TRUE);

-- Reportajes con distintos estados de embargo
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (70, 500, 1, 'Tesoros Ocultos en el Norte', '2027-01-01');
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (71, 501, 5, 'El nuevo chip cuántico', '2026-04-10');
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (72, 502, 8, 'Veredicto Final Caso X', '2026-04-14');
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (73, 503, 2, 'Biografía no autorizada', '2026-04-20');

-- Ofrecimientos para probar lógica de acceso
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, acceso_especial) VALUES (5000, 500, 1, 'ACEPTADO', TRUE, TRUE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, acceso_especial) VALUES (5001, 503, 120, 'ACEPTADO', TRUE, FALSE);

-- Evento 700: embargo vigente
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado)
VALUES (70000, 700, 120, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE);

INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado)
VALUES (70001, 700, 123, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE);

INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado)
VALUES (70002, 700, 121, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE);

-- Evento 701: sin embargo
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado)
VALUES (70010, 701, 120, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE);

INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado)
VALUES (70011, 701, 119, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, TRUE);

INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado)
VALUES (70012, 701, 124, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE);

-- Evento 702: embargo caducado
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, precio, descargado, acceso_especial, pagado)
VALUES (70020, 702, 120, 'ACEPTADO', FALSE, 0.0, FALSE, FALSE, FALSE);

-- ---------------------------------------------------------
-- DATOS SPRINT 3 — HU #34430: Eventos multi-día
-- Agencia 1 (id=1) — reporteros: Carlos(1,GRAFICO), Laura(2,CAMAROGRAFO), Miguel(3,BASE), Ana(4,GRAFICO)
-- Tres eventos con rangos diseñados para probar solapamiento:
--   Ev.600: 2026-09-01 → 2026-09-05  (Carlos asignado)
--   Ev.601: 2026-09-04 → 2026-09-08  (solapa con 600 en 04-05/09)
--   Ev.602: 2026-09-06 → 2026-09-10  (NO solapa con 600; sí solapa con 601 en 06-08/09)
-- ---------------------------------------------------------
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia)
VALUES (600, 'Congreso Multi-Dia A', '2026-09-01', '2026-09-01', '2026-09-05', 1, FALSE, 1);
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia)
VALUES (601, 'Congreso Multi-Dia B', '2026-09-04', '2026-09-04', '2026-09-08', 1, FALSE, 2);
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia)
VALUES (602, 'Congreso Multi-Dia C', '2026-09-06', '2026-09-06', '2026-09-10', 1, FALSE, 3);

-- Carlos (id=1) asignado al evento 600
INSERT INTO Asignacion (id_evento, id_reportero) VALUES (600, 1);

-- DecisionFreelance para Ev.601 (útil para TEST 10 de HU #34437)
-- Laura, Miguel, Ana libres en sept; Carlos bloqueado por Ev.600 (solapa 04-05/sep)
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (601, 500, 'INTERESADO');
INSERT INTO DecisionFreelance (id_evento, id_reportero, decision) VALUES (601, 502, 'INTERESADO');

--Añadimos datos para probar los accesos especiales
INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) 
VALUES (900, 'La moda de la reventa de ropa', '2026-05-01', '2026-05-01', '2026-05-01', 1, TRUE, 1);

INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) 
VALUES (901, 'El festival de las mariposas', '2026-05-02', '2026-05-02', '2026-05-02', 1, TRUE, 1);

INSERT INTO Evento (id, nombre, fecha, fecha_inicio, fecha_fin, id_agencia, asignacion_finalizada, id_provincia) 
VALUES (902, 'La Revolución de los Taxis Voladores en Madrid', '2026-04-10', '2026-04-10', '2026-04-10', 1, TRUE, 1);

INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (900, 900, 1, 'La reventa de ropa', '2030-12-31');
INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (901, 901, 1, 'El festival de las mariposas', '2030-12-31');
INSERT INTO Multimedia (id_reportaje, id_autor, ruta, tipo, estado) VALUES (901, 1, '/media/archivo_protegido.mp4', 'VIDEO', 'DEFINITIVO');
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, acceso_especial) VALUES (9000, 900, 120, 'ACEPTADO', TRUE, FALSE);
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, acceso_especial) VALUES (9001, 901, 120, 'ACEPTADO', TRUE, TRUE);
INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) VALUES (900, 900, 'La nueva forma de los jovenes para ganar dinero y espacio', 'Buscan una forma fácil de ganar dinero y espacio en sus armarios.', '2026-04-16 10:00:00', 'Versión inicial', 1);
INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) VALUES (901, 901, 'El festival triunfa por todo lo alto', 'El pasado viernes se llevo a cabo el festival de las mariposas.', '2026-04-16 10:05:00', 'Versión inicial', 1);

INSERT INTO Reportaje (id, id_evento, id_reportero_autor, titulo, fecha_fin_embargo) VALUES (902, 902, 1, 'La Revolución de los Taxis Voladores en Madrid', '2026-01-01');
INSERT INTO VersionReportaje (id, id_reportaje, subtitulo, cuerpo, fecha_hora, cambios_realizados, id_reportero_modificador) VALUES (902, 902, 'Las primeras pruebas con drones de pasajeros comenzarán el próximo mes en el entorno de IFEMA', 'El cielo de la capital española está a punto de cambiar para siempre. Tras tres años de negociaciones con la Agencia Estatal de Seguridad Aérea, la empresa tecnológica "SkyGlide" ha obtenido finalmente los permisos necesarios para realizar vuelos experimentales con naves biplaza no tripuladas.', '2026-04-16 12:00:00', 'Versión final', 1);
INSERT INTO Multimedia (id_reportaje, id_autor, ruta, tipo, estado) VALUES (902, 1, '/media/foto_libre.jpg', 'IMAGEN', 'DEFINITIVO');
INSERT INTO Ofrecimiento (id, id_evento, id_empresa, estado, acceso_concedido, acceso_especial, descargado) VALUES (9002, 902, 120, 'ACEPTADO', TRUE, FALSE, FALSE);

-- Ajustes para que los reportajes de distribución estén en TERMINADO
UPDATE Reportaje
SET estado = 'TERMINADO'
WHERE id IN (1,2,3,4,50,51,60,61,62,70,71,72,73,900,901,902,7000,7001,7002);