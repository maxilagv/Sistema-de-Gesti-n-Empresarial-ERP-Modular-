-- V3: Crear/actualizar usuario admin local usando placeholders (env) y bcrypt
-- Usa placeholders de Flyway: ${ADMIN_EMAIL} y ${ADMIN_PASSWORD}
-- Configurar en Spring: spring.flyway.placeholders.ADMIN_EMAIL y ADMIN_PASSWORD
-- o por env: SPRING_FLYWAY_PLACEHOLDERS_ADMIN_EMAIL / _ADMIN_PASSWORD

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Inserta o actualiza admin con email/username y password provistos por placeholders
INSERT INTO usuarios (username, password, email, role, enabled)
VALUES (
  '${ADMIN_EMAIL}',
  crypt('${ADMIN_PASSWORD}', gen_salt('bf', 10)),
  '${ADMIN_EMAIL}',
  'ADMIN',
  TRUE
)
ON CONFLICT (username) DO UPDATE SET
  password = EXCLUDED.password,
  email    = EXCLUDED.email,
  role     = EXCLUDED.role,
  enabled  = EXCLUDED.enabled;

