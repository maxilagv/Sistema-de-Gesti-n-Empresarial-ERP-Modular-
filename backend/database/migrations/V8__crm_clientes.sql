-- V8: CRM (prospectos, contactos, cuentas, oportunidades, actividades)

CREATE TABLE IF NOT EXISTS cuenta_cliente (
    id      BIGSERIAL PRIMARY KEY,
    nombre  VARCHAR(200) NOT NULL,
    email   VARCHAR(320)
);

CREATE TABLE IF NOT EXISTS prospecto (
    id      BIGSERIAL PRIMARY KEY,
    nombre  VARCHAR(200) NOT NULL,
    email   VARCHAR(320)
);

CREATE TABLE IF NOT EXISTS contacto (
    id        BIGSERIAL PRIMARY KEY,
    cuenta_id BIGINT REFERENCES cuenta_cliente(id) ON DELETE CASCADE,
    nombre    VARCHAR(200) NOT NULL,
    email     VARCHAR(320)
);

CREATE TABLE IF NOT EXISTS oportunidad (
    id           BIGSERIAL PRIMARY KEY,
    cuenta_id    BIGINT REFERENCES cuenta_cliente(id) ON DELETE SET NULL,
    nombre       VARCHAR(200) NOT NULL,
    etapa        VARCHAR(50)  NOT NULL,
    probabilidad NUMERIC(5,2),
    monto        NUMERIC(19,2),
    moneda       VARCHAR(3)
);

CREATE TABLE IF NOT EXISTS actividad (
    id             BIGSERIAL PRIMARY KEY,
    cuenta_id      BIGINT REFERENCES cuenta_cliente(id) ON DELETE CASCADE,
    oportunidad_id BIGINT REFERENCES oportunidad(id) ON DELETE CASCADE,
    tipo           VARCHAR(30) NOT NULL, -- llamada, reunion, tarea
    fecha          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    descripcion    TEXT
);

