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
import saul.reports.dto.DispositivoRegistroReportDTO;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generador de reportes PDF para registro de dispositivos.
 * Crea reportes en orientación horizontal con encabezado estándar del hospital
 * y tabla de dispositivos con celdas autoajustables.
 */
@Component
@PropertySource("classpath:reports.properties")
public class DispositivoRegistroReportGenerator {

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

    @Value("${report.title.regDisp}")
    private String reportTitle;

    @Value("${report.qr.text1}")
    private String qrText;

    // Colores para el reporte
    private static final Color TABLE_BORDER_COLOR = new Color(0, 51, 102); // Azul oscuro
    private static final Color HEADER_BG_COLOR = new Color(0, 51, 102);
    private static final Color ALTERNATE_ROW_COLOR = new Color(240, 248, 255);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DispositivoRegistroReportGenerator(PDFBoxService pdfBoxService) {
        this.pdfBoxService = pdfBoxService;
    }

    /**
     * Genera un reporte PDF con los dispositivos registrados filtrados.
     *
     * @param dispositivos lista de dispositivos a incluir en el reporte
     * @param fechaInicio fecha de inicio del filtro
     * @param fechaFin fecha de fin del filtro
     * @param filtros información adicional de los filtros aplicados
     * @return bytes del PDF generado
     * @throws IOException si hay error al generar el PDF
     */
    public byte[] generateReport(List<DispositivoRegistroReportDTO> dispositivos, LocalDate fechaInicio,
                                  LocalDate fechaFin, String filtros) throws IOException {
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
                drawFiltersInfo(paginator, fechaInicio, fechaFin, filtros);

                // Agregar tabla de dispositivos
                if (dispositivos != null && !dispositivos.isEmpty()) {
                    drawDispositivosTable(paginator, dispositivos);
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

        // Escribir textos del encabezado
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
        float qrSize = 45f; // Tamaño reducido

        // Configuración del QR con tamaño reducido
        QRCodeConfig qrConfig = QRCodeConfig.medium()
                .size((int) qrSize)
                .margin(0);

        // Posicionar el QR a la derecha
        pdfBoxService.insertQRCodeRight(paginator, qrText, qrConfig);

        // Separación del QR
        paginator.addSpace(12.5f);
    }

    private void drawFiltersInfo(PDFPaginator paginator, LocalDate fechaInicio, LocalDate fechaFin, String filtros) throws IOException {
        StringBuilder filterInfo = new StringBuilder();
        filterInfo.append("Período de registro: ").append(fechaInicio.format(DATE_FORMATTER))
                  .append(" - ").append(fechaFin.format(DATE_FORMATTER));

        if (filtros != null && !filtros.isEmpty()) {
            filterInfo.append(" | ").append(filtros);
        }

        TextBoxConfig filterConfig = TextBoxConfig.normal()
                .fontSize(9f)
                .textAlign(TextBoxConfig.TextAlign.LEFT)
                .textColor(Color.DARK_GRAY);

        pdfBoxService.writeText(paginator, filterInfo.toString(), filterConfig);
        paginator.addSpace(15f);
    }

    private void drawDispositivosTable(PDFPaginator paginator, List<DispositivoRegistroReportDTO> dispositivos) throws IOException {
        // Preparar datos de la tabla
        List<List<String>> tableData = new ArrayList<>();

        // Agregar encabezados
        List<String> headers = List.of("ID", "Tipo", "Marca", "Modelo", "Núm. Serie", "Inventario", "Estado", "F. Compra", "F. Registro", "Costo", "Notas");
        tableData.add(headers);

        // Agregar filas de datos
        for (DispositivoRegistroReportDTO d : dispositivos) {
            // Formatear fechas
            String fechaCompra = d.getFechaCompra() != null ? d.getFechaCompra().format(DATE_FORMATTER) : "-";
            String fechaRegistro = d.getFechaRegistro() != null ? d.getFechaRegistro().format(DATETIME_FORMATTER) : "-";
            String costo = d.getCosto() != null ? "$" + d.getCosto() : "-";

            List<String> row = List.of(
                String.valueOf(d.getIdDispositivo()),
                d.getTipoDispositivo() != null ? d.getTipoDispositivo() : "-",
                d.getMarca() != null ? d.getMarca() : "-",
                d.getModelo() != null ? d.getModelo() : "-",
                d.getNumeroSerie() != null ? d.getNumeroSerie() : "-",
                d.getInventario() != null ? d.getInventario() : "-",
                d.getEstadoDispositivo() != null ? d.getEstadoDispositivo() : "-",
                fechaCompra,
                fechaRegistro,
                costo,
                d.getNotas() != null ? d.getNotas() : "-"
            );
            tableData.add(row);
        }

        // Configuración de tabla con celdas autoajustables
        // Anchos proporcionales: ID(0.6), Tipo(1.2), Marca(1.2), Modelo(1.2), Serie(1.3), Inv(1), Estado(1), FCompra(1), FReg(1.3), Costo(0.8), Notas(3)
        TableConfig tableConfig = TableConfig.minimal()
                .width(paginator.getUsableWidth())
                .columnWidthRatios(0.6f, 1.2f, 1.2f, 1.2f, 1.3f, 1f, 1f, 1f, 1.3f, 0.8f, 3f)
                .rowHeight(0f) // Altura 0 para autoajuste
                .padding(4f)
                .fontSize(7f)
                .headerFontSize(8f)
                .headerBackground(HEADER_BG_COLOR)
                .headerTextColor(Color.WHITE)
                .alternateRowColor(ALTERNATE_ROW_COLOR)
                .border(TableConfig.BorderStyle.SOLID, 0.5f, TABLE_BORDER_COLOR);

        pdfBoxService.createTable(paginator, tableData, tableConfig);
    }


    private void drawNoDataMessage(PDFPaginator paginator) throws IOException {
        TextBoxConfig noDataConfig = TextBoxConfig.normal()
                .fontSize(12f)
                .textAlign(TextBoxConfig.TextAlign.CENTER)
                .textColor(Color.GRAY);

        paginator.addSpace(50);
        pdfBoxService.writeText(paginator, "No se encontraron dispositivos para los filtros especificados.", noDataConfig);
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
