package saul.pdf.renderer;

import java.awt.*;

/**
 * Configuración para cajas de texto en PDF.
 * Define fuente, tamaño, color, alineación y propiedades del borde.
 */
public class TextBoxConfig {

    /**
     * Estilos de fuente disponibles.
     */
    public enum FontStyle {
        NORMAL, BOLD, ITALIC, BOLD_ITALIC
    }

    /**
     * Alineación horizontal del texto.
     */
    public enum TextAlign {
        LEFT, CENTER, RIGHT
    }

    /**
     * Alineación vertical del texto.
     */
    public enum VerticalAlign {
        TOP, MIDDLE, BOTTOM
    }

    private FontStyle fontStyle;
    private float fontSize;
    private Color textColor;
    private TextAlign textAlign;
    private VerticalAlign verticalAlign;
    private float lineSpacing;

    // Propiedades del rectángulo contenedor
    private boolean drawBorder;
    private Color borderColor;
    private float borderWidth;
    private boolean fillBackground;
    private Color backgroundColor;
    private float padding;

    /**
     * Constructor con valores por defecto.
     */
    public TextBoxConfig() {
        this.fontStyle = FontStyle.NORMAL;
        this.fontSize = 12f;
        this.textColor = Color.BLACK;
        this.textAlign = TextAlign.LEFT;
        this.verticalAlign = VerticalAlign.TOP;
        this.lineSpacing = 1.2f;
        this.drawBorder = false;
        this.borderColor = Color.BLACK;
        this.borderWidth = 1f;
        this.fillBackground = false;
        this.backgroundColor = Color.WHITE;
        this.padding = 5f;
    }

    // ==================== MÉTODOS FLUIDOS ====================

    public TextBoxConfig fontStyle(FontStyle style) {
        this.fontStyle = style;
        return this;
    }

    public TextBoxConfig fontSize(float size) {
        this.fontSize = size;
        return this;
    }

    public TextBoxConfig textColor(Color color) {
        this.textColor = color;
        return this;
    }

    public TextBoxConfig textAlign(TextAlign align) {
        this.textAlign = align;
        return this;
    }

    public TextBoxConfig verticalAlign(VerticalAlign align) {
        this.verticalAlign = align;
        return this;
    }

    public TextBoxConfig lineSpacing(float spacing) {
        this.lineSpacing = spacing;
        return this;
    }

    public TextBoxConfig border(Color color, float width) {
        this.drawBorder = true;
        this.borderColor = color;
        this.borderWidth = width;
        return this;
    }

    public TextBoxConfig border(Color color) {
        return border(color, 1f);
    }

    public TextBoxConfig noBorder() {
        this.drawBorder = false;
        return this;
    }

    public TextBoxConfig background(Color color) {
        this.fillBackground = true;
        this.backgroundColor = color;
        return this;
    }

    public TextBoxConfig noBackground() {
        this.fillBackground = false;
        return this;
    }

    public TextBoxConfig padding(float padding) {
        this.padding = padding;
        return this;
    }

    // ==================== CONFIGURACIONES PREDEFINIDAS ====================

    public static TextBoxConfig normal() {
        return new TextBoxConfig();
    }

    public static TextBoxConfig title() {
        return new TextBoxConfig()
                .fontStyle(FontStyle.BOLD)
                .fontSize(18f)
                .textAlign(TextAlign.CENTER);
    }

    public static TextBoxConfig subtitle() {
        return new TextBoxConfig()
                .fontStyle(FontStyle.BOLD)
                .fontSize(14f);
    }

    public static TextBoxConfig header() {
        return new TextBoxConfig()
                .fontStyle(FontStyle.BOLD)
                .fontSize(12f)
                .background(new Color(200, 200, 200))
                .padding(8f);
    }

    public static TextBoxConfig boxed() {
        return new TextBoxConfig()
                .border(Color.BLACK, 1f)
                .padding(10f);
    }

    public static TextBoxConfig highlighted(Color bgColor) {
        return new TextBoxConfig()
                .background(bgColor)
                .padding(8f);
    }

    // ==================== GETTERS Y SETTERS ====================

    public FontStyle getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(FontStyle fontStyle) {
        this.fontStyle = fontStyle;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public TextAlign getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
    }

    public VerticalAlign getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(VerticalAlign verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public float getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(float lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public boolean isDrawBorder() {
        return drawBorder;
    }

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public boolean isFillBackground() {
        return fillBackground;
    }

    public void setFillBackground(boolean fillBackground) {
        this.fillBackground = fillBackground;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getPadding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    /**
     * Calcula el leading (espacio entre líneas) basado en fontSize y lineSpacing.
     */
    public float getLeading() {
        return fontSize * lineSpacing;
    }
}

