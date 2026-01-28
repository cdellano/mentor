package saul.reports.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import saul.reports.service.ReportService;

import java.time.LocalDate;

/**
 * Controlador REST para la generación de reportes en formato PDF.
 * Expone endpoints para generar diferentes tipos de reportes de la aplicación.
 *
 * Todos los endpoints retornan ResponseEntity<byte[]> con headers configurados
 * para visualización o descarga de archivos PDF.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    /**
     * Genera un reporte PDF de equipos rezagados en mantenimiento.
     *
     * El reporte incluye:
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte con filtros aplicados
     * - Código QR (alineado a la derecha)
     * - Tabla con equipos: ID Dispositivo, Inventario-Núm.Serie, Dispositivo, Fecha Último Mant., Días Transcurridos
     * - Bordes de tabla en estilo dotted (punteado)
     * - Pie de página con fecha de generación
     *
     * @param diasMinimos número mínimo de días sin recibir mantenimiento (obligatorio)
     * @param idTipoDispositivo ID del tipo de dispositivo a filtrar (opcional).
     *                         Valores especiales:
     *                         - null: sin filtro de tipo
     *                         - -1: incluye todos los tipos de dispositivos
     *                         - ID válido: filtra por ese tipo específico
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/equipos-rezagados?diasMinimos=30&idTipoDispositivo=1 (filtrar por tipo específico)
     * GET /api/reports/equipos-rezagados?diasMinimos=90&idTipoDispositivo=-1 (todos los tipos)
     * GET /api/reports/equipos-rezagados?diasMinimos=90 (sin filtro de tipo)
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="equipos_rezagados_todos_tipos_30_dias.pdf"
     * - Body: bytes del PDF
     */
    @GetMapping("/equipos-rezagados")
    public ResponseEntity<byte[]> generateEquiposRezagadosReport(
            @RequestParam Integer diasMinimos,
            @RequestParam(required = false) Integer idTipoDispositivo) {
        return reportService.generateEquiposRezagadosReport(diasMinimos, idTipoDispositivo);
    }

    /**
     * Genera un reporte PDF de mantenimientos realizados en un rango de fechas con filtro opcional por tipo de dispositivo.
     *
     * El reporte incluye:
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte
     * - Período de fechas consultado y tipo de dispositivo filtrado (si aplica)
     * - Tabla con los mantenimientos: ID, Dispositivo, Tipo, Fecha Realizada, Estado, Ubicación Actual, Técnico
     * - Pie de página con fecha de generación
     *
     * @param fechaInicio fecha de inicio del rango (formato: yyyy-MM-dd)
     * @param fechaFin fecha de fin del rango (formato: yyyy-MM-dd)
     * @param idTipoDispositivo ID del tipo de dispositivo a filtrar (opcional). Si no se envía, muestra todos los tipos de dispositivos.
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/mantenimientos?fechaInicio=2024-01-01&fechaFin=2024-12-31 (todos los tipos de dispositivos)
     * GET /api/reports/mantenimientos?fechaInicio=2024-01-01&fechaFin=2024-12-31&idTipoDispositivo=1 (tipo específico)
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="mantenimientos_2024-01-01_2024-12-31.pdf"
     * - Content-Disposition: inline; filename="mantenimientos_tipo_Computadora_2024-01-01_2024-12-31.pdf" (con filtro)
     * - Body: bytes del PDF
     */
    @GetMapping("/mantenimientos")
    public ResponseEntity<byte[]> generateMantenimientoReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Integer idTipoDispositivo) {
        return reportService.generateMantenimientoReport(fechaInicio, fechaFin, idTipoDispositivo);
    }

    /**
     * Genera un reporte PDF de entradas de tóner en un rango de fechas con filtro opcional por usuario.
     *
     * El reporte incluye:
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte (obtenido de reports.properties)
     * - Código QR (alineado a la derecha)
     * - Período de fechas consultado y usuario filtrado (si aplica)
     * - Tabla con las entradas: ID, Fecha Entrada, Usuario, Tipo Tóner, Cantidad, Observaciones
     * - Pie de página con fecha de generación
     *
     * @param fechaInicio fecha de inicio del rango (formato: yyyy-MM-dd)
     * @param fechaFin fecha de fin del rango (formato: yyyy-MM-dd)
     * @param idUsuario ID del usuario que registró las entradas (opcional). Si no se envía, muestra todas las entradas del período.
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/entradas-toner?fechaInicio=2024-01-01&fechaFin=2024-12-31 (todas las entradas del período)
     * GET /api/reports/entradas-toner?fechaInicio=2024-01-01&fechaFin=2024-12-31&idUsuario=5 (entradas de un usuario específico)
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="entradas_toner_2024-01-01_2024-12-31.pdf"
     * - Content-Disposition: inline; filename="entradas_toner_usuario_5_2024-01-01_2024-12-31.pdf" (con filtro)
     * - Body: bytes del PDF
     */
    @GetMapping("/entradas-toner")
    public ResponseEntity<byte[]> generateEntradasTonerReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long idUsuario) {
        return reportService.generateEntradasTonerReport(fechaInicio, fechaFin, idUsuario);
    }

    /**
     * Genera un reporte PDF de salidas de tóner en un rango de fechas con filtro opcional por usuario.
     *
     * El reporte incluye:
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte (obtenido de reports.properties: report.title.salidas.toner)
     * - Período de fechas consultado y usuario filtrado (si aplica)
     * - Tabla con las salidas: ID, Fecha Salida, Usuario, Tipo Tóner, Cantidad, Observaciones
     * - Pie de página con fecha de generación
     *
     * @param fechaInicio fecha de inicio del rango (formato: yyyy-MM-dd)
     * @param fechaFin fecha de fin del rango (formato: yyyy-MM-dd)
     * @param idUsuario ID del usuario que registró las salidas (opcional). Si no se envía, muestra todas las salidas del período.
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/salidas-toner?fechaInicio=2024-01-01&fechaFin=2024-12-31 (todas las salidas del período)
     * GET /api/reports/salidas-toner?fechaInicio=2024-01-01&fechaFin=2024-12-31&idUsuario=5 (salidas de un usuario específico)
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="salidas_toner_2024-01-01_2024-12-31.pdf"
     * - Content-Disposition: inline; filename="salidas_toner_usuario_5_2024-01-01_2024-12-31.pdf" (con filtro)
     * - Body: bytes del PDF
     */
    @GetMapping("/salidas-toner")
    public ResponseEntity<byte[]> generateSalidasTonerReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long idUsuario) {
        return reportService.generateSalidasTonerReport(fechaInicio, fechaFin, idUsuario);
    }

    /**
     * Genera un reporte PDF de inventario/stock de tóner de todos los tipos existentes.
     *
     * El reporte incluye:
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte (obtenido de reports.properties: report.title.inventario.toner)
     * - Código QR (alineado a la derecha, tamaño reducido 10%, separación reducida 50%)
     * - Tabla con diseño dashed en color naranja mostrando: ID, Tipo de Tóner, Total Entradas, Total Salidas, Stock Actual
     * - Paginación en formato "p. x de total"
     * - Pie de página con fecha de generación
     *
     * No requiere parámetros - muestra todos los tipos de tóner del sistema.
     *
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplo de uso:
     * GET /api/reports/inventario-toner
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="inventario_toner.pdf"
     * - Body: bytes del PDF con todas las existencias de tóner
     */
    @GetMapping("/inventario-toner")
    public ResponseEntity<byte[]> generateInventarioTonerReport() {
        return reportService.generateStockTonerReport();
    }

    /**
     * Genera un reporte PDF de anotaciones en un rango de fechas con múltiples filtros opcionales.
     *
     * El reporte incluye:
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte (obtenido de reports.properties: report.title.anotaciones)
     * - Período de fechas consultado y filtros aplicados (si corresponde)
     * - Tabla con diseño dotted en color guindo oscuro mostrando: ID, Fecha Anotación, Usuario, Página, Etiquetas, Importante, Contenido
     * - Paginación en formato "p. x de total"
     * - Pie de página con fecha de generación
     * - Ordenamiento de la más reciente a la más antigua
     *
     * @param fechaInicio fecha de inicio del rango (formato: yyyy-MM-dd)
     * @param fechaFin fecha de fin del rango (formato: yyyy-MM-dd)
     * @param idUsuario ID del usuario que creó las anotaciones (opcional)
     * @param contenido texto a buscar en el contenido de las anotaciones usando LIKE (opcional)
     * @param pagina número de página para filtrar anotaciones (opcional)
     * @param etiquetas etiquetas a filtrar (opcional)
     * @param importante filtrar solo anotaciones importantes (opcional) - true/false
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/anotaciones?fechaInicio=2024-01-01&fechaFin=2024-12-31 (todas las anotaciones del período)
     * GET /api/reports/anotaciones?fechaInicio=2024-01-01&fechaFin=2024-12-31&idUsuario=5 (anotaciones de un usuario específico)
     * GET /api/reports/anotaciones?fechaInicio=2024-01-01&fechaFin=2024-12-31&contenido=error&importante=true (anotaciones importantes que contengan "error")
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="anotaciones_2024-01-01_2024-12-31.pdf"
     * - Body: bytes del PDF con las anotaciones filtradas
     */
    @GetMapping("/anotaciones")
    public ResponseEntity<byte[]> generateAnotacionesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) String contenido,
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) String etiquetas,
            @RequestParam(required = false) Boolean importante) {
        return reportService.generateAnotacionesReport(fechaInicio, fechaFin, idUsuario, contenido, pagina, etiquetas, importante);
    }

    /**
     * Genera un reporte PDF de bitácora de servicios informáticos en un rango de fechas con múltiples filtros opcionales.
     *
     * El reporte incluye:
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte (obtenido de reports.properties: report.title.bitacoraServ)
     * - Código QR (alineado a la derecha)
     * - Período de fechas consultado y filtros aplicados (si corresponde)
     * - Tabla con diseño dotted en color magenta oscuro mostrando: ID, Servicio, Tipo Incidente, Estado,
     *   Fecha Inicio, Fecha Fin, Reportado Por, Comentario
     * - Paginación en formato "p. x de total"
     * - Pie de página con fecha de generación
     * - Ordenamiento del más reciente al más antiguo (por createdAt)
     *
     * @param fechaInicio fecha de inicio del rango (formato: yyyy-MM-dd) - filtra por createdAt
     * @param fechaFin fecha de fin del rango (formato: yyyy-MM-dd) - filtra por createdAt
     * @param idUsuario ID del usuario que reportó (reportadoPor) (opcional)
     * @param contenido texto a buscar en el comentario de la bitácora usando LIKE (opcional)
     * @param estado estado de la bitácora (opcional) - valores válidos: NORMAL, DEGRADADO, CAIDO, MANTENIMIENTO, SOPORTE_TECNICO_PROV
     * @param idTipoIncidente ID del tipo de incidente (opcional)
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/bitacora-servicios?fechaInicio=2024-01-01&fechaFin=2024-12-31 (todas las bitácoras del período)
     * GET /api/reports/bitacora-servicios?fechaInicio=2024-01-01&fechaFin=2024-12-31&idUsuario=5 (bitácoras reportadas por un usuario específico)
     * GET /api/reports/bitacora-servicios?fechaInicio=2024-01-01&fechaFin=2024-12-31&estado=CAIDO (bitácoras con estado CAIDO)
     * GET /api/reports/bitacora-servicios?fechaInicio=2024-01-01&fechaFin=2024-12-31&contenido=servidor&estado=DEGRADADO (bitácoras degradadas que contengan "servidor")
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="bitacora_servicios_2024-01-01_2024-12-31.pdf"
     * - Body: bytes del PDF con las bitácoras filtradas
     */
    @GetMapping("/bitacora-servicios")
    public ResponseEntity<byte[]> generateBitacoraServicioReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) String contenido,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long idTipoIncidente) {
        return reportService.generateBitacoraServicioReport(fechaInicio, fechaFin, idUsuario, contenido, estado, idTipoIncidente);
    }

    /**
     * Genera un reporte PDF con la información detallada de un dispositivo individual y su historial de ubicaciones.
     *
     * El reporte incluye:
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte (obtenido de reports.properties: report.title.dispositivo)
     * - Código QR (alineado a la derecha)
     * - Ficha detallada del dispositivo: ID, Tipo, Marca, Modelo, Número de Serie, Inventario, Estado, Fecha de Compra, Costo, Notas
     * - Tabla con diseño dotted en color azul marino mostrando el historial de ubicaciones:
     *   Lugar, Departamento, Piso, Edificio, Fecha Entrada, Fecha Salida, Días en Lugar, Usuario que Asignó
     * - Los días en la ubicación actual se calculan hasta la fecha de generación del reporte
     * - Paginación en formato "p. x de total"
     * - Pie de página con fecha de generación
     *
     * Si el dispositivo no existe, genera un PDF con mensaje de error indicando que no se encontró.
     *
     * @param idDispositivo ID del dispositivo a consultar (obligatorio)
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/dispositivo?idDispositivo=1 (reporte del dispositivo con ID 1)
     * GET /api/reports/dispositivo?idDispositivo=999 (genera PDF con mensaje de dispositivo no encontrado)
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="dispositivo_1_INV001.pdf"
     * - Body: bytes del PDF con la información del dispositivo y su historial de ubicaciones
     */
    @GetMapping("/dispositivo")
    public ResponseEntity<byte[]> generateDispositivoReport(
            @RequestParam Long idDispositivo) {
        return reportService.generateDispositivoReport(idDispositivo);
    }

    /**
     * Genera un reporte PDF del historial de ubicaciones (movimientos) de dispositivos en un rango de fechas.
     *
     * El reporte incluye:
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte (obtenido de reports.properties: report.title.historial)
     * - Código QR (alineado a la derecha)
     * - Período de fechas consultado
     * - Tabla con diseño dashed en color negro-anaranjado mostrando:
     *   ID Historial, Dispositivo (Inventario/Serie), Tipo, Lugar, Departamento, Piso/Edificio,
     *   Fecha Entrada, Fecha Salida, Días en Lugar, Usuario que Asignó
     * - Los días en la ubicación actual se calculan hasta la fecha de generación del reporte
     * - Ordenamiento del más reciente al más antiguo (por fechaEntrada)
     * - Paginación en formato "p. x de total"
     * - Pie de página con fecha de generación
     *
     * @param fechaInicio fecha de inicio del rango (formato: yyyy-MM-dd) - filtra por fechaEntrada
     * @param fechaFin fecha de fin del rango (formato: yyyy-MM-dd) - filtra por fechaEntrada
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/historial-ubicaciones?fechaInicio=2024-01-01&fechaFin=2024-12-31 (todos los movimientos del período)
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="historial_ubicaciones_2024-01-01_2024-12-31.pdf"
     * - Body: bytes del PDF con el historial de ubicaciones
     */
    @GetMapping("/historial-ubicaciones")
    public ResponseEntity<byte[]> generateHistorialUbicacionReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return reportService.generateHistorialUbicacionReport(fechaInicio, fechaFin);
    }

    /**
     * Genera un reporte PDF de tickets en un rango de fechas con múltiples filtros opcionales.
     *
     * El reporte incluye:
     * - Orientación horizontal (landscape) en tamaño carta
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte (obtenido de reports.properties: report.title.tickets)
     * - Código QR (alineado a la derecha)
     * - Período de fechas consultado y filtros aplicados (si corresponde)
     * - Tabla con diseño dotted en color negro mostrando:
     *   ID, Asunto, Departamento, Estado, Prioridad, Creador, Asignado, F. Creación, F. Cierre
     * - Paginación en formato "p. x de total"
     * - Pie de página con fecha de generación
     * - Ordenamiento del más reciente al más antiguo (por fechaCreacion)
     * - Usa query nativa considerando soft delete (borrado = false)
     *
     * @param fechaInicio fecha de inicio del rango (formato: yyyy-MM-dd) - filtra por fechaCreacion
     * @param fechaFin fecha de fin del rango (formato: yyyy-MM-dd) - filtra por fechaCreacion
     * @param idDepartamento ID del departamento al que pertenece el ticket (opcional)
     * @param idEstado ID del estado del ticket (opcional)
     * @param idPrioridad ID de la prioridad del ticket (opcional)
     * @param descripcion texto a buscar en la descripción del ticket usando ILIKE (opcional)
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/tickets?fechaInicio=2024-01-01&fechaFin=2024-12-31 (todos los tickets del período)
     * GET /api/reports/tickets?fechaInicio=2024-01-01&fechaFin=2024-12-31&idDepartamento=1 (tickets de un departamento específico)
     * GET /api/reports/tickets?fechaInicio=2024-01-01&fechaFin=2024-12-31&idEstado=2&idPrioridad=3 (tickets con estado y prioridad específicos)
     * GET /api/reports/tickets?fechaInicio=2024-01-01&fechaFin=2024-12-31&descripcion=impresora (tickets cuya descripción contenga "impresora")
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="tickets_2024-01-01_2024-12-31.pdf"
     * - Body: bytes del PDF con los tickets filtrados
     */
    @GetMapping("/tickets")
    public ResponseEntity<byte[]> generateTicketReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Integer idDepartamento,
            @RequestParam(required = false) Integer idEstado,
            @RequestParam(required = false) Integer idPrioridad,
            @RequestParam(required = false) String descripcion) {
        return reportService.generateTicketReport(fechaInicio, fechaFin, idDepartamento, idEstado, idPrioridad, descripcion);
    }

    /**
     * Genera un reporte PDF de dispositivos registrados en un rango de fechas con múltiples filtros opcionales.
     *
     * El reporte incluye:
     * - Orientación horizontal (landscape) en tamaño carta
     * - Encabezado con imagen i1 (izquierda), información del hospital (centro), e imagen i2 (derecha)
     * - Título del reporte (obtenido de reports.properties: report.title.regDisp)
     * - Código QR (alineado a la derecha)
     * - Período de fechas consultado y filtros aplicados (si corresponde)
     * - Tabla con celdas autoajustables mostrando:
     *   ID, Tipo, Marca, Modelo, Núm. Serie, Inventario, Estado, F. Compra, F. Registro, Costo, Notas
     * - Paginación en formato "p. x de total"
     * - Pie de página con fecha de generación
     * - Ordenamiento del más reciente al más antiguo (por fechaRegistro)
     * - Usa query nativa considerando soft delete (borrado = false)
     *
     * @param fechaInicio fecha de inicio del rango (formato: yyyy-MM-dd) - filtra por fechaRegistro
     * @param fechaFin fecha de fin del rango (formato: yyyy-MM-dd) - filtra por fechaRegistro
     * @param marca texto a buscar en la marca del dispositivo usando ILIKE (opcional)
     * @param modelo texto a buscar en el modelo del dispositivo usando ILIKE (opcional)
     * @param numeroserie texto a buscar en el número de serie del dispositivo usando ILIKE (opcional)
     * @param inventario texto a buscar en el inventario del dispositivo usando ILIKE (opcional)
     * @param notas texto a buscar en las notas del dispositivo usando ILIKE (opcional)
     * @param idTipoEstado ID del estado del dispositivo a filtrar (opcional)
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     *
     * Ejemplos de uso:
     * GET /api/reports/registro-dispositivos?fechaInicio=2024-01-01&fechaFin=2024-12-31 (todos los dispositivos registrados en el período)
     * GET /api/reports/registro-dispositivos?fechaInicio=2024-01-01&fechaFin=2024-12-31&marca=HP (dispositivos cuya marca contenga "HP")
     * GET /api/reports/registro-dispositivos?fechaInicio=2024-01-01&fechaFin=2024-12-31&modelo=LaserJet&marca=HP (filtros combinados)
     * GET /api/reports/registro-dispositivos?fechaInicio=2024-01-01&fechaFin=2024-12-31&notas=urgente (dispositivos cuyas notas contengan "urgente")
     * GET /api/reports/registro-dispositivos?fechaInicio=2024-01-01&fechaFin=2024-12-31&idTipoEstado=1 (dispositivos con estado ID 1)
     *
     * Response:
     * - Content-Type: application/pdf
     * - Content-Disposition: inline; filename="registro_dispositivos_2024-01-01_2024-12-31.pdf"
     * - Body: bytes del PDF con los dispositivos registrados filtrados
     */
    @GetMapping("/registro-dispositivos")
    public ResponseEntity<byte[]> generateDispositivoRegistroReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String numeroserie,
            @RequestParam(required = false) String inventario,
            @RequestParam(required = false) String notas,
            @RequestParam(required = false) Integer idTipoEstado) {
        return reportService.generateDispositivoRegistroReport(fechaInicio, fechaFin, marca, modelo, numeroserie, inventario, notas, idTipoEstado);
    }




}
