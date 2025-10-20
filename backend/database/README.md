Base de datos para ERP Backend

Objetivo
- Definir el esquema relacional acorde al dominio actual del backend (productos, usuarios/empleados, ventas y facturas) y proveer migraciones Flyway.

Relación con el código
- Producto: proyecto-Java/backend/src/main/java/com/example/erp/entities/Producto.java
- Usuario: proyecto-Java/backend/src/main/java/com/example/erp/entities/Usuario.java
- Empleado (subclase de Usuario): proyecto-Java/backend/src/main/java/com/example/erp/entities/Empleado.java
- Venta: proyecto-Java/backend/src/main/java/com/example/erp/entities/Venta.java
- Factura: proyecto-Java/backend/src/main/java/com/example/erp/entities/Factura.java
- Impuestos: 19% por defecto, 0% para exento (estrategias en tax/)

Contenido
- migrations/V1__initial_schema.sql: Esquema inicial (tablas, claves foráneas, checks e índices).
- migrations/V2__seed_productos.sql: Carga básica de productos de ejemplo.
- migrations/V3__seed_admin_local.sql: Admin local (usa pgcrypto: bcrypt). Solo para desarrollo.

Modelo de datos (PostgreSQL)
- productos: id, nombre, precio, exento_impuesto
- usuarios: id, username único, password (BCrypt), email único, role, enabled
- empleados: id (FK a usuarios), departamento, salario
- ventas: id, fecha, producto_id (FK), cantidad, precio_unitario
- facturas: id, venta_id (FK único), subtotal, impuesto, total

Reglas clave
- No se permite borrar productos con ventas (ON DELETE RESTRICT).
- Borrar usuarios elimina su fila hija en empleados (ON DELETE CASCADE).
- En ventas se persiste precio_unitario para mantener histórico.
- Índices: ventas(producto_id), ventas(fecha); unicidades en usuarios(username,email); facturas(venta_id) único.

Aplicar con Flyway CLI (opción 1)
1) Instalar/usar Flyway CLI.
2) Ejecutar (ajustar credenciales/host/DB):
   flyway -locations=filesystem:proyecto-Java/backend/database/migrations \
          -url=jdbc:postgresql://localhost:5432/erpdb \
          -user=erp -password=TU_PASSWORD migrate

Integración Spring Boot (opción 2)
1) Agregar dependencias en pom.xml:
   - org.flywaydb:flyway-core
   - org.postgresql:postgresql
2) application-prod.properties:
   spring.datasource.url=jdbc:postgresql://HOST:5432/erpdb
   spring.datasource.username=erp
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=validate
   spring.flyway.enabled=true
   # spring.flyway.locations=classpath:db/migration (por defecto)
3) Mueve/copiar migraciones a src/main/resources/db/migration o configura spring.flyway.locations para apuntar a esta carpeta.

Seed de usuarios (admin local)
- V3 inserta/actualiza un admin usando placeholders de Flyway:
  - ${ADMIN_EMAIL} (por defecto: maxilavagetto@gmail.com)
  - ${ADMIN_PASSWORD} (obligatorio)
- El hash se genera en DB con pgcrypto (bcrypt) y no queda expuesto en el repo.
- Requisitos: privilegio para `CREATE EXTENSION pgcrypto` (con Docker por defecto el usuario es superusuario).
- Producción: no uses V3. Crea usuarios por otros medios y con políticas de contraseñas propias.

Variables de entorno útiles (PowerShell)
- $env:SPRING_PROFILES_ACTIVE='prod'
- $env:ADMIN_EMAIL='maxilavagetto@gmail.com'
- $env:ADMIN_PASSWORD='[tu_password]'
- $env:DB_PASSWORD='changeMe!'
- $env:JWT_SECRET='TU_SECRETO_32+_BYTES'

Notas
- facturas.total tiene CHECK con total = subtotal + impuesto (ambos NUMERIC(19,2)).
- Campos monetarios NUMERIC(19,2) alineados con el backend.
- Futuro: detalle de venta (N ítems), auditoría (created_at/updated_at), catálogo de impuestos por categoría.
