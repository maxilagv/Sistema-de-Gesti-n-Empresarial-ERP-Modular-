-- V1: Esquema inicial ERP (PostgreSQL)
-- Tablas: productos, usuarios, empleados, ventas, facturas

-- Productos
CREATE TABLE IF NOT EXISTS productos (
    id               BIGSERIAL PRIMARY KEY,
    nombre           VARCHAR(200)      NOT NULL,
    precio           NUMERIC(19,2)     NOT NULL CHECK (precio > 0),
    exento_impuesto  BOOLEAN           NOT NULL DEFAULT FALSE
);

-- Usuarios base
CREATE TABLE IF NOT EXISTS usuarios (
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(100)  NOT NULL UNIQUE,
    password  VARCHAR(255)  NOT NULL,
    email     VARCHAR(320)  NOT NULL UNIQUE,
    role      VARCHAR(20)   NOT NULL DEFAULT 'USER',
    enabled   BOOLEAN       NOT NULL DEFAULT TRUE
    -- Opcional: CHECK (role IN ('USER','ADMIN'))
);

-- Empleados (herencia JOINED de Usuario)
CREATE TABLE IF NOT EXISTS empleados (
    id            BIGINT        PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE,
    departamento  VARCHAR(100),
    salario       NUMERIC(19,2) CHECK (salario >= 0)
);

-- Ventas
CREATE TABLE IF NOT EXISTS ventas (
    id              BIGSERIAL PRIMARY KEY,
    fecha           TIMESTAMP       NOT NULL DEFAULT NOW(),
    producto_id     BIGINT          NOT NULL REFERENCES productos(id) ON DELETE RESTRICT,
    cantidad        INT             NOT NULL DEFAULT 1 CHECK (cantidad >= 1),
    precio_unitario NUMERIC(19,2)   NOT NULL CHECK (precio_unitario > 0)
);

-- Índices ventas
CREATE INDEX IF NOT EXISTS idx_ventas_producto_id ON ventas(producto_id);
CREATE INDEX IF NOT EXISTS idx_ventas_fecha       ON ventas(fecha);

-- Facturas (1:1 con venta)
CREATE TABLE IF NOT EXISTS facturas (
    id        BIGSERIAL PRIMARY KEY,
    venta_id  BIGINT        NOT NULL UNIQUE REFERENCES ventas(id) ON DELETE CASCADE,
    subtotal  NUMERIC(19,2) NOT NULL,
    impuesto  NUMERIC(19,2) NOT NULL,
    total     NUMERIC(19,2) NOT NULL,
    CHECK (total = subtotal + impuesto)
);

-- Notas:
-- - PKs y FKs alineadas con las entidades JPA.
-- - Restricciones monetarias y de negocio básicas incluidas.
-- - ON DELETE: usuarios->empleados CASCADE; productos->ventas RESTRICT; ventas->facturas CASCADE.

