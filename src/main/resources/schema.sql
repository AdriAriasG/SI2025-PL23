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
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE Reportero (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    id_agencia INT NOT NULL,
    FOREIGN KEY (id_agencia) REFERENCES AgenciaPrensa(id)
);

CREATE TABLE Evento (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    id_agencia INT NOT NULL,
    FOREIGN KEY (id_agencia) REFERENCES AgenciaPrensa(id)
);

CREATE TABLE Asignacion (
    id_evento INT NOT NULL,
    id_reportero INT NOT NULL,
    PRIMARY KEY (id_evento, id_reportero),
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_reportero) REFERENCES Reportero(id)
);

CREATE TABLE Reportaje (
    id INT PRIMARY KEY NOT NULL,
    id_evento INT UNIQUE NOT NULL,
    id_reportero_autor INT NOT NULL,
    titulo VARCHAR(255) UNIQUE NOT NULL,
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_reportero_autor) REFERENCES Reportero(id)
);

CREATE TABLE VersionReportaje (
    id INT PRIMARY KEY NOT NULL,
    id_reportaje INT NOT NULL,
    subtitulo VARCHAR(255),
    cuerpo TEXT,
    fecha_hora TIMESTAMP NOT NULL,
    cambios_realizados VARCHAR(255),
    id_reportero_modificador INT NOT NULL,
    FOREIGN KEY (id_reportaje) REFERENCES Reportaje(id),
    FOREIGN KEY (id_reportero_modificador) REFERENCES Reportero(id)
);

CREATE TABLE EmpresaComunicacion (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(255) NOT NULL
);

CREATE TABLE Ofrecimiento (
    id INT PRIMARY KEY NOT NULL,
    id_evento INT NOT NULL,
    id_empresa INT NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('PENDIENTE', 'ACEPTADO', 'RECHAZADO')),
    acceso_concedido BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_evento) REFERENCES Evento(id),
    FOREIGN KEY (id_empresa) REFERENCES EmpresaComunicacion(id)
);
