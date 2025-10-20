-- V9: Multi-moneda (moneda, tasa_cambio) e i18n básico (placeholders)

CREATE TABLE IF NOT EXISTS moneda (
    codigo   VARCHAR(3) PRIMARY KEY, -- ISO 4217
    nombre   VARCHAR(50) NOT NULL,
    decimales INT NOT NULL DEFAULT 2
);

CREATE TABLE IF NOT EXISTS tasa_cambio (
    id          BIGSERIAL PRIMARY KEY,
    moneda_base VARCHAR(3) NOT NULL REFERENCES moneda(codigo) ON DELETE CASCADE,
    moneda_otra VARCHAR(3) NOT NULL REFERENCES moneda(codigo) ON DELETE CASCADE,
    fecha       DATE NOT NULL,
    tasa        NUMERIC(19,6) NOT NULL,
    UNIQUE (moneda_base, moneda_otra, fecha)
);

-- i18n de descripciones (ejemplo genérico por entidad y campo)
CREATE TABLE IF NOT EXISTS i18n_texto (
    id           BIGSERIAL PRIMARY KEY,
    entidad      VARCHAR(50) NOT NULL,
    entidad_id   BIGINT      NOT NULL,
    campo        VARCHAR(50) NOT NULL,
    locale       VARCHAR(10) NOT NULL, -- es-AR, en-US, etc.
    texto        TEXT,
    UNIQUE (entidad, entidad_id, campo, locale)
);

