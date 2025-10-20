-- V2: Seed básico de productos de ejemplo

INSERT INTO productos (nombre, precio, exento_impuesto) VALUES
  ('Servicio de consultoría', 100.00, TRUE),
  ('Mouse inalámbrico',       25.99,  FALSE),
  ('Laptop 14 pulgadas',     800.00,  FALSE);

-- NOTA: No se inserta usuario admin por defecto para evitar exponer hashes.
-- Para crear un admin:
-- 1) Genera un hash BCrypt del password deseado.
-- 2) Inserta manualmente, por ejemplo:
-- INSERT INTO usuarios (username, password, email, role, enabled)
-- VALUES ('admin', '$2a$10$REEMPLAZA_AQUI_HASH_BCRYPT', 'admin@local', 'ADMIN', TRUE);

