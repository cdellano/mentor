-- =====================================================
-- DATOS DE PRUEBA PARA HSGH (Hospital System General Help)
-- =====================================================

-- -----------------------------------------------------
-- 1. TABLAS DE CATÁLOGOS/TIPOS (sin dependencias)
-- -----------------------------------------------------

-- TipoDispositivo
INSERT INTO tipodispositivo (nombretipo, borrado) VALUES
('Computadora de Escritorio', false),
('Laptop', false),
('Impresora', false),
('Escáner', false),
('Monitor', false),
('Proyector', false),
('Servidor', false),
('Router', false),
('Switch', false),
('Tablet', false);

-- TipoEstadoDisp (Estados de dispositivos)
INSERT INTO tipos_estado_dispositivo (nombreestado, descripcion, activo, borrado) VALUES
('OPERATIVO', 'Dispositivo funcionando correctamente', true, false),
('EN_REPARACION', 'Dispositivo en proceso de reparación', true, false),
('DAÑADO', 'Dispositivo con daños que requieren atención', true, false),
('EN_MANTENIMIENTO', 'Dispositivo en mantenimiento preventivo', true, false),
('OBSOLETO', 'Dispositivo fuera de uso por obsolescencia', true, false),
('BAJA', 'Dispositivo dado de baja definitivamente', true, false),
('EN_ALMACEN', 'Dispositivo almacenado sin asignar', true, false),
('PRESTADO', 'Dispositivo prestado temporalmente', true, false);

-- TipoMantenimiento
INSERT INTO tipos_mantenimiento (nombre_tipo_mantenimiento, borrado) VALUES
('Preventivo', false),
('Correctivo', false),
('Predictivo', false),
('Limpieza General', false),
('Actualización de Software', false),
('Reemplazo de Componentes', false),
('Diagnóstico', false);

-- TipoPrioridadTicket
INSERT INTO tipos_prioridad_ticket (nombre_prioridad, descripcion, borrado) VALUES
('BAJA', 'Solicitud de baja prioridad, puede esperar', false),
('MEDIA', 'Solicitud de prioridad normal', false),
('ALTA', 'Solicitud urgente que requiere atención pronta', false),
('CRITICA', 'Solicitud crítica que afecta operaciones esenciales', false);

-- EstadoTicket
INSERT INTO estados_ticket (nombre, descripcion, color, activo, orden, borrado) VALUES
('ABIERTO', 'Ticket recién creado', '#3498db', true, 1, false),
('EN_PROCESO', 'Ticket siendo atendido', '#f39c12', true, 2, false),
('EN_ESPERA', 'Ticket en espera de información o recursos', '#9b59b6', true, 3, false),
('RESUELTO', 'Ticket resuelto satisfactoriamente', '#27ae60', true, 4, false),
('CERRADO', 'Ticket cerrado definitivamente', '#7f8c8d', true, 5, false),
('CANCELADO', 'Ticket cancelado', '#e74c3c', true, 6, false);

-- TipoToner
INSERT INTO tipotoner (nombretipotoner, borrado) VALUES
('HP 85A Negro', false),
('HP 83A Negro', false),
('HP 12A Negro', false),
('HP 36A Negro', false),
('HP 78A Negro', false),
('Canon 128 Negro', false),
('Brother TN-660 Negro', false),
('Samsung MLT-D101S Negro', false),
('Xerox 106R02773 Negro', false),
('Epson C13S050614 Negro', false);

-- -----------------------------------------------------
-- 2. DEPARTAMENTOS
-- -----------------------------------------------------

INSERT INTO departamentos (nombredepartamento, codigo, descripcion, activo, fecharegistro, borrado) VALUES
('Tecnologías de la Información', 'TI-001', 'Departamento de soporte técnico y desarrollo', true, NOW(), false),
('Recursos Humanos', 'RH-001', 'Gestión del personal y nóminas', true, NOW(), false),
('Contabilidad', 'CONT-001', 'Manejo de finanzas y contabilidad', true, NOW(), false),
('Administración', 'ADM-001', 'Administración general del hospital', true, NOW(), false),
('Urgencias', 'URG-001', 'Área de atención de emergencias', true, NOW(), false),
('Medicina General', 'MED-001', 'Consultas de medicina general', true, NOW(), false),
('Laboratorio Clínico', 'LAB-001', 'Análisis clínicos y laboratorio', true, NOW(), false),
('Radiología', 'RAD-001', 'Estudios de imagen y radiografías', true, NOW(), false),
('Farmacia', 'FARM-001', 'Dispensación de medicamentos', true, NOW(), false),
('Archivo Clínico', 'ARCH-001', 'Gestión de expedientes médicos', true, NOW(), false);

-- -----------------------------------------------------
-- 3. LUGARES (dependen de departamentos)
-- -----------------------------------------------------

INSERT INTO lugares (nombrelugar, iddepartamento, piso, edificio, descripcion, fecharegistro, borrado) VALUES
('Oficina Principal TI', 1, '2', 'Edificio Administrativo', 'Oficina principal del departamento de TI', NOW(), false),
('Sala de Servidores', 1, '1', 'Edificio Administrativo', 'Centro de datos principal', NOW(), false),
('Recepción RH', 2, '2', 'Edificio Administrativo', 'Área de atención a empleados', NOW(), false),
('Oficina Contabilidad', 3, '3', 'Edificio Administrativo', 'Oficinas de contabilidad general', NOW(), false),
('Dirección General', 4, '4', 'Edificio Administrativo', 'Oficina del director general', NOW(), false),
('Sala de Urgencias A', 5, '1', 'Edificio Hospital', 'Sala principal de urgencias', NOW(), false),
('Consultorio 101', 6, '1', 'Edificio Hospital', 'Consultorio de medicina general', NOW(), false),
('Consultorio 102', 6, '1', 'Edificio Hospital', 'Consultorio de medicina general', NOW(), false),
('Laboratorio Central', 7, 'Planta Baja', 'Edificio Hospital', 'Laboratorio de análisis clínicos', NOW(), false),
('Sala de Rayos X', 8, 'Planta Baja', 'Edificio Hospital', 'Área de radiología', NOW(), false),
('Farmacia Principal', 9, 'Planta Baja', 'Edificio Hospital', 'Farmacia de despacho', NOW(), false),
('Archivo Central', 10, 'Sótano', 'Edificio Administrativo', 'Almacén de expedientes', NOW(), false);

-- -----------------------------------------------------
-- 4. USUARIOS
-- -----------------------------------------------------

INSERT INTO usuarios (nombre, apellido, usuariologin, contrasenahash, sub, email, estado, fecharegistro, rol, borrado) VALUES
('Juan Carlos', 'Pérez López', 'jperez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'jperez@hospital.com', 'ACTIVO', NOW(), 'ROLE_ADMIN', false),
('María Elena', 'García Hernández', 'mgarcia', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'mgarcia@hospital.com', 'ACTIVO', NOW(), 'ROLE_TECNICO', false),
('Roberto', 'Martínez Sánchez', 'rmartinez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'rmartinez@hospital.com', 'ACTIVO', NOW(), 'ROLE_TECNICO', false),
('Ana Laura', 'Rodríguez Cruz', 'arodriguez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'arodriguez@hospital.com', 'ACTIVO', NOW(), 'ROLE_USER', false),
('Carlos Alberto', 'Fernández Ruiz', 'cfernandez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'cfernandez@hospital.com', 'ACTIVO', NOW(), 'ROLE_USER', false),
('Patricia', 'López Mendoza', 'plopez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'plopez@hospital.com', 'ACTIVO', NOW(), 'ROLE_USER', false),
('Miguel Ángel', 'Ramírez Torres', 'mramirez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'mramirez@hospital.com', 'ACTIVO', NOW(), 'ROLE_SUPERVISOR', false),
('Gabriela', 'Sánchez Morales', 'gsanchez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'gsanchez@hospital.com', 'ACTIVO', NOW(), 'ROLE_USER', false),
('Luis Fernando', 'Díaz Castillo', 'ldiaz', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'ldiaz@hospital.com', 'INACTIVO', NOW(), 'ROLE_USER', false),
('Sofía', 'Vargas Jiménez', 'svargas', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/h9bSg.4V6qZSU9p7c3rqG', NULL, 'svargas@hospital.com', 'ACTIVO', NOW(), 'ROLE_ADMIN', false);

-- -----------------------------------------------------
-- 5. DISPOSITIVOS (dependen de TipoDispositivo y TipoEstadoDisp)
-- -----------------------------------------------------

INSERT INTO dispositivos (idtipodispositivo, marca, modelo, numeroserie, inventario, idtipoestado, fechacompra, costo, notas, fecharegistro, fechabaja, borrado) VALUES
(1, 'Dell', 'OptiPlex 7090', 'DELL-2023-001', 'INV-PC-001', 1, '2023-01-15', 15000.00, 'Computadora para recepción TI', NOW(), NULL, false),
(1, 'HP', 'ProDesk 400 G7', 'HP-2023-002', 'INV-PC-002', 1, '2023-02-20', 12500.00, 'Computadora para contabilidad', NOW(), NULL, false),
(1, 'Lenovo', 'ThinkCentre M920', 'LEN-2023-003', 'INV-PC-003', 1, '2023-03-10', 14000.00, 'Computadora para RH', NOW(), NULL, false),
(2, 'Dell', 'Latitude 5520', 'DELL-LAP-001', 'INV-LAP-001', 1, '2023-01-20', 22000.00, 'Laptop para trabajo remoto', NOW(), NULL, false),
(2, 'HP', 'EliteBook 840 G8', 'HP-LAP-002', 'INV-LAP-002', 2, '2022-11-15', 25000.00, 'Laptop en reparación - pantalla', NOW(), NULL, false),
(2, 'Lenovo', 'ThinkPad X1 Carbon', 'LEN-LAP-003', 'INV-LAP-003', 1, '2023-04-05', 35000.00, 'Laptop para dirección', NOW(), NULL, false),
(3, 'HP', 'LaserJet Pro M404dn', 'HP-IMP-001', 'INV-IMP-001', 1, '2022-06-10', 6500.00, 'Impresora para oficina TI', NOW(), NULL, false),
(3, 'Canon', 'imageCLASS MF445dw', 'CAN-IMP-002', 'INV-IMP-002', 1, '2022-08-22', 8500.00, 'Impresora multifuncional para RH', NOW(), NULL, false),
(3, 'Brother', 'HL-L2370DW', 'BRO-IMP-003', 'INV-IMP-003', 4, '2021-12-01', 4500.00, 'Impresora en mantenimiento', NOW(), NULL, false),
(3, 'Xerox', 'B210', 'XER-IMP-004', 'INV-IMP-004', 3, '2020-05-15', 3800.00, 'Impresora dañada - requiere servicio', NOW(), NULL, false),
(4, 'Epson', 'WorkForce ES-500W', 'EPS-ESC-001', 'INV-ESC-001', 1, '2023-02-28', 7200.00, 'Escáner para archivo', NOW(), NULL, false),
(4, 'Canon', 'imageFORMULA DR-C225', 'CAN-ESC-002', 'INV-ESC-002', 1, '2022-09-14', 9500.00, 'Escáner de alta velocidad', NOW(), NULL, false),
(5, 'Dell', 'P2422H', 'DELL-MON-001', 'INV-MON-001', 1, '2023-01-15', 4500.00, 'Monitor 24 pulgadas', NOW(), NULL, false),
(5, 'LG', '27UK650-W', 'LG-MON-002', 'INV-MON-002', 1, '2023-03-20', 8000.00, 'Monitor 4K para diseño', NOW(), NULL, false),
(5, 'Samsung', 'S24R350', 'SAM-MON-003', 'INV-MON-003', 5, '2019-04-10', 3200.00, 'Monitor obsoleto', NOW(), '2024-01-15', false),
(6, 'Epson', 'PowerLite X49', 'EPS-PRO-001', 'INV-PRO-001', 1, '2022-07-18', 12000.00, 'Proyector para sala de juntas', NOW(), NULL, false),
(6, 'BenQ', 'MH733', 'BEN-PRO-002', 'INV-PRO-002', 7, '2021-11-25', 15000.00, 'Proyector en almacén', NOW(), NULL, false),
(7, 'Dell', 'PowerEdge R740', 'DELL-SRV-001', 'INV-SRV-001', 1, '2022-01-10', 85000.00, 'Servidor principal', NOW(), NULL, false),
(7, 'HP', 'ProLiant DL380 Gen10', 'HP-SRV-002', 'INV-SRV-002', 1, '2022-01-10', 92000.00, 'Servidor de respaldo', NOW(), NULL, false),
(8, 'Cisco', 'RV340', 'CIS-ROU-001', 'INV-ROU-001', 1, '2022-03-05', 8500.00, 'Router principal', NOW(), NULL, false);

-- -----------------------------------------------------
-- 6. HISTORIAL DE UBICACIÓN (depende de dispositivos, lugares y usuarios)
-- -----------------------------------------------------

INSERT INTO historialubicacion (iddispositivo, idlugar, idusuario, fechaentrada, fechasalida, borrado) VALUES
(1, 1, 1, '2023-01-16 09:00:00', NULL, false),
(2, 4, 1, '2023-02-21 10:30:00', NULL, false),
(3, 3, 2, '2023-03-11 08:00:00', NULL, false),
(4, 1, 1, '2023-01-21 09:00:00', '2023-06-15 17:00:00', false),
(4, 5, 1, '2023-06-16 09:00:00', NULL, false),
(5, 1, 2, '2022-11-16 08:00:00', NULL, false),
(6, 5, 1, '2023-04-06 09:00:00', NULL, false),
(7, 1, 2, '2022-06-11 08:00:00', NULL, false),
(8, 3, 2, '2022-08-23 09:00:00', NULL, false),
(9, 11, 3, '2021-12-02 08:00:00', NULL, false),
(10, 4, 2, '2020-05-16 08:00:00', NULL, false),
(11, 12, 3, '2023-03-01 08:00:00', NULL, false),
(12, 12, 3, '2022-09-15 08:00:00', NULL, false),
(13, 1, 1, '2023-01-16 09:00:00', NULL, false),
(14, 1, 1, '2023-03-21 09:00:00', NULL, false),
(16, 5, 1, '2022-07-19 08:00:00', NULL, false),
(18, 2, 1, '2022-01-11 08:00:00', NULL, false),
(19, 2, 1, '2022-01-11 08:00:00', NULL, false),
(20, 2, 1, '2022-03-06 08:00:00', NULL, false);

-- -----------------------------------------------------
-- 7. MANTENIMIENTOS (depende de dispositivos, usuarios y tipoMantenimiento)
-- -----------------------------------------------------

INSERT INTO mantenimientos (iddispositivo, idusuariosolicita, idusuarioatiende, idtipomantenimiento, descripcion, fechaprogramada, fecharealizado, estado, costo, notas, fecharegistro, pila_cmos, pasta_cpu, limpieza, borrado) VALUES
(1, 4, 2, 1, 'Mantenimiento preventivo trimestral', '2023-04-15', '2023-04-15', 'COMPLETADO', 500.00, 'Se realizó limpieza general y actualización de sistema', NOW(), false, false, true, false),
(2, 5, 2, 1, 'Mantenimiento preventivo trimestral', '2023-05-10', '2023-05-10', 'COMPLETADO', 500.00, 'Limpieza interna y externa', NOW(), false, false, true, false),
(3, 4, 3, 4, 'Limpieza general del equipo', '2023-06-20', '2023-06-20', 'COMPLETADO', 350.00, 'Limpieza de ventiladores y cambio de pasta térmica', NOW(), false, true, true, false),
(5, 6, 2, 2, 'Reparación de pantalla', '2023-07-01', NULL, 'EN_PROCESO', 3500.00, 'Pantalla dañada, se solicitó repuesto', NOW(), false, false, false, false),
(7, 7, 3, 1, 'Mantenimiento preventivo de impresora', '2023-08-15', '2023-08-15', 'COMPLETADO', 800.00, 'Limpieza de cabezales y rodillos', NOW(), false, false, true, false),
(9, 4, 2, 6, 'Reemplazo de fusor', '2023-09-01', NULL, 'PROGRAMADO', 1200.00, 'El fusor presenta desgaste', NOW(), false, false, false, false),
(10, 5, 3, 2, 'Diagnóstico de falla', '2023-09-10', '2023-09-10', 'COMPLETADO', 0.00, 'Falla en la placa principal, se recomienda baja', NOW(), false, false, false, false),
(18, 1, 2, 1, 'Mantenimiento preventivo de servidor', '2023-10-01', NULL, 'PROGRAMADO', 2500.00, 'Revisión general y actualización de firmware', NOW(), false, true, true, false),
(19, 1, 2, 5, 'Actualización de sistema operativo', '2023-10-15', NULL, 'PROGRAMADO', 0.00, 'Actualización a última versión de Windows Server', NOW(), false, false, false, false),
(4, 8, 3, 1, 'Mantenimiento preventivo', '2023-11-01', NULL, 'PROGRAMADO', 600.00, 'Revisión general de laptop', NOW(), true, true, true, false),
-- Registros adicionales de mantenimientos
(1, 5, 2, 1, 'Mantenimiento preventivo semestral', '2023-07-20', '2023-07-20', 'COMPLETADO', 550.00, 'Limpieza completa y revisión de componentes', NOW(), true, true, true, false),
(2, 6, 3, 4, 'Limpieza profunda de equipo', '2023-08-05', '2023-08-05', 'COMPLETADO', 400.00, 'Limpieza de ventiladores y filtros', NOW(), false, false, true, false),
(3, 7, 2, 5, 'Actualización de drivers', '2023-08-25', '2023-08-25', 'COMPLETADO', 150.00, 'Actualización de controladores de red y video', NOW(), false, false, false, false),
(4, 4, 3, 1, 'Revisión de batería', '2023-09-12', '2023-09-12', 'COMPLETADO', 200.00, 'Diagnóstico de batería - capacidad al 75%', NOW(), false, false, false, false),
(6, 5, 2, 2, 'Reparación de bisagra', '2023-09-20', '2023-09-22', 'COMPLETADO', 850.00, 'Reemplazo de bisagra izquierda dañada', NOW(), false, false, false, false),
(7, 8, 3, 6, 'Cambio de tóner', '2023-10-05', '2023-10-05', 'COMPLETADO', 450.00, 'Reemplazo de cartucho de tóner', NOW(), false, false, false, false),
(8, 4, 2, 1, 'Mantenimiento preventivo', '2023-10-10', '2023-10-10', 'COMPLETADO', 650.00, 'Limpieza y calibración de impresora', NOW(), false, false, true, false),
(11, 6, 3, 7, 'Diagnóstico de escáner', '2023-10-18', '2023-10-18', 'COMPLETADO', 0.00, 'Revisión de funcionamiento - todo en orden', NOW(), false, false, false, false),
(12, 7, 2, 4, 'Limpieza de lentes', '2023-10-25', '2023-10-25', 'COMPLETADO', 300.00, 'Limpieza de cristal y lentes del escáner', NOW(), false, false, true, false),
(13, 5, 3, 5, 'Actualización de firmware', '2023-11-02', '2023-11-02', 'COMPLETADO', 0.00, 'Actualización de firmware del monitor', NOW(), false, false, false, false),
(14, 4, 2, 2, 'Calibración de colores', '2023-11-08', '2023-11-08', 'COMPLETADO', 200.00, 'Calibración profesional de pantalla', NOW(), false, false, false, false),
(16, 8, 3, 1, 'Mantenimiento de proyector', '2023-11-15', '2023-11-15', 'COMPLETADO', 750.00, 'Limpieza de filtros y revisión de lámpara', NOW(), false, false, true, false),
(18, 6, 2, 3, 'Mantenimiento predictivo', '2023-11-22', '2023-11-22', 'COMPLETADO', 1800.00, 'Análisis de discos y memoria del servidor', NOW(), false, false, false, false),
(19, 7, 3, 6, 'Reemplazo de disco', '2023-11-28', '2023-11-30', 'COMPLETADO', 4500.00, 'Reemplazo de disco duro por falla predictiva', NOW(), false, false, false, false),
(20, 5, 2, 1, 'Mantenimiento preventivo de router', '2023-12-05', '2023-12-05', 'COMPLETADO', 350.00, 'Revisión de configuración y limpieza', NOW(), false, false, true, false),
(1, 4, 3, 4, 'Limpieza trimestral', '2023-12-10', '2023-12-10', 'COMPLETADO', 400.00, 'Limpieza interna completa', NOW(), false, true, true, false),
(2, 8, 2, 5, 'Actualización de antivirus', '2023-12-12', '2023-12-12', 'COMPLETADO', 0.00, 'Actualización de definiciones de virus', NOW(), false, false, false, false),
(3, 6, 3, 1, 'Revisión anual', '2023-12-18', '2023-12-18', 'COMPLETADO', 600.00, 'Mantenimiento preventivo completo', NOW(), true, true, true, false),
(4, 7, 2, 2, 'Reparación de puerto USB', '2023-12-20', '2023-12-21', 'COMPLETADO', 350.00, 'Soldadura de puerto USB dañado', NOW(), false, false, false, false),
(6, 5, 3, 4, 'Limpieza de teclado', '2024-01-05', '2024-01-05', 'COMPLETADO', 150.00, 'Limpieza profunda de teclado', NOW(), false, false, true, false),
(7, 4, 2, 6, 'Cambio de rodillos', '2024-01-10', '2024-01-10', 'COMPLETADO', 550.00, 'Reemplazo de rodillos de alimentación', NOW(), false, false, false, false),
(8, 8, 3, 1, 'Mantenimiento preventivo', '2024-01-15', '2024-01-15', 'COMPLETADO', 700.00, 'Revisión general de impresora multifuncional', NOW(), false, false, true, false),
(9, 6, 2, 2, 'Reparación de fusor', '2024-01-22', '2024-01-25', 'COMPLETADO', 1100.00, 'Reemplazo de unidad de fusión', NOW(), false, false, false, false),
(11, 7, 3, 4, 'Limpieza de sensor', '2024-02-01', '2024-02-01', 'COMPLETADO', 250.00, 'Limpieza de sensores de digitalización', NOW(), false, false, true, false),
(12, 5, 2, 5, 'Actualización de software', '2024-02-08', '2024-02-08', 'COMPLETADO', 0.00, 'Actualización de drivers del escáner', NOW(), false, false, false, false),
(13, 4, 3, 1, 'Revisión de conectores', '2024-02-15', '2024-02-15', 'COMPLETADO', 100.00, 'Limpieza de conectores HDMI y DisplayPort', NOW(), false, false, true, false),
(16, 8, 2, 6, 'Cambio de lámpara', '2024-02-20', '2024-02-22', 'COMPLETADO', 2500.00, 'Reemplazo de lámpara del proyector', NOW(), false, false, false, false),
(18, 6, 3, 3, 'Análisis de rendimiento', '2024-03-01', '2024-03-01', 'COMPLETADO', 1500.00, 'Análisis predictivo de componentes', NOW(), false, false, false, false),
(19, 7, 2, 5, 'Actualización de BIOS', '2024-03-08', '2024-03-08', 'COMPLETADO', 0.00, 'Actualización de BIOS del servidor', NOW(), false, false, false, false),
(20, 5, 3, 1, 'Mantenimiento trimestral', '2024-03-15', '2024-03-15', 'COMPLETADO', 400.00, 'Revisión de configuración de red', NOW(), false, false, true, false),
(1, 4, 2, 4, 'Limpieza de ventiladores', '2024-03-22', '2024-03-22', 'COMPLETADO', 350.00, 'Limpieza y lubricación de ventiladores', NOW(), false, true, true, false),
(2, 8, 3, 2, 'Reparación de fuente', '2024-03-28', '2024-03-30', 'COMPLETADO', 800.00, 'Reemplazo de fuente de poder', NOW(), false, false, false, false),
(3, 6, 2, 1, 'Mantenimiento preventivo', '2024-04-05', '2024-04-05', 'COMPLETADO', 500.00, 'Revisión completa del equipo', NOW(), true, true, true, false),
(4, 7, 3, 5, 'Actualización de Windows', '2024-04-12', '2024-04-12', 'COMPLETADO', 0.00, 'Actualización de sistema operativo', NOW(), false, false, false, false),
(6, 5, 2, 4, 'Limpieza externa', '2024-04-18', '2024-04-18', 'COMPLETADO', 200.00, 'Limpieza y desinfección del equipo', NOW(), false, false, true, false),
(7, 4, 3, 1, 'Revisión de cabezales', '2024-04-25', '2024-04-25', 'COMPLETADO', 650.00, 'Alineación y limpieza de cabezales', NOW(), false, false, true, false),
(8, 8, 2, 6, 'Cambio de cartucho', '2024-05-02', '2024-05-02', 'COMPLETADO', 480.00, 'Reemplazo de cartucho de tóner', NOW(), false, false, false, false),
(18, 6, 3, 1, 'Mantenimiento semestral', '2024-05-10', '2024-05-10', 'COMPLETADO', 3000.00, 'Mantenimiento preventivo completo de servidor', NOW(), false, true, true, false),
(19, 7, 2, 3, 'Análisis de logs', '2024-05-18', '2024-05-18', 'COMPLETADO', 500.00, 'Análisis predictivo basado en logs del sistema', NOW(), false, false, false, false);

-- -----------------------------------------------------
-- 8. TICKETS (depende de usuarios, estadoTicket, tipoPrioridadTicket y departamentos)
-- -----------------------------------------------------

INSERT INTO tickets (idusuariocreador, idusuarioasignado, iddepartamento, asunto, descripcion, idestado, id_prioridad, fechacreacion, fechacierre, borrado) VALUES
(4, 2, 1, 'Computadora lenta', 'Mi computadora tarda mucho en iniciar y las aplicaciones se cierran solas', 1, 2, NOW() - INTERVAL '5 days', NULL, false),
(5, 2, 2, 'No puedo imprimir', 'La impresora no responde cuando envío documentos a imprimir', 2, 3, NOW() - INTERVAL '3 days', NULL, false),
(6, 3, 3, 'Monitor con líneas', 'El monitor muestra líneas horizontales intermitentes', 1, 2, NOW() - INTERVAL '2 days', NULL, false),
(4, 2, 1, 'Instalación de software', 'Requiero instalación de Microsoft Office en mi equipo', 4, 1, NOW() - INTERVAL '10 days', NOW() - INTERVAL '8 days', false),
(7, 3, 4, 'Problema con red', 'No tengo conexión a internet desde ayer', 2, 4, NOW() - INTERVAL '1 day', NULL, false),
(8, 2, 5, 'Teclado no funciona', 'Algunas teclas del teclado no responden', 3, 2, NOW() - INTERVAL '4 days', NULL, false),
(5, 3, 2, 'Configuración de correo', 'Necesito configurar mi cuenta de correo institucional', 4, 1, NOW() - INTERVAL '15 days', NOW() - INTERVAL '14 days', false),
(6, 2, 3, 'Virus en computadora', 'Aparecen ventanas emergentes constantemente', 2, 4, NOW() - INTERVAL '1 day', NULL, false),
(4, NULL, 1, 'Solicitud de equipo nuevo', 'Requiero una computadora nueva para el área de contabilidad', 1, 2, NOW(), NULL, false),
(7, 3, 4, 'Escáner no funciona', 'El escáner no es reconocido por la computadora', 5, 2, NOW() - INTERVAL '20 days', NOW() - INTERVAL '18 days', false),
(8, 2, 5, 'Actualización de Windows', 'El sistema solicita actualización pero no tengo permisos', 6, 1, NOW() - INTERVAL '7 days', NOW() - INTERVAL '6 days', false),
(5, 3, 2, 'Problema con proyector', 'El proyector de la sala de juntas no enciende', 4, 3, NOW() - INTERVAL '12 days', NOW() - INTERVAL '11 days', false);

-- -----------------------------------------------------
-- 9. ENTRADAS DE TONER (depende de usuarios y tipoToner)
-- -----------------------------------------------------

INSERT INTO entradas_toner (usuarioentrada, fechaentrada, tipotoner, cantidad, observaciones, borrado) VALUES
(1, NOW() - INTERVAL '60 days', 1, 20, 'Compra inicial de tóners HP 85A', false),
(1, NOW() - INTERVAL '60 days', 2, 15, 'Compra inicial de tóners HP 83A', false),
(1, NOW() - INTERVAL '60 days', 3, 10, 'Compra inicial de tóners HP 12A', false),
(1, NOW() - INTERVAL '45 days', 6, 8, 'Compra de tóners Canon 128', false),
(1, NOW() - INTERVAL '45 days', 7, 12, 'Compra de tóners Brother TN-660', false),
(2, NOW() - INTERVAL '30 days', 1, 15, 'Reposición de tóners HP 85A', false),
(2, NOW() - INTERVAL '30 days', 2, 10, 'Reposición de tóners HP 83A', false),
(1, NOW() - INTERVAL '15 days', 4, 8, 'Compra de tóners HP 36A', false),
(1, NOW() - INTERVAL '15 days', 5, 6, 'Compra de tóners HP 78A', false),
(2, NOW() - INTERVAL '7 days', 8, 5, 'Compra de tóners Samsung', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(1, NOW() - INTERVAL '3 days', 1, 10, 'Compra adicional HP 85A', false),
(2, NOW() - INTERVAL '1 day', 9, 4, 'Compra de tóners Xerox', false);

-- -----------------------------------------------------
-- 10. SALIDAS DE TONER (depende de usuarios, tipoToner y lugares)
-- -----------------------------------------------------

INSERT INTO salidas_toner (usuarioinstala, usuariorecibe, tipotoner, fechasalida, cantidad, departamento, observaciones, borrado) VALUES
(2, 4, 1, NOW() - INTERVAL '55 days', 2, 1, 'Instalación en impresora de TI', false),
(2, 5, 2, NOW() - INTERVAL '50 days', 1, 4, 'Instalación en impresora de Contabilidad', false),
(3, 6, 1, NOW() - INTERVAL '45 days', 2, 3, 'Instalación en impresora de RH', false),
(2, 4, 6, NOW() - INTERVAL '40 days', 1, 8, 'Instalación en impresora Canon de Radiología', false),
(3, 7, 7, NOW() - INTERVAL '35 days', 2, 9, 'Instalación en impresora Brother de Laboratorio', false),
(2, 5, 1, NOW() - INTERVAL '30 days', 1, 4, 'Reemplazo de tóner en Contabilidad', false),
(3, 8, 2, NOW() - INTERVAL '25 days', 1, 6, 'Instalación en Urgencias', false),
(2, 4, 3, NOW() - INTERVAL '20 days', 2, 7, 'Instalación en consultorios', false),
(3, 6, 1, NOW() - INTERVAL '15 days', 1, 3, 'Reemplazo en RH', false),
(2, 7, 4, NOW() - INTERVAL '10 days', 1, 9, 'Instalación en Laboratorio', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(3, 5, 1, NOW() - INTERVAL '5 days', 2, 11, 'Instalación en Farmacia', false),
(2, 8, 5, NOW() - INTERVAL '2 days', 1, 12, 'Instalación en Archivo', false);

-- -----------------------------------------------------
-- 11. ANOTACIONES (depende de usuarios)
-- -----------------------------------------------------

INSERT INTO anotaciones (idusuario, titulo, contenido, pagina, fechaanotacion, etiquetas, importante, borrado) VALUES
(1, 'Configuración de red principal', 'Los switches principales están configurados con VLANs separadas para cada departamento. VLAN 10: Administración, VLAN 20: Hospital, VLAN 30: Servidores.', 1, NOW() - INTERVAL '30 days', 'red,configuración,vlans', true, false),
(2, 'Procedimiento de backup', 'El backup se realiza diariamente a las 2:00 AM. Los respaldos se almacenan en el servidor secundario y en la nube. Retención: 30 días local, 90 días en nube.', 2, NOW() - INTERVAL '25 days', 'backup,procedimiento,servidor', true, false),
(3, 'Inventario de licencias', 'Licencias de Windows Server: 5, Office 365: 150, Antivirus: 200. Renovación en diciembre.', 1, NOW() - INTERVAL '20 days', 'licencias,inventario', false, false),
(1, 'Contactos de proveedores', 'Dell: 800-123-4567 (Juan Pérez), HP: 800-234-5678 (María López), Canon: 800-345-6789 (Roberto García)', 3, NOW() - INTERVAL '15 days', 'proveedores,contactos', false, false),
(2, 'Problema recurrente impresora HP', 'La impresora HP LaserJet del área de RH presenta atascos frecuentes. Se recomienda revisar los rodillos de alimentación en el próximo mantenimiento.', 1, NOW() - INTERVAL '10 days', 'impresora,problema,hp', true, false),
(3, 'Actualización de antivirus', 'Se actualizó el antivirus en todos los equipos. Nueva versión: 2024.1.0. No se reportaron problemas de compatibilidad.', 2, NOW() - INTERVAL '7 days', 'antivirus,actualización', false, false),
(1, 'Configuración VPN', 'La VPN está configurada para acceso remoto. Puerto: 1194, Protocolo: OpenVPN. Los usuarios deben solicitar certificado al área de TI.', 1, NOW() - INTERVAL '5 days', 'vpn,configuración,remoto', true, false),
(2, 'Mantenimiento de servidores', 'Se programó mantenimiento para el primer fin de semana del mes. Incluye: actualización de firmware, limpieza física y revisión de discos.', 3, NOW() - INTERVAL '3 days', 'servidores,mantenimiento', true, false),
(3, 'Política de contraseñas', 'Las contraseñas deben tener mínimo 12 caracteres, incluir mayúsculas, minúsculas, números y símbolos. Cambio obligatorio cada 90 días.', 1, NOW() - INTERVAL '1 day', 'seguridad,contraseñas,política', false, false),
(1, 'Incidencia de red resuelta', 'El problema de conectividad en el edificio del hospital fue causado por un cable dañado en el piso 2. Se reemplazó y se verificó la conexión.', 2, NOW(), 'red,incidencia,resuelto', false, false);

-- -----------------------------------------------------
-- 12. TIPO INCIDENTE (catálogo para bitácora de servicios)
-- -----------------------------------------------------

INSERT INTO tipo_incidente (nombre, descripcion, borrado) VALUES
('Falla de Hardware', 'Problemas relacionados con componentes físicos del sistema', false),
('Error de Software', 'Errores en aplicaciones o sistemas operativos', false),
('Corte de Red', 'Interrupción en la conectividad de red', false),
('Corte de Energía', 'Interrupción del suministro eléctrico', false),
('Mantenimiento Programado', 'Mantenimiento preventivo planificado', false),
('Ataque de Seguridad', 'Incidente de ciberseguridad detectado', false),
('Sobrecarga de Sistema', 'Alto consumo de recursos del sistema', false);

-- -----------------------------------------------------
-- 13. SERVICIOS (catálogo de servicios informáticos)
-- -----------------------------------------------------

INSERT INTO servicios (nombre, descripcion, activo, created_at, updated_at, borrado) VALUES
('Correo Electrónico', 'Servicio de correo institucional Microsoft 365', true, NOW() - INTERVAL '365 days', NOW(), false),
('Red Interna', 'Conectividad de red local LAN/WLAN', true, NOW() - INTERVAL '365 days', NOW(), false),
('Sistema ERP', 'Sistema de planificación de recursos empresariales', true, NOW() - INTERVAL '300 days', NOW(), false),
('Servidor de Archivos', 'Almacenamiento compartido de documentos', true, NOW() - INTERVAL '365 days', NOW(), false),
('VPN Corporativa', 'Acceso remoto seguro a la red interna', true, NOW() - INTERVAL '200 days', NOW(), false),
('Sistema de Tickets', 'Mesa de ayuda y gestión de incidentes', true, NOW() - INTERVAL '180 days', NOW(), false),
('Antivirus Centralizado', 'Protección contra malware en endpoints', true, NOW() - INTERVAL '365 days', NOW(), false),
('Backup Corporativo', 'Respaldo automatizado de información crítica', true, NOW() - INTERVAL '365 days', NOW(), false),
('Telefonía IP', 'Sistema de comunicación telefónica VoIP', true, NOW() - INTERVAL '250 days', NOW(), false),
('Portal Intranet', 'Portal web interno de la organización', false, NOW() - INTERVAL '150 days', NOW() - INTERVAL '30 days', false);

-- -----------------------------------------------------
-- 14. BITACORA SERVICIOS (registro de incidentes y estados)
-- -----------------------------------------------------

INSERT INTO bitacora_servicios (servicio_id, tipo_incidente_id, estado, comentario, fecha_inicio, fecha_fin, reportado_por, created_at, updated_at, borrado) VALUES
-- Correo Electrónico (servicio_id = 1)
(1, 3, 'CAIDO', 'Corte total del servicio de correo por falla en proveedor cloud', NOW() - INTERVAL '60 days', NOW() - INTERVAL '60 days' + INTERVAL '4 hours', 1, NOW() - INTERVAL '60 days', NOW() - INTERVAL '60 days' + INTERVAL '4 hours', false),
(1, 5, 'MANTENIMIENTO', 'Actualización programada del servidor de correo', NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days' + INTERVAL '2 hours', 2, NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days' + INTERVAL '2 hours', false),

-- Red Interna (servicio_id = 2)
(2, 3, 'CAIDO', 'Falla en switch principal, toda la red afectada', NOW() - INTERVAL '45 days', NOW() - INTERVAL '45 days' + INTERVAL '1 hour', 1, NOW() - INTERVAL '45 days', NOW() - INTERVAL '45 days' + INTERVAL '1 hour', false),
(2, 1, 'DEGRADADO', 'Router con problemas de rendimiento, lentitud en conexiones', NOW() - INTERVAL '20 days', NOW() - INTERVAL '20 days' + INTERVAL '6 hours', 3, NOW() - INTERVAL '20 days', NOW() - INTERVAL '20 days' + INTERVAL '6 hours', false),

-- Sistema ERP (servicio_id = 3)
(3, 2, 'CAIDO', 'Error crítico en base de datos del ERP', NOW() - INTERVAL '35 days', NOW() - INTERVAL '35 days' + INTERVAL '3 hours', 2, NOW() - INTERVAL '35 days', NOW() - INTERVAL '35 days' + INTERVAL '3 hours', false),
(3, 7, 'DEGRADADO', 'Alto consumo de CPU en servidor de aplicaciones', NOW() - INTERVAL '15 days', NOW() - INTERVAL '15 days' + INTERVAL '8 hours', 1, NOW() - INTERVAL '15 days', NOW() - INTERVAL '15 days' + INTERVAL '8 hours', false),
(3, 5, 'MANTENIMIENTO', 'Actualización de versión del ERP', NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days' + INTERVAL '12 hours', 2, NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days' + INTERVAL '12 hours', false),

-- VPN Corporativa (servicio_id = 5)
(5, 6, 'CAIDO', 'Detección de intentos de intrusión, servicio suspendido temporalmente', NOW() - INTERVAL '25 days', NOW() - INTERVAL '25 days' + INTERVAL '5 hours', 1, NOW() - INTERVAL '25 days', NOW() - INTERVAL '25 days' + INTERVAL '5 hours', false),
(5, 1, 'SOPORTE_TECNICO_PROV', 'Falla en certificados SSL, esperando respuesta del proveedor', NOW() - INTERVAL '10 days', NOW() - INTERVAL '10 days' + INTERVAL '24 hours', 3, NOW() - INTERVAL '10 days', NOW() - INTERVAL '10 days' + INTERVAL '24 hours', false),

-- Servidor de Archivos (servicio_id = 4)
(4, 4, 'CAIDO', 'Corte de energía afectó el servidor de archivos', NOW() - INTERVAL '40 days', NOW() - INTERVAL '40 days' + INTERVAL '30 minutes', 1, NOW() - INTERVAL '40 days', NOW() - INTERVAL '40 days' + INTERVAL '30 minutes', false),
(4, 1, 'DEGRADADO', 'Disco duro con sectores defectuosos, rendimiento reducido', NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days' + INTERVAL '48 hours', 2, NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days' + INTERVAL '48 hours', false),

-- Backup Corporativo (servicio_id = 8)
(8, 1, 'CAIDO', 'Falla en unidad de cinta de respaldo', NOW() - INTERVAL '50 days', NOW() - INTERVAL '50 days' + INTERVAL '72 hours', 2, NOW() - INTERVAL '50 days', NOW() - INTERVAL '50 days' + INTERVAL '72 hours', false),

-- Servicios actualmente en estado anormal (sin fecha_fin = incidente activo)
(2, 5, 'MANTENIMIENTO', 'Mantenimiento de switches de distribución', NOW() - INTERVAL '2 hours', NULL, 1, NOW() - INTERVAL '2 hours', NULL, false),
(6, 2, 'DEGRADADO', 'Lentitud en la respuesta del sistema de tickets', NOW() - INTERVAL '1 day', NULL, 3, NOW() - INTERVAL '1 day', NULL, false),
(9, 1, 'SOPORTE_TECNICO_PROV', 'Problemas con gateway de telefonía, proveedor en sitio', NOW() - INTERVAL '3 hours', NULL, 1, NOW() - INTERVAL '3 hours', NULL, false);


