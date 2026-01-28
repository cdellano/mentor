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
import saul.reports.dto.EquipoRezagadoReportDTO;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generador de reportes PDF para equipos rezagados en mantenimiento.
 * Incluye encabezado con imágenes institucionales e información del hospital.
 */
@Component
@PropertySource("classpath:reports.properties")
public class EquipoRezagadoReportGenerator {

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

    @Value("${report.qr.text1}")
    private String qrText;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public EquipoRezagadoReportGenerator(PDFBoxService pdfBoxService) {
        this.pdfBoxService = pdfBoxService;
    }

    /**
     * Genera un reporte PDF de equipos rezagados en mantenimiento.
     *
     * @param equiposRezagados lista de equipos rezagados a incluir en el reporte
     * @param diasMinimos      número mínimo de días sin mantenimiento
     * @param tipoDispositivo  tipo de dispositivo filtrado (puede ser null)
     * @return byte[] del PDF generado
     * @throws IOException si ocurre un error al generar el PDF
     */
    public byte[] generateReport(List<EquipoRezagadoReportDTO> equiposRezagados,
                                 Integer diasMinimos, String tipoDispositivo) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Configuración de página tamaño carta con márgenes
            PDFPageConfig pageConfig = new PDFPageConfig()
                    .pageSize(PDRectangle.LETTER)
                    .margins(40f);

            try (PDFPaginator paginator = new PDFPaginator(document, pageConfig)) {
                // Dibujar encabezado
                drawHeader(paginator);

                // Agregar título del reporte
                drawTitle(paginator, diasMinimos, tipoDispositivo);

                // Agregar tabla de equipos rezagados
                if (equiposRezagados != null && !equiposRezagados.isEmpty()) {
                    drawEquiposRezagadosTable(paginator, equiposRezagados);
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
        paginator.addSpace(15);
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
    private void drawTitle(PDFPaginator paginator, Integer diasMinimos, String tipoDispositivo) throws IOException {
        String title = "Reporte de Equipos Rezagados en Mantenimiento";

        TextBoxConfig titleConfig = TextBoxConfig.title()
                .fontStyle(TextBoxConfig.FontStyle.BOLD)
                .fontSize(16f)
                .textColor(new Color(0, 51, 102));

        pdfBoxService.writeText(paginator, title, titleConfig);
        paginator.addSpace(7.5f);

        // Agregar código QR alineado a la derecha
        drawQRCode(paginator);

        // Agregar información de filtros
        drawFilterInfo(paginator, diasMinimos, tipoDispositivo);
        paginator.addSpace(15);
    }

    /**
     * Dibuja un código QR alineado a la derecha con el texto configurado en reports.properties.
     */
    private void drawQRCode(PDFPaginator paginator) throws IOException {
        PDDocument doc = paginator.getDocument();
        PDPageContentStream cs = paginator.getContentStream();

        float qrSize = 72f; // Tamaño del QR en puntos
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
     * Dibuja la información de los filtros aplicados.
     */
    private void drawFilterInfo(PDFPaginator paginator, Integer diasMinimos, String tipoDispositivo) throws IOException {
        String filterInfo = String.format("Filtros: Mínimo %d días sin mantenimiento", diasMinimos);
        if (tipoDispositivo != null && !tipoDispositivo.isEmpty()) {
            filterInfo += " | Tipo: " + tipoDispositivo;
        }

        TextBoxConfig filterConfig = TextBoxConfig.normal()
                .fontSize(10f)
                .textColor(Color.DARK_GRAY);

        pdfBoxService.writeText(paginator, filterInfo, filterConfig);
    }

    /**
     * Dibuja la tabla de equipos rezagados en mantenimiento.
     */
    private void drawEquiposRezagadosTable(PDFPaginator paginator, List<EquipoRezagadoReportDTO> equiposRezagados) throws IOException {
        // Preparar datos de la tabla
        List<List<String>> tableData = new ArrayList<>();

        // Encabezados
        List<String> headers = List.of(
                "ID Dispositivo", "Inventario - Núm. Serie", "Dispositivo", "Fecha Último Mant.", "Días Transcurridos"
        );
        tableData.add(headers);

        // Datos
        for (EquipoRezagadoReportDTO equipo : equiposRezagados) {
            List<String> row = new ArrayList<>();
            row.add(equipo.getIdDispositivo() != null ? equipo.getIdDispositivo().toString() : "-");
            row.add(formatSerieInventario(equipo));
            row.add(formatDispositivo(equipo));
            row.add(equipo.getFechaUltimoMantenimiento() != null ?
                   equipo.getFechaUltimoMantenimiento().format(DATE_FORMATTER) : "Sin mantenimiento");
            row.add(equipo.getDiasTranscurridos() != null ?
                   equipo.getDiasTranscurridos().toString() + " días" : "-");
            tableData.add(row);
        }

        // Configuración de la tabla con estilo dotted y columnas proporcionales (autoajustables)
        // Columnas: ID Disp(1), Inv-Serie(2), Dispositivo(2.5), Fecha Último Mant(1.5), Días Transcurridos(1.5)
        TableConfig tableConfig = TableConfig.minimal()
            .width(paginator.getUsableWidth())
            .columnWidthRatios(1f, 2f, 2.5f, 1.5f, 1.5f)
            .rowHeight(22f)
            .padding(4f)
            .fontSize(8f)
            .headerFontSize(9f)
            .headerBackground(new Color(0, 102, 153))
            .headerTextColor(Color.WHITE)
            .alternateRowColor(new Color(240, 248, 255))
            .border(TableConfig.BorderStyle.DOTTED, 0.5f, new Color(180, 180, 180));

        pdfBoxService.createTable(paginator, tableData, tableConfig);
    }

    /**
     * Formatea la información de serie e inventario del dispositivo.
     */
    private String formatSerieInventario(EquipoRezagadoReportDTO equipo) {
        StringBuilder sb = new StringBuilder();
        if (equipo.getInventario() != null && !equipo.getInventario().isEmpty()) {
            sb.append("Inv: ").append(equipo.getInventario());
        }
        if (equipo.getNumeroSerie() != null && !equipo.getNumeroSerie().isEmpty()) {
            if (sb.length() > 0) sb.append(" | ");
            sb.append("S/N: ").append(equipo.getNumeroSerie());
        }
        return sb.toString();
    }

    /**
     * Formatea la información del dispositivo para mostrar en la tabla.
     */
    private String formatDispositivo(EquipoRezagadoReportDTO equipo) {
        StringBuilder sb = new StringBuilder();
        if (equipo.getTipoDispositivo() != null) {
            sb.append(equipo.getTipoDispositivo());
        }
        if (equipo.getMarca() != null) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(equipo.getMarca());
        }
        if (equipo.getModelo() != null) {
            sb.append(" ").append(equipo.getModelo());
        }
        return sb.length() > 0 ? sb.toString() : "-";
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
        pdfBoxService.writeText(paginator, "No se encontraron equipos rezagados con los filtros especificados.", noDataConfig);
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
