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
import saul.reports.dto.HistorialUbicacionReportDTO;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generador de reportes PDF para historial de ubicaciones de dispositivos.
 * Crea reportes con encabezado estándar del hospital y tabla de movimientos de dispositivos.
 */
@Component
@PropertySource("classpath:reports.properties")
public class HistorialUbicacionReportGenerator {

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

    @Value("${report.title.historial}")
    private String reportTitle;

    @Value("${report.qr.text1}")
    private String qrText;

    // Color negro-anaranjado para bordes de tabla
    private static final Color DARK_ORANGE = new Color(255, 140, 0);
    private static final Color LIGHT_ORANGE = new Color(255, 245, 238);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public HistorialUbicacionReportGenerator(PDFBoxService pdfBoxService) {
        this.pdfBoxService = pdfBoxService;
    }

    /**
     * Genera un reporte PDF con el historial de ubicaciones de dispositivos.
     *
     * @param historial lista de registros de historial de ubicaciones
     * @param fechaInicio fecha de inicio del filtro
     * @param fechaFin fecha de fin del filtro
     * @return bytes del PDF generado
     * @throws IOException si hay error al generar el PDF
     */
    public byte[] generateReport(List<HistorialUbicacionReportDTO> historial, LocalDate fechaInicio, LocalDate fechaFin) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Configuración de página tamaño carta HORIZONTAL con márgenes
            PDRectangle landscapeLetter = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
            PDFPageConfig pageConfig = new PDFPageConfig()
                    .pageSize(landscapeLetter)
                    .margins(40f);

            try (PDFPaginator paginator = new PDFPaginator(document, pageConfig)) {
                // Dibujar encabezado
                drawHeader(paginator);

                // Agregar título del reporte
                drawTitle(paginator);

                // Agregar código QR
                drawQRCode(paginator);

                // Agregar información de filtros
                drawFiltersInfo(paginator, fechaInicio, fechaFin);

                // Agregar tabla de historial
                if (historial != null && !historial.isEmpty()) {
                    drawHistorialTable(paginator, historial);
                } else {
                    drawNoDataMessage(paginator);
                }

                // Dibujar pie de página
                drawFooter(paginator);

                // Agregar numeración de páginas después de generar todo el contenido
                addPageNumbers(document);
            }

            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void drawHeader(PDFPaginator paginator) throws IOException {
        PDDocument doc = paginator.getDocument();
        PDPageContentStream cs = paginator.getContentStream();

        float startX = paginator.getStartX();
        float startY = paginator.getCurrentY();
        float usableWidth = paginator.getUsableWidth();

        // Tamaño de las imágenes
        float imageWidth = 60f;
        float imageHeight = 60f;

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

        // Posiciones de las imágenes
        float rightImageX = startX + usableWidth - imageWidth;
        float imageY = startY - imageHeight;

        // Dibujar las imágenes
        pdfBoxService.insertImage(doc, cs, imageLeft, startX, startY, leftConfig);
        pdfBoxService.insertImage(doc, cs, imageRight, rightImageX, startY, rightConfig);

        // Configurar el área para el texto del hospital (entre las imágenes)
        float textAreaX = startX + imageWidth + 10f;
        float textAreaWidth = rightImageX - textAreaX - 10f;
        float textAreaY = startY - 10f;

        // Configuración de texto para el encabezado del hospital
        TextBoxConfig hospitalConfig = TextBoxConfig.title()
                .fontSize(14f)
                .fontStyle(TextBoxConfig.FontStyle.BOLD)
                .textAlign(TextBoxConfig.TextAlign.CENTER);

        TextBoxConfig departmentConfig = TextBoxConfig.normal()
                .fontSize(12f)
                .textAlign(TextBoxConfig.TextAlign.CENTER);

        TextBoxConfig addressConfig = TextBoxConfig.normal()
                .fontSize(10f)
                .textAlign(TextBoxConfig.TextAlign.CENTER);

        // Escribir textos del encabezado usando writeTextBox para centrarlos en el área disponible
        float textHeight = 20f;
        pdfBoxService.writeTextBox(doc, cs, hospitalName, textAreaX, textAreaY, textAreaWidth, textHeight, hospitalConfig);
        textAreaY -= 16f;

        pdfBoxService.writeTextBox(doc, cs, departmentName, textAreaX, textAreaY, textAreaWidth, textHeight, departmentConfig);
        textAreaY -= 14f;

        pdfBoxService.writeTextBox(doc, cs, address, textAreaX, textAreaY, textAreaWidth, textHeight, addressConfig);
        textAreaY -= 12f;

        pdfBoxService.writeTextBox(doc, cs, phone, textAreaX, textAreaY, textAreaWidth, textHeight, addressConfig);

        // Actualizar posición Y del paginador
        paginator.setCurrentY(imageY - 20f);
    }

    private void drawTitle(PDFPaginator paginator) throws IOException {
        TextBoxConfig titleConfig = TextBoxConfig.title()
                .fontStyle(TextBoxConfig.FontStyle.BOLD)
                .fontSize(16f)
                .textAlign(TextBoxConfig.TextAlign.CENTER)
                .textColor(new Color(0, 51, 102));

        pdfBoxService.writeText(paginator, reportTitle, titleConfig);
        paginator.addSpace(7.5f);
    }

    private void drawQRCode(PDFPaginator paginator) throws IOException {
        float qrSize = 45f; // Reducido 10% del tamaño original

        // Configuración del QR con tamaño reducido
        QRCodeConfig qrConfig = QRCodeConfig.medium()
                .size((int) qrSize)
                .margin(0);

        // Para posicionar el QR a la derecha, usar el método específico
        pdfBoxService.insertQRCodeRight(paginator, qrText, qrConfig);

        // Reducir separación del QR del título al 50%
        paginator.addSpace(12.5f);
    }

    private void drawFiltersInfo(PDFPaginator paginator, LocalDate fechaInicio, LocalDate fechaFin) throws IOException {
        StringBuilder filterInfo = new StringBuilder();
        filterInfo.append("Período de fechas de entrada: ").append(fechaInicio.format(DATE_FORMATTER))
                  .append(" - ").append(fechaFin.format(DATE_FORMATTER));

        TextBoxConfig filterConfig = TextBoxConfig.normal()
                .fontSize(9f)
                .textAlign(TextBoxConfig.TextAlign.LEFT)
                .textColor(Color.DARK_GRAY);

        pdfBoxService.writeText(paginator, filterInfo.toString(), filterConfig);
        paginator.addSpace(15f);
    }

    private void drawHistorialTable(PDFPaginator paginator, List<HistorialUbicacionReportDTO> historial) throws IOException {
        // Preparar datos de la tabla
        List<List<String>> tableData = new ArrayList<>();

        // Agregar encabezados
        List<String> headers = List.of("ID", "Dispositivo", "Tipo", "Lugar", "Depto.", "Piso/Edificio", "F. Entrada", "F. Salida", "Días", "Usuario");
        tableData.add(headers);

        // Agregar filas de datos
        for (HistorialUbicacionReportDTO h : historial) {
            // Formatear información del dispositivo (inventario/serie)
            String dispositivo = formatDispositivoInfo(h.getInventario(), h.getNumeroSerie());

            // Formatear piso/edificio
            String pisoEdificio = formatPisoEdificio(h.getPiso(), h.getEdificio());

            // Formatear fechas
            String fechaEntrada = h.getFechaEntrada() != null ? h.getFechaEntrada().format(DATETIME_FORMATTER) : "-";
            String fechaSalida = h.getFechaSalida() != null ? h.getFechaSalida().format(DATETIME_FORMATTER) : "Actual";

            // Formatear días
            String dias = h.getDiasEnLugar() != null ? String.valueOf(h.getDiasEnLugar()) : "-";

            List<String> row = List.of(
                String.valueOf(h.getIdHistorial()),
                dispositivo,
                h.getTipoDispositivo() != null ? h.getTipoDispositivo() : "-",
                h.getNombreLugar() != null ? h.getNombreLugar() : "-",
                h.getDepartamento() != null ? truncateText(h.getDepartamento(), 20) : "-",
                pisoEdificio,
                fechaEntrada,
                fechaSalida,
                dias,
                h.getUsuarioAsigno() != null ? truncateText(h.getUsuarioAsigno(), 18) : "-"
            );
            tableData.add(row);
        }

        // Configuración de tabla con estilo dashed, color negro-anaranjado y columnas proporcionales
        // Columnas: ID(0.6), Dispositivo(1.5), Tipo(1.2), Lugar(1.3), Depto(1.3), Piso/Edificio(1.2), F.Entrada(1.3), F.Salida(1.3), Días(0.6), Usuario(1.3)
        // Orientación horizontal aprovecha el mayor ancho disponible
        TableConfig tableConfig = TableConfig.minimal()
                .width(paginator.getUsableWidth())
                .columnWidthRatios(0.6f, 1.5f, 1.2f, 1.3f, 1.3f, 1.2f, 1.3f, 1.3f, 0.6f, 1.3f)
                .rowHeight(20f)
                .padding(4f)
                .fontSize(9f)
                .headerFontSize(10f)
                .headerBackground(DARK_ORANGE)
                .headerTextColor(Color.WHITE)
                .alternateRowColor(LIGHT_ORANGE)
                .border(TableConfig.BorderStyle.DASHED, 1.0f, DARK_ORANGE);

        pdfBoxService.createTable(paginator, tableData, tableConfig);
    }

    private String formatDispositivoInfo(String inventario, String numeroSerie) {
        if (inventario != null && numeroSerie != null) {
            return truncateText(inventario, 15) + "/" + truncateText(numeroSerie, 15);
        } else if (inventario != null) {
            return truncateText(inventario, 20);
        } else if (numeroSerie != null) {
            return truncateText(numeroSerie, 20);
        }
        return "-";
    }

    private String formatPisoEdificio(String piso, String edificio) {
        StringBuilder sb = new StringBuilder();
        if (piso != null && !piso.isEmpty()) {
            sb.append(piso);
        }
        if (edificio != null && !edificio.isEmpty()) {
            if (sb.length() > 0) {
                sb.append("/");
            }
            sb.append(truncateText(edificio, 15));
        }
        return sb.length() > 0 ? sb.toString() : "-";
    }

    private String truncateText(String text, int maxLength) {
        if (text == null) return "-";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    private void drawNoDataMessage(PDFPaginator paginator) throws IOException {
        TextBoxConfig noDataConfig = TextBoxConfig.normal()
                .fontSize(12f)
                .textAlign(TextBoxConfig.TextAlign.CENTER)
                .textColor(Color.GRAY);

        paginator.addSpace(50);
        pdfBoxService.writeText(paginator, "No se encontraron registros de historial de ubicaciones para el período especificado.", noDataConfig);
    }

    private void drawFooter(PDFPaginator paginator) throws IOException {
        paginator.addSpace(20);
        String generatedDate = "Reporte generado el: " + LocalDate.now().format(DATE_FORMATTER);

        TextBoxConfig footerConfig = TextBoxConfig.normal()
                .fontSize(8f)
                .textAlign(TextBoxConfig.TextAlign.RIGHT)
                .textColor(Color.GRAY);

        pdfBoxService.writeText(paginator, generatedDate, footerConfig);
    }

    /**
     * Carga una imagen desde los recursos del classpath.
     */
    private byte[] loadImageFromResources(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return inputStream.readAllBytes();
        }
    }

    /**
     * Agrega numeración de páginas a todo el documento en formato "p. x de total".
     */
    private void addPageNumbers(PDDocument document) throws IOException {
        int totalPages = document.getNumberOfPages();

        for (int i = 0; i < totalPages; i++) {
            try (PDPageContentStream contentStream = new PDPageContentStream(
                    document, document.getPage(i), PDPageContentStream.AppendMode.APPEND, true)) {

                // Posición del número de página (parte inferior derecha)
                float pageWidth = document.getPage(i).getMediaBox().getWidth();
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
}
