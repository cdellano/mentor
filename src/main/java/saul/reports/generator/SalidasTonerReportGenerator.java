package saul.reports.generator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import saul.entity.SalidasToner;
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
 * Generador de reportes PDF para salidas de tóner.
 * Crea reportes con encabezado estándar del hospital y tabla de salidas de tóner.
 */
@Component
@PropertySource("classpath:reports.properties")
public class SalidasTonerReportGenerator {

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

    @Value("${report.title.salidas.toner}")
    private String reportTitle;

    @Value("${report.qr.text1}")
    private String qrText;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SalidasTonerReportGenerator(PDFBoxService pdfBoxService) {
        this.pdfBoxService = pdfBoxService;
    }

    /**
     * Genera un reporte PDF con las salidas de tóner.
     *
     * @param salidas lista de salidas de tóner a incluir en el reporte
     * @param fechaInicio fecha de inicio del filtro
     * @param fechaFin fecha de fin del filtro
     * @param idUsuario ID del usuario filtrado (opcional)
     * @return bytes del PDF generado
     * @throws IOException si hay error al generar el PDF
     */
    public byte[] generateReport(List<SalidasToner> salidas, LocalDate fechaInicio, LocalDate fechaFin, Long idUsuario) throws IOException {
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

                // Agregar información de filtros
                drawFiltersInfo(paginator, fechaInicio, fechaFin, idUsuario);

                // Agregar tabla de salidas
                if (salidas != null && !salidas.isEmpty()) {
                    drawSalidasTable(paginator, salidas);
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

        // Escribir textos del encabezado usando textBox para centrarlos en el área disponible
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

    private void drawFiltersInfo(PDFPaginator paginator, LocalDate fechaInicio, LocalDate fechaFin, Long idUsuario) throws IOException {
        TextBoxConfig filterConfig = TextBoxConfig.normal()
                .fontSize(10f)
                .textAlign(TextBoxConfig.TextAlign.LEFT);

        String periodoText = String.format("Período: %s - %s",
            fechaInicio.format(DATE_FORMATTER), fechaFin.format(DATE_FORMATTER));
        pdfBoxService.writeText(paginator, periodoText, filterConfig);

        if (idUsuario != null) {
            paginator.addSpace(2f);
            String usuarioText = String.format("Filtrado por Usuario ID: %d", idUsuario);
            pdfBoxService.writeText(paginator, usuarioText, filterConfig);
        }

        paginator.addSpace(15f);
    }

    private void drawSalidasTable(PDFPaginator paginator, List<SalidasToner> salidas) throws IOException {
        // Preparar datos de la tabla
        List<List<String>> tableData = new ArrayList<>();

        // Agregar encabezados
        List<String> headers = List.of("ID", "Fecha Salida", "Usuario", "Tipo Tóner", "Cantidad", "Departamento", "Observaciones");
        tableData.add(headers);

        // Convertir salidas a filas de datos
        for (SalidasToner salida : salidas) {
            List<String> row = List.of(
                salida.getId().toString(),
                salida.getFechaSalida().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                salida.getUsuarioInstala() != null ? salida.getUsuarioInstala().getNombreCompleto() : "",
                salida.getTipoToner() != null ? salida.getTipoToner().getNombreTipoToner() : "",
                salida.getCantidad().toString(),
                salida.getDepartamento() != null ? salida.getDepartamento().getNombreLugar() : "",
                salida.getObservaciones() != null ? salida.getObservaciones() : ""
            );
            tableData.add(row);
        }

        // Configuración de tabla con anchos de columna proporcionales (autoajustables)
        // Columnas: ID(0.6), Fecha(1.3), Usuario(1.5), TipoToner(1.5), Cantidad(0.8), Departamento(1.5), Observaciones(2)
        TableConfig tableConfig = TableConfig.minimal()
                .width(paginator.getUsableWidth())
                .columnWidthRatios(0.6f, 1.3f, 1.5f, 1.5f, 0.8f, 1.5f, 2f)
                .rowHeight(22f)
                .padding(4f)
                .fontSize(9f)
                .headerFontSize(10f)
                .headerBackground(new Color(0, 51, 102))
                .headerTextColor(Color.WHITE)
                .alternateRowColor(new Color(245, 245, 245))
                .border(TableConfig.BorderStyle.SOLID, 0.5f, Color.BLACK);

        pdfBoxService.createTable(paginator, tableData, tableConfig);
    }

    private void drawNoDataMessage(PDFPaginator paginator) throws IOException {
        TextBoxConfig noDataConfig = TextBoxConfig.normal()
                .fontSize(12f)
                .textAlign(TextBoxConfig.TextAlign.CENTER)
                .textColor(Color.GRAY);

        paginator.addSpace(50);
        pdfBoxService.writeText(paginator, "No se encontraron salidas de tóner en el período especificado.", noDataConfig);
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
}
