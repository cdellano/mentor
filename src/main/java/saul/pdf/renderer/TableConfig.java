package saul.pdf.renderer;

import java.awt.*;

/**
 * Configuración para tablas en PDF.
 * Define colores, bordes, padding, estilos de header y celdas.
 */
public class TableConfig {

    /**
     * Tipos de línea para bordes.
     */
    public enum BorderStyle {
        SOLID, DASHED, DOTTED, NONE
    }

    // Propiedades generales
    private float width;
    private float rowHeight;
    private float padding;
    private int numColumns;

    // Anchos de columnas personalizados (proporcionales)
    private float[] columnWidthRatios;

    // Propiedades de fuente
    private float fontSize;
    private float headerFontSize;
    private TextBoxConfig.FontStyle fontStyle;
    private TextBoxConfig.FontStyle headerFontStyle;
    private Color textColor;
    private Color headerTextColor;

    // Propiedades de fondo
    private Color headerBackgroundColor;
    private Color rowBackgroundColor;
    private Color alternateRowColor;
    private boolean useAlternateColors;

    // Propiedades de bordes
    private BorderStyle borderStyle;
    private float borderWidth;
    private Color borderColor;
    private boolean drawHeaderBorder;
    private boolean drawCellBorders;

    // Alineación
    private TextBoxConfig.TextAlign defaultAlign;
    private TextBoxConfig.TextAlign headerAlign;

    /**
     * Constructor con valores por defecto.
     */
    public TableConfig() {
        this.width = 500f;
        this.rowHeight = 25f;
        this.padding = 5f;
        this.numColumns = 3;
        this.columnWidthRatios = null;

        this.fontSize = 10f;
        this.headerFontSize = 10f;
        this.fontStyle = TextBoxConfig.FontStyle.NORMAL;
        this.headerFontStyle = TextBoxConfig.FontStyle.BOLD;
        this.textColor = Color.BLACK;
        this.headerTextColor = Color.BLACK;

        this.headerBackgroundColor = new Color(200, 200, 200);
        this.rowBackgroundColor = Color.WHITE;
        this.alternateRowColor = new Color(245, 245, 245);
        this.useAlternateColors = true;

        this.borderStyle = BorderStyle.SOLID;
        this.borderWidth = 0.5f;
        this.borderColor = Color.GRAY;
        this.drawHeaderBorder = true;
        this.drawCellBorders = true;

        this.defaultAlign = TextBoxConfig.TextAlign.LEFT;
        this.headerAlign = TextBoxConfig.TextAlign.CENTER;
    }

    // ==================== MÉTODOS FLUIDOS ====================

    public TableConfig width(float width) {
        this.width = width;
        return this;
    }

    public TableConfig rowHeight(float height) {
        this.rowHeight = height;
        return this;
    }

    public TableConfig padding(float padding) {
        this.padding = padding;
        return this;
    }

    public TableConfig columns(int num) {
        this.numColumns = num;
        return this;
    }

    /**
     * Define anchos de columna proporcionales.
     * @param ratios arreglo de proporciones (ej: {1, 2, 1} significa columna 2 es el doble de ancho)
     */
    public TableConfig columnWidthRatios(float... ratios) {
        this.columnWidthRatios = ratios;
        return this;
    }

    public TableConfig fontSize(float size) {
        this.fontSize = size;
        return this;
    }

    public TableConfig headerFontSize(float size) {
        this.headerFontSize = size;
        return this;
    }

    public TableConfig fontStyle(TextBoxConfig.FontStyle style) {
        this.fontStyle = style;
        return this;
    }

    public TableConfig headerFontStyle(TextBoxConfig.FontStyle style) {
        this.headerFontStyle = style;
        return this;
    }

    public TableConfig textColor(Color color) {
        this.textColor = color;
        return this;
    }

    public TableConfig headerTextColor(Color color) {
        this.headerTextColor = color;
        return this;
    }

    public TableConfig headerBackground(Color color) {
        this.headerBackgroundColor = color;
        return this;
    }

    public TableConfig rowBackground(Color color) {
        this.rowBackgroundColor = color;
        return this;
    }

    public TableConfig alternateRowColor(Color color) {
        this.alternateRowColor = color;
        this.useAlternateColors = true;
        return this;
    }

    public TableConfig noAlternateColors() {
        this.useAlternateColors = false;
        return this;
    }

    public TableConfig border(BorderStyle style, float width, Color color) {
        this.borderStyle = style;
        this.borderWidth = width;
        this.borderColor = color;
        return this;
    }

    public TableConfig border(BorderStyle style) {
        this.borderStyle = style;
        return this;
    }

    public TableConfig noBorders() {
        this.drawCellBorders = false;
        this.drawHeaderBorder = false;
        return this;
    }

    public TableConfig align(TextBoxConfig.TextAlign align) {
        this.defaultAlign = align;
        return this;
    }

    public TableConfig headerAlign(TextBoxConfig.TextAlign align) {
        this.headerAlign = align;
        return this;
    }

    // ==================== CONFIGURACIONES PREDEFINIDAS ====================

    public static TableConfig simple() {
        return new TableConfig();
    }

    public static TableConfig modern() {
        return new TableConfig()
                .headerBackground(new Color(51, 122, 183))
                .headerTextColor(Color.WHITE)
                .border(BorderStyle.NONE)
                .alternateRowColor(new Color(240, 248, 255));
    }

    public static TableConfig minimal() {
        return new TableConfig()
                .headerBackground(Color.WHITE)
                .headerFontStyle(TextBoxConfig.FontStyle.BOLD)
                .border(BorderStyle.SOLID, 0.5f, Color.LIGHT_GRAY)
                .noAlternateColors();
    }

    public static TableConfig colorful(Color headerColor) {
        return new TableConfig()
                .headerBackground(headerColor)
                .headerTextColor(Color.WHITE)
                .alternateRowColor(new Color(
                        Math.min(255, headerColor.getRed() + 200),
                        Math.min(255, headerColor.getGreen() + 200),
                        Math.min(255, headerColor.getBlue() + 200)
                ));
    }

    // ==================== MÉTODOS DE CÁLCULO ====================

    /**
     * Calcula el ancho de cada columna (uniforme).
     */
    public float getColumnWidth() {
        return width / numColumns;
    }

    /**
     * Calcula el ancho de una columna específica usando ratios personalizados.
     * @param columnIndex índice de la columna (base 0)
     * @return ancho calculado de la columna
     */
    public float getColumnWidth(int columnIndex) {
        if (columnWidthRatios == null || columnIndex >= columnWidthRatios.length) {
            return getColumnWidth();
        }
        float totalRatio = 0;
        for (float ratio : columnWidthRatios) {
            totalRatio += ratio;
        }
        return (columnWidthRatios[columnIndex] / totalRatio) * width;
    }

    /**
     * Calcula todos los anchos de columnas.
     * @return arreglo con los anchos de cada columna
     */
    public float[] getColumnWidths() {
        float[] widths = new float[numColumns];
        if (columnWidthRatios != null && columnWidthRatios.length == numColumns) {
            float totalRatio = 0;
            for (float ratio : columnWidthRatios) {
                totalRatio += ratio;
            }
            for (int i = 0; i < numColumns; i++) {
                widths[i] = (columnWidthRatios[i] / totalRatio) * width;
            }
        } else {
            float uniformWidth = width / numColumns;
            for (int i = 0; i < numColumns; i++) {
                widths[i] = uniformWidth;
            }
        }
        return widths;
    }

    /**
     * Verifica si tiene anchos de columna personalizados.
     */
    public boolean hasCustomColumnWidths() {
        return columnWidthRatios != null;
    }

    public float[] getColumnWidthRatios() {
        return columnWidthRatios;
    }

    public void setColumnWidthRatios(float[] columnWidthRatios) {
        this.columnWidthRatios = columnWidthRatios;
    }

    /**
     * Obtiene el color de fondo para una fila específica.
     */
    public Color getRowColor(int rowIndex) {
        if (!useAlternateColors) {
            return rowBackgroundColor;
        }
        return (rowIndex % 2 == 0) ? rowBackgroundColor : alternateRowColor;
    }

    // ==================== GETTERS Y SETTERS ====================

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
    }

    public float getPadding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public float getHeaderFontSize() {
        return headerFontSize;
    }

    public void setHeaderFontSize(float headerFontSize) {
        this.headerFontSize = headerFontSize;
    }

    public TextBoxConfig.FontStyle getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(TextBoxConfig.FontStyle fontStyle) {
        this.fontStyle = fontStyle;
    }

    public TextBoxConfig.FontStyle getHeaderFontStyle() {
        return headerFontStyle;
    }

    public void setHeaderFontStyle(TextBoxConfig.FontStyle headerFontStyle) {
        this.headerFontStyle = headerFontStyle;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Color getHeaderTextColor() {
        return headerTextColor;
    }

    public void setHeaderTextColor(Color headerTextColor) {
        this.headerTextColor = headerTextColor;
    }

    public Color getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(Color headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }

    public Color getRowBackgroundColor() {
        return rowBackgroundColor;
    }

    public void setRowBackgroundColor(Color rowBackgroundColor) {
        this.rowBackgroundColor = rowBackgroundColor;
    }

    public Color getAlternateRowColor() {
        return alternateRowColor;
    }

    public void setAlternateRowColor(Color alternateRowColor) {
        this.alternateRowColor = alternateRowColor;
    }

    public boolean isUseAlternateColors() {
        return useAlternateColors;
    }

    public void setUseAlternateColors(boolean useAlternateColors) {
        this.useAlternateColors = useAlternateColors;
    }

    public BorderStyle getBorderStyle() {
        return borderStyle;
    }

    public void setBorderStyle(BorderStyle borderStyle) {
        this.borderStyle = borderStyle;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isDrawHeaderBorder() {
        return drawHeaderBorder;
    }

    public void setDrawHeaderBorder(boolean drawHeaderBorder) {
        this.drawHeaderBorder = drawHeaderBorder;
    }

    public boolean isDrawCellBorders() {
        return drawCellBorders;
    }

    public void setDrawCellBorders(boolean drawCellBorders) {
        this.drawCellBorders = drawCellBorders;
    }

    public TextBoxConfig.TextAlign getDefaultAlign() {
        return defaultAlign;
    }

    public void setDefaultAlign(TextBoxConfig.TextAlign defaultAlign) {
        this.defaultAlign = defaultAlign;
    }

    public TextBoxConfig.TextAlign getHeaderAlign() {
        return headerAlign;
    }

    public void setHeaderAlign(TextBoxConfig.TextAlign headerAlign) {
        this.headerAlign = headerAlign;
    }
}

