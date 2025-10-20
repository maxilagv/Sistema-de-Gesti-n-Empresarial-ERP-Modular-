# ERP Modular (Spring Boot + React)

API REST para gestión empresarial (ERP) con backend en Spring Boot 3 (Java 17) y un frontend React (esqueleto). Incluye autenticación JWT, gestión de usuarios/roles, CRUD de productos, ventas y facturación con Strategy Pattern para impuestos. Documentación con OpenAPI/Swagger.

## Arquitectura

- Monorepo con dos paquetes:
  - `backend/`: API REST (Spring Boot, JPA/Hibernate, Spring Security + JWT, Swagger, perfiles `dev`/`prod`).
  - `frontend/`: Esqueleto SPA (React + Vite) listo para completar vistas y rutas.

## Funcionalidades (backend)

- Autenticación y autorización
  - `POST /api/auth/login` entrega JWT con roles.
  - Seguridad centralizada en Spring Security y filtro JWT; Swagger y H2 abiertos en `dev`.
- Usuarios (ADMIN)
  - CRUD en `/api/usuarios` con contraseñas en BCrypt.
- Productos
  - CRUD paginado en `/api/productos` (crear/editar/eliminar requieren ADMIN).
- Ventas y facturación
  - `POST /api/ventas/facturar` genera factura y calcula impuestos por estrategia.
- Observabilidad y DX
  - Swagger UI en `/swagger-ui.html`.
  - Consola H2 en `/h2-console` (perfil `dev`).

## Estructura

```
proyecto-Java/
├─ backend/
│  ├─ pom.xml
│  ├─ Dockerfile
│  ├─ docker-compose.yml
│  └─ src/
│     ├─ main/java/com/example/erp/
│     │  ├─ config/ (SecurityConfig, SwaggerConfig)
│     │  ├─ controllers/ (Auth, Producto, Usuario, Venta, Factura)
│     │  ├─ dto/ (Auth, producto, usuario, venta, factura)
│     │  ├─ entities/ (Usuario, Producto, Venta, Factura, ...)
│     │  ├─ exceptions/ (GlobalExceptionHandler, ResourceNotFoundException)
│     │  ├─ repositories/ (JPA repositories)
│     │  ├─ security/ (JwtAuthFilter, JwtTokenProvider, UserDetailsServiceImpl)
│     │  └─ services/ (UsuarioService, ProductoService, VentaService, ...)
│     └─ main/resources/
│        ├─ application.properties (perfil activo)
│        ├─ application-dev.properties (H2 en memoria)
│        └─ application-prod.properties (placeholders y Flyway)
└─ frontend/ (esqueleto React + Vite)
```

## Tecnologías

- Java 17, Spring Boot 3.x, Spring Security, Spring Data JPA
- JWT (jjwt), H2 (dev), PostgreSQL (prod), OpenAPI (springdoc)
- Maven, JUnit
- Docker
- React + Vite (frontend esqueleto)

## Requisitos

- Java 17 y Maven 3.9+
- Docker (opcional para DB/productivo)
- Node.js 18+ (opcional para frontend)

## Puesta en marcha (backend)

Desarrollo (perfil `dev` por defecto):

```
cd backend
mvn spring-boot:run
```

Swagger: `http://localhost:8080/swagger-ui.html`
H2 Console (dev): `http://localhost:8080/h2-console`

Producción (con PostgreSQL + Flyway):

1) Iniciar base de datos (ver `backend/docker-compose.yml`).
2) Exportar variables de entorno requeridas (ver sección Configuración) y arrancar con `SPRING_PROFILES_ACTIVE=prod`.

```
cd backend
mvn -DskipTests package
java -jar target/erp-backend-*.jar
```

## Configuración

Variables (nombres de ejemplo):

- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `JWT_SECRET` (32+ bytes), `jwt.expiration-ms`
- `ADMIN_EMAIL`, `ADMIN_PASSWORD` (semilla admin vía Flyway en `prod`)

Credenciales y datos sensibles:

- Por seguridad del proyecto no se pueden detallar las credenciales.
- Configura las variables por entorno o gestor de secretos según el despliegue.

## Autenticación JWT

- Login: `POST /api/auth/login` con cuerpo `{"username":"<USUARIO>","password":"<CONTRASEÑA>"}`.
- Usa el token recibido en `Authorization: Bearer <TOKEN>` para acceder a rutas protegidas.
- Roles: rutas de administración usan `@PreAuthorize("hasRole('ADMIN')")`.

Ejemplos (placeholders):

```
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"<USUARIO>","password":"<CONTRASEÑA>"}'

curl http://localhost:8080/api/productos \
  -H "Authorization: Bearer <TOKEN>"
```

## Endpoints principales

- `POST /api/auth/login` → `AuthResponse { token, expiresInMs }`
- Productos (`/api/productos`): `GET /`, `GET /{id}`, `POST /` (ADMIN), `PUT /{id}` (ADMIN), `DELETE /{id}` (ADMIN)
- Usuarios (`/api/usuarios`, ADMIN): `GET /`, `GET /{id}`, `POST /`, `PUT /{id}`, `DELETE /{id}`
- Ventas (`/api/ventas`): `GET /`, `GET /{id}`, `POST /facturar`
- Facturas (`/api/facturas`): `GET /`, `GET /{id}`

La seguridad global permite sin token: `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/h2-console/**` (ver `backend/src/main/java/com/example/erp/config/SecurityConfig.java`).

## Base de datos y migraciones

- `dev`: H2 en memoria (sin persistencia) para iteración rápida.
- `prod`: PostgreSQL con migraciones Flyway en `backend/database/migrations` (incluye semilla de admin mediante placeholders).

## Tests

```
cd backend
mvn test
```

## Frontend

Esqueleto inicial con React + Vite listo para completar. Sugerencia rápida:

```
cd frontend
npm install
npm run dev
```

Configura `API_BASE_URL` del cliente según entorno.

## Solución de problemas

- 401/403: token ausente, inválido o sin rol requerido.
- 500 al loguear en dev: crear usuario inicial; en `prod` verifica variables y conexión a DB.
- `jwt.secret` debe tener 32+ bytes.

## Contribución

- Flujo sugerido: ramas por feature, commits atómicos y PR con contexto.

## Licencia

Este proyecto está licenciado bajo la licencia MIT. Ver `LICENSE`.

