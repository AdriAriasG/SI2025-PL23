-- DROP TABLES in reverse order of dependencies
DROP TABLE IF EXISTS Ofrecimiento;
DROP TABLE IF EXISTS EmpresaComunicacion;
DROP TABLE IF EXISTS VersionReportaje;
DROP TABLE IF EXISTS Reportaje;
DROP TABLE IF EXISTS Asignacion;
DROP TABLE IF EXISTS Evento;
DROP TABLE IF EXISTS Reportero;
DROP TABLE IF EXISTS AgenciaPrensa;

-- CREATE TABLES
CREATE TABLE AgenciaPrensa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE Reportero (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(255) NOT NULL,
    id_agencia INTEGER NOT NULL,
    FOREIGN KEY (id_agencia) REFERENCES AgenciaPrensa(id)
);

CREATE TABLE Evento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    id_agencia INTEGER NOT NULL,
    FOREIGN KEY (id_agencia) REFERENCES AgenciaPrensa(id)
);

CREATE TABLE Asignacion (
    id_evento INTEGER NOT NULL,
    id_reportero INTEGER NOT NULL,
    PRIMARY KEY (id_evento, id_reportero),
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_reportero) REFERENCES Reportero(id)
);

CREATE TABLE Reportaje (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_evento INTEGER UNIQUE NOT NULL,
    id_reportero_autor INTEGER NOT NULL,
    titulo VARCHAR(255) UNIQUE NOT NULL,
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
    nombre VARCHAR(255) NOT NULL
);

CREATE TABLE Ofrecimiento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_evento INTEGER NOT NULL,
    id_empresa INTEGER NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('PENDIENTE', 'ACEPTADO', 'RECHAZADO')),
    acceso_concedido BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_empresa) REFERENCES EmpresaComunicacion(id)
);
