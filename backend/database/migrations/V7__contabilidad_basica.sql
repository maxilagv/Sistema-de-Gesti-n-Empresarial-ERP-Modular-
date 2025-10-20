-- V7: Contabilidad básica (plan de cuentas, diario, asientos)

CREATE TABLE IF NOT EXISTS periodo (
    id        BIGSERIAL PRIMARY KEY,
    nombre    VARCHAR(20) NOT NULL UNIQUE,
    desde     DATE NOT NULL,
    hasta     DATE NOT NULL,
    cerrado   BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS cuenta_contable (
    id         BIGSERIAL PRIMARY KEY,
    codigo     VARCHAR(20) NOT NULL UNIQUE,
    nombre     VARCHAR(200) NOT NULL,
    tipo       VARCHAR(20)  NOT NULL -- ACTIVO, PASIVO, PATRIMONIO, INGRESO, GASTO
);

CREATE TABLE IF NOT EXISTS asiento (
    id        BIGSERIAL PRIMARY KEY,
    fecha     DATE NOT NULL,
    periodo_id BIGINT REFERENCES periodo(id) ON DELETE SET NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS asiento_detalle (
    id           BIGSERIAL PRIMARY KEY,
    asiento_id   BIGINT NOT NULL REFERENCES asiento(id) ON DELETE CASCADE,
    cuenta_id    BIGINT NOT NULL REFERENCES cuenta_contable(id) ON DELETE RESTRICT,
    debe         NUMERIC(19,2) NOT NULL DEFAULT 0,
    haber        NUMERIC(19,2) NOT NULL DEFAULT 0,
    CHECK (NOT (debe = 0 AND haber = 0))
);

CREATE INDEX IF NOT EXISTS idx_asiento_fecha ON asiento(fecha);

