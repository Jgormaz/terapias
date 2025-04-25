CREATE DATABASE IF NOT EXISTS terapias;
USE terapias;

CREATE TABLE Admin ( user_name VARCHAR(10) PRIMARY KEY DEFAULT 'Admin', password VARCHAR(255) NOT NULL );

-- Tabla Region
CREATE TABLE Region (
    ID_region VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla Comuna
CREATE TABLE Comuna (
    ID_comuna VARCHAR(50) PRIMARY KEY,
    ID_region VARCHAR(50) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    FOREIGN KEY (ID_region) REFERENCES Region(ID_region)
);

-- Tabla Categoria
CREATE TABLE Categoria (
    ID_categoria VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla Paciente
CREATE TABLE Paciente (
    ID_paciente VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    ape_paterno VARCHAR(50) NOT NULL,
    ape_materno VARCHAR(50),
    direccion VARCHAR(50) NOT NULL,
    ID_comuna VARCHAR(50) NOT NULL,
    ID_region VARCHAR(50) NOT NULL,
    telefono VARCHAR(50),
    correo VARCHAR(50),
    atenciones INT DEFAULT 0,
    evaluacion INT DEFAULT 0,
    ID_categoria VARCHAR(50),
    FOREIGN KEY (ID_comuna) REFERENCES Comuna(ID_comuna),
    FOREIGN KEY (ID_region) REFERENCES Region(ID_region),
    FOREIGN KEY (ID_categoria) REFERENCES Categoria(ID_categoria)
);

-- Tabla Terapeuta
CREATE TABLE Terapeuta (
    ID_terapeuta VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    ape_paterno VARCHAR(50) NOT NULL,
    ape_materno VARCHAR(50),
    user_name VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    direccion_principal VARCHAR(50) NOT NULL,
    ID_comuna VARCHAR(50) NOT NULL,
    ID_region VARCHAR(50) NOT NULL,
    telefono VARCHAR(50),
    correo VARCHAR(50),
    evaluacion INT DEFAULT 0,
    url_foto VARCHAR(255),
    FOREIGN KEY (ID_comuna) REFERENCES Comuna(ID_comuna),
    FOREIGN KEY (ID_region) REFERENCES Region(ID_region)
);

-- Tabla DireccionConsulta
CREATE TABLE DireccionConsulta (
    ID_direccionConsulta VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    direccion VARCHAR(50) NOT NULL,
    ID_comuna VARCHAR(50) NOT NULL,
    ID_region VARCHAR(50) NOT NULL,
    FOREIGN KEY (ID_comuna) REFERENCES Comuna(ID_comuna),
    FOREIGN KEY (ID_region) REFERENCES Region(ID_region)
);

-- Tabla ConsultaTerapeuta
CREATE TABLE ConsultaTerapeuta (
    ID_consultaTerapeuta INT AUTO_INCREMENT PRIMARY KEY,
    ID_direccionConsulta VARCHAR(50) NOT NULL,
    ID_terapeuta VARCHAR(50) NOT NULL,
    codigo_dias_disponible INT,
    FOREIGN KEY (ID_direccionConsulta) REFERENCES DireccionConsulta(ID_direccionConsulta),
    FOREIGN KEY (ID_terapeuta) REFERENCES Terapeuta(ID_terapeuta)
);


-- Tabla Especialidad
CREATE TABLE Especialidad (
    ID_especialidad VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT
);

-- Tabla Servicio
CREATE TABLE Servicio (
    ID_servicio VARCHAR(50) PRIMARY KEY,
    ID_especialidad VARCHAR(50) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    FOREIGN KEY (ID_especialidad) REFERENCES Especialidad(ID_especialidad)
);


-- Tabla ServicioTerapeuta
CREATE TABLE ServicioTerapeuta (
    ID_servicio_terapeuta INT AUTO_INCREMENT PRIMARY KEY,
    ID_servicio VARCHAR(50) NOT NULL,
    ID_terapeuta VARCHAR(50) NOT NULL,
    FOREIGN KEY (ID_servicio) REFERENCES Servicio(ID_servicio),
    FOREIGN KEY (ID_terapeuta) REFERENCES Terapeuta(ID_terapeuta)
);

-- Tabla MotivoBloqueo
CREATE TABLE MotivoBloqueo (
    ID_motivoBloqueo VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL
);

-- Tabla Bloqueo
CREATE TABLE Bloqueo (
    ID_bloqueo INT AUTO_INCREMENT PRIMARY KEY,
    ID_paciente VARCHAR(50) NOT NULL,
    ID_terapeuta VARCHAR(50) NOT NULL,
    ID_motivoBloqueo VARCHAR(50) NOT NULL,
    FOREIGN KEY (ID_paciente) REFERENCES Paciente(ID_paciente),
    FOREIGN KEY (ID_terapeuta) REFERENCES Terapeuta(ID_terapeuta),
    FOREIGN KEY (ID_motivoBloqueo) REFERENCES MotivoBloqueo(ID_motivoBloqueo)
);

-- Tabla Atencion
CREATE TABLE Atencion (
    ID_atencion VARCHAR(50) PRIMARY KEY,
    ID_terapeuta VARCHAR(50) NOT NULL,
    ID_servicio VARCHAR(50) NOT NULL,
    tamaño_bloque INT,
    duracion_min INT,
    duracion_max INT,
    precio_bloque INT,
    descuento_bloque_consecutivo INT,
    bloques_entre_reservas INT,
    bloqueo_por_distancia TINYINT(1),
    distancia_max_transporte INT,
    distancia_max_dir_princip INT,
    FOREIGN KEY (ID_terapeuta) REFERENCES Terapeuta(ID_terapeuta),
    FOREIGN KEY (ID_servicio) REFERENCES Servicio(ID_servicio)
);

-- Tabla Reserva
CREATE TABLE Reserva (
    ID_reserva VARCHAR(50) PRIMARY KEY,
    ID_paciente VARCHAR(50) NOT NULL,
    ID_atencion VARCHAR(50) NOT NULL, 
    nombre VARCHAR(50) NOT NULL,
    direccion_atencion VARCHAR(50) NOT NULL,
    ID_comuna VARCHAR(50) NOT NULL,
    ID_region VARCHAR(50) NOT NULL,
    hora_ini TIME NOT NULL,
    hora_fin TIME NOT NULL,
    precio INT NOT NULL,
    abono INT DEFAULT 0,
    estado_reserva ENUM('agendada', 'cancelada', 'noshow', 'completada', 'reagendada') NOT NULL DEFAULT 'agendada',
    FOREIGN KEY (ID_paciente) REFERENCES Paciente(ID_paciente),
    FOREIGN KEY (ID_comuna) REFERENCES Comuna(ID_comuna),
    FOREIGN KEY (ID_region) REFERENCES Region(ID_region),
    FOREIGN KEY (ID_atencion) REFERENCES Atencion(ID_atencion)
);


-- Tabla Calendario
CREATE TABLE Calendario (
    ID_calendario VARCHAR(50) PRIMARY KEY,
    ID_atencion VARCHAR(50) NOT NULL,
    ID_terapeuta VARCHAR(50) NOT NULL, -- Nueva clave foránea
    fecha_ini DATE NOT NULL,
    semana_ini VARCHAR(50),
    semana_fin VARCHAR(50),
    semanas_visibles INT,
    FOREIGN KEY (ID_atencion) REFERENCES Atencion(ID_atencion),
    FOREIGN KEY (ID_terapeuta) REFERENCES Terapeuta(ID_terapeuta)
);

-- Tabla Semana
CREATE TABLE Semana (
    ID_semana VARCHAR(50) PRIMARY KEY,
    ID_calendario VARCHAR(50) NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (ID_calendario) REFERENCES Calendario(ID_calendario)
);

-- Tabla Dia
CREATE TABLE Dia (
    ID_dia VARCHAR(50) PRIMARY KEY,
    ID_semana VARCHAR(50) NOT NULL,
    fecha DATE NOT NULL,
    hora_ini INT,
    hora_fin INT,
    tamaño_bloque INT,
    descuento_especial INT,
    FOREIGN KEY (ID_semana) REFERENCES Semana(ID_semana)
);

-- Tabla Bloque
CREATE TABLE Bloque (
    ID_bloque VARCHAR(50) PRIMARY KEY,
    ID_dia VARCHAR(50) NOT NULL,
    ID_reserva VARCHAR(50),
    hora_ini INT,
    hora_fin INT,
    disponible TINYINT(1),
    precio INT,
    FOREIGN KEY (ID_dia) REFERENCES Dia(ID_dia),
    FOREIGN KEY (ID_reserva) REFERENCES Reserva(ID_reserva)
);

INSERT INTO Admin (user_name, password) VALUES ('Admin', 'admin');

INSERT INTO categoria values ('CAT0001','Nuevo')

INSERT INTO Region (ID_region, nombre) VALUES
('REG001', 'Región Metropolitana'),
('REG002', 'Valparaíso'),
('REG003', 'Biobío'),
('REG004', 'Araucanía'),
('REG005', 'Los Lagos');

INSERT INTO Comuna (ID_comuna, ID_region, nombre) VALUES
-- Región Metropolitana
('COM001', 'REG001', 'Santiago'),
('COM002', 'REG001', 'Providencia'),
('COM003', 'REG001', 'Las Condes'),

-- Región de Valparaíso
('COM004', 'REG002', 'Valparaíso'),
('COM005', 'REG002', 'Viña del Mar'),

-- Región del Biobío
('COM006', 'REG003', 'Concepción'),
('COM007', 'REG003', 'Talcahuano'),
('COM008', 'REG003', 'Chiguayante'),

-- Región de la Araucanía
('COM009', 'REG004', 'Temuco'),
('COM010', 'REG004', 'Villarrica'),

-- Región de Los Lagos
('COM011', 'REG005', 'Puerto Montt'),
('COM012', 'REG005', 'Castro'),
('COM013', 'REG005', 'Osorno');



--Limpia calendarios
delete from bloque;
delete from dia;
delete from semana;
delete from calendario;

INSERT INTO Especialidad (ID_especialidad, nombre, descripcion) VALUES
('ESP001', 'Masajes',' '),
('ESP002', 'Holístico',' '),
('ESP003', 'Deportivo',' ');

INSERT INTO Servicio (ID_servicio, ID_especialidad, nombre, descripcion) VALUES
-- Servicios para Masajes
('SER001', 'ESP001', 'Masaje relajación',' '),
('SER002', 'ESP001', 'Masaje descontracturante',' '),
('SER003', 'ESP001', 'Masaje linfático',' '),

-- Servicios para Holístico
('SER004', 'ESP002', 'Reiki',' '),
('SER005', 'ESP002', 'Terapia floral',' '),
('SER006', 'ESP002', 'Aromaterapia',' '),

-- Servicios para Deportivo
('SER007', 'ESP003', 'Terapia física',' '),
('SER008', 'ESP003', 'Taping',' '),
('SER009', 'ESP003', 'Kinesiología',' '),
('SER010', 'ESP003', 'Crioterapia',' ');

INSERT INTO Terapeuta (
    ID_terapeuta, nombre, ape_paterno, ape_materno, user_name, password,
    enabled, direccion_principal, ID_comuna, ID_region, telefono, correo, evaluacion, url_foto
)VALUES
('TER001', 'Carlos', 'González', 'López', 'cgonza','1111',TRUE, 'Av. Principal 123', 'COM001', 'REG001', '987654321', 'carlos@example.com', 5,' '),
('TER002', 'María', 'Rodríguez', 'Pérez', 'mrodri','1111',TRUE, 'Calle Secundaria 456', 'COM002', 'REG001', '987654322', 'maria@example.com', 4,' '),
('TER003', 'Fernando', 'Martínez', 'Soto', 'fmarti','1111',TRUE,  'Pasaje Tranquilo 789', 'COM003', 'REG001', '987654323', 'fernando@example.com', 5,' '),
('TER004', 'Ana', 'Fernández', 'Gómez', 'aferna','1111',TRUE,  'Diagonal Centro 321', 'COM004', 'REG002', '987654324', 'ana@example.com', 4,' '),
('TER005', 'Javier', 'López', 'Ramírez', 'jlopez','1111',TRUE,  'Ruta Norte 654', 'COM005', 'REG002', '987654325', 'javier@example.com', 3,' '),
('TER006', 'Elena', 'Sánchez', 'Hernández', 'esanch','1111',TRUE,  'Paseo Central 987', 'COM006', 'REG003', '987654326', 'elena@example.com', 5,' '),
('TER007', 'Roberto', 'Díaz', 'Torres', 'rdiazt','1111',TRUE,  'Camino Verde 159', 'COM007', 'REG003', '987654327', 'roberto@example.com', 4,' '),
('TER008', 'Laura', 'Pérez', 'Castro', 'lperez','1111',TRUE,  'Sendero Azul 753', 'COM008', 'REG003', '987654328', 'laura@example.com', 5,' '),
('TER009', 'Daniel', 'Morales', 'Ríos', 'dmoral','1111',TRUE,  'Avenida Mar 852', 'COM009', 'REG004', '987654329', 'daniel@example.com', 4,' '),
('TER010', 'Patricia', 'Gómez', 'Vargas', 'pgomez','1111',TRUE,  'Calle Estrella 963', 'COM010', 'REG004', '987654330', 'patricia@example.com', 3,' ');