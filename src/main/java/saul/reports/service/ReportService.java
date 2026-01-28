package saul.reports.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import saul.entity.Dispositivo;
import saul.entity.HistorialUbicacion;
import saul.entity.Mantenimiento;
import saul.entity.TipoDispositivo;
import saul.entity.EntradasToner;
import saul.entity.SalidasToner;
import saul.entity.Anotacion;
import saul.entity.BitacoraServicio;
import saul.entity.EstadoBitacora;
import saul.reports.dto.EquipoRezagadoReportDTO;
import saul.reports.dto.MantenimientoReportDTO;
import saul.reports.dto.BitacoraServicioReportDTO;
import saul.reports.generator.EquipoRezagadoReportGenerator;
import saul.reports.generator.ImagePageGenerator;
import saul.reports.generator.MantenimientoReportGenerator;
import saul.reports.generator.EntradasTonerReportGenerator;
import saul.reports.generator.SalidasTonerReportGenerator;
import saul.reports.generator.StockTonerReportGenerator;
import saul.reports.generator.AnotacionesReportGenerator;
import saul.reports.generator.BitacoraServicioReportGenerator;
import saul.reports.generator.DispositivoReportGenerator;
import saul.reports.generator.HistorialUbicacionReportGenerator;
import saul.reports.generator.TicketReportGenerator;
import saul.reports.generator.DispositivoRegistroReportGenerator;
import saul.reports.dto.AnotacionReportDTO;
import saul.reports.dto.DispositivoReportDTO;
import saul.reports.dto.DispositivoRegistroReportDTO;
import saul.reports.dto.HistorialUbicacionReportDTO;
import saul.reports.dto.TicketReportDTO;
import saul.dto.response.StockTonerResponse;
import saul.service.StockTonerService;
import saul.repository.DispositivoRepository;
import saul.repository.HistorialUbicacionRepository;
import saul.repository.MantenimientoRepository;
import saul.repository.TipoDispositivoRepository;
import saul.repository.EntradasTonerRepository;
import saul.repository.SalidasTonerRepository;
import saul.repository.AnotacionRepository;
import saul.repository.BitacoraServicioRepository;
import saul.repository.TicketRepository;
import saul.entity.Ticket;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de reportes que coordina la generación de PDFs y retorna ResponseEntity
 * con los headers HTTP apropiados para descarga o visualización de PDFs.
 */
@Service
public class ReportService {

    private final ImagePageGenerator imagePageGenerator;
    private final MantenimientoReportGenerator mantenimientoReportGenerator;
    private final EquipoRezagadoReportGenerator equipoRezagadoReportGenerator;
    private final EntradasTonerReportGenerator entradasTonerReportGenerator;
    private final SalidasTonerReportGenerator salidasTonerReportGenerator;
    private final StockTonerReportGenerator stockTonerReportGenerator;
    private final AnotacionesReportGenerator anotacionesReportGenerator;
    private final BitacoraServicioReportGenerator bitacoraServicioReportGenerator;
    private final DispositivoReportGenerator dispositivoReportGenerator;
    private final HistorialUbicacionReportGenerator historialUbicacionReportGenerator;
    private final TicketReportGenerator ticketReportGenerator;
    private final DispositivoRegistroReportGenerator dispositivoRegistroReportGenerator;
    private final MantenimientoRepository mantenimientoRepository;
    private final DispositivoRepository dispositivoRepository;
    private final TipoDispositivoRepository tipoDispositivoRepository;
    private final HistorialUbicacionRepository historialUbicacionRepository;
    private final EntradasTonerRepository entradasTonerRepository;
    private final SalidasTonerRepository salidasTonerRepository;
    private final AnotacionRepository anotacionRepository;
    private final BitacoraServicioRepository bitacoraServicioRepository;
    private final TicketRepository ticketRepository;
    private final StockTonerService stockTonerService;

    public ReportService(ImagePageGenerator imagePageGenerator,
                         MantenimientoReportGenerator mantenimientoReportGenerator,
                         EquipoRezagadoReportGenerator equipoRezagadoReportGenerator,
                         EntradasTonerReportGenerator entradasTonerReportGenerator,
                         SalidasTonerReportGenerator salidasTonerReportGenerator,
                         StockTonerReportGenerator stockTonerReportGenerator,
                         AnotacionesReportGenerator anotacionesReportGenerator,
                         BitacoraServicioReportGenerator bitacoraServicioReportGenerator,
                         DispositivoReportGenerator dispositivoReportGenerator,
                         HistorialUbicacionReportGenerator historialUbicacionReportGenerator,
                         TicketReportGenerator ticketReportGenerator,
                         DispositivoRegistroReportGenerator dispositivoRegistroReportGenerator,
                         MantenimientoRepository mantenimientoRepository,
                         DispositivoRepository dispositivoRepository,
                         TipoDispositivoRepository tipoDispositivoRepository,
                         HistorialUbicacionRepository historialUbicacionRepository,
                         EntradasTonerRepository entradasTonerRepository,
                         SalidasTonerRepository salidasTonerRepository,
                         AnotacionRepository anotacionRepository,
                         BitacoraServicioRepository bitacoraServicioRepository,
                         TicketRepository ticketRepository,
                         StockTonerService stockTonerService) {
        this.imagePageGenerator = imagePageGenerator;
        this.mantenimientoReportGenerator = mantenimientoReportGenerator;
        this.equipoRezagadoReportGenerator = equipoRezagadoReportGenerator;
        this.entradasTonerReportGenerator = entradasTonerReportGenerator;
        this.salidasTonerReportGenerator = salidasTonerReportGenerator;
        this.stockTonerReportGenerator = stockTonerReportGenerator;
        this.anotacionesReportGenerator = anotacionesReportGenerator;
        this.bitacoraServicioReportGenerator = bitacoraServicioReportGenerator;
        this.dispositivoReportGenerator = dispositivoReportGenerator;
        this.historialUbicacionReportGenerator = historialUbicacionReportGenerator;
        this.ticketReportGenerator = ticketReportGenerator;
        this.dispositivoRegistroReportGenerator = dispositivoRegistroReportGenerator;
        this.mantenimientoRepository = mantenimientoRepository;
        this.dispositivoRepository = dispositivoRepository;
        this.tipoDispositivoRepository = tipoDispositivoRepository;
        this.historialUbicacionRepository = historialUbicacionRepository;
        this.entradasTonerRepository = entradasTonerRepository;
        this.salidasTonerRepository = salidasTonerRepository;
        this.anotacionRepository = anotacionRepository;
        this.bitacoraServicioRepository = bitacoraServicioRepository;
        this.ticketRepository = ticketRepository;
        this.stockTonerService = stockTonerService;
    }

    /**
     * Genera un reporte con la imagen "i2.png" en una página tamaño carta.
     *
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateImagePageReport() {
        try {
            byte[] pdfBytes = imagePageGenerator.generateImagePage();
            return buildPdfResponse(pdfBytes, "image-report.pdf", false);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Genera un reporte con una imagen específica en una página tamaño carta.
     *
     * @param imagePath ruta de la imagen en resources
     * @param fileName nombre del archivo PDF resultante
     * @return ResponseEntity con el PDF y headers configurados
     */
    public ResponseEntity<byte[]> generateImagePageReport(String imagePath, String fileName) {
        try {
            byte[] pdfBytes = imagePageGenerator.generateImagePage(imagePath);
            return buildPdfResponse(pdfBytes, fileName, false);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Genera un reporte PDF de mantenimientos realizados en un rango de fechas.
     * Incluye la ubicación más reciente de cada dispositivo.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin    fecha de fin del rango
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateMantenimientoReport(LocalDate fechaInicio, LocalDate fechaFin) {
        return generateMantenimientoReport(fechaInicio, fechaFin, null);
    }

    /**
     * Genera un reporte PDF de mantenimientos realizados en un rango de fechas con filtro opcional por tipo de dispositivo.
     * Incluye la ubicación más reciente de cada dispositivo.
     *
     * @param fechaInicio       fecha de inicio del rango
     * @param fechaFin          fecha de fin del rango
     * @param idTipoDispositivo ID del tipo de dispositivo a filtrar (opcional). Si es null, incluye todos los tipos de dispositivos.
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateMantenimientoReport(LocalDate fechaInicio, LocalDate fechaFin, Integer idTipoDispositivo) {
        try {
            // Obtener mantenimientos del repositorio con filtro opcional por tipo de dispositivo
            List<Mantenimiento> mantenimientos = mantenimientoRepository
                    .findByFechaRealizadoBetweenAndTipoDispositivoForReport(fechaInicio, fechaFin, idTipoDispositivo);

            // Obtener información del tipo de dispositivo si se especificó un filtro
            String tipoDispositivoInfo = null;
            if (idTipoDispositivo != null) {
                Optional<TipoDispositivo> tipoDispositivoOpt = tipoDispositivoRepository.findById(idTipoDispositivo);
                if (tipoDispositivoOpt.isPresent()) {
                    TipoDispositivo tipoDispositivo = tipoDispositivoOpt.get();
                    tipoDispositivoInfo = tipoDispositivo.getNombreTipo();
                }
            }

            // Convertir a DTOs incluyendo la ubicación actual
            List<MantenimientoReportDTO> dtos = convertToReportDTOs(mantenimientos);

            // Generar el PDF con información del tipo de dispositivo filtrado
            byte[] pdfBytes = mantenimientoReportGenerator.generateReport(dtos, fechaInicio, fechaFin, tipoDispositivoInfo);

            // Nombre del archivo con las fechas y tipo de dispositivo si aplica
            String fileName;
            if (idTipoDispositivo != null && tipoDispositivoInfo != null) {
                fileName = String.format("mantenimientos_tipo_%s_%s_%s.pdf",
                        tipoDispositivoInfo.replaceAll("\\s+", "_"), fechaInicio.toString(), fechaFin.toString());
            } else {
                fileName = String.format("mantenimientos_%s_%s.pdf",
                        fechaInicio.toString(), fechaFin.toString());
            }

            return buildPdfResponse(pdfBytes, fileName, false);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte de mantenimientos: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Convierte una lista de entidades Mantenimiento a DTOs para el reporte,
     * incluyendo la ubicación más reciente de cada dispositivo.
     */
    private List<MantenimientoReportDTO> convertToReportDTOs(List<Mantenimiento> mantenimientos) {
        List<MantenimientoReportDTO> dtos = new ArrayList<>();

        for (Mantenimiento m : mantenimientos) {
            MantenimientoReportDTO.MantenimientoReportDTOBuilder builder = MantenimientoReportDTO.builder()
                    .idMantenimiento(m.getIdMantenimiento())
                    .descripcion(m.getDescripcion())
                    .fechaProgramada(m.getFechaProgramada())
                    .fechaRealizado(m.getFechaRealizado())
                    .estado(m.getEstado())
                    .costo(m.getCosto())
                    .notas(m.getNotas())
                    .pilaCmos(m.getPilaCmos())
                    .pastaCpu(m.getPastaCpu())
                    .limpieza(m.getLimpieza());

            // Información del tipo de mantenimiento
            if (m.getTipoMantenimiento() != null) {
                builder.tipoMantenimiento(m.getTipoMantenimiento().getNombreTipoMantenimiento());
            }

            // Información del dispositivo
            if (m.getDispositivo() != null) {
                builder.idDispositivo(m.getDispositivo().getIdDispositivo())
                        .marca(m.getDispositivo().getMarca())
                        .modelo(m.getDispositivo().getModelo())
                        .numeroSerie(m.getDispositivo().getNumeroSerie())
                        .inventario(m.getDispositivo().getInventario());

                if (m.getDispositivo().getTipoDispositivo() != null) {
                    builder.tipoDispositivo(m.getDispositivo().getTipoDispositivo().getNombreTipo());
                }
                if (m.getDispositivo().getTipoEstado() != null) {
                    builder.estadoDispositivo(m.getDispositivo().getTipoEstado().getNombreEstado());
                }

                // Obtener ubicación más reciente del dispositivo
                Optional<HistorialUbicacion> ubicacionActual = historialUbicacionRepository
                        .findCurrentByDispositivo(m.getDispositivo().getIdDispositivo());

                if (ubicacionActual.isPresent()) {
                    HistorialUbicacion hu = ubicacionActual.get();
                    if (hu.getLugar() != null) {
                        builder.lugarActual(hu.getLugar().getNombreLugar())
                                .pisoActual(hu.getLugar().getPiso())
                                .edificioActual(hu.getLugar().getEdificio());

                        if (hu.getLugar().getDepartamento() != null) {
                            builder.departamentoActual(hu.getLugar().getDepartamento().getNombreDepartamento());
                        }
                    }
                }
            }

            // Información de usuarios
            if (m.getUsuarioSolicita() != null) {
                builder.usuarioSolicita(m.getUsuarioSolicita().getNombreCompleto());
            }
            if (m.getUsuarioAtiende() != null) {
                builder.usuarioAtiende(m.getUsuarioAtiende().getNombreCompleto());
            }

            dtos.add(builder.build());
        }

        return dtos;
    }

    /**
     * Genera un reporte de equipos rezagados en mantenimiento.
     *
     * @param diasMinimos número mínimo de días sin recibir mantenimiento
     * @param idTipoDispositivo ID del tipo de dispositivo a filtrar (puede ser null).
     *                         Si es -1, incluye todos los tipos de dispositivos.
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateEquiposRezagadosReport(Integer diasMinimos, Integer idTipoDispositivo) {
        try {
            // Obtener dispositivos rezagados
            List<Dispositivo> dispositivos = dispositivoRepository.findEquiposRezagadosEnMantenimiento(
                    diasMinimos, idTipoDispositivo);

            // Determinar el nombre del tipo de dispositivo para el título y nombre del archivo
            String tipoDispositivoNombre = null;
            if (idTipoDispositivo != null) {
                if (idTipoDispositivo == -1) {
                    tipoDispositivoNombre = "Todos los tipos";
                } else {
                    Optional<TipoDispositivo> tipoOpt = tipoDispositivoRepository.findById(idTipoDispositivo);
                    if (tipoOpt.isPresent()) {
                        tipoDispositivoNombre = tipoOpt.get().getNombreTipo();
                    }
                }
            }

            // Convertir a DTOs
            List<EquipoRezagadoReportDTO> dtos = convertToEquipoRezagadoReportDTOs(dispositivos);

            // Generar el PDF
            byte[] pdfBytes = equipoRezagadoReportGenerator.generateReport(dtos, diasMinimos, tipoDispositivoNombre);

            // Nombre del archivo
            String fileName = String.format("equipos_rezagados_%d_dias.pdf", diasMinimos);
            if (tipoDispositivoNombre != null) {
                String tipoParaArchivo = tipoDispositivoNombre.equals("Todos los tipos")
                    ? "todos_tipos"
                    : tipoDispositivoNombre.replaceAll("\\s+", "_");
                fileName = String.format("equipos_rezagados_%s_%d_dias.pdf", tipoParaArchivo, diasMinimos);
            }

            return buildPdfResponse(pdfBytes, fileName, false);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte de equipos rezagados: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Convierte una lista de dispositivos a DTOs para el reporte de equipos rezagados,
     * incluyendo la fecha del último mantenimiento y días transcurridos.
     */
    private List<EquipoRezagadoReportDTO> convertToEquipoRezagadoReportDTOs(List<Dispositivo> dispositivos) {
        List<EquipoRezagadoReportDTO> dtos = new ArrayList<>();

        for (Dispositivo dispositivo : dispositivos) {
            EquipoRezagadoReportDTO.EquipoRezagadoReportDTOBuilder builder = EquipoRezagadoReportDTO.builder()
                    .idDispositivo(dispositivo.getIdDispositivo())
                    .marca(dispositivo.getMarca())
                    .modelo(dispositivo.getModelo())
                    .numeroSerie(dispositivo.getNumeroSerie())
                    .inventario(dispositivo.getInventario());

            // Información del tipo de dispositivo
            if (dispositivo.getTipoDispositivo() != null) {
                builder.tipoDispositivo(dispositivo.getTipoDispositivo().getNombreTipo());
            }

            // Buscar último mantenimiento
            List<Mantenimiento> mantenimientos = mantenimientoRepository.findLastMantenimientoByDispositivo(
                    dispositivo.getIdDispositivo());

            LocalDate fechaUltimoMantenimiento = null;
            Long diasTranscurridos = null;

            if (!mantenimientos.isEmpty()) {
                fechaUltimoMantenimiento = mantenimientos.get(0).getFechaRealizado();
                if (fechaUltimoMantenimiento != null) {
                    diasTranscurridos = ChronoUnit.DAYS.between(fechaUltimoMantenimiento, LocalDate.now());
                }
            } else {
                // Si no tiene mantenimientos, calcular desde una fecha muy antigua o usar null
                diasTranscurridos = 9999L; // Un número alto para indicar que nunca ha tenido mantenimiento
            }

            builder.fechaUltimoMantenimiento(fechaUltimoMantenimiento)
                   .diasTranscurridos(diasTranscurridos);

            // Buscar ubicación actual
            Optional<HistorialUbicacion> ubicacionActual = historialUbicacionRepository
                    .findCurrentByDispositivo(dispositivo.getIdDispositivo());

            if (ubicacionActual.isPresent()) {
                HistorialUbicacion historial = ubicacionActual.get();
                if (historial.getLugar() != null) {
                    builder.lugarActual(historial.getLugar().getNombreLugar());
                    if (historial.getLugar().getDepartamento() != null) {
                        builder.departamentoActual(historial.getLugar().getDepartamento().getNombreDepartamento());
                    }
                }
            }

            dtos.add(builder.build());
        }

        return dtos;
    }

    /**
     * Genera un reporte de entradas de tóner en un rango de fechas con filtro opcional por usuario.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @param idUsuario ID del usuario que registró las entradas (opcional). Si no se envía, muestra todas las entradas.
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateEntradasTonerReport(LocalDate fechaInicio, LocalDate fechaFin, Long idUsuario) {
        try {
            // Convertir fechas a LocalDateTime para la consulta
            var fechaInicioDateTime = fechaInicio.atStartOfDay();
            var fechaFinDateTime = fechaFin.atTime(23, 59, 59);

            // Obtener entradas de tóner del repositorio con filtros
            List<EntradasToner> entradas;
            if (idUsuario != null) {
                entradas = entradasTonerRepository.findByFechaEntradaBetweenAndUsuarioEntradaIdUsuario(
                    fechaInicioDateTime, fechaFinDateTime, idUsuario);
            } else {
                entradas = entradasTonerRepository.findByFechaEntradaBetween(
                    fechaInicioDateTime, fechaFinDateTime);
            }

            // Generar el PDF
            byte[] pdfBytes = entradasTonerReportGenerator.generateReport(entradas, fechaInicio, fechaFin, idUsuario);

            // Nombre del archivo
            String fileName = String.format("entradas_toner_%s_%s.pdf",
                fechaInicio, fechaFin);
            if (idUsuario != null) {
                fileName = String.format("entradas_toner_usuario_%d_%s_%s.pdf",
                    idUsuario, fechaInicio, fechaFin);
            }

            return buildPdfResponse(pdfBytes, fileName, false);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte de entradas de tóner: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Genera un reporte de salidas de tóner en un rango de fechas con filtro opcional por usuario.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @param idUsuario ID del usuario que registró las salidas (opcional). Si no se envía, muestra todas las salidas.
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateSalidasTonerReport(LocalDate fechaInicio, LocalDate fechaFin, Long idUsuario) {
        try {
            // Convertir fechas a LocalDateTime para la consulta
            var fechaInicioDateTime = fechaInicio.atStartOfDay();
            var fechaFinDateTime = fechaFin.atTime(23, 59, 59);

            // Obtener salidas de tóner del repositorio con filtros
            List<SalidasToner> salidas;
            if (idUsuario != null) {
                salidas = salidasTonerRepository.findByFechaSalidaBetweenAndUsuarioInstalaIdUsuario(
                    fechaInicioDateTime, fechaFinDateTime, idUsuario);
            } else {
                salidas = salidasTonerRepository.findByFechaSalidaBetween(
                    fechaInicioDateTime, fechaFinDateTime);
            }

            // Generar el PDF
            byte[] pdfBytes = salidasTonerReportGenerator.generateReport(salidas, fechaInicio, fechaFin, idUsuario);

            // Nombre del archivo
            String fileName = String.format("salidas_toner_%s_%s.pdf",
                fechaInicio, fechaFin);
            if (idUsuario != null) {
                fileName = String.format("salidas_toner_usuario_%d_%s_%s.pdf",
                    idUsuario, fechaInicio, fechaFin);
            }

            return buildPdfResponse(pdfBytes, fileName, false);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte de salidas de tóner: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Genera un reporte PDF de inventario/stock de tóner.
     *
     * Muestra todos los tipos de tóner existentes en el sistema con su stock actual
     * calculado basándose en entradas y salidas registradas.
     *
     * @return ResponseEntity con el PDF generado y headers para visualización inline
     * @throws RuntimeException si hay error al generar el PDF
     */
    public ResponseEntity<byte[]> generateStockTonerReport() {
        try {
            // Obtener todos los stocks de tóner usando el servicio
            List<StockTonerResponse> stockList = stockTonerService.findAllExistencias();

            // Generar el PDF
            byte[] pdfBytes = stockTonerReportGenerator.generateReport(stockList);

            // Nombre del archivo
            String fileName = "inventario_toner.pdf";

            return buildPdfResponse(pdfBytes, fileName, false);

        } catch (IOException e) {
            throw new RuntimeException("Error al generar reporte de inventario de tóner: " + e.getMessage(), e);
        }
    }

    /**
     * Genera un reporte PDF de anotaciones con múltiples filtros opcionales.
     *
     * @param fechaInicio fecha de inicio del rango de consulta
     * @param fechaFin fecha de fin del rango de consulta
     * @param idUsuario ID del usuario que creó las anotaciones (opcional)
     * @param contenido texto a buscar en el contenido usando LIKE (opcional)
     * @param pagina número de página para filtrar (opcional)
     * @param etiquetas etiquetas a filtrar (opcional)
     * @param importante filtrar solo anotaciones importantes (opcional)
     * @return ResponseEntity con el PDF generado y headers configurados
     * @throws RuntimeException si hay error al generar el PDF
     */
    public ResponseEntity<byte[]> generateAnotacionesReport(LocalDate fechaInicio, LocalDate fechaFin,
                                                          Long idUsuario, String contenido, Integer pagina,
                                                          String etiquetas, Boolean importante) {
        try {
            // Convertir LocalDate a LocalDateTime para la consulta
            LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
            LocalDateTime fechaFinDateTime = fechaFin.plusDays(1).atStartOfDay().minusSeconds(1);

            // Obtener todas las anotaciones con los filtros aplicados
            List<Anotacion> anotacionesEntities = anotacionRepository.findForReport(
                fechaInicioDateTime, fechaFinDateTime, idUsuario, contenido, pagina, etiquetas, importante,
                org.springframework.data.domain.Pageable.unpaged()
            ).getContent();

            // Convertir entidades a DTOs para el reporte
            List<AnotacionReportDTO> anotacionesDTOs = new ArrayList<>();
            for (Anotacion anotacion : anotacionesEntities) {
                AnotacionReportDTO dto = AnotacionReportDTO.builder()
                        .idAnotacion(anotacion.getIdAnotacion())
                        .fechaAnotacion(anotacion.getFechaAnotacion())
                        .usuario(anotacion.getUsuario() != null ? anotacion.getUsuario().getNombre() : "Usuario desconocido")
                        .pagina(anotacion.getPagina())
                        .etiquetas(anotacion.getEtiquetas())
                        .importante(anotacion.getImportante())
                        .contenido(anotacion.getContenido())
                        .build();
                anotacionesDTOs.add(dto);
            }

            // Generar el PDF
            byte[] pdfBytes = anotacionesReportGenerator.generateReport(
                anotacionesDTOs, fechaInicio, fechaFin, idUsuario, contenido, pagina, etiquetas, importante
            );

            // Generar nombre del archivo
            String fileName = String.format("anotaciones_%s_%s.pdf",
                fechaInicio, fechaFin);

            return buildPdfResponse(pdfBytes, fileName, false);

        } catch (IOException e) {
            throw new RuntimeException("Error al generar reporte de anotaciones: " + e.getMessage(), e);
        }
    }

    /**
     * Genera un reporte PDF de bitácora de servicios informáticos con múltiples filtros opcionales.
     *
     * @param fechaInicio fecha de inicio del rango de consulta (createdAt)
     * @param fechaFin fecha de fin del rango de consulta (createdAt)
     * @param idUsuario ID del usuario que reportó (reportadoPor) (opcional)
     * @param contenido texto a buscar en el comentario usando LIKE (opcional)
     * @param estado estado de la bitácora como String (opcional)
     * @param idTipoIncidente ID del tipo de incidente (opcional)
     * @return ResponseEntity con el PDF generado y headers configurados
     * @throws RuntimeException si hay error al generar el PDF
     */
    public ResponseEntity<byte[]> generateBitacoraServicioReport(LocalDate fechaInicio, LocalDate fechaFin,
                                                                Long idUsuario, String contenido, String estado,
                                                                Long idTipoIncidente) {
        try {
            // Convertir LocalDate a LocalDateTime para la consulta
            LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
            LocalDateTime fechaFinDateTime = fechaFin.plusDays(1).atStartOfDay().minusSeconds(1);

            // Convertir estado String a EstadoBitacora enum si es proporcionado
            EstadoBitacora estadoEnum = null;
            if (estado != null && !estado.trim().isEmpty()) {
                try {
                    estadoEnum = EstadoBitacora.valueOf(estado.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Estado inválido: " + estado +
                        ". Valores válidos: NORMAL, DEGRADADO, CAIDO, MANTENIMIENTO, SOPORTE_TECNICO_PROV");
                }
            }

            // Obtener todas las bitácoras con los filtros aplicados
            List<BitacoraServicio> bitacorasEntities = bitacoraServicioRepository.findForReport(
                fechaInicioDateTime, fechaFinDateTime, idUsuario, contenido,
                estadoEnum != null ? estadoEnum.name() : null, idTipoIncidente
            );

            // Convertir entidades a DTOs para el reporte
            List<BitacoraServicioReportDTO> bitacorasDTOs = new ArrayList<>();
            for (BitacoraServicio bitacora : bitacorasEntities) {
                BitacoraServicioReportDTO dto = BitacoraServicioReportDTO.builder()
                        .id(bitacora.getId())
                        .servicio(bitacora.getServicio() != null ? bitacora.getServicio().getNombre() : "N/A")
                        .tipoIncidente(bitacora.getTipoIncidente() != null ? bitacora.getTipoIncidente().getNombre() : "N/A")
                        .estado(bitacora.getEstado() != null ? bitacora.getEstado().name() : "N/A")
                        .comentario(bitacora.getComentario())
                        .fechaInicio(bitacora.getFechaInicio())
                        .fechaFin(bitacora.getFechaFin())
                        .reportadoPor(bitacora.getReportadoPor() != null ? bitacora.getReportadoPor().getNombre() : "N/A")
                        .createdAt(bitacora.getCreatedAt())
                        .build();
                bitacorasDTOs.add(dto);
            }

            // Generar el PDF
            byte[] pdfBytes = bitacoraServicioReportGenerator.generateReport(
                bitacorasDTOs, fechaInicio, fechaFin, idUsuario, contenido, estado, idTipoIncidente
            );

            // Generar nombre del archivo
            String fileName = String.format("bitacora_servicios_%s_%s.pdf",
                fechaInicio, fechaFin);

            return buildPdfResponse(pdfBytes, fileName, false);

        } catch (IOException e) {
            throw new RuntimeException("Error al generar reporte de bitácora de servicios: " + e.getMessage(), e);
        }
    }

    /**
     * Genera un reporte PDF con la información detallada de un dispositivo y su historial de ubicaciones.
     * Si el dispositivo no existe, genera un PDF con mensaje de error.
     *
     * @param idDispositivo ID del dispositivo a reportar
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateDispositivoReport(Long idDispositivo) {
        try {
            // Buscar el dispositivo
            Optional<Dispositivo> dispositivoOpt = dispositivoRepository.findById(idDispositivo);

            if (dispositivoOpt.isEmpty()) {
                // Generar PDF con mensaje de error
                byte[] pdfBytes = dispositivoReportGenerator.generateNotFoundReport(idDispositivo);
                return buildPdfResponse(pdfBytes, "dispositivo_no_encontrado.pdf", false);
            }

            Dispositivo dispositivo = dispositivoOpt.get();

            // Obtener historial de ubicaciones ordenado por fecha de entrada descendente
            List<HistorialUbicacion> historial = historialUbicacionRepository
                    .findAllByDispositivoIdOrderByFechaEntradaDesc(idDispositivo);

            // Convertir a DTO
            DispositivoReportDTO dto = convertToDispositivoReportDTO(dispositivo, historial);

            // Generar el PDF
            byte[] pdfBytes = dispositivoReportGenerator.generateReport(dto);

            // Nombre del archivo
            String fileName = String.format("dispositivo_%d_%s.pdf",
                idDispositivo,
                dispositivo.getInventario() != null ? dispositivo.getInventario() : "sin_inventario");

            return buildPdfResponse(pdfBytes, fileName, false);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte del dispositivo: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Convierte un dispositivo y su historial de ubicaciones a DTO para el reporte,
     * calculando los días en cada ubicación.
     */
    private DispositivoReportDTO convertToDispositivoReportDTO(Dispositivo dispositivo,
                                                                 List<HistorialUbicacion> historial) {
        DispositivoReportDTO.DispositivoReportDTOBuilder builder = DispositivoReportDTO.builder()
                .idDispositivo(dispositivo.getIdDispositivo())
                .marca(dispositivo.getMarca())
                .modelo(dispositivo.getModelo())
                .numeroSerie(dispositivo.getNumeroSerie())
                .inventario(dispositivo.getInventario())
                .fechaCompra(dispositivo.getFechaCompra())
                .costo(dispositivo.getCosto())
                .notas(dispositivo.getNotas())
                .fechaRegistro(dispositivo.getFechaRegistro())
                .fechaBaja(dispositivo.getFechaBaja());

        // Información del tipo de dispositivo
        if (dispositivo.getTipoDispositivo() != null) {
            builder.tipoDispositivo(dispositivo.getTipoDispositivo().getNombreTipo());
        }

        // Información del estado actual
        if (dispositivo.getTipoEstado() != null) {
            builder.estadoActual(dispositivo.getTipoEstado().getNombreEstado());
        }

        // Convertir historial de ubicaciones
        List<DispositivoReportDTO.UbicacionHistorialDTO> ubicacionesDTOs = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();

        for (HistorialUbicacion hu : historial) {
            DispositivoReportDTO.UbicacionHistorialDTO.UbicacionHistorialDTOBuilder ubBuilder =
                    DispositivoReportDTO.UbicacionHistorialDTO.builder()
                            .idHistorial(hu.getIdHistorial())
                            .fechaEntrada(hu.getFechaEntrada())
                            .fechaSalida(hu.getFechaSalida());

            // Información del lugar
            if (hu.getLugar() != null) {
                ubBuilder.nombreLugar(hu.getLugar().getNombreLugar())
                        .piso(hu.getLugar().getPiso())
                        .edificio(hu.getLugar().getEdificio());

                if (hu.getLugar().getDepartamento() != null) {
                    ubBuilder.departamento(hu.getLugar().getDepartamento().getNombreDepartamento());
                }
            }

            // Información del usuario que asignó
            if (hu.getUsuario() != null) {
                ubBuilder.usuarioAsigno(hu.getUsuario().getNombreCompleto());
            }

            // Calcular días en el lugar
            LocalDateTime fechaFin = hu.getFechaSalida() != null ? hu.getFechaSalida() : ahora;
            if (hu.getFechaEntrada() != null) {
                long dias = ChronoUnit.DAYS.between(hu.getFechaEntrada(), fechaFin);
                ubBuilder.diasEnLugar(dias);
            }

            ubicacionesDTOs.add(ubBuilder.build());
        }

        builder.historialUbicaciones(ubicacionesDTOs);

        return builder.build();
    }

    /**
     * Genera un reporte PDF con el historial de ubicaciones de dispositivos en un rango de fechas.
     * Incluye información del dispositivo, lugar, departamento, fechas de entrada/salida y días en cada ubicación.
     *
     * @param fechaInicio fecha de inicio del rango (por fechaEntrada)
     * @param fechaFin    fecha de fin del rango (por fechaEntrada)
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateHistorialUbicacionReport(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            // Convertir fechas a LocalDateTime para la consulta
            LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
            LocalDateTime fechaFinDateTime = fechaFin.atTime(23, 59, 59);

            // Obtener historial de ubicaciones del repositorio ordenado descendente
            List<HistorialUbicacion> historial = historialUbicacionRepository
                    .findByFechaEntradaBetweenOrderByFechaEntradaDesc(fechaInicioDateTime, fechaFinDateTime);

            // Convertir a DTOs
            List<HistorialUbicacionReportDTO> dtos = convertToHistorialUbicacionReportDTOs(historial);

            // Generar el PDF
            byte[] pdfBytes = historialUbicacionReportGenerator.generateReport(dtos, fechaInicio, fechaFin);

            // Nombre del archivo con las fechas
            String fileName = String.format("historial_ubicaciones_%s_%s.pdf",
                    fechaInicio.toString(), fechaFin.toString());

            return buildPdfResponse(pdfBytes, fileName, false);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte de historial de ubicaciones: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Convierte una lista de entidades HistorialUbicacion a DTOs para el reporte,
     * calculando los días en cada ubicación (usando fechaSalida o fecha actual si es null).
     */
    private List<HistorialUbicacionReportDTO> convertToHistorialUbicacionReportDTOs(List<HistorialUbicacion> historial) {
        List<HistorialUbicacionReportDTO> dtos = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();

        for (HistorialUbicacion h : historial) {
            HistorialUbicacionReportDTO.HistorialUbicacionReportDTOBuilder builder = HistorialUbicacionReportDTO.builder()
                    .idHistorial(h.getIdHistorial())
                    .fechaEntrada(h.getFechaEntrada())
                    .fechaSalida(h.getFechaSalida());

            // Información del dispositivo
            if (h.getDispositivo() != null) {
                builder.idDispositivo(h.getDispositivo().getIdDispositivo())
                        .inventario(h.getDispositivo().getInventario())
                        .numeroSerie(h.getDispositivo().getNumeroSerie());

                // Marca y modelo combinados
                String marca = h.getDispositivo().getMarca();
                String modelo = h.getDispositivo().getModelo();
                if (marca != null && modelo != null) {
                    builder.marcaModelo(marca + " " + modelo);
                } else if (marca != null) {
                    builder.marcaModelo(marca);
                } else if (modelo != null) {
                    builder.marcaModelo(modelo);
                }

                // Tipo de dispositivo
                if (h.getDispositivo().getTipoDispositivo() != null) {
                    builder.tipoDispositivo(h.getDispositivo().getTipoDispositivo().getNombreTipo());
                }
            }

            // Información del lugar
            if (h.getLugar() != null) {
                builder.nombreLugar(h.getLugar().getNombreLugar())
                        .piso(h.getLugar().getPiso())
                        .edificio(h.getLugar().getEdificio());

                if (h.getLugar().getDepartamento() != null) {
                    builder.departamento(h.getLugar().getDepartamento().getNombreDepartamento());
                }
            }

            // Información del usuario que asignó
            if (h.getUsuario() != null) {
                builder.usuarioAsigno(h.getUsuario().getNombreCompleto());
            }

            // Calcular días en el lugar
            LocalDateTime fechaFin = h.getFechaSalida() != null ? h.getFechaSalida() : ahora;
            if (h.getFechaEntrada() != null) {
                long dias = ChronoUnit.DAYS.between(h.getFechaEntrada(), fechaFin);
                builder.diasEnLugar(dias);
            }

            dtos.add(builder.build());
        }

        return dtos;
    }

    /**
     * Genera un reporte PDF de tickets en un rango de fechas con múltiples filtros opcionales.
     * Usa query nativa considerando soft delete.
     *
     * @param fechaInicio fecha de inicio del rango (fechaCreacion)
     * @param fechaFin fecha de fin del rango (fechaCreacion)
     * @param idDepartamento ID del departamento a filtrar (opcional)
     * @param idEstado ID del estado del ticket a filtrar (opcional)
     * @param idPrioridad ID de la prioridad a filtrar (opcional)
     * @param descripcion texto a buscar en la descripción (opcional, usa ILIKE)
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateTicketReport(LocalDate fechaInicio, LocalDate fechaFin,
                                                        Integer idDepartamento, Integer idEstado,
                                                        Integer idPrioridad, String descripcion) {
        try {
            // Convertir fechas a LocalDateTime para la consulta
            LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
            LocalDateTime fechaFinDateTime = fechaFin.atTime(23, 59, 59);

            // Obtener tickets del repositorio usando query nativa
            List<Ticket> tickets = ticketRepository.findTicketsForReport(
                    fechaInicioDateTime, fechaFinDateTime,
                    idDepartamento, idEstado, idPrioridad, descripcion
            );

            // Convertir a DTOs
            List<TicketReportDTO> dtos = convertToTicketReportDTOs(tickets);

            // Construir información de filtros aplicados
            String filtros = buildTicketFiltersInfo(idDepartamento, idEstado, idPrioridad, descripcion);

            // Generar el PDF
            byte[] pdfBytes = ticketReportGenerator.generateReport(dtos, fechaInicio, fechaFin, filtros);

            // Nombre del archivo con las fechas
            String fileName = String.format("tickets_%s_%s.pdf",
                    fechaInicio.toString(), fechaFin.toString());

            return buildPdfResponse(pdfBytes, fileName, false);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte de tickets: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Convierte una lista de entidades Ticket a DTOs para el reporte.
     */
    private List<TicketReportDTO> convertToTicketReportDTOs(List<Ticket> tickets) {
        List<TicketReportDTO> dtos = new ArrayList<>();

        for (Ticket t : tickets) {
            TicketReportDTO.TicketReportDTOBuilder builder = TicketReportDTO.builder()
                    .idTicket(t.getIdTicket())
                    .asunto(t.getAsunto())
                    .descripcion(t.getDescripcion())
                    .fechaCreacion(t.getFechaCreacion())
                    .fechaCierre(t.getFechaCierre());

            // Usuario creador
            if (t.getUsuarioCreador() != null) {
                builder.usuarioCreador(t.getUsuarioCreador().getNombreCompleto());
            }

            // Usuario asignado
            if (t.getUsuarioAsignado() != null) {
                builder.usuarioAsignado(t.getUsuarioAsignado().getNombreCompleto());
            }

            // Departamento
            if (t.getDepartamento() != null) {
                builder.departamento(t.getDepartamento().getNombreDepartamento());
            }

            // Estado del ticket
            if (t.getEstadoTicket() != null) {
                builder.estadoTicket(t.getEstadoTicket().getNombre())
                       .colorEstado(t.getEstadoTicket().getColor());
            }

            // Prioridad
            if (t.getPrioridad() != null) {
                builder.prioridad(t.getPrioridad().getNombrePrioridad());
            }

            dtos.add(builder.build());
        }

        return dtos;
    }

    /**
     * Construye la información de filtros aplicados para el reporte de tickets.
     */
    private String buildTicketFiltersInfo(Integer idDepartamento, Integer idEstado,
                                          Integer idPrioridad, String descripcion) {
        List<String> filtros = new ArrayList<>();

        if (idDepartamento != null) {
            filtros.add("Depto. ID: " + idDepartamento);
        }
        if (idEstado != null) {
            filtros.add("Estado ID: " + idEstado);
        }
        if (idPrioridad != null) {
            filtros.add("Prioridad ID: " + idPrioridad);
        }
        if (descripcion != null && !descripcion.isEmpty()) {
            filtros.add("Descripción contiene: \"" + descripcion + "\"");
        }

        return filtros.isEmpty() ? null : String.join(" | ", filtros);
    }

    /**
     * Genera un reporte PDF de dispositivos registrados en un rango de fechas con múltiples filtros opcionales.
     * Usa query nativa considerando soft delete.
     *
     * @param fechaInicio fecha de inicio del rango (fechaRegistro)
     * @param fechaFin fecha de fin del rango (fechaRegistro)
     * @param marca marca del dispositivo a filtrar (opcional, usa ILIKE)
     * @param modelo modelo del dispositivo a filtrar (opcional, usa ILIKE)
     * @param numeroserie número de serie del dispositivo a filtrar (opcional, usa ILIKE)
     * @param inventario inventario del dispositivo a filtrar (opcional, usa ILIKE)
     * @param notas texto a buscar en las notas del dispositivo (opcional, usa ILIKE)
     * @param idTipoEstado ID del estado del dispositivo a filtrar (opcional)
     * @return ResponseEntity con el PDF y headers configurados para visualización inline
     */
    public ResponseEntity<byte[]> generateDispositivoRegistroReport(LocalDate fechaInicio, LocalDate fechaFin,
                                                                     String marca, String modelo,
                                                                     String numeroserie, String inventario,
                                                                     String notas, Integer idTipoEstado) {
        try {
            // Convertir fechas a LocalDateTime para la consulta
            LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
            LocalDateTime fechaFinDateTime = fechaFin.atTime(23, 59, 59);

            // Obtener dispositivos del repositorio usando query nativa
            List<Dispositivo> dispositivos = dispositivoRepository.findDispositivosForReport(
                    fechaInicioDateTime, fechaFinDateTime,
                    marca, modelo, numeroserie, inventario, notas, idTipoEstado
            );

            // Convertir a DTOs
            List<DispositivoRegistroReportDTO> dtos = convertToDispositivoRegistroReportDTOs(dispositivos);

            // Construir información de filtros aplicados
            String filtros = buildDispositivoRegistroFiltersInfo(marca, modelo, numeroserie, inventario, notas, idTipoEstado);

            // Generar el PDF
            byte[] pdfBytes = dispositivoRegistroReportGenerator.generateReport(dtos, fechaInicio, fechaFin, filtros);

            // Nombre del archivo con las fechas
            String fileName = String.format("registro_dispositivos_%s_%s.pdf",
                    fechaInicio.toString(), fechaFin.toString());

            return buildPdfResponse(pdfBytes, fileName, false);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el reporte de registro de dispositivos: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Convierte una lista de entidades Dispositivo a DTOs para el reporte de registro.
     */
    private List<DispositivoRegistroReportDTO> convertToDispositivoRegistroReportDTOs(List<Dispositivo> dispositivos) {
        List<DispositivoRegistroReportDTO> dtos = new ArrayList<>();

        for (Dispositivo d : dispositivos) {
            DispositivoRegistroReportDTO.DispositivoRegistroReportDTOBuilder builder = DispositivoRegistroReportDTO.builder()
                    .idDispositivo(d.getIdDispositivo())
                    .marca(d.getMarca())
                    .modelo(d.getModelo())
                    .numeroSerie(d.getNumeroSerie())
                    .inventario(d.getInventario())
                    .fechaCompra(d.getFechaCompra())
                    .fechaRegistro(d.getFechaRegistro())
                    .costo(d.getCosto())
                    .notas(d.getNotas());

            // Tipo de dispositivo
            if (d.getTipoDispositivo() != null) {
                builder.tipoDispositivo(d.getTipoDispositivo().getNombreTipo());
            }

            // Estado del dispositivo
            if (d.getTipoEstado() != null) {
                builder.estadoDispositivo(d.getTipoEstado().getNombreEstado());
            }

            dtos.add(builder.build());
        }

        return dtos;
    }

    /**
     * Construye la información de filtros aplicados para el reporte de registro de dispositivos.
     */
    private String buildDispositivoRegistroFiltersInfo(String marca, String modelo, String numeroserie,
                                                        String inventario, String notas, Integer idTipoEstado) {
        List<String> filtros = new ArrayList<>();

        if (marca != null && !marca.isEmpty()) {
            filtros.add("Marca contiene: \"" + marca + "\"");
        }
        if (modelo != null && !modelo.isEmpty()) {
            filtros.add("Modelo contiene: \"" + modelo + "\"");
        }
        if (numeroserie != null && !numeroserie.isEmpty()) {
            filtros.add("N. Serie contiene: \"" + numeroserie + "\"");
        }
        if (inventario != null && !inventario.isEmpty()) {
            filtros.add("Inventario contiene: \"" + inventario + "\"");
        }
        if (notas != null && !notas.isEmpty()) {
            filtros.add("Notas contiene: \"" + notas + "\"");
        }
        if (idTipoEstado != null) {
            filtros.add("Estado ID: " + idTipoEstado);
        }

        return filtros.isEmpty() ? null : String.join(" | ", filtros);
    }

    /**
     * Construye un ResponseEntity con los headers HTTP apropiados para un PDF.
     * Servirá para todos los reportes generados.
     *
     * @param pdfBytes bytes del PDF generado
     * @param fileName nombre del archivo para Content-Disposition
     * @param asAttachment true para descarga forzada, false para visualización inline
     * @return ResponseEntity configurado
     */
    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdfBytes, String fileName, boolean asAttachment) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String disposition = asAttachment ? "attachment" : "inline";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + fileName + "\"");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}

