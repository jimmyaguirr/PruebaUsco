
   ---DATOS DE PRUEBA COMPLETOS
   -- Universidad Surcolombiana

   --- Ejecutar TODO este script de una sola vez (Execute SQL Script
   --- en DBeaver), conectado a la base bdConvocatorias, tener en cuenta los comentarios de ejecutar ciertas consultas antes de cualquier cosa

   --- IMPORTANTE: este script asume que las 5 tablas ya existen
   --- (creadas con script_creacion_bd_dbeaver.sql). Si ya corriste
   -- un seed anterior con usuarios/categorías de ejemplo y quieres
   -- partir de cero, descomenta la sección 0 de limpieza.
  


-- DELETE FROM convocatorias.postulaciones;
-- DELETE FROM convocatorias.convocatoria_categoria;
-- DELETE FROM convocatorias.convocatorias;
-- DELETE FROM convocatorias.categorias;
-- DELETE FROM convocatorias.usuarios;

--- Ejecitar cuando se ejecute todo el script de los datos para afectar las contraseñas y encriptarlas
--- UPDATE convocatorias.usuarios
--- SET password_hash = '$2a$10$cgibWAXE0n3gBmxccXVbq.mwNlE9HcxNdPIAXGKJJhhmYiKPi2z0q';

DECLARE @idAdmin1        UNIQUEIDENTIFIER = NEWID();
DECLARE @idAdmin2        UNIQUEIDENTIFIER = NEWID();
DECLARE @idDocente1      UNIQUEIDENTIFIER = NEWID();
DECLARE @idEstudiante1   UNIQUEIDENTIFIER = NEWID();
DECLARE @idEstudiante2   UNIQUEIDENTIFIER = NEWID();
DECLARE @idEstudiante3   UNIQUEIDENTIFIER = NEWID();
DECLARE @idEstudiante4   UNIQUEIDENTIFIER = NEWID();
DECLARE @idEstudiante5   UNIQUEIDENTIFIER = NEWID();
DECLARE @idEstudiante6   UNIQUEIDENTIFIER = NEWID();

INSERT INTO convocatorias.usuarios (id, identificacion, nombre, correo, password_hash, rol, estado)
VALUES
    (@idAdmin1,      '900000001', 'María Fernanda López',  'mflopez@usco.edu.co',       'temp123', 'ADMINISTRADOR', 'ACTIVO'),
    (@idAdmin2,      '900000002', 'Jorge Iván Castro',     'jicastro@usco.edu.co',      'temp123', 'ADMINISTRADOR', 'ACTIVO'),
    (@idDocente1,    '800000001', 'Claudia Patricia Ríos', 'cprios@usco.edu.co',        'temp123', 'DOCENTE',       'ACTIVO'),
    (@idEstudiante1, '2000000001','Ana Torres',            'ana.torres@usco.edu.co',    'temp123', 'ESTUDIANTE',    'ACTIVO'),
    (@idEstudiante2, '2000000002','Carlos Ramírez',        'carlos.ramirez@usco.edu.co','temp123', 'ESTUDIANTE',    'ACTIVO'),
    (@idEstudiante3, '2000000003','Laura Gómez',           'laura.gomez@usco.edu.co',   'temp123', 'ESTUDIANTE',    'ACTIVO'),
    (@idEstudiante4, '2000000004','Pedro Martínez',        'pedro.martinez@usco.edu.co','temp123', 'ESTUDIANTE',    'ACTIVO'),
    (@idEstudiante5, '2000000005','Sofía Valencia',        'sofia.valencia@usco.edu.co','temp123', 'ESTUDIANTE',    'ACTIVO'),
    (@idEstudiante6, '2000000006','Daniel Ortiz',          'daniel.ortiz@usco.edu.co',  'temp123', 'ESTUDIANTE',    'INACTIVO');


-- CATEGORÍAS 

IF NOT EXISTS (SELECT 1 FROM convocatorias.categorias WHERE nombre = 'Investigación')
    INSERT INTO convocatorias.categorias (nombre, descripcion) VALUES ('Investigación', 'Convocatorias relacionadas con proyectos de investigación');

IF NOT EXISTS (SELECT 1 FROM convocatorias.categorias WHERE nombre = 'Bienestar')
    INSERT INTO convocatorias.categorias (nombre, descripcion) VALUES ('Bienestar', 'Convocatorias de bienestar universitario');

IF NOT EXISTS (SELECT 1 FROM convocatorias.categorias WHERE nombre = 'Académica')
    INSERT INTO convocatorias.categorias (nombre, descripcion) VALUES ('Académica', 'Convocatorias académicas, monitorías y similares');

IF NOT EXISTS (SELECT 1 FROM convocatorias.categorias WHERE nombre = 'Deportiva')
    INSERT INTO convocatorias.categorias (nombre, descripcion) VALUES ('Deportiva', 'Convocatorias relacionadas con actividades deportivas');

IF NOT EXISTS (SELECT 1 FROM convocatorias.categorias WHERE nombre = 'Cultural')
    INSERT INTO convocatorias.categorias (nombre, descripcion) VALUES ('Cultural', 'Convocatorias relacionadas con actividades culturales');

DECLARE @idCatInvestigacion UNIQUEIDENTIFIER = (SELECT id FROM convocatorias.categorias WHERE nombre = 'Investigación');
DECLARE @idCatBienestar     UNIQUEIDENTIFIER = (SELECT id FROM convocatorias.categorias WHERE nombre = 'Bienestar');
DECLARE @idCatAcademica     UNIQUEIDENTIFIER = (SELECT id FROM convocatorias.categorias WHERE nombre = 'Académica');
DECLARE @idCatDeportiva     UNIQUEIDENTIFIER = (SELECT id FROM convocatorias.categorias WHERE nombre = 'Deportiva');
DECLARE @idCatCultural      UNIQUEIDENTIFIER = (SELECT id FROM convocatorias.categorias WHERE nombre = 'Cultural');


--  CONVOCATORIAS (variedad de estados y categorías)

DECLARE @idConvAmplia UNIQUEIDENTIFIER = NEWID();
INSERT INTO convocatorias.convocatorias
    (id, nombre, descripcion, fecha_inicio, fecha_fin, cupos_disponibles, estado, creado_por)
VALUES
    (@idConvAmplia, 'Monitorías Académicas 2026-1',
     'Convocatoria para apoyo en monitorías de pregrado durante el primer semestre',
     '2026-07-01', '2026-07-31', 20, 'PUBLICADA', @idAdmin1);
INSERT INTO convocatorias.convocatoria_categoria (convocatoria_id, categoria_id)
VALUES (@idConvAmplia, @idCatAcademica), (@idConvAmplia, @idCatBienestar);

--- Convocatoria PUBLICADA con cupos limitados (2), para probar tope de cupos
DECLARE @idConvLimitada UNIQUEIDENTIFIER = NEWID();
INSERT INTO convocatorias.convocatorias
    (id, nombre, descripcion, fecha_inicio, fecha_fin, cupos_disponibles, estado, creado_por)
VALUES
    (@idConvLimitada, 'Beca de Investigación Junior',
     'Convocatoria de prueba con solo 2 cupos disponibles para validar el límite',
     '2026-07-05', '2026-08-05', 2, 'PUBLICADA', @idAdmin1);
INSERT INTO convocatorias.convocatoria_categoria (convocatoria_id, categoria_id)
VALUES (@idConvLimitada, @idCatInvestigacion);

--- Convocatoria CERRADA, para probar el rechazo por cierre
DECLARE @idConvCerrada UNIQUEIDENTIFIER = NEWID();
INSERT INTO convocatorias.convocatorias
    (id, nombre, descripcion, fecha_inicio, fecha_fin, cupos_disponibles, estado, creado_por)
VALUES
    (@idConvCerrada, 'Convocatoria Cultural 2025-2 (Finalizada)',
     'Convocatoria de prueba ya cerrada, para validar el rechazo de postulación',
     '2025-11-01', '2025-11-30', 8, 'CERRADA', @idAdmin2);
INSERT INTO convocatorias.convocatoria_categoria (convocatoria_id, categoria_id)
VALUES (@idConvCerrada, @idCatCultural);

--- Convocatoria en BORRADOR, para probar el rechazo por no publicada
DECLARE @idConvBorrador UNIQUEIDENTIFIER = NEWID();
INSERT INTO convocatorias.convocatorias
    (id, nombre, descripcion, fecha_inicio, fecha_fin, cupos_disponibles, estado, creado_por)
VALUES
    (@idConvBorrador, 'Convocatoria Deportiva 2026-2 (En preparación)',
     'Convocatoria de prueba aún en borrador, para validar el rechazo de postulación',
     '2026-09-01', '2026-09-30', 10, 'BORRADOR', @idAdmin2);
INSERT INTO convocatorias.convocatoria_categoria (convocatoria_id, categoria_id)
VALUES (@idConvBorrador, @idCatDeportiva);

-- Segunda convocatoria PUBLICADA, multi-categoría, sin postulaciones aún
DECLARE @idConvNueva UNIQUEIDENTIFIER = NEWID();
INSERT INTO convocatorias.convocatorias
    (id, nombre, descripcion, fecha_inicio, fecha_fin, cupos_disponibles, estado, creado_por)
VALUES
    (@idConvNueva, 'Capacitación en Liderazgo Estudiantil',
     'Convocatoria de capacitación para representantes estudiantiles',
     '2026-08-01', '2026-08-15', 15, 'PUBLICADA', @idAdmin1);
INSERT INTO convocatorias.convocatoria_categoria (convocatoria_id, categoria_id)
VALUES (@idConvNueva, @idCatAcademica), (@idConvNueva, @idCatCultural), (@idConvNueva, @idCatBienestar);


-- POSTULACIONES (mezcla de estados: pendientes, aprobadas, rechazadas)

-- Postulaciones a la convocatoria amplia (Monitorías Académicas)
INSERT INTO convocatorias.postulaciones (usuario_id, convocatoria_id, estado, fecha_resolucion)
VALUES
    (@idEstudiante1, @idConvAmplia, 'APROBADA', SYSDATETIME()),
    (@idEstudiante2, @idConvAmplia, 'APROBADA', SYSDATETIME()),
    (@idEstudiante3, @idConvAmplia, 'PENDIENTE', NULL),
    (@idEstudiante4, @idConvAmplia, 'RECHAZADA', SYSDATETIME());

-- Postulaciones a la convocatoria limitada (2 cupos): 1 ya ocupado y aprobado
INSERT INTO convocatorias.postulaciones (usuario_id, convocatoria_id, estado, fecha_resolucion)
VALUES
    (@idEstudiante5, @idConvLimitada, 'APROBADA', SYSDATETIME());
-- Queda 1 cupo libre en @idConvLimitada para que sigas probando POST /api/postulaciones

-- Postulación pendiente en la nueva convocatoria de liderazgo
INSERT INTO convocatorias.postulaciones (usuario_id, convocatoria_id, estado)
VALUES
    (@idEstudiante1, @idConvNueva, 'PENDIENTE');


SELECT 'Admin 1 (María Fernanda López)' AS descripcion, @idAdmin1 AS id
UNION ALL SELECT 'Admin 2 (Jorge Iván Castro)', @idAdmin2
UNION ALL SELECT 'Docente 1 (Claudia Patricia Ríos)', @idDocente1
UNION ALL SELECT 'Estudiante 1 (Ana Torres) - tiene postulaciones APROBADA y PENDIENTE', @idEstudiante1
UNION ALL SELECT 'Estudiante 2 (Carlos Ramírez) - tiene postulación APROBADA', @idEstudiante2
UNION ALL SELECT 'Estudiante 3 (Laura Gómez) - tiene postulación PENDIENTE', @idEstudiante3
UNION ALL SELECT 'Estudiante 4 (Pedro Martínez) - tiene postulación RECHAZADA', @idEstudiante4
UNION ALL SELECT 'Estudiante 5 (Sofía Valencia) - ocupó 1 cupo en convocatoria limitada', @idEstudiante5
UNION ALL SELECT 'Estudiante 6 (Daniel Ortiz) - usuario INACTIVO, sin postulaciones', @idEstudiante6
UNION ALL SELECT '--- CONVOCATORIAS ---', NULL
UNION ALL SELECT 'Convocatoria AMPLIA (20 cupos, PUBLICADA) - Monitorías Académicas', @idConvAmplia
UNION ALL SELECT 'Convocatoria LIMITADA (2 cupos, PUBLICADA, 1 disponible) - Beca Investigación Junior', @idConvLimitada
UNION ALL SELECT 'Convocatoria CERRADA - Cultural 2025-2', @idConvCerrada
UNION ALL SELECT 'Convocatoria BORRADOR - Deportiva 2026-2', @idConvBorrador
UNION ALL SELECT 'Convocatoria NUEVA sin cupos ocupados (15 cupos, PUBLICADA) - Liderazgo Estudiantil', @idConvNueva
UNION ALL SELECT '--- CATEGORÍAS ---', NULL
UNION ALL SELECT 'Categoría Investigación', @idCatInvestigacion
UNION ALL SELECT 'Categoría Bienestar', @idCatBienestar
UNION ALL SELECT 'Categoría Académica', @idCatAcademica
UNION ALL SELECT 'Categoría Deportiva', @idCatDeportiva
UNION ALL SELECT 'Categoría Cultural', @idCatCultural;