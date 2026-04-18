-- DROP TABLES in reverse order of dependencies
DROP TABLE IF EXISTS DecisionFreelance;
DROP TABLE IF EXISTS RevisionReportaje;
DROP TABLE IF EXISTS Multimedia;
DROP TABLE IF EXISTS EmpresaTematica;
DROP TABLE IF EXISTS ReporteroTematica;
DROP TABLE IF EXISTS EventoTematica;
DROP TABLE IF EXISTS Tematica;
DROP TABLE IF EXISTS Ofrecimiento;
DROP TABLE IF EXISTS EmpresaComunicacion;
DROP TABLE IF EXISTS VersionReportaje;
DROP TABLE IF EXISTS Reportaje;
DROP TABLE IF EXISTS Asignacion;
DROP TABLE IF EXISTS Evento;
DROP TABLE IF EXISTS Reportero;
DROP TABLE IF EXISTS AgenciaPrensa;
DROP TABLE IF EXISTS EventoDieta;
DROP TABLE IF EXISTS Pais;
DROP TABLE IF EXISTS Provincia;


-- TABLAS SPRINT 3 
CREATE TABLE Pais (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(20) NOT NULL UNIQUE,
    precio_manutencion REAL NOT NULL
);

CREATE TABLE Provincia (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(20) NOT NULL,
    precio_alojamiento REAL NOT NULL,
    id_pais INTEGER NOT NULL,
    FOREIGN KEY (id_pais) REFERENCES Pais(id)
);

-- CREATE TABLES
CREATE TABLE AgenciaPrensa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE Reportero (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) CHECK (tipo IN ('GRAFICO', 'CAMAROGRAFO', 'BASE')) DEFAULT 'BASE',
    id_agencia INTEGER,
    id_provincia INTEGER NOT NULL,
    email VARCHAR(255),
    FOREIGN KEY (id_agencia) REFERENCES AgenciaPrensa(id),
    FOREIGN KEY (id_provincia) REFERENCES Provincia(id)
);

CREATE TABLE Evento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    fecha_inicio DATE NOT NULL, 
    fecha_fin DATE NOT NULL,
    id_agencia INTEGER NOT NULL,
    asignacion_finalizada BOOLEAN DEFAULT FALSE,
    id_provincia INTEGER NOT NULL,
    FOREIGN KEY (id_agencia) REFERENCES AgenciaPrensa(id),
    FOREIGN KEY (id_provincia) REFERENCES Provincia(id)
);

CREATE TABLE Asignacion (
    id_evento INTEGER NOT NULL,
    id_reportero INTEGER NOT NULL,
    es_responsable BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id_evento, id_reportero),
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_reportero) REFERENCES Reportero(id)
);

CREATE TABLE Reportaje (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_evento INTEGER UNIQUE NOT NULL,
    id_reportero_autor INTEGER NOT NULL,
    titulo VARCHAR(255) UNIQUE NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('NORMAL', 'EN_REVISION', 'TERMINADO')) DEFAULT 'NORMAL',
    fecha_fin_embargo DATE DEFAULT NULL,
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_reportero_autor) REFERENCES Reportero(id)
);

CREATE TABLE VersionReportaje (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_reportaje INTEGER NOT NULL,
    subtitulo VARCHAR(255),
    cuerpo TEXT,
    fecha_hora TIMESTAMP NOT NULL,
    cambios_realizados VARCHAR(255),
    id_reportero_modificador INTEGER NOT NULL,
    FOREIGN KEY (id_reportaje) REFERENCES Reportaje(id),
    FOREIGN KEY (id_reportero_modificador) REFERENCES Reportero(id)
);

CREATE TABLE EmpresaComunicacion (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    tiene_tarifa_plana BOOLEAN NOT NULL DEFAULT FALSE,
    al_corriente_pago BOOLEAN NOT NULL DEFAULT FALSE,
    acepta_embargo BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE Ofrecimiento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_evento INTEGER NOT NULL,
    id_empresa INTEGER NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('PENDIENTE', 'ACEPTADO', 'RECHAZADO')),
    acceso_concedido BOOLEAN DEFAULT FALSE,
    precio REAL DEFAULT 0.0,
    descargado BOOLEAN DEFAULT FALSE,
    acceso_especial BOOLEAN DEFAULT FALSE,
    pagado BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(id_evento, id_empresa),
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_empresa) REFERENCES EmpresaComunicacion(id)
);

-- NUEVAS TABLAS SPRINT 2

CREATE TABLE Tematica (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE EventoTematica (
    id_evento INTEGER NOT NULL,
    id_tematica INTEGER NOT NULL,
    PRIMARY KEY (id_evento, id_tematica),
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_tematica) REFERENCES Tematica(id)
);

CREATE TABLE ReporteroTematica (
    id_reportero INTEGER NOT NULL,
    id_tematica INTEGER NOT NULL,
    PRIMARY KEY (id_reportero, id_tematica),
    FOREIGN KEY (id_reportero) REFERENCES Reportero(id),
    FOREIGN KEY (id_tematica) REFERENCES Tematica(id)
);

CREATE TABLE EmpresaTematica (
    id_empresa INTEGER NOT NULL,
    id_tematica INTEGER NOT NULL,
    PRIMARY KEY (id_empresa, id_tematica),
    FOREIGN KEY (id_empresa) REFERENCES EmpresaComunicacion(id),
    FOREIGN KEY (id_tematica) REFERENCES Tematica(id)
);

CREATE TABLE Multimedia (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_reportaje INTEGER NOT NULL,
    id_autor INTEGER NOT NULL,
    ruta VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) CHECK (tipo IN ('IMAGEN', 'VIDEO')),
    estado VARCHAR(20) CHECK (estado IN ('BORRADOR', 'DEFINITIVO')) DEFAULT 'BORRADOR',
    UNIQUE(id_reportaje, ruta),
    FOREIGN KEY (id_reportaje) REFERENCES Reportaje(id),
    FOREIGN KEY (id_autor) REFERENCES Reportero(id)
);

CREATE TABLE RevisionReportaje (
    id_reportaje INTEGER NOT NULL,
    id_reportero INTEGER NOT NULL,
    comentario TEXT,
    estado VARCHAR(20) CHECK (estado IN ('PENDIENTE', 'FINALIZADA')) DEFAULT 'PENDIENTE',
    PRIMARY KEY (id_reportaje, id_reportero),
    FOREIGN KEY (id_reportaje) REFERENCES Reportaje(id),
    FOREIGN KEY (id_reportero) REFERENCES Reportero(id)
); 	

CREATE TABLE DecisionFreelance (
    id_evento INTEGER NOT NULL,
    id_reportero INTEGER NOT NULL,
    decision VARCHAR(20) CHECK (decision IN ('INTERESADO', 'NO_INTERESADO', 'DUDOSO')),
    PRIMARY KEY (id_evento, id_reportero),
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_reportero) REFERENCES Reportero(id)
);

