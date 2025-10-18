# Sistema de Gestión Empresarial (ERP) Modular

ERP modular con backend en Spring Boot 3 (Java 17) y un frontend React (esqueleto inicial). Incluye autenticación JWT, gestión de usuarios y roles, CRUD de productos, ventas y generación de facturas con cálculo de impuestos mediante Strategy Pattern. Documentación de API con OpenAPI/Swagger.

## Arquitectura

- Monorepo con dos paquetes principales:
  - `backend/`: API REST con Spring Boot, JPA/Hibernate, autenticación JWT, documentación OpenAPI, perfiles `dev/prod/test`, H2 en memoria para desarrollo y Dockerfile de despliegue.
  - `frontend/`: Esqueleto de SPA (React + Vite) con estructura base de archivos y estilos. Pendiente de completar dependencias, rutas y vistas.

## Funcionalidades clave (backend)

- Autenticación y autorización
  - Login (`POST /api/auth/login`) que entrega un JWT con roles.
  - Seguridad con `Spring Security`, filtros JWT, CORS abierto en dev, acceso a Swagger/H2 sin autenticación.
- Gestión de usuarios (rol `ADMIN` requerido)
  - CRUD de usuarios (`/api/usuarios`). Contraseñas hasheadas con BCrypt.
- Gestión de productos
  - CRUD paginado (`/api/productos`). Atributos: nombre, precio, exento de impuesto.
- Ventas y facturación
  - Registro de ventas y generación de factura (`/api/ventas/facturar`).
  - Strategy Pattern para cálculo de impuestos (`19%` normal o exento).
- Observabilidad y DX
  - OpenAPI/Swagger UI en `GET /swagger-ui.html`.
  - Consola H2 en `GET /h2-console` (solo perfil `dev`).

## Estructura del repositorio

```
proyecto-Java/
├─ backend/
│  ├─ pom.xml
│  ├─ Dockerfile
│  └─ src/
│     ├─ main/
│     │  ├─ java/com/example/erp/
│     │  │  ├─ ErpApplication.java
│     │  │  ├─ config/ (SecurityConfig, SwaggerConfig)
│     │  │  ├─ controllers/ (Auth, Producto, Usuario, Venta, Factura)
│     │  │  ├─ dto/ (Auth, producto, usuario, venta, factura)
│     │  │  ├─ entities/ (Usuario, Producto, Venta, Factura, Empleado)
│     │  │  ├─ exceptions/ (GlobalExceptionHandler, ResourceNotFoundException)
│     │  │  ├─ repositories/ (JPA repositories)
│     │  │  ├─ security/ (JwtAuthFilter, JwtTokenProvider, UserDetailsServiceImpl)
│     │  │  ├─ services/ (UsuarioService, ProductoService, VentaService, CalculoImpuestoService)
│     │  │  └─ tax/ (ImpuestoStrategy, Normal, Exento)
│     │  └─ resources/
│     │     ├─ application.properties (perfil activo: dev)
│     │     ├─ application-dev.properties (H2 en memoria)
│     │     └─ application-prod.properties (placeholders para prod)
│     └─ test/ (unitarios e integración; perfil `test` con H2)
└─ frontend/
   ├─ Punto de Entrada/ (index.html, main.jsx)
   ├─ estilos/ (theme.css, globalStyles.css)
   ├─ paginas/, estructura/, ComponentesdeUI/, ServiciosAPI/
   └─ Config. y Dependencias/ (package.json, vite.config.js — placeholders)
```

## Tecnologías

- Java 17, Spring Boot 3.3, Spring Security, Spring Data JPA
- JWT (jjwt), H2 (dev/test), OpenAPI (springdoc)
- Maven, JUnit, Jacoco
- Docker (eclipse-temurin:17-jre)
- Frontend (esqueleto): React + Vite + CSS utilitario (pendiente de completar)

## Requisitos previos

- Java 17 y Maven 3.9+
- Docker (opcional, para contenedores)
- Node.js 18+ y npm (opcional, para el frontend)

## Configuración y ejecución (backend)

Variables y puertos relevantes:

- `server.port=8080`
- Swagger UI: `GET http://localhost:8080/swagger-ui.html`
- H2 console (dev): `GET http://localhost:8080/h2-console` — JDBC URL: `jdbc:h2:mem:erpdb`
- JWT
  - `jwt.secret` (mín. 32 bytes). En `dev` viene definido; en `prod` se debe inyectar por env: `JWT_SECRET`.
  - `jwt.expiration-ms=3600000` (1 hora).

Ejecutar en desarrollo (perfil `dev` por defecto):

```
cd backend
mvn spring-boot:run
```

Build del JAR:

```
cd backend
mvn -DskipTests package
java -jar target/erp-backend-0.0.1-SNAPSHOT.jar
```

### Usuario inicial (crear el primer ADMIN)

El endpoint de login requiere que exista al menos un usuario. Opciones:

1) Consola H2 (rápido en dev):

   - Abrir `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:erpdb` | user: `sa` | password vacío
   - Insertar un usuario con contraseña hasheada en BCrypt, por ejemplo:

   ```sql
   INSERT INTO usuarios (username, password, email, role, enabled)
   VALUES ('admin', '{BCRYPT_HASH_AQUI}', 'admin@example.com', 'ADMIN', TRUE);
   ```

   Genera el hash BCrypt de tu contraseña (usa una herramienta local o tu IDE; el encoder es `BCryptPasswordEncoder`).

2) Semilla por `data.sql` (pendiente):

   - Alternativa recomendada a futuro: añadir `src/main/resources/data.sql` solo en `dev` con el insert anterior.

### Autenticación y uso básico

1) Obtener token JWT:

```
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"TU_PASSWORD"}'
```

2) Usar el token en endpoints protegidos:

```
curl http://localhost:8080/api/productos \
  -H "Authorization: Bearer TU_TOKEN"
```

## Endpoints principales (resumen)

- `POST /api/auth/login` → `AuthResponse { token, expiresInMs }`
- Productos (`/api/productos`)
  - `GET /` (paginado), `GET /{id}`, `POST /` (ADMIN), `PUT /{id}` (ADMIN), `DELETE /{id}` (ADMIN)
- Usuarios (`/api/usuarios`) — requiere `ADMIN`
  - `GET /` (paginado), `GET /{id}`, `POST /`, `PUT /{id}`, `DELETE /{id}`
- Ventas (`/api/ventas`)
  - `GET /` (paginado), `GET /{id}`, `POST /facturar` → genera `Factura`
- Facturas (`/api/facturas`)
  - `GET /` (paginado), `GET /{id}`

Swagger documenta esquemas y ejemplos: `GET /swagger-ui.html`.

## Estrategia de impuestos

Implementación con Strategy Pattern:

- `ImpuestoStrategy` → interfaz de cálculo
- `ImpuestoNormalStrategy` (19%) y `ImpuestoExentoStrategy` (0%)
- `CalculoImpuestoService` selecciona la estrategia según `producto.exentoImpuesto`

## Perfiles y configuración

- `dev`: H2 en memoria, Swagger y H2 console habilitados.
- `prod`: usar base de datos real (ej. PostgreSQL), `JWT_SECRET` por variable de entorno, CORS restringido.
- `test`: H2 en memoria, `application-test.properties` dedicado.

## Docker (backend)

1) Construir JAR:

```
cd backend
mvn -DskipTests package
```

2) Construir imagen:

```
docker build -t erp-backend:latest -f backend/Dockerfile backend
```

3) Ejecutar contenedor:

```
docker run --rm -p 8080:8080 \
  -e JWT_SECRET="cambia-este-secreto-de-32+bytes" \
  erp-backend:latest
```

## Tests (backend)

```
cd backend
mvn test
```

Incluye pruebas de servicios, repositorios, JWT y mapeos básicos.

## Frontend (estado actual y próximos pasos)

El frontend es un esqueleto listo para completar. Actualmente contiene:

- Entrada: `Punto de Entrada/index.html` y `main.jsx` con una AppShell mínima.
- Estilos: `estilos/theme.css`, `estilos/globalStyles.css`.
- Placeholders para rutas (`estructura/`), páginas (`paginas/`), componentes (`ComponentesdeUI/`), y servicios (`ServiciosAPI/`).
- Archivos vacíos de configuración en `Config. y Dependencias/` (`package.json`, `vite.config.js`).

Sugerencia de arranque con Vite + React:

```
cd frontend
npm create vite@latest . -- --template react
npm install
npm run dev
```

Luego integrar la estructura existente (estilos, páginas, componentes) y apuntar el cliente a `http://localhost:8080` para la API. Considerar almacenar el `API_BASE_URL` en `.env`.

## Roadmap sugerido

- Persistencia en PostgreSQL + migraciones con Flyway
- Módulos adicionales: inventario, compras, contabilidad, RR.HH., CRM
- Auditoría (createdAt, updatedAt), soft-delete
- Observabilidad (actuators, logs estructurados)
- CI/CD (GitHub Actions), calidad (linters, coverage thresholds)
- Frontend completo: rutas protegidas, formularios, tablas y gráficos

## Contribución

1) Crear rama feature: `git checkout -b feature/nombre`
2) Commit atómico y con contexto
3) Pull Request con descripción y checklist

## Licencia

Por definir. Si deseas, puedo añadir una licencia (MIT/Apache-2.0) en este repositorio.

