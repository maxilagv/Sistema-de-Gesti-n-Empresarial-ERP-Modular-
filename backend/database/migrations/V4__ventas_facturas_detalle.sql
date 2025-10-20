-- V4: Ventas/Facturas con ítems por línea (venta/venta_detalle, factura/factura_detalle)

-- Cliente (mínimo)
CREATE TABLE IF NOT EXISTS cliente (
    id       BIGSERIAL PRIMARY KEY,
    nombre   VARCHAR(200) NOT NULL,
    email    VARCHAR(320) UNIQUE
);

-- Impuesto (catálogo, opcional de uso en cálculo)
CREATE TABLE IF NOT EXISTS impuesto (
    id          BIGSERIAL PRIMARY KEY,
    codigo      VARCHAR(20)  NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    tasa        NUMERIC(7,4) NOT NULL CHECK (tasa >= 0), -- 0.1200 = 12%
    incluido    BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Venta cabecera (nueva, distinta de 'ventas' existente)
CREATE TABLE IF NOT EXISTS venta (
    id              BIGSERIAL PRIMARY KEY,
    cliente_id      BIGINT REFERENCES cliente(id) ON DELETE SET NULL,
    fecha           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    moneda          VARCHAR(3),
    tasa_cambio     NUMERIC(19,6),
    subtotal        NUMERIC(19,2) NOT NULL DEFAULT 0,
    descuento_total NUMERIC(19,2) NOT NULL DEFAULT 0,
    impuesto_total  NUMERIC(19,2) NOT NULL DEFAULT 0,
    total           NUMERIC(19,2) NOT NULL DEFAULT 0,
    estado          VARCHAR(20)  NOT NULL DEFAULT 'CREADA'
);

CREATE INDEX IF NOT EXISTS idx_venta_fecha ON venta(fecha);
CREATE INDEX IF NOT EXISTS idx_venta_cliente ON venta(cliente_id);

-- Venta detalle
CREATE TABLE IF NOT EXISTS venta_detalle (
    id               BIGSERIAL PRIMARY KEY,
    venta_id         BIGINT       NOT NULL REFERENCES venta(id) ON DELETE CASCADE,
    producto_id      BIGINT       NOT NULL REFERENCES productos(id) ON DELETE RESTRICT,
    desc_producto    VARCHAR(255),
    cantidad         NUMERIC(19,3) NOT NULL CHECK (cantidad > 0),
    precio_unitario  NUMERIC(19,2) NOT NULL CHECK (precio_unitario >= 0),
    descuento_pct    NUMERIC(5,2),
    descuento_valor  NUMERIC(19,2),
    impuestos_json   TEXT,
    subtotal         NUMERIC(19,2) NOT NULL DEFAULT 0,
    impuestos_total  NUMERIC(19,2) NOT NULL DEFAULT 0,
    total_linea      NUMERIC(19,2) NOT NULL DEFAULT 0,
    moneda           VARCHAR(3),
    tasa_cambio      NUMERIC(19,6),
    precio_lista_ref NUMERIC(19,2)
);

CREATE INDEX IF NOT EXISTS idx_venta_detalle_venta ON venta_detalle(venta_id);
CREATE INDEX IF NOT EXISTS idx_venta_detalle_prod ON venta_detalle(producto_id);

-- Factura cabecera (nueva, distinta de 'facturas' existente)
CREATE TABLE IF NOT EXISTS factura (
    id             BIGSERIAL PRIMARY KEY,
    venta_id       BIGINT REFERENCES venta(id) ON DELETE SET NULL,
    fecha_emision  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    subtotal       NUMERIC(19,2) NOT NULL DEFAULT 0,
    impuesto_total NUMERIC(19,2) NOT NULL DEFAULT 0,
    total          NUMERIC(19,2) NOT NULL DEFAULT 0,
    estado         VARCHAR(20)  NOT NULL DEFAULT 'EMITIDA'
);

CREATE INDEX IF NOT EXISTS idx_factura_fecha ON factura(fecha_emision);

-- Factura detalle
CREATE TABLE IF NOT EXISTS factura_detalle (
    id              BIGSERIAL PRIMARY KEY,
    factura_id      BIGINT       NOT NULL REFERENCES factura(id) ON DELETE CASCADE,
    producto_id     BIGINT       NOT NULL REFERENCES productos(id) ON DELETE RESTRICT,
    desc_producto   VARCHAR(255),
    cantidad        NUMERIC(19,3) NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(19,2) NOT NULL CHECK (precio_unitario >= 0),
    descuento_pct   NUMERIC(5,2),
    descuento_valor NUMERIC(19,2),
    impuestos_json  TEXT,
    subtotal        NUMERIC(19,2) NOT NULL DEFAULT 0,
    impuestos_total NUMERIC(19,2) NOT NULL DEFAULT 0,
    total_linea     NUMERIC(19,2) NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_factura_detalle_fact ON factura_detalle(factura_id);

