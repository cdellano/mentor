package saul.reports.generator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import saul.reports.dto.BitacoraServicioReportDTO;
import saul.pdf.renderer.*;
import saul.pdf.service.PDFBoxService;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generador de reportes PDF para bitácora de servicios informáticos.
 * Crea reportes con encabezado estándar del hospital y tabla de bitácora con estilo dotted magenta oscuro.
 */
@Component
@PropertySource("classpath:reports.properties")
public class BitacoraServicioReportGenerator {

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

    @Value("${report.title.bitacoraServ}")
    private String reportTitle;

    @Value("${report.qr.text1}")
    private String qrText;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public BitacoraServicioReportGenerator(PDFBoxService pdfBoxService) {
        this.pdfBoxService = pdfBoxService;
    }

    /**
     * Genera un reporte PDF con las bitácoras de servicio.
     *
     * @param bitacoras lista de bitácoras a incluir en el reporte
     * @param fechaInicio fecha de inicio del filtro
     * @param fechaFin fecha de fin del filtro
     * @param idUsuario ID del usuario filtrado (opcional)
     * @param contenido texto filtrado en comentario (opcional)
     * @param estado estado filtrado (opcional)
     * @param idTipoIncidente ID del tipo de incidente filtrado (opcional)
     * @return bytes del PDF generado
     * @throws IOException si hay error al generar el PDF
     */
    public byte[] generateReport(List<BitacoraServicioReportDTO> bitacoras, LocalDate fechaInicio, LocalDate fechaFin,
                               Long idUsuario, String contenido, String estado, Long idTipoIncidente) throws IOException {
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

                // Agregar código QR
                drawQRCode(paginator);

                // Agregar información de filtros
                drawFiltersInfo(paginator, fechaInicio, fechaFin, idUsuario, contenido, estado, idTipoIncidente);

                // Agregar tabla de bitácora de servicios
                if (bitacoras != null && !bitacoras.isEmpty()) {
                    drawBitacoraTable(paginator, bitacoras);
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

    private void drawFiltersInfo(PDFPaginator paginator, LocalDate fechaInicio, LocalDate fechaFin,
                               Long idUsuario, String contenido, String estado, Long idTipoIncidente) throws IOException {
        StringBuilder filterInfo = new StringBuilder();
        filterInfo.append("Período: ").append(fechaInicio.format(DATE_FORMATTER))
                  .append(" - ").append(fechaFin.format(DATE_FORMATTER));

        if (idUsuario != null) {
            filterInfo.append(" | Usuario ID: ").append(idUsuario);
        }
        if (contenido != null && !contenido.trim().isEmpty()) {
            filterInfo.append(" | Comentario: ").append(contenido);
        }
        if (estado != null && !estado.trim().isEmpty()) {
            filterInfo.append(" | Estado: ").append(estado);
        }
        if (idTipoIncidente != null) {
            filterInfo.append(" | Tipo Incidente ID: ").append(idTipoIncidente);
        }

        TextBoxConfig filterConfig = TextBoxConfig.normal()
                .fontSize(9f)
                .textAlign(TextBoxConfig.TextAlign.LEFT)
                .textColor(Color.DARK_GRAY);

        pdfBoxService.writeText(paginator, filterInfo.toString(), filterConfig);
        paginator.addSpace(15f);
    }

    private void drawBitacoraTable(PDFPaginator paginator, List<BitacoraServicioReportDTO> bitacoras) throws IOException {
        // Preparar datos de la tabla
        List<List<String>> tableData = new ArrayList<>();

        // Agregar encabezados
        List<String> headers = List.of("ID", "Servicio", "Tipo Inc.", "Estado", "Fecha Inicio", "Fecha Fin", "Reportado Por", "Comentario");
        tableData.add(headers);

        // Agregar filas de datos
        for (BitacoraServicioReportDTO bitacora : bitacoras) {
            // Truncar comentario si es muy largo
            String comentario = bitacora.getComentario();
            if (comentario != null && comentario.length() > 40) {
                comentario = comentario.substring(0, 37) + "...";
            }

            List<String> row = List.of(
                String.valueOf(bitacora.getId()),
                bitacora.getServicio() != null ? bitacora.getServicio() : "N/A",
                bitacora.getTipoIncidente() != null ? bitacora.getTipoIncidente() : "N/A",
                bitacora.getEstado() != null ? bitacora.getEstado() : "N/A",
                bitacora.getFechaInicio() != null ? bitacora.getFechaInicio().format(DATETIME_FORMATTER) : "-",
                bitacora.getFechaFin() != null ? bitacora.getFechaFin().format(DATETIME_FORMATTER) : "-",
                bitacora.getReportadoPor() != null ? bitacora.getReportadoPor() : "N/A",
                comentario != null ? comentario : "-"
            );
            tableData.add(row);
        }

        // Configuración de tabla con estilo dotted y color magenta oscuro
        // Columnas: ID(0.6), Servicio(1.5), TipoInc.(1.2), Estado(1.2), F.Inicio(1.5), F.Fin(1.5), ReportadoPor(1.5), Comentario(2.5)
        Color magentaOscuro = new Color(139, 0, 139); // #8B008B - Magenta oscuro
        TableConfig tableConfig = TableConfig.minimal()
                .width(paginator.getUsableWidth())
                .columnWidthRatios(0.6f, 1.5f, 1.2f, 1.2f, 1.5f, 1.5f, 1.5f, 2.5f)
                .rowHeight(20f)
                .padding(3f)
                .fontSize(7.5f)
                .headerFontSize(8.5f)
                .headerBackground(magentaOscuro)
                .headerTextColor(Color.WHITE)
                .alternateRowColor(new Color(248, 240, 248)) // Color magenta muy claro para filas alternas
                .border(TableConfig.BorderStyle.DOTTED, 1.0f, magentaOscuro); // Borde dotted magenta oscuro

        pdfBoxService.createTable(paginator, tableData, tableConfig);
    }

    private void drawNoDataMessage(PDFPaginator paginator) throws IOException {
        TextBoxConfig noDataConfig = TextBoxConfig.normal()
                .fontSize(12f)
                .textAlign(TextBoxConfig.TextAlign.CENTER)
                .textColor(Color.GRAY);

        paginator.addSpace(50);
        pdfBoxService.writeText(paginator, "No se encontraron registros de bitácora para los filtros especificados.", noDataConfig);
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
