package saul.pdf.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Service;
import saul.pdf.renderer.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio principal para generación de PDFs con PDFBox.
 * Incluye soporte para paginación automática, textos, tablas, imágenes,
 * marcas de agua, líneas y gráficos JFreeChart.
 */
@Service
public class PDFBoxService {

    // Cache de fuentes para evitar cargarlas múltiples veces
    private final Map<String, byte[]> fontCache = new HashMap<>();

    private static final String FONT_REGULAR = "/fonts/NotoSans-Regular.ttf";
    private static final String FONT_BOLD = "/fonts/NotoSans-Bold.ttf";
    private static final String FONT_ITALIC = "/fonts/NotoSans-Italic.ttf";
    private static final String FONT_BOLD_ITALIC = "/fonts/NotoSans-BoldItalic.ttf";

    // ==================== ESCRITURA DE TEXTO ====================

    /**
     * Escribe texto simple en una posición específica.
     */
    public void writeText(PDDocument doc, PDPageContentStream cs, String text,
                          float x, float y, TextBoxConfig config) throws IOException {
        if (text == null || text.isEmpty()) return;

        PDFont font = getFont(doc, config.getFontStyle());
        cs.setFont(font, config.getFontSize());
        cs.setNonStrokingColor(config.getTextColor());
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
    }

    /**
     * Escribe texto dentro de un rectángulo con ajuste automático de líneas.
     * Opcionalmente dibuja el borde y/o relleno del rectángulo.
     */
    public float writeTextBox(PDDocument doc, PDPageContentStream cs, String text,
                              float x, float y, float width, float height,
                              TextBoxConfig config) throws IOException {
        if (text == null) text = "";

        PDFont font = getFont(doc, config.getFontStyle());
        float padding = config.getPadding();
        float textWidth = width - (padding * 2);
        float leading = config.getLeading();

        // Dividir texto en líneas
        List<String> lines = wrapText(text, font, config.getFontSize(), textWidth);
        float textHeight = lines.size() * leading + (padding * 2);
        float actualHeight = Math.max(height, textHeight);

        // Dibujar fondo si está configurado
        if (config.isFillBackground()) {
            cs.setNonStrokingColor(config.getBackgroundColor());
            cs.addRect(x, y - actualHeight, width, actualHeight);
            cs.fill();
        }

        // Dibujar borde si está configurado
        if (config.isDrawBorder()) {
            cs.setStrokingColor(config.getBorderColor());
            cs.setLineWidth(config.getBorderWidth());
            cs.addRect(x, y - actualHeight, width, actualHeight);
            cs.stroke();
        }

        // Calcular posición Y inicial del texto según alineación vertical
        float textY = calculateTextY(y, actualHeight, lines.size(), leading, padding, config.getVerticalAlign());

        // Escribir texto
        cs.setNonStrokingColor(config.getTextColor());
        cs.setFont(font, config.getFontSize());

        for (String line : lines) {
            float textX = calculateTextX(x, width, line, font, config.getFontSize(),
                                          padding, config.getTextAlign());
            cs.beginText();
            cs.newLineAtOffset(textX, textY);
            cs.showText(line);
            cs.endText();
            textY -= leading;
        }

        return actualHeight;
    }

/**
 * Escribe texto usando el paginador con paginación automática y ajuste de líneas.
 */
public void writeText(PDFPaginator paginator, String text, TextBoxConfig config) throws IOException {
    if (text == null || text.isEmpty()) return;

    PDFont font = getFont(paginator.getDocument(), config.getFontStyle());
    float maxWidth = paginator.getUsableWidth();
    float leading = config.getLeading();

    // Dividir texto en líneas que quepan en el ancho disponible
    List<String> lines = wrapText(text, font, config.getFontSize(), maxWidth);

    for (String line : lines) {
        float lineHeight = leading + 2;
        paginator.checkSpace(lineHeight);

        PDPageContentStream cs = paginator.getContentStream();
        cs.setFont(font, config.getFontSize());
        cs.setNonStrokingColor(config.getTextColor());

        // Calcular posición X según alineación
        float x = calculateTextX(paginator.getStartX(), maxWidth, line, font,
                                  config.getFontSize(), 0, config.getTextAlign());

        cs.beginText();
        cs.newLineAtOffset(x, paginator.getCurrentY());
        cs.showText(line);
        cs.endText();

        paginator.advanceY(lineHeight);
    }
}

    /**
     * Escribe caja de texto usando el paginador.
     */
    public void writeTextBox(PDFPaginator paginator, String text, float width,
                             float minHeight, TextBoxConfig config) throws IOException {
        PDFont font = getFont(paginator.getDocument(), config.getFontStyle());
        List<String> lines = wrapText(text, font, config.getFontSize(),
                                      width - (config.getPadding() * 2));
        float textHeight = lines.size() * config.getLeading() + (config.getPadding() * 2);
        float actualHeight = Math.max(minHeight, textHeight);

        paginator.checkSpace(actualHeight);

        writeTextBox(paginator.getDocument(), paginator.getContentStream(), text,
                    paginator.getStartX(), paginator.getCurrentY(), width, minHeight, config);
        paginator.advanceY(actualHeight);
    }

    // ==================== DIBUJO DE LÍNEAS ====================

    /**
     * Dibuja una línea horizontal.
     */
    public void drawHorizontalLine(PDPageContentStream cs, float x, float y,
                                    float length, LineConfig config) throws IOException {
        applyLineStyle(cs, config);
        cs.moveTo(x, y);
        cs.lineTo(x + length, y);
        cs.stroke();
        resetLineStyle(cs);
    }

    /**
     * Dibuja una línea vertical.
     */
    public void drawVerticalLine(PDPageContentStream cs, float x, float y,
                                  float length, LineConfig config) throws IOException {
        applyLineStyle(cs, config);
        cs.moveTo(x, y);
        cs.lineTo(x, y - length);
        cs.stroke();
        resetLineStyle(cs);
    }

    /**
     * Dibuja una línea horizontal usando el paginador.
     */
    public void drawHorizontalLine(PDFPaginator paginator, LineConfig config) throws IOException {
        paginator.checkSpace(config.getThickness() + 5);
        drawHorizontalLine(paginator.getContentStream(), paginator.getStartX(),
                          paginator.getCurrentY(), paginator.getUsableWidth(), config);
        paginator.advanceY(config.getThickness() + 5);
    }

    private void applyLineStyle(PDPageContentStream cs, LineConfig config) throws IOException {
        cs.setStrokingColor(config.getColor());
        cs.setLineWidth(config.getThickness());

        float[] pattern = config.getDashPattern();
        if (pattern != null) {
            cs.setLineDashPattern(pattern, 0);
        }
    }

    private void resetLineStyle(PDPageContentStream cs) throws IOException {
        cs.setLineDashPattern(new float[]{}, 0);
    }

    // ==================== INSERCIÓN DE IMÁGENES ====================

    /**
     * Inserta una imagen desde bytes.
     */
    public void insertImage(PDDocument doc, PDPageContentStream cs, byte[] imageBytes,
                            float x, float y, ImageConfig config) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        insertImage(doc, cs, bufferedImage, x, y, config);
    }

    /**
     * Inserta una imagen desde BufferedImage.
     */
    public void insertImage(PDDocument doc, PDPageContentStream cs, BufferedImage image,
                            float x, float y, ImageConfig config) throws IOException {
        if (image == null) return;

        PDImageXObject pdImage = LosslessFactory.createFromImage(doc, image);

        // Calcular dimensiones según modo de escala
        float[] dimensions = calculateImageDimensions(pdImage, config);
        float drawWidth = dimensions[0];
        float drawHeight = dimensions[1];

        // Calcular posición X según alineación
        float drawX = x;
        // La Y en PDFBox es desde abajo, restar altura
        float drawY = y - drawHeight;

        // Dibujar borde si está configurado
        if (config.isDrawBorder()) {
            cs.setStrokingColor(config.getBorderColor());
            cs.setLineWidth(config.getBorderWidth());
            cs.addRect(drawX, drawY, drawWidth, drawHeight);
            cs.stroke();
        }

        cs.drawImage(pdImage, drawX, drawY, drawWidth, drawHeight);
    }

    /**
     * Inserta una imagen usando el paginador.
     */
    public void insertImage(PDFPaginator paginator, byte[] imageBytes,
                            ImageConfig config) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        PDImageXObject pdImage = LosslessFactory.createFromImage(paginator.getDocument(), image);
        float[] dims = calculateImageDimensions(pdImage, config);
        float height = dims[1];

        paginator.checkSpace(height);

        float x = paginator.getStartX();
        if (config.getHorizontalAlign() == ImageConfig.HorizontalAlign.CENTER) {
            x = paginator.getStartX() + (paginator.getUsableWidth() - dims[0]) / 2;
        } else if (config.getHorizontalAlign() == ImageConfig.HorizontalAlign.RIGHT) {
            x = paginator.getStartX() + paginator.getUsableWidth() - dims[0];
        }

        insertImage(paginator.getDocument(), paginator.getContentStream(),
                   image, x, paginator.getCurrentY(), config);
        paginator.advanceY(height + 5);
    }

    private float[] calculateImageDimensions(PDImageXObject image, ImageConfig config) {
        float origWidth = image.getWidth();
        float origHeight = image.getHeight();
        float targetWidth = config.getWidth();
        float targetHeight = config.getHeight();

        switch (config.getScaleMode()) {
            case ORIGINAL:
                return new float[] { origWidth, origHeight };
            case FIT_WIDTH:
                float scaleW = targetWidth / origWidth;
                return new float[] { targetWidth, origHeight * scaleW };
            case FIT_HEIGHT:
                float scaleH = targetHeight / origHeight;
                return new float[] { origWidth * scaleH, targetHeight };
            case STRETCH:
                return new float[] { targetWidth, targetHeight };
            case FIT_BOX:
            default:
                float scale = Math.min(targetWidth / origWidth, targetHeight / origHeight);
                return new float[] { origWidth * scale, origHeight * scale };
        }
    }

    // ==================== CREACIÓN DE TABLAS ====================

    /**
     * Crea una tabla desde datos manuales (lista de filas, cada fila es lista de celdas).
     */
    public float createTable(PDDocument doc, PDPageContentStream cs,
                             List<List<String>> data, float x, float y,
                             TableConfig config) throws IOException {
        if (data == null || data.isEmpty()) return 0;

        int numCols = data.get(0).size();
        config.setNumColumns(numCols);
        float[] colWidths = config.getColumnWidths();
        float currentY = y;

        for (int row = 0; row < data.size(); row++) {
            List<String> rowData = data.get(row);
            boolean isHeader = (row == 0);

            // Calcular altura de la fila (usa el máximo entre todas las columnas)
            float rowHeight = calculateRowHeightWithVariableWidths(doc, rowData, colWidths, config, isHeader);

            // Dibujar celdas de la fila
            float cellX = x;
            for (int col = 0; col < rowData.size(); col++) {
                drawTableCell(doc, cs, rowData.get(col), cellX, currentY,
                             colWidths[col], rowHeight, config, isHeader, row);
                cellX += colWidths[col];
            }

            currentY -= rowHeight;
        }

        return y - currentY;
    }

    /**
     * Crea una tabla desde una lista de DTOs.
     */
    public <T> float createTableFromDTO(PDDocument doc, PDPageContentStream cs,
                                         List<T> dtoList, float x, float y,
                                         TableConfig config) throws IOException {
        if (dtoList == null || dtoList.isEmpty()) return 0;

        List<List<String>> tableData = convertDTOToTableData(dtoList);
        return createTable(doc, cs, tableData, x, y, config);
    }

    /**
     * Crea una tabla usando el paginador con paginación automática.
     */
    public void createTable(PDFPaginator paginator, List<List<String>> data,
                            TableConfig config) throws IOException {
        if (data == null || data.isEmpty()) return;

        int numCols = data.get(0).size();
        config.setNumColumns(numCols);
        config.setWidth(paginator.getUsableWidth());
        float[] colWidths = config.getColumnWidths();

        List<String> headerRow = data.get(0);
        float headerHeight = calculateRowHeightWithVariableWidths(paginator.getDocument(), headerRow, colWidths, config, true);

        for (int row = 0; row < data.size(); row++) {
            List<String> rowData = data.get(row);
            boolean isHeader = (row == 0);
            float rowHeight = calculateRowHeightWithVariableWidths(paginator.getDocument(), rowData, colWidths, config, isHeader);

            // Verificar espacio y crear nueva página si es necesario
            if (paginator.checkSpace(rowHeight) && row > 0) {
                // Redibujar header en nueva página
                float cellX = paginator.getStartX();
                for (int col = 0; col < headerRow.size(); col++) {
                    drawTableCell(paginator.getDocument(), paginator.getContentStream(),
                                 headerRow.get(col), cellX, paginator.getCurrentY(),
                                 colWidths[col], headerHeight, config, true, 0);
                    cellX += colWidths[col];
                }
                paginator.advanceY(headerHeight);
            }

            // Dibujar fila actual
            float cellX = paginator.getStartX();
            for (int col = 0; col < rowData.size(); col++) {
                drawTableCell(paginator.getDocument(), paginator.getContentStream(),
                             rowData.get(col), cellX, paginator.getCurrentY(),
                             colWidths[col], rowHeight, config, isHeader, row);
                cellX += colWidths[col];
            }
            paginator.advanceY(rowHeight);
        }
    }

    /**
     * Crea una tabla desde DTOs usando el paginador.
     */
    public <T> void createTableFromDTO(PDFPaginator paginator, List<T> dtoList,
                                        TableConfig config) throws IOException {
        List<List<String>> tableData = convertDTOToTableData(dtoList);
        createTable(paginator, tableData, config);
    }

    private void drawTableCell(PDDocument doc, PDPageContentStream cs, String text,
                               float x, float y, float width, float height,
                               TableConfig config, boolean isHeader, int rowIndex) throws IOException {
        // Color de fondo
        Color bgColor = isHeader ? config.getHeaderBackgroundColor() : config.getRowColor(rowIndex);
        cs.setNonStrokingColor(bgColor);
        cs.addRect(x, y - height, width, height);
        cs.fill();

        // Bordes
        if (config.isDrawCellBorders()) {
            applyTableBorderStyle(cs, config);
            cs.addRect(x, y - height, width, height);
            cs.stroke();
            resetLineStyle(cs);
        }

        // Texto
        if (text != null && !text.isEmpty()) {
            TextBoxConfig.FontStyle fontStyle = isHeader ? config.getHeaderFontStyle() : config.getFontStyle();
            float fontSize = isHeader ? config.getHeaderFontSize() : config.getFontSize();
            Color textColor = isHeader ? config.getHeaderTextColor() : config.getTextColor();
            TextBoxConfig.TextAlign align = isHeader ? config.getHeaderAlign() : config.getDefaultAlign();

            PDFont font = getFont(doc, fontStyle);
            float padding = config.getPadding();
            float textWidth = width - (padding * 2);
            List<String> lines = wrapText(text, font, fontSize, textWidth);
            float leading = fontSize * 1.2f;

            float textY = y - padding - fontSize;
            cs.setNonStrokingColor(textColor);
            cs.setFont(font, fontSize);

            for (String line : lines) {
                float textX = calculateTextX(x, width, line, font, fontSize, padding, align);
                cs.beginText();
                cs.newLineAtOffset(textX, textY);
                cs.showText(line);
                cs.endText();
                textY -= leading;
            }
        }
    }

    private void applyTableBorderStyle(PDPageContentStream cs, TableConfig config) throws IOException {
        cs.setStrokingColor(config.getBorderColor());
        cs.setLineWidth(config.getBorderWidth());

        switch (config.getBorderStyle()) {
            case DASHED:
                cs.setLineDashPattern(new float[] { 5, 3 }, 0);
                break;
            case DOTTED:
                cs.setLineDashPattern(new float[] { 1, 2 }, 0);
                break;
            default:
                break;
        }
    }

    private float calculateRowHeight(PDDocument doc, List<String> rowData, float colWidth,
                                     TableConfig config, boolean isHeader) throws IOException {
        float maxHeight = config.getRowHeight();
        float fontSize = isHeader ? config.getHeaderFontSize() : config.getFontSize();
        TextBoxConfig.FontStyle fontStyle = isHeader ? config.getHeaderFontStyle() : config.getFontStyle();
        PDFont font = getFont(doc, fontStyle);
        float padding = config.getPadding();
        float textWidth = colWidth - (padding * 2);
        float leading = fontSize * 1.2f;

        for (String cell : rowData) {
            if (cell != null && !cell.isEmpty()) {
                List<String> lines = wrapText(cell, font, fontSize, textWidth);
                float cellHeight = (lines.size() * leading) + (padding * 2);
                maxHeight = Math.max(maxHeight, cellHeight);
            }
        }

        return maxHeight;
    }

    /**
     * Calcula la altura de la fila considerando anchos de columna variables.
     */
    private float calculateRowHeightWithVariableWidths(PDDocument doc, List<String> rowData, float[] colWidths,
                                                       TableConfig config, boolean isHeader) throws IOException {
        float maxHeight = config.getRowHeight();
        float fontSize = isHeader ? config.getHeaderFontSize() : config.getFontSize();
        TextBoxConfig.FontStyle fontStyle = isHeader ? config.getHeaderFontStyle() : config.getFontStyle();
        PDFont font = getFont(doc, fontStyle);
        float padding = config.getPadding();
        float leading = fontSize * 1.2f;

        for (int i = 0; i < rowData.size(); i++) {
            String cell = rowData.get(i);
            float colWidth = (i < colWidths.length) ? colWidths[i] : colWidths[colWidths.length - 1];
            float textWidth = colWidth - (padding * 2);

            if (cell != null && !cell.isEmpty()) {
                List<String> lines = wrapText(cell, font, fontSize, textWidth);
                float cellHeight = (lines.size() * leading) + (padding * 2);
                maxHeight = Math.max(maxHeight, cellHeight);
            }
        }

        return maxHeight;
    }

    private <T> List<List<String>> convertDTOToTableData(List<T> dtoList) {
        List<List<String>> tableData = new ArrayList<>();
        if (dtoList.isEmpty()) return tableData;

        Class<?> clazz = dtoList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<Field> validFields = new ArrayList<>();

        for (Field field : fields) {
            if (!field.isSynthetic() && !java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                validFields.add(field);
            }
        }

        // Header row
        List<String> headerRow = new ArrayList<>();
        for (Field field : validFields) {
            headerRow.add(formatFieldName(field.getName()));
        }
        tableData.add(headerRow);

        // Data rows
        for (T dto : dtoList) {
            List<String> row = new ArrayList<>();
            for (Field field : validFields) {
                row.add(getFieldValue(dto, field));
            }
            tableData.add(row);
        }

        return tableData;
    }

    // ==================== MARCA DE AGUA ====================

    /**
     * Agrega marca de agua a todas las páginas del documento.
     */
    public void addWatermark(PDDocument doc, WatermarkConfig config) throws IOException {
        for (PDPage page : doc.getPages()) {
            addWatermarkToPage(doc, page, config);
        }
    }

    private void addWatermarkToPage(PDDocument doc, PDPage page, WatermarkConfig config) throws IOException {
        try (PDPageContentStream cs = new PDPageContentStream(doc, page,
                PDPageContentStream.AppendMode.APPEND, true, true)) {

            // Configurar transparencia
            PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
            graphicsState.setNonStrokingAlphaConstant(config.getOpacity());
            graphicsState.setStrokingAlphaConstant(config.getOpacity());
            cs.setGraphicsStateParameters(graphicsState);

            if (config.getType() == WatermarkConfig.WatermarkType.TEXT) {
                addTextWatermark(doc, cs, page, config);
            } else {
                addImageWatermark(doc, cs, page, config);
            }
        }
    }

    private void addTextWatermark(PDDocument doc, PDPageContentStream cs,
                                   PDPage page, WatermarkConfig config) throws IOException {
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();

        PDFont font = getFont(doc, config.getFontStyle());
        float fontSize = config.getFontSize();
        String text = config.getText();
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;

        cs.setNonStrokingColor(config.getColor());
        cs.setFont(font, fontSize);

        switch (config.getPosition()) {
            case DIAGONAL:
                float x = (pageWidth - textWidth) / 2;
                float y = pageHeight / 2;

                cs.beginText();
                Matrix matrix = Matrix.getRotateInstance(
                        Math.toRadians(config.getRotation()), x, y);
                cs.setTextMatrix(matrix);
                cs.showText(text);
                cs.endText();
                break;

            case CENTER:
                cs.beginText();
                cs.newLineAtOffset((pageWidth - textWidth) / 2, pageHeight / 2);
                cs.showText(text);
                cs.endText();
                break;

            case TILED:
                float spacingX = config.getTileSpacingX();
                float spacingY = config.getTileSpacingY();

                for (float tileY = 50; tileY < pageHeight; tileY += spacingY) {
                    for (float tileX = 50; tileX < pageWidth; tileX += spacingX) {
                        cs.beginText();
                        Matrix tileMatrix = Matrix.getRotateInstance(
                                Math.toRadians(config.getRotation()), tileX, tileY);
                        cs.setTextMatrix(tileMatrix);
                        cs.showText(text);
                        cs.endText();
                    }
                }
                break;

            case TOP:
                cs.beginText();
                cs.newLineAtOffset((pageWidth - textWidth) / 2, pageHeight - 50);
                cs.showText(text);
                cs.endText();
                break;

            case BOTTOM:
                cs.beginText();
                cs.newLineAtOffset((pageWidth - textWidth) / 2, 50);
                cs.showText(text);
                cs.endText();
                break;
        }
    }

    private void addImageWatermark(PDDocument doc, PDPageContentStream cs,
                                    PDPage page, WatermarkConfig config) throws IOException {
        if (config.getImageBytes() == null) return;

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(config.getImageBytes()));
        PDImageXObject pdImage = LosslessFactory.createFromImage(doc, image);

        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        float imgWidth = pdImage.getWidth();
        float imgHeight = pdImage.getHeight();

        float x = (pageWidth - imgWidth) / 2;
        float y = (pageHeight - imgHeight) / 2;

        cs.drawImage(pdImage, x, y, imgWidth, imgHeight);
    }

    // ==================== GRÁFICOS JFREECHART ====================

    /**
     * Inserta un gráfico JFreeChart en el PDF.
     */
    public void insertChart(PDDocument doc, PDPageContentStream cs, JFreeChart chart,
                            float x, float y, ChartConfig config) throws IOException {
        ChartGenerator generator = new ChartGenerator();
        BufferedImage chartImage = generator.chartToImage(chart,
                (int) config.getWidth(), (int) config.getHeight());

        ImageConfig imgConfig = new ImageConfig()
                .size(config.getWidth(), config.getHeight())
                .scaleMode(ImageConfig.ScaleMode.STRETCH);

        insertImage(doc, cs, chartImage, x, y, imgConfig);
    }

    /**
     * Crea e inserta un gráfico usando el paginador.
     */
    public void insertChart(PDFPaginator paginator, ChartConfig config,
                            List<?> data) throws IOException {
        ChartGenerator generator = new ChartGenerator();
        JFreeChart chart = generator.createChart(config, data);
        BufferedImage chartImage = generator.chartToImage(chart,
                (int) config.getWidth(), (int) config.getHeight());

        paginator.checkSpace(config.getHeight());

        float x = paginator.getStartX();
        // Centrar gráfico
        x = paginator.getStartX() + (paginator.getUsableWidth() - config.getWidth()) / 2;

        ImageConfig imgConfig = new ImageConfig()
                .size(config.getWidth(), config.getHeight())
                .scaleMode(ImageConfig.ScaleMode.STRETCH);

        insertImage(paginator.getDocument(), paginator.getContentStream(),
                   chartImage, x, paginator.getCurrentY(), imgConfig);
        paginator.advanceY(config.getHeight() + 10);
    }

    // ==================== NUMERACIÓN DE PÁGINAS ====================

    /**
     * Numera todas las páginas del documento.
     */
    public void addPageNumbers(PDDocument doc, float marginRight, float marginBottom,
                               float fontSize, TextBoxConfig.FontStyle fontStyle) throws IOException {
        int totalPages = doc.getNumberOfPages();
        PDFont font = getFont(doc, fontStyle);

        for (int i = 0; i < totalPages; i++) {
            PDPage page = doc.getPage(i);
            float pageWidth = page.getMediaBox().getWidth();

            String text = String.format("Página %d de %d", i + 1, totalPages);
            float textWidth = font.getStringWidth(text) / 1000 * fontSize;

            try (PDPageContentStream cs = new PDPageContentStream(doc, page,
                    PDPageContentStream.AppendMode.APPEND, true, true)) {
                cs.setFont(font, fontSize);
                cs.setNonStrokingColor(Color.DARK_GRAY);
                cs.beginText();
                cs.newLineAtOffset(pageWidth - marginRight - textWidth, marginBottom);
                cs.showText(text);
                cs.endText();
            }
        }
    }

    /**
     * Numera páginas con formato simple (solo número).
     */
    public void addSimplePageNumbers(PDDocument doc, float marginRight,
                                      float marginBottom, float fontSize) throws IOException {
        int totalPages = doc.getNumberOfPages();
        PDFont font = getFont(doc, TextBoxConfig.FontStyle.NORMAL);

        for (int i = 0; i < totalPages; i++) {
            PDPage page = doc.getPage(i);
            float pageWidth = page.getMediaBox().getWidth();

            String text = String.valueOf(i + 1);
            float textWidth = font.getStringWidth(text) / 1000 * fontSize;

            try (PDPageContentStream cs = new PDPageContentStream(doc, page,
                    PDPageContentStream.AppendMode.APPEND, true, true)) {
                cs.setFont(font, fontSize);
                cs.setNonStrokingColor(Color.DARK_GRAY);
                cs.beginText();
                cs.newLineAtOffset(pageWidth - marginRight - textWidth, marginBottom);
                cs.showText(text);
                cs.endText();
            }
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Obtiene una fuente según el estilo.
     */
    public PDFont getFont(PDDocument doc, TextBoxConfig.FontStyle style) throws IOException {
        String fontPath;
        switch (style) {
            case BOLD:
                fontPath = FONT_BOLD;
                break;
            case ITALIC:
                fontPath = FONT_ITALIC;
                break;
            case BOLD_ITALIC:
                fontPath = FONT_BOLD_ITALIC;
                break;
            case NORMAL:
            default:
                fontPath = FONT_REGULAR;
                break;
        }
        return loadFont(doc, fontPath);
    }

    private PDFont loadFont(PDDocument doc, String fontPath) throws IOException {
        byte[] fontBytes = fontCache.get(fontPath);

        if (fontBytes == null) {
            try (InputStream is = getClass().getResourceAsStream(fontPath)) {
                if (is == null) {
                    throw new IOException("Fuente no encontrada: " + fontPath);
                }
                fontBytes = is.readAllBytes();
                fontCache.put(fontPath, fontBytes);
            }
        }

        return PDType0Font.load(doc, new ByteArrayInputStream(fontBytes), true);
    }

    /**
     * Divide texto en líneas que quepan en el ancho especificado.
     */
    public List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) return lines;

        String[] paragraphs = text.split("\n");

        for (String paragraph : paragraphs) {
            String[] words = paragraph.split("\\s+");
            StringBuilder currentLine = new StringBuilder();

            for (String word : words) {
                String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                float width = font.getStringWidth(testLine) / 1000 * fontSize;

                if (width <= maxWidth) {
                    currentLine = new StringBuilder(testLine);
                } else {
                    if (currentLine.length() > 0) {
                        lines.add(currentLine.toString());
                    }
                    // Verificar si la palabra sola excede el ancho
                    float wordWidth = font.getStringWidth(word) / 1000 * fontSize;
                    if (wordWidth > maxWidth) {
                        lines.addAll(breakLongWord(word, font, fontSize, maxWidth));
                        currentLine = new StringBuilder();
                    } else {
                        currentLine = new StringBuilder(word);
                    }
                }
            }

            if (currentLine.length() > 0) {
                lines.add(currentLine.toString());
            }
        }

        return lines;
    }

    private List<String> breakLongWord(String word, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (char c : word.toCharArray()) {
            String test = current.toString() + c;
            float width = font.getStringWidth(test) / 1000 * fontSize;

            if (width <= maxWidth) {
                current.append(c);
            } else {
                if (current.length() > 0) {
                    parts.add(current.toString());
                }
                current = new StringBuilder(String.valueOf(c));
            }
        }

        if (current.length() > 0) {
            parts.add(current.toString());
        }

        return parts;
    }

    private float calculateTextX(float boxX, float boxWidth, String text, PDFont font,
                                  float fontSize, float padding, TextBoxConfig.TextAlign align) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;

        switch (align) {
            case CENTER:
                return boxX + (boxWidth - textWidth) / 2;
            case RIGHT:
                return boxX + boxWidth - textWidth - padding;
            case LEFT:
            default:
                return boxX + padding;
        }
    }

    private float calculateTextY(float boxY, float boxHeight, int numLines, float leading,
                                  float padding, TextBoxConfig.VerticalAlign align) {
        float textHeight = numLines * leading;

        switch (align) {
            case MIDDLE:
                return boxY - (boxHeight - textHeight) / 2 - leading + (leading - leading / 1.2f);
            case BOTTOM:
                return boxY - boxHeight + textHeight + padding - leading + (leading - leading / 1.2f);
            case TOP:
            default:
                return boxY - padding - (leading - leading / 1.2f);
        }
    }

    private String formatFieldName(String fieldName) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (i == 0) {
                result.append(Character.toUpperCase(c));
            } else if (Character.isUpperCase(c)) {
                result.append(' ').append(c);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private String getFieldValue(Object obj, Field field) {
        try {
            Object value = field.get(obj);
            if (value == null) return "";

            if (value instanceof LocalDate) {
                return ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else if (value instanceof LocalDateTime) {
                return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            } else if (value instanceof Boolean) {
                return (Boolean) value ? "Sí" : "No";
            } else if (value instanceof Double || value instanceof Float) {
                return String.format("%.2f", value);
            }

            return value.toString();
        } catch (IllegalAccessException e) {
            return "";
        }
    }

    // ==================== INSERCIÓN DE CÓDIGOS DE BARRAS ====================

    /**
     * Inserta un código de barras en el PDF.
     *
     * @param doc     Documento PDF
     * @param cs      Content Stream de la página
     * @param content Contenido a codificar
     * @param x       Posición X
     * @param y       Posición Y (desde arriba)
     * @param config  Configuración del código de barras
     * @throws IOException Si hay error al insertar
     */
    public void insertBarcode(PDDocument doc, PDPageContentStream cs, String content,
                              float x, float y, BarcodeConfig config) throws IOException {
        try {
            BufferedImage barcodeImage = BarcodeGenerator.generateBarcode(content, config);

            // Calcular altura real para posicionar correctamente
            float drawY = y - config.getHeight();

            // Crear imagen PDFBox
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, barcodeImage);

            // Aplicar opacidad si es diferente de 1.0
            if (config.getOpacity() < 1.0f) {
                PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                graphicsState.setNonStrokingAlphaConstant(config.getOpacity());
                cs.setGraphicsStateParameters(graphicsState);
            }

            cs.drawImage(pdImage, x, drawY, config.getWidth(), config.getHeight());

            // Restaurar opacidad
            if (config.getOpacity() < 1.0f) {
                PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                graphicsState.setNonStrokingAlphaConstant(1.0f);
                cs.setGraphicsStateParameters(graphicsState);
            }
        } catch (com.google.zxing.WriterException e) {
            throw new IOException("Error al generar código de barras: " + e.getMessage(), e);
        }
    }

    /**
     * Inserta un código de barras con configuración por defecto.
     */
    public void insertBarcode(PDDocument doc, PDPageContentStream cs, String content,
                              float x, float y) throws IOException {
        insertBarcode(doc, cs, content, x, y, new BarcodeConfig());
    }

    /**
     * Inserta un código de barras usando el paginador con paginación automática.
     *
     * @param paginator Paginador
     * @param content   Contenido a codificar
     * @param config    Configuración del código de barras
     * @throws IOException Si hay error al insertar
     */
    public void insertBarcode(PDFPaginator paginator, String content,
                              BarcodeConfig config) throws IOException {
        float height = config.getHeight();
        paginator.checkSpace(height + 5);

        insertBarcode(paginator.getDocument(), paginator.getContentStream(),
                     content, paginator.getStartX(), paginator.getCurrentY(), config);
        paginator.advanceY(height + 5);
    }

    /**
     * Inserta un código de barras centrado usando el paginador.
     */
    public void insertBarcodeCentered(PDFPaginator paginator, String content,
                                       BarcodeConfig config) throws IOException {
        float height = config.getHeight();
        paginator.checkSpace(height + 5);

        float x = paginator.getStartX() + (paginator.getUsableWidth() - config.getWidth()) / 2;
        insertBarcode(paginator.getDocument(), paginator.getContentStream(),
                     content, x, paginator.getCurrentY(), config);
        paginator.advanceY(height + 5);
    }

    /**
     * Inserta un código de barras CODE_128 con tamaño específico.
     */
    public void insertCode128(PDDocument doc, PDPageContentStream cs, String content,
                              float x, float y, int width, int height) throws IOException {
        insertBarcode(doc, cs, content, x, y, BarcodeConfig.code128().size(width, height));
    }

    /**
     * Inserta un código de barras EAN-13 con tamaño específico.
     */
    public void insertEAN13(PDDocument doc, PDPageContentStream cs, String content,
                            float x, float y, int width, int height) throws IOException {
        insertBarcode(doc, cs, content, x, y, BarcodeConfig.ean13().size(width, height));
    }

    // ==================== INSERCIÓN DE CÓDIGOS QR ====================

    /**
     * Inserta un código QR en el PDF.
     *
     * @param doc     Documento PDF
     * @param cs      Content Stream de la página
     * @param content Contenido a codificar (URL, texto, etc.)
     * @param x       Posición X
     * @param y       Posición Y (desde arriba)
     * @param config  Configuración del código QR
     * @throws IOException Si hay error al insertar
     */
    public void insertQRCode(PDDocument doc, PDPageContentStream cs, String content,
                             float x, float y, QRCodeConfig config) throws IOException {
        try {
            BufferedImage qrImage = BarcodeGenerator.generateQRCode(content, config);

            // Calcular posición Y (PDFBox usa coordenadas desde abajo)
            float drawY = y - config.getSize();

            // Crear imagen PDFBox
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, qrImage);

            // Aplicar opacidad si es diferente de 1.0
            if (config.getOpacity() < 1.0f) {
                PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                graphicsState.setNonStrokingAlphaConstant(config.getOpacity());
                cs.setGraphicsStateParameters(graphicsState);
            }

            cs.drawImage(pdImage, x, drawY, config.getSize(), config.getSize());

            // Restaurar opacidad
            if (config.getOpacity() < 1.0f) {
                PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                graphicsState.setNonStrokingAlphaConstant(1.0f);
                cs.setGraphicsStateParameters(graphicsState);
            }
        } catch (com.google.zxing.WriterException e) {
            throw new IOException("Error al generar código QR: " + e.getMessage(), e);
        }
    }

    /**
     * Inserta un código QR con configuración por defecto.
     */
    public void insertQRCode(PDDocument doc, PDPageContentStream cs, String content,
                             float x, float y) throws IOException {
        insertQRCode(doc, cs, content, x, y, new QRCodeConfig());
    }

    /**
     * Inserta un código QR con tamaño específico.
     */
    public void insertQRCode(PDDocument doc, PDPageContentStream cs, String content,
                             float x, float y, int size) throws IOException {
        insertQRCode(doc, cs, content, x, y, QRCodeConfig.defaults().size(size));
    }

    /**
     * Inserta un código QR usando el paginador con paginación automática.
     *
     * @param paginator Paginador
     * @param content   Contenido a codificar
     * @param config    Configuración del código QR
     * @throws IOException Si hay error al insertar
     */
    public void insertQRCode(PDFPaginator paginator, String content,
                             QRCodeConfig config) throws IOException {
        float size = config.getSize();
        paginator.checkSpace(size + 5);

        insertQRCode(paginator.getDocument(), paginator.getContentStream(),
                    content, paginator.getStartX(), paginator.getCurrentY(), config);
        paginator.advanceY(size + 5);
    }

    /**
     * Inserta un código QR centrado usando el paginador.
     */
    public void insertQRCodeCentered(PDFPaginator paginator, String content,
                                      QRCodeConfig config) throws IOException {
        float size = config.getSize();
        paginator.checkSpace(size + 5);

        float x = paginator.getStartX() + (paginator.getUsableWidth() - size) / 2;
        insertQRCode(paginator.getDocument(), paginator.getContentStream(),
                    content, x, paginator.getCurrentY(), config);
        paginator.advanceY(size + 5);
    }

    /**
     * Inserta un código QR alineado a la derecha usando el paginador.
     */
    public void insertQRCodeRight(PDFPaginator paginator, String content,
                                   QRCodeConfig config) throws IOException {
        float size = config.getSize();
        paginator.checkSpace(size + 5);

        float x = paginator.getStartX() + paginator.getUsableWidth() - size;
        insertQRCode(paginator.getDocument(), paginator.getContentStream(),
                    content, x, paginator.getCurrentY(), config);
        paginator.advanceY(size + 5);
    }

    /**
     * Inserta un código QR para URL.
     */
    public void insertURLQRCode(PDDocument doc, PDPageContentStream cs, String url,
                                float x, float y, int size) throws IOException {
        insertQRCode(doc, cs, url, x, y, QRCodeConfig.defaults().size(size).highErrorCorrection());
    }

    /**
     * Inserta un código QR para vCard (contacto).
     */
    public void insertVCardQRCode(PDDocument doc, PDPageContentStream cs,
                                   String name, String phone, String email, String company,
                                   float x, float y, int size) throws IOException {
        try {
            BufferedImage qrImage = BarcodeGenerator.generateVCardQRCode(name, phone, email, company, size);
            float drawY = y - size;
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, qrImage);
            cs.drawImage(pdImage, x, drawY, size, size);
        } catch (com.google.zxing.WriterException e) {
            throw new IOException("Error al generar código QR vCard: " + e.getMessage(), e);
        }
    }

    /**
     * Inserta un código QR para conexión WiFi.
     */
    public void insertWiFiQRCode(PDDocument doc, PDPageContentStream cs,
                                  String ssid, String password, String encryption, boolean hidden,
                                  float x, float y, int size) throws IOException {
        try {
            BufferedImage qrImage = BarcodeGenerator.generateWiFiQRCode(ssid, password, encryption, hidden, size);
            float drawY = y - size;
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, qrImage);
            cs.drawImage(pdImage, x, drawY, size, size);
        } catch (com.google.zxing.WriterException e) {
            throw new IOException("Error al generar código QR WiFi: " + e.getMessage(), e);
        }
    }

    /**
     * Inserta un código QR para geolocalización.
     */
    public void insertGeoQRCode(PDDocument doc, PDPageContentStream cs,
                                 double latitude, double longitude,
                                 float x, float y, int size) throws IOException {
        try {
            BufferedImage qrImage = BarcodeGenerator.generateGeoQRCode(latitude, longitude, size);
            float drawY = y - size;
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, qrImage);
            cs.drawImage(pdImage, x, drawY, size, size);
        } catch (com.google.zxing.WriterException e) {
            throw new IOException("Error al generar código QR Geo: " + e.getMessage(), e);
        }
    }
}

