-- V5: Inventario (almacenes, stock, lotes/series) y Compras (ordenes, recepciones)

-- Almacén y ubicaciones
CREATE TABLE IF NOT EXISTS almacen (
    id      BIGSERIAL PRIMARY KEY,
    codigo  VARCHAR(20)  NOT NULL UNIQUE,
    nombre  VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS ubicacion (
    id         BIGSERIAL PRIMARY KEY,
    almacen_id BIGINT NOT NULL REFERENCES almacen(id) ON DELETE CASCADE,
    codigo     VARCHAR(50) NOT NULL,
    UNIQUE (almacen_id, codigo)
);

-- Lotes y series (opcionales por producto)
CREATE TABLE IF NOT EXISTS lote (
    id          BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    codigo      VARCHAR(100) NOT NULL,
    fecha_venc  DATE,
    UNIQUE (producto_id, codigo)
);

CREATE TABLE IF NOT EXISTS serie (
    id          BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    numero      VARCHAR(150) NOT NULL,
    UNIQUE (producto_id, numero)
);

-- Stock agregado por producto/almacén/lote/serie
CREATE TABLE IF NOT EXISTS stock (
    id            BIGSERIAL PRIMARY KEY,
    producto_id   BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    almacen_id    BIGINT NOT NULL REFERENCES almacen(id)   ON DELETE CASCADE,
    lote_id       BIGINT REFERENCES lote(id)   ON DELETE SET NULL,
    serie_id      BIGINT REFERENCES serie(id)  ON DELETE SET NULL,
    qty_disponible NUMERIC(19,3) NOT NULL DEFAULT 0,
    qty_reservada  NUMERIC(19,3) NOT NULL DEFAULT 0
    -- Se elimina la restricción UNIQUE de la definición de la tabla
);

-- ******* SOLUCIÓN AL ERROR DE SINTAXIS *******
-- Creamos un índice único expresivo que logra el mismo objetivo.
-- Esto permite el uso de COALESCE() para manejar los valores NULL.
CREATE UNIQUE INDEX idx_stock_unique_combined ON stock
(
    producto_id,
    almacen_id,
    COALESCE(lote_id, 0),
    COALESCE(serie_id, 0)
);

-- FIN DE LA TABLA ANTERIOR, LIMPIO ESTE ESPACIO PARA EVITAR EL ERROR
-- ----------------------------------------------------------------------

-- Proveedor
CREATE TABLE IF NOT EXISTS proveedor (
    id      BIGSERIAL PRIMARY KEY,
    nombre  VARCHAR(200) NOT NULL,
    email   VARCHAR(320)
);

-- Orden de Compra (OC)
CREATE TABLE IF NOT EXISTS orden_compra (
    id            BIGSERIAL PRIMARY KEY,
    proveedor_id  BIGINT NOT NULL REFERENCES proveedor(id) ON DELETE RESTRICT,
    fecha         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    moneda        VARCHAR(3),
    subtotal      NUMERIC(19,2) NOT NULL DEFAULT 0,
    impuesto_total NUMERIC(19,2) NOT NULL DEFAULT 0,
    total         NUMERIC(19,2) NOT NULL DEFAULT 0,
    estado        VARCHAR(20) NOT NULL DEFAULT 'ABIERTA' -- ABIERTA, APROBADA, CANCELADA
);

CREATE TABLE IF NOT EXISTS oc_detalle (
    id              BIGSERIAL PRIMARY KEY,
    oc_id           BIGINT NOT NULL REFERENCES orden_compra(id) ON DELETE CASCADE,
    producto_id     BIGINT NOT NULL REFERENCES productos(id) ON DELETE RESTRICT,
    cantidad        NUMERIC(19,3) NOT NULL CHECK (cantidad>0),
    precio_unitario NUMERIC(19,2) NOT NULL,
    impuestos_json  TEXT,
    subtotal        NUMERIC(19,2) NOT NULL DEFAULT 0,
    impuestos_total NUMERIC(19,2) NOT NULL DEFAULT 0,
    total_linea     NUMERIC(19,2) NOT NULL DEFAULT 0
);

-- Movimientos de inventario
CREATE TABLE IF NOT EXISTS mov_inventario (
    id            BIGSERIAL PRIMARY KEY,
    fecha         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    tipo          VARCHAR(20) NOT NULL, -- ENTRADA, SALIDA, AJUSTE
    motivo        VARCHAR(100),
    referencia    VARCHAR(100),
    producto_id   BIGINT NOT NULL REFERENCES productos(id),
    almacen_id    BIGINT NOT NULL REFERENCES almacen(id),
    lote_id       BIGINT REFERENCES lote(id),
    serie_id      BIGINT REFERENCES serie(id),
    cantidad      NUMERIC(19,3) NOT NULL,
    UNIQUE(id)
);

-- Recepciones (desde OC)
CREATE TABLE IF NOT EXISTS recepcion (
    id         BIGSERIAL PRIMARY KEY,
    oc_id      BIGINT NOT NULL REFERENCES orden_compra(id) ON DELETE CASCADE,
    fecha      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    almacen_id BIGINT NOT NULL REFERENCES almacen(id)
);

CREATE TABLE IF NOT EXISTS recepcion_det (
    id            BIGSERIAL PRIMARY KEY,
    recepcion_id  BIGINT NOT NULL REFERENCES recepcion(id) ON DELETE CASCADE,
    oc_detalle_id BIGINT NOT NULL REFERENCES oc_detalle(id) ON DELETE RESTRICT,
    cantidad_recibida NUMERIC(19,3) NOT NULL CHECK (cantidad_recibida>0),
    lote_id       BIGINT REFERENCES lote(id),
    serie_id      BIGINT REFERENCES serie(id)
);
