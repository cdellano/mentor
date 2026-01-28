package saul.reports.generator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import saul.pdf.renderer.*;
import saul.pdf.service.PDFBoxService;
import saul.reports.dto.MantenimientoReportDTO;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generador de reportes PDF para mantenimientos de dispositivos.
 * Incluye encabezado con imágenes institucionales e información del hospital.
 */
@Component
@PropertySource("classpath:reports.properties")
public class MantenimientoReportGenerator {

    private final PDFBoxService pdfBoxService;

    // Configuración del encabezado desde reports.properties
    @Value("${report.header.hospital}")
    private String hospitalName;

    @Value("${report.header.department}")
    private String departmentName;

    @Value("${report.header.address}")
    private String address;

    @Value("${report.header.phone}")
    private String phone;

    @Value("${report.title.mantenimiento}")
    private String reportTitle;

    @Value("${report.qr.text1}")
    private String qrText;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MantenimientoReportGenerator(PDFBoxService pdfBoxService) {
        this.pdfBoxService = pdfBoxService;
    }

    /**
     * Genera un reporte PDF de mantenimientos realizados en un rango de fechas.
     *
     * @param mantenimientos lista de mantenimientos a incluir en el reporte
     * @param fechaInicio    fecha de inicio del rango
     * @param fechaFin       fecha de fin del rango
     * @return byte[] del PDF generado
     * @throws IOException si ocurre un error al generar el PDF
     */
    public byte[] generateReport(List<MantenimientoReportDTO> mantenimientos,
                                  LocalDate fechaInicio, LocalDate fechaFin) throws IOException {
        return generateReport(mantenimientos, fechaInicio, fechaFin, null);
    }

    /**
     * Genera un reporte PDF de mantenimientos realizados en un rango de fechas con filtro opcional por tipo de dispositivo.
     *
     * @param mantenimientos        lista de mantenimientos a incluir en el reporte
     * @param fechaInicio           fecha de inicio del rango
     * @param fechaFin              fecha de fin del rango
     * @param tipoDispositivoInfo   información del tipo de dispositivo filtrado (opcional)
     * @return byte[] del PDF generado
     * @throws IOException si ocurre un error al generar el PDF
     */
    public byte[] generateReport(List<MantenimientoReportDTO> mantenimientos,
                                  LocalDate fechaInicio, LocalDate fechaFin, String tipoDispositivoInfo) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Configuración de página tamaño carta con márgenes
            PDFPageConfig pageConfig = new PDFPageConfig()
                    .pageSize(PDRectangle.LETTER)
                    .margins(40f);

            try (PDFPaginator paginator = new PDFPaginator(document, pageConfig)) {
                // Dibujar encabezado
                drawHeader(paginator);

                // Agregar título del reporte con información del tipo de dispositivo si aplica
                drawTitle(paginator, fechaInicio, fechaFin, tipoDispositivoInfo);

                // Agregar tabla de mantenimientos
                if (mantenimientos != null && !mantenimientos.isEmpty()) {
                    drawMantenimientosTable(paginator, mantenimientos);
                } else {
                    drawNoDataMessage(paginator);
                }
            }

            // Agregar numeración de páginas después de generar todo el contenido
            addPageNumbers(document);

            document.save(baos);
            return baos.toByteArray();
        }
    }

    /**
     * Dibuja el encabezado con las imágenes i1 (izquierda) e i2 (derecha)
     * y la información del hospital en el centro.
     */
    private void drawHeader(PDFPaginator paginator) throws IOException {
        PDDocument doc = paginator.getDocument();
        PDPageContentStream cs = paginator.getContentStream();

        float startX = paginator.getStartX();
        float startY = paginator.getCurrentY();
        float usableWidth = paginator.getUsableWidth();

        // Tamaño de las imágenes
        float imageWidth = 70f;
        float imageHeight = 70f;

        // Cargar imágenes
        byte[] imageLeft = loadImageFromResources("img/i1.png");
        byte[] imageRight = loadImageFromResources("img/i2.png");

        // Configuración para imagen izquierda
        ImageConfig leftConfig = new ImageConfig()
                .size(imageWidth, imageHeight)
                .scaleMode(ImageConfig.ScaleMode.FIT_BOX);

        // Configuración para imagen derecha
        ImageConfig rightConfig = new ImageConfig()
                .size(imageWidth, imageHeight)
                .scaleMode(ImageConfig.ScaleMode.FIT_BOX);

        // Dibujar imagen izquierda
        pdfBoxService.insertImage(doc, cs, imageLeft, startX, startY, leftConfig);

        // Dibujar imagen derecha
        float rightX = startX + usableWidth - imageWidth;
        pdfBoxService.insertImage(doc, cs, imageRight, rightX, startY, rightConfig);

        // Área central para el texto (entre las dos imágenes)
        float textX = startX + imageWidth + 10;
        float textWidth = usableWidth - (imageWidth * 2) - 20;
        float textY = startY - 5;

        // Configuración de texto para el encabezado
        TextBoxConfig headerConfig = TextBoxConfig.normal()
                .fontStyle(TextBoxConfig.FontStyle.BOLD)
                .fontSize(11f)
                .textAlign(TextBoxConfig.TextAlign.CENTER);

        TextBoxConfig subHeaderConfig = TextBoxConfig.normal()
                .fontSize(9f)
                .textAlign(TextBoxConfig.TextAlign.CENTER);

        // Escribir información del hospital centrada
        writeHeaderText(doc, cs, hospitalName, textX, textY, textWidth, headerConfig);
        textY -= 14;
        writeHeaderText(doc, cs, departmentName, textX, textY, textWidth, subHeaderConfig);
        textY -= 12;
        writeHeaderText(doc, cs, address, textX, textY, textWidth, subHeaderConfig);
        textY -= 12;
        writeHeaderText(doc, cs, phone, textX, textY, textWidth, subHeaderConfig);

        // Avanzar Y después del encabezado
        paginator.advanceY(imageHeight + 15);

        // Dibujar línea separadora
        LineConfig lineConfig = new LineConfig()
                .color(new Color(0, 102, 153))
                .thickness(1.5f);
        pdfBoxService.drawHorizontalLine(cs, startX, paginator.getCurrentY(), usableWidth, lineConfig);
        paginator.advanceY(15);
    }

    /**
     * Escribe texto del encabezado centrado en un área específica.
     */
    private void writeHeaderText(PDDocument doc, PDPageContentStream cs, String text,
                                  float x, float y, float width, TextBoxConfig config) throws IOException {
        pdfBoxService.writeTextBox(doc, cs, text, x, y, width, 12, config);
    }

    /**
     * Dibuja el título del reporte y un código QR centrado debajo.
     */
    private void drawTitle(PDFPaginator paginator) throws IOException {
        drawTitle(paginator, null, null, null);
    }

    /**
     * Dibuja el título del reporte con información opcional del tipo de dispositivo filtrado.
     */
    private void drawTitle(PDFPaginator paginator, LocalDate fechaInicio, LocalDate fechaFin, String tipoDispositivoInfo) throws IOException {
        TextBoxConfig titleConfig = TextBoxConfig.title()
                .fontStyle(TextBoxConfig.FontStyle.BOLD)
                .fontSize(16f)
                .textColor(new Color(0, 51, 102));

        pdfBoxService.writeText(paginator, reportTitle, titleConfig);
        paginator.addSpace(0.5f); // Reducido al 50% (de 15 a 7.5)

        // Agregar código QR alineado a la derecha
        drawQRCode(paginator);

        // Agregar información de filtros si están disponibles
        if (fechaInicio != null && fechaFin != null) {
            drawFilterInfo(paginator, fechaInicio, fechaFin, tipoDispositivoInfo);
        }

        paginator.addSpace(15);
    }

    /**
     * Dibuja la información de filtros aplicados.
     */
    private void drawFilterInfo(PDFPaginator paginator, LocalDate fechaInicio, LocalDate fechaFin, String tipoDispositivoInfo) throws IOException {
        String filterInfo = String.format("Período: %s al %s",
            fechaInicio.format(DATE_FORMATTER),
            fechaFin.format(DATE_FORMATTER));

        if (tipoDispositivoInfo != null && !tipoDispositivoInfo.isEmpty()) {
            filterInfo += " | Tipo de Dispositivo: " + tipoDispositivoInfo;
        }

        TextBoxConfig filterConfig = TextBoxConfig.normal()
                .fontSize(10f)
                .textColor(Color.DARK_GRAY);

        pdfBoxService.writeText(paginator, filterInfo, filterConfig);
    }

    /**
     * Dibuja un código QR alineado a la derecha con el texto configurado en reports.properties.
     */
    private void drawQRCode(PDFPaginator paginator) throws IOException {
        PDDocument doc = paginator.getDocument();
        PDPageContentStream cs = paginator.getContentStream();

        float qrSize = 72f; // Tamaño del QR en puntos (reducido 10%)
        // Alinear a la derecha
        float rightX = paginator.getStartX() + paginator.getUsableWidth() - qrSize;
        float currentY = paginator.getCurrentY();

        // Configuración del QR
        QRCodeConfig qrConfig = QRCodeConfig.small()
                .size((int)qrSize)
                .mediumErrorCorrection()
                .margin(1);

        try {
            pdfBoxService.insertQRCode(doc, cs, qrText, rightX, currentY, qrConfig);
            paginator.advanceY(qrSize + 1);
        } catch (Exception e) {
            // Si falla la generación del QR, continuar sin él
            System.err.println("Error generando QR: " + e.getMessage());
        }
    }

    /**
     * Dibuja el rango de fechas del reporte.
     */
    private void drawDateRange(PDFPaginator paginator, LocalDate fechaInicio, LocalDate fechaFin) throws IOException {
        String dateRange = String.format("Período: %s al %s",
                fechaInicio.format(DATE_FORMATTER),
                fechaFin.format(DATE_FORMATTER));

        TextBoxConfig dateConfig = TextBoxConfig.normal()
                .fontSize(10f)
                .textColor(Color.DARK_GRAY);

        pdfBoxService.writeText(paginator, dateRange, dateConfig);
        paginator.addSpace(15);
    }

    /**
     * Dibuja la tabla de mantenimientos.
     */
    private void drawMantenimientosTable(PDFPaginator paginator, List<MantenimientoReportDTO> mantenimientos) throws IOException {
        // Preparar datos de la tabla
        List<List<String>> tableData = new ArrayList<>();

        // Encabezados
        List<String> headers = List.of(
                "ID Dispositivo", "Inv - Num. Serie", "Dispositivo", "Tipo", "Fecha Realiz.",
                "Estado", "Ubicación Actual", "Informático que realiza"
        );
        tableData.add(headers);

        // Datos
        for (MantenimientoReportDTO m : mantenimientos) {
            List<String> row = new ArrayList<>();
            row.add(m.getIdDispositivo() != null ? m.getIdDispositivo().toString() : "-");
            row.add(formatSerieInventario(m));
            row.add(formatDispositivo(m));
            row.add(m.getTipoMantenimiento() != null ? m.getTipoMantenimiento() : "-");
            row.add(m.getFechaRealizado() != null ? m.getFechaRealizado().format(DATE_FORMATTER) : "-");
            row.add(m.getEstado() != null ? m.getEstado() : "-");
            row.add(formatUbicacion(m));
            row.add(m.getUsuarioAtiende() != null ? m.getUsuarioAtiende() : "-");
            tableData.add(row);
        }

        // Configuración de la tabla con anchos de columna proporcionales (autoajustables)
        // Columnas: ID Disp(1), Inv-Serie(1.5), Dispositivo(2), Tipo(1.5), Fecha(1.5), Estado(1.2), Ubicación(2), Usuario(1.8)
        TableConfig tableConfig = TableConfig.minimal()
            .width(paginator.getUsableWidth())
            .columnWidthRatios(1f, 1.5f, 2f, 1.5f, 1.5f, 1.2f, 2f, 1.8f)
            .rowHeight(22f)
            .padding(4f)
            .fontSize(8f)
            .headerFontSize(9f)
            .headerBackground(new Color(0, 102, 153))
            .headerTextColor(Color.WHITE)
            .alternateRowColor(new Color(240, 248, 255))
            .border(TableConfig.BorderStyle.SOLID, 0.5f, new Color(180, 180, 180));

        pdfBoxService.createTable(paginator, tableData, tableConfig);
    }

    /**
     * Formatea la información de serie e inventario del dispositivo.
     */
    private String formatSerieInventario(MantenimientoReportDTO m) {
        StringBuilder sb = new StringBuilder();
        if (m.getInventario() != null && !m.getInventario().isEmpty()) {
            sb.append("Inv: ").append(m.getInventario());
        }
        if (m.getNumeroSerie() != null && !m.getNumeroSerie().isEmpty()) {
            if (sb.length() > 0) sb.append(" | ");
            sb.append("S/N: ").append(m.getNumeroSerie());
        }
        return sb.toString();
    }

    /**
     * Formatea la información del dispositivo para mostrar en la tabla.
     */
    private String formatDispositivo(MantenimientoReportDTO m) {
        StringBuilder sb = new StringBuilder();
        if (m.getTipoDispositivo() != null) {
            sb.append(m.getTipoDispositivo());
        }
        if (m.getMarca() != null) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(m.getMarca());
        }
        if (m.getModelo() != null) {
            sb.append(" ").append(m.getModelo());
        }
        return sb.length() > 0 ? sb.toString() : "-";
    }

    /**
     * Formatea la ubicación actual del dispositivo.
     */
    private String formatUbicacion(MantenimientoReportDTO m) {
        StringBuilder sb = new StringBuilder();
        if (m.getLugarActual() != null && !m.getLugarActual().isEmpty()) {
            sb.append(m.getLugarActual());
        }
        if (m.getDepartamentoActual() != null && !m.getDepartamentoActual().isEmpty()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(m.getDepartamentoActual());
        }
        return sb.length() > 0 ? sb.toString() : "Sin ubicación";
    }

    /**
     * Dibuja mensaje cuando no hay datos.
     */
    private void drawNoDataMessage(PDFPaginator paginator) throws IOException {
        TextBoxConfig noDataConfig = TextBoxConfig.normal()
                .fontSize(12f)
                .textAlign(TextBoxConfig.TextAlign.CENTER)
                .textColor(Color.GRAY);

        paginator.addSpace(50);
        pdfBoxService.writeText(paginator, "No se encontraron mantenimientos en el período especificado.", noDataConfig);
    }

    /**
     * Dibuja el pie de página con la fecha de generación.
     */
    private void drawFooter(PDFPaginator paginator) throws IOException {
        paginator.addSpace(20);

        String generatedDate = "Reporte generado el: " + LocalDate.now().format(DATE_FORMATTER);

        TextBoxConfig footerConfig = TextBoxConfig.normal()
                .fontSize(8f)
                .textColor(Color.GRAY)
                .textAlign(TextBoxConfig.TextAlign.LEFT);

        pdfBoxService.writeText(paginator, generatedDate, footerConfig);
    }

    /**
     * Agrega numeración de páginas a todo el documento.
     */
    private void addPageNumbers(PDDocument document) throws IOException {
        int totalPages = document.getNumberOfPages();

        for (int i = 0; i < totalPages; i++) {
            try (PDPageContentStream contentStream = new PDPageContentStream(
                    document, document.getPage(i), PDPageContentStream.AppendMode.APPEND, true)) {

                // Posición del número de página (parte inferior derecha)
                float pageWidth = document.getPage(i).getMediaBox().getWidth();
                float pageHeight = document.getPage(i).getMediaBox().getHeight();
                float x = pageWidth - 80f; // Margen derecho
                float y = 25f; // Margen inferior

                String pageNumber = String.format("p. %d de %d", i + 1, totalPages);

                TextBoxConfig pageNumberConfig = TextBoxConfig.normal()
                        .fontSize(8f)
                        .textColor(Color.GRAY);

                pdfBoxService.writeTextBox(document, contentStream, pageNumber,
                        x, y, 60f, 10f, pageNumberConfig);

                // Agregar fecha de generación en la parte inferior izquierda de cada página
                String generatedDate = "Generado: " + LocalDate.now().format(DATE_FORMATTER);
                pdfBoxService.writeTextBox(document, contentStream, generatedDate,
                        40f, y, 150f, 10f, pageNumberConfig);
            }
        }
    }

    /**
     * Carga una imagen desde el directorio resources.
     */
    private byte[] loadImageFromResources(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (InputStream is = resource.getInputStream()) {
            return is.readAllBytes();
        }
    }
}
