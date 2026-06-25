# Sistema de Gestión de Convocatorias Institucionales

Sistema web para administrar convocatorias institucionales dirigidas a estudiantes, docentes y administrativos de la **Universidad Surcolombiana**, desarrollado como prueba técnica para el cargo de Desarrollador(a) Full Stack.

Permite gestionar convocatorias (becas, monitorías, eventos, capacitaciones), categorías, usuarios con roles diferenciados, postulaciones con validaciones de negocio, y reportes con visualización en tabla y gráfico.

---

## Stack Tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 21, Spring Boot 3.5, Spring Security, JWT |
| Base de datos | SQL Server 2022 |
| ORM | Spring Data JPA / Hibernate |
| Frontend | Angular 22 (standalone components), Angular Material |
| Gráficos | Chart.js + ng2-charts |
| Build / Gestión de dependencias | Maven (backend), npm (frontend) |

---

## Estructura del repositorio

Este es un **monorepo** que contiene backend y frontend en carpetas separadas:

```
PruebaUsco/
├── backend/
│   └── convocatorias/          # Proyecto Spring Boot
│       ├── src/main/java/com/usco/convocatorias/
│       │   ├── model/          # Entidades JPA y enums
│       │   ├── repository/     # Spring Data JPA repositories
│       │   ├── service/        # Interfaces de servicio
│       │   │   └── impl/       # Implementaciones
│       │   ├── controller/     # Controladores REST
│       │   ├── dto/            # DTOs de request/response
│       │   ├── security/       # JWT (filtro, servicio de tokens)
│       │   ├── config/         # SecurityConfig, UserDetailsService
│       │   └── exception/      # Excepciones personalizadas + manejador global
│       └── src/main/resources/
│           └── application.properties
│
├── convocatorias-frontend/     # Proyecto Angular
│   └── src/app/
│       ├── core/
│       │   ├── models/         # Interfaces TypeScript
│       │   ├── services/       # Servicios HTTP
│       │   ├── guards/         # authGuard, rolGuard
│       │   └── interceptors/   # Interceptor JWT
│       ├── features/
│       │   ├── auth/login/
│       │   ├── dashboard/
│       │   ├── usuarios/
│       │   ├── categorias/
│       │   ├── convocatorias/
│       │   ├── postulaciones/
│       │   └── reportes/
│       └── shared/layout/      # Sidebar + toolbar
│
├── script_creacion_bd.sql      # Script de creación de esquema y tablas
└── README.md
```

---

## Requisitos previos

- **Java 21** (JDK)
- **Maven** (o usar el wrapper incluido en el proyecto)
- **SQL Server 2022** (Express o superior), con autenticación SQL habilitada (modo mixto)
- **Node.js 22.x LTS** y **npm**
- **Angular CLI 22**: `npm install -g @angular/cli`

---

## 1. Configuración de la base de datos

### 1.1 Crear la base de datos

Conéctate a tu instancia de SQL Server y ejecuta:

```sql
CREATE DATABASE bdConvocatorias;
```

### 1.2 Ejecutar el script de creación de tablas

Ejecuta el archivo `script_creacion_bd.sql` (ubicado en la raíz del repositorio) **completo, en un solo lote** ("Execute SQL Script" en DBeaver o equivalente), conectado a la base `bdConvocatorias`. Este script crea:

- El esquema `convocatorias`
- Las 5 tablas: `usuarios`, `categorias`, `convocatorias`, `convocatoria_categoria` (relación N:M), `postulaciones`
- Constraints de integridad referencial, índices, y datos de prueba (seed)

### 1.3 Crear un usuario de aplicación (SQL Server Authentication)

```sql
CREATE LOGIN appConvocatorias WITH PASSWORD = 'TuPasswordSegura123!';

USE bdConvocatorias;
CREATE USER appConvocatorias FOR LOGIN appConvocatorias;
ALTER ROLE db_owner ADD MEMBER appConvocatorias;


## 2. Configuración y ejecución del Backend

### 2.1 Configurar `application.properties`

En `backend/convocatorias/src/main/resources/application.properties`, ajusta la conexión:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=bdConvocatorias;encrypt=true;trustServerCertificate=true
spring.datasource.username=appConvocatorias
spring.datasource.password=TuPasswordSegura123!
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.default_schema=convocatorias
spring.jpa.open-in-view=false

app.jwt.secret=ClaveSecretaUSCOConvocatorias2026CambiarEnProduccionPorFavor
app.jwt.expiration-ms=86400000

server.port=8080
```

> Por seguridad, este archivo **no debe subirse al repositorio con credenciales reales**. Usa variables de entorno o un archivo `.env`/`application-local.properties` ignorado por Git para producción.

### 2.2 Ejecutar el backend

Desde `backend/convocatorias/`:

```bash
mvn spring-boot:run
```

O ejecuta la clase `ConvocatoriasApplication` directamente desde tu IDE (IntelliJ IDEA, Eclipse, etc.).

El backend queda disponible en: `http://localhost:8080`

---

## 3. Configuración y ejecución del Frontend

Desde `convocatorias-frontend/`:

```bash
npm install
ng serve
```

La aplicación queda disponible en: `http://localhost:4200`

> El frontend está configurado para consumir el backend en `http://localhost:8080/api` (ver `src/environments/environment.ts`). Si cambias el puerto del backend, actualiza esa URL.

---

## Credenciales de prueba

Todos los usuarios de prueba comparten la misma contraseña: **`Usco2026*`**

| Rol | Correo | Contraseña |
|---|---|---|
| ADMINISTRADOR | `mflopez@usco.edu.co` | `Usco2026*` |
| DOCENTE | `cprios@usco.edu.co` | `Usco2026*` |
| ESTUDIANTE | `ana.torres@usco.edu.co` | `Usco2026*` |

### Permisos por rol

| Acción | ADMINISTRADOR | DOCENTE | ESTUDIANTE |
|---|---|---|---|
| Gestionar usuarios (CRUD) | ✅ | ❌ | ❌ |
| Gestionar categorías (CRUD) | ✅ | ❌ (solo lectura) | ❌ (solo lectura) |
| Gestionar convocatorias (CRUD) | ✅ | ❌ (solo lectura) | ❌ (solo lectura) |
| Postularse a convocatorias | ❌ | ❌ | ✅ |
| Aprobar / rechazar postulaciones | ✅ | ✅ | ❌ |
| Ver reportes | ✅ | ❌ | ❌ |

---

## Funcionalidades principales

### Backend
- Autenticación con JWT (`POST /api/auth/login`) y autorización por rol vía Spring Security
- CRUD completo de Usuarios, Categorías y Convocatorias
- Relación N:M entre Convocatorias y Categorías
- Postulaciones con 3 validaciones de negocio: no duplicar postulación, no postularse a convocatorias cerradas o no publicadas, no exceder el cupo disponible
- 3 reportes: convocatorias por categoría, postulaciones por convocatoria, resultado de postulaciones (aprobadas/rechazadas)
- Manejo global de excepciones con respuestas HTTP estructuradas

### Frontend
- Login con interceptor HTTP que adjunta el token JWT automáticamente
- Guards de ruta por autenticación y por rol
- Layout con sidebar colapsable
- Dashboard con indicadores (KPIs) para administradores
- CRUD visual de Usuarios, Categorías y Convocatorias con diálogos modales
- Postulación a convocatorias (estudiantes) y gestión de aprobación/rechazo (administradores y docentes)
- Reportes con tabla y gráfico (barras y dona) por cada reporte

---

## Endpoints principales de la API

```
POST   /api/auth/login

GET    /api/usuarios
POST   /api/usuarios
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}

GET    /api/categorias
POST   /api/categorias
PUT    /api/categorias/{id}
DELETE /api/categorias/{id}

GET    /api/convocatorias
POST   /api/convocatorias
PUT    /api/convocatorias/{id}
PATCH  /api/convocatorias/{id}/estado
DELETE /api/convocatorias/{id}

POST   /api/postulaciones
GET    /api/postulaciones
PUT    /api/postulaciones/{id}/estado

GET    /api/reportes/convocatorias-categoria
GET    /api/reportes/postulaciones-convocatoria
GET    /api/reportes/resultado-postulaciones
```

Colección de Postman disponible en la raíz del repositorio: `coleccion_postman_convocatorias.json`.

---

## Autor

Desarrollado por Jimmy Aguirre como prueba técnica para el cargo de Desarrollador(a) Full Stack — Universidad Surcolombiana.
