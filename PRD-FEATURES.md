## Introducción
Las tecnologias que este proyecto utilizará son:
- **Backend**: Spring Boot (Java)
- **Base de Datos**: PostgreSQL
- **Control de Versiones**: Git y GitHub
- **Documentación de la API**: Swagger

## Objetivo del Producto
Desarrollar una API RESTful que permita gestionar los quehaceres diarios del departamento de TI
de un hospital, facilitando la organización, seguimiento y resolución de tareas técnicas.

## Genralidades del Proyecto
- En TODO el proyecto se usará el Soft Delete para la eliminación lógica de registros en la base de datos.
- En este proyecto no existen deletes físicos. Toda eliminación es lógica y se hace en servicios.
- Las entidades eliminadas pueden restaurarse mediante servicio dedicado. No se permiten restauraciones implícitas.
- La API seguirá los principios RESTful para garantizar una arquitectura escalable y mantenible.



## Funcionalidades Principales (Son los rest controllers con los servicios que deberian tener)

1. **Gestión de Anotaciones**
   - La entidad ya está creada dentro del paquete entity se llama Anotacion.
   - Las anotaciones estarán vinculadas a un diario físico paginado en desuso.
   - Las anotaciones no tienen estado.
   - Crear, leer, actualizar y eliminar anotaciones.
   - Asociar anotaciones a usuarios específicos del departamento de TI.
   - Filtrar anotaciones por id, fecha, etiquetas y pagina.
   - Filtrar las anotaciones haciendo LIKE en el contenido de la anotación.
   - Filtrar todas las anotaciones de un usuario específico.
   - Mostrar todas las anotaciones de una página específica del diario.
   - Mostrar todas las anotaciones que contengan una etiqueta específica.
   - Mostrar todas las anotaciones existentes en el sistema.
   
2. **Gestión de Departamentos**
   - La entidad ya está creada dentro del paquete entity se llama Departamento.
   - Crear, leer, actualizar y eliminar departamentos.
   - Filtrar departamentos por id.
   - Filtrar departamentos nombre.
   - Mostrar todos los departamentos existentes en el sistema.

3. **Gestión de lugares**
   - La entidad ya está creada dentro del paquete entity se llama Lugar.
   - Crear, leer, actualizar y eliminar lugares.
   - Asociar lugares a departamentos específicos.
   - Filtrar lugares por id.
   - Filtrar lugares por nombre.
   - Filtrar lugares por departamento asociado.
   - Mostrar todos los lugares existentes en el sistema.

4. **Gestión de tipo de dispositivos** 
   - La entidad ya está creada dentro del paquete entity se llama TipoDispositivo.
   - Crear, leer, actualizar y eliminar tipos de dispositivos.
   - Filtrar tipos de dispositivos por id.
   - Filtrar tipos de dispositivos por nombre.
   - Mostrar todos los tipos de dispositivos existentes en el sistema.

5. **Gestión de tipos de estados de dispositivos**
   - La entidad ya está creada dentro del paquete entity se llama TipoEstadoDisp.
   - Crear, leer, actualizar y eliminar tipos de estados de dispositivos.
   - Filtrar tipos de estados de dispositivos por id.
   - Filtrar tipos de estados de dispositivos por nombre.
   - Mostrar todos los tipos de estados de dispositivos existentes en el sistema.

6. **Gestión de tipos de mantenimientos**
   - La entidad ya está creada dentro del paquete entity se llama TipoMantenimiento.
   - Crear, leer, actualizar y eliminar tipos de mantenimientos.
   - Filtrar tipos de mantenimientos por id.
   - Filtrar tipos de mantenimientos por nombre.
   - Mostrar todos los tipos de mantenimientos existentes en el sistema.

7. **Gestión de tipos de prioridad de los tickets**
   - La entidad ya está creada dentro del paquete entity se llama TipoPrioridadTicket.
   - Crear, leer, actualizar y eliminar tipos de prioridad de los tickets.
   - Filtrar tipos de prioridad de ticket por id.
   - Filtrar tipos de prioridad de ticket por nombre.
   - Mostrar todos los tipos de prioridad de ticket existentes en el sistema.

8. **Gestión de tipo de toner**
   - La entidad ya está creada dentro del paquete entity se llama TipoToner.
   - Crear, leer, actualizar y eliminar tipos de toner.
   - Filtrar tipos de toner por id.
   - Filtrar tipos de toner por nombre (Like).
   - Mostrar todos los tipos de toner existentes en el sistema.

9. **Gestión de Dispositivos**
   - La entidad ya está creada dentro del paquete entity se llama Dispositivo.
   - Crear, leer, actualizar y eliminar dispositivos.
   - Asociar dispositivos a TipoDispositivo, TipoEstadoDisp y HistorialUbicacion.
   - Filtrar dispositivos por id.
   - Filtrar dispositivos por tipoEstado.
   - Filtrar dispositivos por numero de serie.
   - Filtrar dispositivos por tipoDispositivo.
   - Filtrar dispositivos por modelo.
   - Filtrar dispositivos por su ubicación actual (Listar todos los dispositivos, donde su estado no sea "Baja" y su historial de ubicacion sea el más reciente).
   - Filtrar dispositivos por lugar asociado a su ubicación actual (Muestra todos los dispositivos en su lugar actual y donde su estado no sea "Baja", filtrados por el Id de su Lugar).
   - Filtrar dispositivos por departamento asociado a su ubicación actual (Muestra todos los dispositivos en su lugar actual y donde su estado no sea "Baja", filtrados por el Id de su Departamento).

10. **Gestión de Estado de Tickets**
    - La entidad ya está creada dentro del paquete entity se llama EstadoTicket.
    - Crear, leer, actualizar y eliminar estados de tickets.
    - Filtrar estados de tickets por id.
    - Filtrar estados de tickets por nombre.
    - Mostrar todos los estados de tickets existentes en el sistema.

11. **Gestión de Tickets**
    - La entidad ya está creada dentro del paquete entity se llama Ticket.
    - Crear, leer, actualizar y eliminar tickets.
    - Asociar tickets a Usuario, EstadoTicket, TipoPrioridadTicket.
    - Filtrar tickets por id.
    - Filtrar tickets por estado del ticket.
    - Filtrar tickets por prioridad del ticket.
    - Filtrar tickets por fecha de creación (rango de fechas).
    - Mostrar todos los tickets existentes en el sistema.

12. **Gestión de Entradas de Toner**
    - La entidad ya está creada dentro del paquete entity se llama EntradasToner.
    - Crear, leer, actualizar y eliminar entradas de toner.
    - Asociar entradas de toner a TipoToner y Usuario.
    - Filtrar entradas de toner por id.
    - Filtrar entradas de toner por tipo de toner.
    - Filtrar entradas de toner por usuario que realizó la entrada.
    - Filtrar entradas de toner por fecha de entrada (rango de fechas).
    - Mostrar todas las entradas de toner existentes en el sistema.

13. **Gestión de Salidas de Toner**
    - La entidad ya está creada dentro del paquete entity se llama SalidasToner.
    - Crear, leer, actualizar y eliminar salidas de toner.
    - Asociar salidas de toner a TipoToner y Usuario.
    - Filtrar salidas de toner por id.
    - Filtrar salidas de toner por tipo de toner.
    - Filtrar salidas de toner por usuario que realizó la salida.
    - Filtrar salidas de toner por fecha de salida (rango de fechas).
    - Mostrar todas las salidas de toner existentes en el sistema.

14. **Gestión de Historial de Ubicación de Dispositivos**
    - La entidad ya está creada dentro del paquete entity se llama HistorialUbicacion.
    - Crear, leer, actualizar y eliminar registros de historial de ubicación.
    - Asociar historial de ubicación a Dispositivo, Lugar y Usuarios.
    - Filtrar historial de ubicación por id.
    - Filtrar historial de ubicación por dispositivo.
    - Filtrar historial de ubicación por lugar.
    - Filtrar historial de ubicación por fecha de cambio (rango de fechas).
    - Mostrar todos los registros de historial de ubicación existentes en el sistema.

15. **Gestion de Mantenimientos**
    - La entidad ya está creada dentro del paquete entity se llama Mantenimiento.
    - Crear, leer, actualizar y eliminar mantenimientos.
    - Asociar mantenimientos a Dispositivo, TipoMantenimiento y Usuario.
    - Filtrar mantenimientos por id.
    - Filtrar mantenimientos por dispositivo.
    - Filtrar mantenimientos por tipo de mantenimiento.
    - Filtrar mantenimientos por fecha de mantenimiento (rango de fechas).
    - Mostrar todos los mantenimientos existentes en el sistema.

16. **Gestión de Stock de Toner**
    - StockToner no es una entity, es un DTO y está dentro del paquete entity.
    - Mostrar el stock actual de toner por tipo de toner.
    - Mostrar el stock actual de todos los tipos de toner.

17. **Gestión de Usuarios**
    - La entidad ya está creada dentro del paquete entity se llama Usuario.
    - Crear, leer, actualizar y eliminar usuarios.
    - Filtrar usuarios por id.
    - Filtrar usuarios por nombre (Like).
    - Filtrar usuarios por rol.
    - Mostrar todos los usuarios existentes en el sistema.


