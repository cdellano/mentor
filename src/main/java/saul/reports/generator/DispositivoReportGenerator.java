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
import saul.reports.dto.DispositivoReportDTO;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generador de reportes PDF para información detallada de un dispositivo individual.
 * Incluye encabezado con imágenes institucionales, ficha del dispositivo y tabla de historial de ubicaciones.
 */
@Component
@PropertySource("classpath:reports.properties")
public class DispositivoReportGenerator {

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

    @Value("${report.title.dispositivo}")
    private String reportTitle;

    @Value("${report.qr.text1}")
    private String qrText;

    // Color azul marino para bordes de tabla
    private static final Color NAVY_BLUE = new Color(0, 0, 128);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DispositivoReportGenerator(PDFBoxService pdfBoxService) {
        this.pdfBoxService = pdfBoxService;
    }

    /**
     * Genera un reporte PDF con la información de un dispositivo y su historial de ubicaciones.
     *
     * @param dispositivo DTO con la información del dispositivo
     * @return byte[] del PDF generado
     * @throws IOException si ocurre un error al generar el PDF
     */
    public byte[] generateReport(DispositivoReportDTO dispositivo) throws IOException {
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
                drawTitle(paginator);

                // Dibujar ficha del dispositivo
                drawDispositivoInfo(paginator, dispositivo);

                // Agregar tabla de historial de ubicaciones
                if (dispositivo.getHistorialUbicaciones() != null && !dispositivo.getHistorialUbicaciones().isEmpty()) {
                    drawHistorialTable(paginator, dispositivo.getHistorialUbicaciones());
                } else {
                    drawNoHistorialMessage(paginator);
                }
            }

            // Agregar numeración de páginas después de generar todo el contenido
            addPageNumbers(document);

            document.save(baos);
            return baos.toByteArray();
        }
    }

    /**
     * Genera un reporte PDF con mensaje de error cuando el dispositivo no existe.
     *
     * @param idDispositivo ID del dispositivo que no fue encontrado
     * @return byte[] del PDF generado
     * @throws IOException si ocurre un error al generar el PDF
     */
    public byte[] generateNotFoundReport(Long idDispositivo) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDFPageConfig pageConfig = new PDFPageConfig()
                    .pageSize(PDRectangle.LETTER)
                    .margins(40f);

            try (PDFPaginator paginator = new PDFPaginator(document, pageConfig)) {
                // Dibujar encabezado
                drawHeader(paginator);

                // Título
                TextBoxConfig titleConfig = TextBoxConfig.title()
                        .fontStyle(TextBoxConfig.FontStyle.BOLD)
                        .fontSize(16f)
                        .textColor(new Color(0, 51, 102));

                pdfBoxService.writeText(paginator, reportTitle, titleConfig);
                paginator.addSpace(30);

                // Mensaje de error
                TextBoxConfig errorConfig = TextBoxConfig.normal()
                        .fontSize(14f)
                        .textAlign(TextBoxConfig.TextAlign.CENTER)
                        .textColor(Color.RED);

                pdfBoxService.writeText(paginator, "Dispositivo no encontrado", errorConfig);
                paginator.addSpace(15);

                TextBoxConfig detailConfig = TextBoxConfig.normal()
                        .fontSize(12f)
                        .textAlign(TextBoxConfig.TextAlign.CENTER)
                        .textColor(Color.DARK_GRAY);

                pdfBoxService.writeText(paginator,
                    String.format("No se encontró ningún dispositivo con el ID: %d", idDispositivo),
                    detailConfig);
            }

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
        paginator.advanceY(25);
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
        TextBoxConfig titleConfig = TextBoxConfig.title()
                .fontStyle(TextBoxConfig.FontStyle.BOLD)
                .fontSize(16f)
                .textColor(new Color(0, 51, 102));

        pdfBoxService.writeText(paginator, reportTitle, titleConfig);
        paginator.addSpace(0.5f);

        // Agregar código QR alineado a la derecha
        drawQRCode(paginator);

        paginator.addSpace(15);
    }

    /**
     * Dibuja un código QR alineado a la derecha con el texto configurado en reports.properties.
     */
    private void drawQRCode(PDFPaginator paginator) throws IOException {
        PDDocument doc = paginator.getDocument();
        PDPageContentStream cs = paginator.getContentStream();

        float qrSize = 72f;
        float rightX = paginator.getStartX() + paginator.getUsableWidth() - qrSize;
        float currentY = paginator.getCurrentY();

        QRCodeConfig qrConfig = QRCodeConfig.small()
                .size((int)qrSize)
                .mediumErrorCorrection()
                .margin(1);

        try {
            pdfBoxService.insertQRCode(doc, cs, qrText, rightX, currentY, qrConfig);
            paginator.advanceY(qrSize + 1);
        } catch (Exception e) {
            System.err.println("Error generando QR: " + e.getMessage());
        }
    }

    /**
     * Dibuja la ficha con la información del dispositivo.
     */
    private void drawDispositivoInfo(PDFPaginator paginator, DispositivoReportDTO dispositivo) throws IOException {
        // Título de sección
        TextBoxConfig sectionTitleConfig = TextBoxConfig.normal()
                .fontStyle(TextBoxConfig.FontStyle.BOLD)
                .fontSize(12f)
                .textColor(NAVY_BLUE);

        pdfBoxService.writeText(paginator, "Información del Dispositivo", sectionTitleConfig);
        paginator.addSpace(10);

        // Configuración para etiquetas y valores
        TextBoxConfig labelConfig = TextBoxConfig.normal()
                .fontStyle(TextBoxConfig.FontStyle.BOLD)
                .fontSize(10f)
                .textColor(Color.DARK_GRAY);

        TextBoxConfig valueConfig = TextBoxConfig.normal()
                .fontSize(10f)
                .textColor(Color.BLACK);

        // Dibujar campos de información en pares
        drawInfoRow(paginator, "ID Dispositivo:",
            dispositivo.getIdDispositivo() != null ? dispositivo.getIdDispositivo().toString() : "N/A",
            labelConfig, valueConfig);

        drawInfoRow(paginator, "Tipo de Dispositivo:",
            dispositivo.getTipoDispositivo() != null ? dispositivo.getTipoDispositivo() : "N/A",
            labelConfig, valueConfig);

        drawInfoRow(paginator, "Marca:",
            dispositivo.getMarca() != null ? dispositivo.getMarca() : "N/A",
            labelConfig, valueConfig);

        drawInfoRow(paginator, "Modelo:",
            dispositivo.getModelo() != null ? dispositivo.getModelo() : "N/A",
            labelConfig, valueConfig);

        drawInfoRow(paginator, "Número de Serie:",
            dispositivo.getNumeroSerie() != null ? dispositivo.getNumeroSerie() : "N/A",
            labelConfig, valueConfig);

        drawInfoRow(paginator, "Inventario:",
            dispositivo.getInventario() != null ? dispositivo.getInventario() : "N/A",
            labelConfig, valueConfig);

        drawInfoRow(paginator, "Estado Actual:",
            dispositivo.getEstadoActual() != null ? dispositivo.getEstadoActual() : "N/A",
            labelConfig, valueConfig);

        drawInfoRow(paginator, "Fecha de Compra:",
            dispositivo.getFechaCompra() != null ? dispositivo.getFechaCompra().format(DATE_FORMATTER) : "N/A",
            labelConfig, valueConfig);

        drawInfoRow(paginator, "Costo:",
            dispositivo.getCosto() != null ? String.format("$%,.2f", dispositivo.getCosto()) : "N/A",
            labelConfig, valueConfig);

        drawInfoRow(paginator, "Fecha de Registro:",
            dispositivo.getFechaRegistro() != null ? dispositivo.getFechaRegistro().format(DATETIME_FORMATTER) : "N/A",
            labelConfig, valueConfig);

        if (dispositivo.getFechaBaja() != null) {
            drawInfoRow(paginator, "Fecha de Baja:",
                dispositivo.getFechaBaja().format(DATE_FORMATTER),
                labelConfig, valueConfig);
        }

        if (dispositivo.getNotas() != null && !dispositivo.getNotas().isEmpty()) {
            drawInfoRow(paginator, "Notas:",
                dispositivo.getNotas(),
                labelConfig, valueConfig);
        }

        paginator.addSpace(20);

        // Título de sección historial
        pdfBoxService.writeText(paginator, "Historial de Ubicaciones", sectionTitleConfig);
        paginator.addSpace(10);
    }

    /**
     * Dibuja una fila de información con etiqueta y valor.
     */
    private void drawInfoRow(PDFPaginator paginator, String label, String value,
                              TextBoxConfig labelConfig, TextBoxConfig valueConfig) throws IOException {
        PDDocument doc = paginator.getDocument();
        PDPageContentStream cs = paginator.getContentStream();

        float startX = paginator.getStartX();
        float currentY = paginator.getCurrentY();
        float labelWidth = 130f;
        float valueWidth = paginator.getUsableWidth() - labelWidth - 10;

        // Escribir etiqueta
        pdfBoxService.writeTextBox(doc, cs, label, startX, currentY, labelWidth, 12, labelConfig);

        // Escribir valor
        pdfBoxService.writeTextBox(doc, cs, value, startX + labelWidth + 10, currentY, valueWidth, 12, valueConfig);

        paginator.advanceY(14);
    }

    /**
     * Dibuja la tabla de historial de ubicaciones con estilo dotted azul marino.
     */
    private void drawHistorialTable(PDFPaginator paginator,
                                     List<DispositivoReportDTO.UbicacionHistorialDTO> historial) throws IOException {
        // Preparar datos de la tabla
        List<List<String>> tableData = new ArrayList<>();

        // Encabezados
        List<String> headers = List.of(
                "Lugar", "Departamento", "Piso", "Edificio",
                "Fecha Entrada", "Fecha Salida", "Días", "Asignó"
        );
        tableData.add(headers);

        // Datos
        for (DispositivoReportDTO.UbicacionHistorialDTO ubicacion : historial) {
            List<String> row = new ArrayList<>();
            row.add(ubicacion.getNombreLugar() != null ? ubicacion.getNombreLugar() : "-");
            row.add(ubicacion.getDepartamento() != null ? ubicacion.getDepartamento() : "-");
            row.add(ubicacion.getPiso() != null ? ubicacion.getPiso() : "-");
            row.add(ubicacion.getEdificio() != null ? ubicacion.getEdificio() : "-");
            row.add(ubicacion.getFechaEntrada() != null ? ubicacion.getFechaEntrada().format(DATETIME_FORMATTER) : "-");
            row.add(ubicacion.getFechaSalida() != null ? ubicacion.getFechaSalida().format(DATETIME_FORMATTER) : "Actual");
            row.add(ubicacion.getDiasEnLugar() != null ? ubicacion.getDiasEnLugar().toString() : "-");
            row.add(ubicacion.getUsuarioAsigno() != null ? ubicacion.getUsuarioAsigno() : "-");
            tableData.add(row);
        }

        // Configuración de la tabla con bordes dotted azul marino y columnas proporcionales (autoajustables)
        // Columnas: Lugar(1.5), Departamento(1.5), Piso(0.8), Edificio(1), F.Entrada(1.3), F.Salida(1.3), Días(0.7), Asignó(1.2)
        TableConfig tableConfig = TableConfig.minimal()
            .width(paginator.getUsableWidth())
            .columnWidthRatios(1.5f, 1.5f, 0.8f, 1f, 1.3f, 1.3f, 0.7f, 1.2f)
            .rowHeight(22f)
            .padding(4f)
            .fontSize(8f)
            .headerFontSize(9f)
            .headerBackground(NAVY_BLUE)
            .headerTextColor(Color.WHITE)
            .alternateRowColor(new Color(240, 248, 255))
            .border(TableConfig.BorderStyle.DOTTED, 1.0f, NAVY_BLUE);

        pdfBoxService.createTable(paginator, tableData, tableConfig);
    }

    /**
     * Dibuja mensaje cuando no hay historial de ubicaciones.
     */
    private void drawNoHistorialMessage(PDFPaginator paginator) throws IOException {
        TextBoxConfig noDataConfig = TextBoxConfig.normal()
                .fontSize(12f)
                .textAlign(TextBoxConfig.TextAlign.CENTER)
                .textColor(Color.GRAY);

        paginator.addSpace(30);
        pdfBoxService.writeText(paginator, "Sin historial de ubicaciones registrado", noDataConfig);
    }

    /**
     * Agrega numeración de páginas a todo el documento.
     */
    private void addPageNumbers(PDDocument document) throws IOException {
        int totalPages = document.getNumberOfPages();

        for (int i = 0; i < totalPages; i++) {
            try (PDPageContentStream contentStream = new PDPageContentStream(
                    document, document.getPage(i), PDPageContentStream.AppendMode.APPEND, true)) {

                float pageWidth = document.getPage(i).getMediaBox().getWidth();
                float x = pageWidth - 80f;
                float y = 25f;

                String pageNumber = String.format("p. %d de %d", i + 1, totalPages);

                TextBoxConfig pageNumberConfig = TextBoxConfig.normal()
                        .fontSize(8f)
                        .textColor(Color.GRAY);

                pdfBoxService.writeTextBox(document, contentStream, pageNumber,
                        x, y, 60f, 10f, pageNumberConfig);

                // Agregar fecha de generación en la parte inferior izquierda
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
