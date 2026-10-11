-- =====================================================================
-- Script de creación de base de datos - Sistema Bancario
-- Autor: Danny Quezada
-- Fecha: 11-10-2026
-- Motor: MySQL / PostgreSQL 
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. CREACIÓN DE LA BASE DE DATOS
-- ---------------------------------------------------------------------
DROP DATABASE IF EXISTS sistema_bancario;
CREATE DATABASE sistema_bancario
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sistema_bancario;

-- ---------------------------------------------------------------------
-- 2. TABLA PERSONA
--    Almacena los datos personales básicos.
-- ---------------------------------------------------------------------
CREATE TABLE persona (
    persona_id      BIGINT       NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(150) NOT NULL,
    genero          VARCHAR(20)  NOT NULL,
    edad            INT          NOT NULL,
    identificacion  VARCHAR(30)  NOT NULL,
    direccion       VARCHAR(250),
    telefono        VARCHAR(30),
    PRIMARY KEY (persona_id),
    CONSTRAINT uk_persona_identificacion UNIQUE (identificacion)
);

-- ---------------------------------------------------------------------
-- 3. TABLA CLIENTE
--    Tiene su propia PK (cliente_id) y una FK a persona (persona_id).
--    Un cliente tiene exactamente una persona asociada.
-- ---------------------------------------------------------------------
CREATE TABLE cliente (
    cliente_id   BIGINT       NOT NULL AUTO_INCREMENT,
    persona_id   BIGINT       NOT NULL,
    contrasena   VARCHAR(255) NOT NULL,
    estado       BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (cliente_id),
    CONSTRAINT uk_cliente_persona UNIQUE (persona_id),
    CONSTRAINT fk_cliente_persona
        FOREIGN KEY (persona_id) REFERENCES persona (persona_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- 4. TABLA CUENTA
--    Cada cuenta pertenece a un cliente.
-- ---------------------------------------------------------------------
CREATE TABLE cuenta (
    cuenta_id        BIGINT        NOT NULL AUTO_INCREMENT,
    numero_cuenta    VARCHAR(30)   NOT NULL,
    tipo_cuenta      VARCHAR(30)   NOT NULL,
    saldo_inicial    DECIMAL(15,2) NOT NULL,
    saldo_disponible DECIMAL(15,2) NOT NULL,
    estado           BOOLEAN       NOT NULL DEFAULT TRUE,
    cliente_id       BIGINT        NOT NULL,
    PRIMARY KEY (cuenta_id),
    CONSTRAINT uk_cuenta_numero UNIQUE (numero_cuenta),
    CONSTRAINT fk_cuenta_cliente
        FOREIGN KEY (cliente_id) REFERENCES cliente (cliente_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- 5. TABLA MOVIMIENTO
--    Cada movimiento pertenece a una cuenta.
-- ---------------------------------------------------------------------
CREATE TABLE movimiento (
    movimiento_id   BIGINT        NOT NULL AUTO_INCREMENT,
    fecha           DATETIME      NOT NULL,
    tipo_movimiento VARCHAR(30)   NOT NULL,
    valor           DECIMAL(15,2) NOT NULL,
    saldo           DECIMAL(15,2) NOT NULL,
    cuenta_id       BIGINT        NOT NULL,
    PRIMARY KEY (movimiento_id),
    CONSTRAINT fk_movimiento_cuenta
        FOREIGN KEY (cuenta_id) REFERENCES cuenta (cuenta_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- 6. ÍNDICES (opcionales, para mejorar rendimiento de consultas)
-- ---------------------------------------------------------------------
CREATE INDEX idx_cliente_persona    ON cliente   (persona_id);
CREATE INDEX idx_cuenta_cliente     ON cuenta    (cliente_id);
CREATE INDEX idx_movimiento_cuenta  ON movimiento(cuenta_id);
CREATE INDEX idx_movimiento_fecha   ON movimiento(fecha);

-- ---------------------------------------------------------------------
-- 7. DATOS DE EJEMPLO (casos de uso del PDF)
-- ---------------------------------------------------------------------

-- 7.1 Personas
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) VALUES
('Jose Lema',          'M', 30, '1234567890',   'Otavalo sn y principal',     '098254785'),
('Marianela Montalvo', 'F', 28, '0987654321',   'Amazonas y NNUU',            '097548965'),
('Juan Osorio',        'M', 35, '1122334455',   '13 junio y Equinoccial',     '098874587');

-- 7.2 Clientes (asociados a las personas anteriores)
INSERT INTO cliente (persona_id, contrasena, estado) VALUES
(1, '1234', TRUE),   -- Jose Lema
(2, '5678', TRUE),   -- Marianela Montalvo
(3, '1245', TRUE);   -- Juan Osorio

-- 7.3 Cuentas
INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id) VALUES
('478758', 'Ahorro',    2000.00, 2000.00, TRUE, 1),  -- Jose Lema
('225487', 'Corriente',  100.00,  100.00, TRUE, 2),  -- Marianela Montalvo
('495878', 'Ahorros',      0.00,    0.00, TRUE, 3),  -- Juan Osorio
('496825', 'Ahorros',    540.00,  540.00, TRUE, 2),  -- Marianela Montalvo
('585545', 'Corriente', 1000.00, 1000.00, TRUE, 1);  -- Jose Lema (nueva)

-- 7.4 Movimientos (ejemplos del PDF)
INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, cuenta_id) VALUES
('2022-02-10 10:00:00', 'RETIRO',   -575.00, 1425.00, 1),  -- Jose Lema retira 575
('2022-02-10 11:00:00', 'DEPOSITO',  600.00,  700.00, 2),  -- Marianela deposita 600
('2022-02-10 12:00:00', 'DEPOSITO',  150.00,  150.00, 3),  -- Juan deposita 150
('2022-02-08 09:00:00', 'RETIRO',   -540.00,    0.00, 4);  -- Marianela retira 540

-- ---------------------------------------------------------------------
-- 8. VERIFICACIÓN
-- ---------------------------------------------------------------------
SELECT 'persona'    AS tabla, COUNT(*) AS registros FROM persona
UNION ALL SELECT 'cliente',    COUNT(*) FROM cliente
UNION ALL SELECT 'cuenta',     COUNT(*) FROM cuenta
UNION ALL SELECT 'movimiento', COUNT(*) FROM movimiento;