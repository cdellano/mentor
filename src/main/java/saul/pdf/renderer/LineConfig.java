package saul.pdf.renderer;

import java.awt.*;

/**
 * Configuración para líneas en PDF.
 * Define tipo, grosor, color y estilo.
 */
public class LineConfig {

    /**
     * Tipos de línea disponibles.
     */
    public enum LineType {
        /** Línea sólida continua */
        SOLID,
        /** Línea con guiones */
        DASHED,
        /** Línea punteada */
        DOTTED
    }

    /**
     * Orientación de la línea.
     */
    public enum LineOrientation {
        HORIZONTAL,
        VERTICAL
    }

    private LineType lineType;
    private float thickness;
    private Color color;
    private float dashLength;
    private float gapLength;
    private float dotRadius;

    /**
     * Constructor con valores por defecto.
     */
    public LineConfig() {
        this.lineType = LineType.SOLID;
        this.thickness = 1f;
        this.color = Color.BLACK;
        this.dashLength = 5f;
        this.gapLength = 3f;
        this.dotRadius = 1f;
    }

    // ==================== MÉTODOS FLUIDOS ====================

    public LineConfig type(LineType type) {
        this.lineType = type;
        return this;
    }

    public LineConfig solid() {
        this.lineType = LineType.SOLID;
        return this;
    }

    public LineConfig dashed() {
        this.lineType = LineType.DASHED;
        return this;
    }

    public LineConfig dashed(float dashLength, float gapLength) {
        this.lineType = LineType.DASHED;
        this.dashLength = dashLength;
        this.gapLength = gapLength;
        return this;
    }

    public LineConfig dotted() {
        this.lineType = LineType.DOTTED;
        return this;
    }

    public LineConfig dotted(float radius) {
        this.lineType = LineType.DOTTED;
        this.dotRadius = radius;
        return this;
    }

    public LineConfig thickness(float thickness) {
        this.thickness = thickness;
        return this;
    }

    public LineConfig color(Color color) {
        this.color = color;
        return this;
    }

    // ==================== CONFIGURACIONES PREDEFINIDAS ====================

    public static LineConfig solidLine() {
        return new LineConfig().type(LineType.SOLID);
    }

    public static LineConfig solidLine(Color color) {
        return new LineConfig().type(LineType.SOLID).color(color);
    }

    public static LineConfig solidLine(Color color, float thickness) {
        return new LineConfig().type(LineType.SOLID).color(color).thickness(thickness);
    }

    public static LineConfig dashedLine() {
        return new LineConfig().type(LineType.DASHED);
    }

    public static LineConfig dashedLine(Color color) {
        return new LineConfig().type(LineType.DASHED).color(color);
    }

    public static LineConfig dottedLine() {
        return new LineConfig().type(LineType.DOTTED);
    }

    public static LineConfig dottedLine(Color color) {
        return new LineConfig().type(LineType.DOTTED).color(color);
    }

    public static LineConfig separator() {
        return new LineConfig()
                .type(LineType.SOLID)
                .thickness(0.5f)
                .color(Color.GRAY);
    }

    public static LineConfig thick() {
        return new LineConfig()
                .type(LineType.SOLID)
                .thickness(3f);
    }

    public static LineConfig thin() {
        return new LineConfig()
                .type(LineType.SOLID)
                .thickness(0.5f);
    }

    // ==================== MÉTODOS PARA PATRÓN DE LÍNEA ====================

    /**
     * Obtiene el patrón de línea para PDFBox.
     * @return arreglo con patrón de dash, o null para línea sólida
     */
    public float[] getDashPattern() {
        switch (lineType) {
            case DASHED:
                return new float[] { dashLength, gapLength };
            case DOTTED:
                return new float[] { dotRadius, gapLength };
            case SOLID:
            default:
                return null;
        }
    }

    // ==================== GETTERS Y SETTERS ====================

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getDashLength() {
        return dashLength;
    }

    public void setDashLength(float dashLength) {
        this.dashLength = dashLength;
    }

    public float getGapLength() {
        return gapLength;
    }

    public void setGapLength(float gapLength) {
        this.gapLength = gapLength;
    }

    public float getDotRadius() {
        return dotRadius;
    }

    public void setDotRadius(float dotRadius) {
        this.dotRadius = dotRadius;
    }
}

