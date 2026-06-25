
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

------

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
## Requisitos previos

- **Java 21** (JDK)
- **Maven** (o usar el wrapper incluido en el proyecto)
- **SQL Server 2022** (Express o superior), con autenticación SQL habilitada (modo mixto)
- **Node.js 22.x LTS** y **npm**
- **Angular CLI 22**: `npm install -g @angular/cli
