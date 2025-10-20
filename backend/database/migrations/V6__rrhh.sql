-- V6: RRHH (departamentos, roles de puesto, salarios, ausencias, vacaciones, evaluaciones)

CREATE TABLE IF NOT EXISTS departamento (
    id     BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS rol_puesto (
    id     BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Historial salarial (empleados.id ya existe y referencia usuarios)
CREATE TABLE IF NOT EXISTS salario_hist (
    id           BIGSERIAL PRIMARY KEY,
    empleado_id  BIGINT NOT NULL REFERENCES empleados(id) ON DELETE CASCADE,
    salario      NUMERIC(19,2) NOT NULL CHECK (salario>=0),
    vigente_desde DATE NOT NULL,
    vigente_hasta DATE
);

CREATE TABLE IF NOT EXISTS ausencia (
    id           BIGSERIAL PRIMARY KEY,
    empleado_id  BIGINT NOT NULL REFERENCES empleados(id) ON DELETE CASCADE,
    desde        DATE NOT NULL,
    hasta        DATE NOT NULL,
    motivo       VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS vacacion (
    id           BIGSERIAL PRIMARY KEY,
    empleado_id  BIGINT NOT NULL REFERENCES empleados(id) ON DELETE CASCADE,
    desde        DATE NOT NULL,
    hasta        DATE NOT NULL,
    estado       VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' -- PENDIENTE/APROBADA/RECHAZADA
);

CREATE TABLE IF NOT EXISTS evaluacion (
    id           BIGSERIAL PRIMARY KEY,
    empleado_id  BIGINT NOT NULL REFERENCES empleados(id) ON DELETE CASCADE,
    fecha        DATE NOT NULL,
    puntuacion   NUMERIC(5,2),
    comentarios  TEXT
);

