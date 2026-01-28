package saul.pdf.renderer;

/**
 * Configuración para imágenes en PDF.
 * Define posición, tamaño, escala y alineación.
 */
public class ImageConfig {

    /**
     * Modo de ajuste de la imagen.
     */
    public enum ScaleMode {
        /** Mantiene dimensiones originales */
        ORIGINAL,
        /** Escala para ajustar al ancho especificado manteniendo proporción */
        FIT_WIDTH,
        /** Escala para ajustar al alto especificado manteniendo proporción */
        FIT_HEIGHT,
        /** Escala para ajustar dentro de width x height manteniendo proporción */
        FIT_BOX,
        /** Estira la imagen para llenar width x height exactos */
        STRETCH
    }

    /**
     * Alineación horizontal de la imagen.
     */
    public enum HorizontalAlign {
        LEFT, CENTER, RIGHT
    }

    private float width;
    private float height;
    private ScaleMode scaleMode;
    private HorizontalAlign horizontalAlign;
    private float opacity;
    private boolean drawBorder;
    private float borderWidth;
    private java.awt.Color borderColor;

    /**
     * Constructor con valores por defecto.
     */
    public ImageConfig() {
        this.width = 200f;
        this.height = 150f;
        this.scaleMode = ScaleMode.FIT_BOX;
        this.horizontalAlign = HorizontalAlign.LEFT;
        this.opacity = 1.0f;
        this.drawBorder = false;
        this.borderWidth = 1f;
        this.borderColor = java.awt.Color.BLACK;
    }

    // ==================== MÉTODOS FLUIDOS ====================

    public ImageConfig width(float width) {
        this.width = width;
        return this;
    }

    public ImageConfig height(float height) {
        this.height = height;
        return this;
    }

    public ImageConfig size(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ImageConfig scaleMode(ScaleMode mode) {
        this.scaleMode = mode;
        return this;
    }

    public ImageConfig align(HorizontalAlign align) {
        this.horizontalAlign = align;
        return this;
    }

    public ImageConfig center() {
        this.horizontalAlign = HorizontalAlign.CENTER;
        return this;
    }

    public ImageConfig opacity(float opacity) {
        this.opacity = Math.max(0f, Math.min(1f, opacity));
        return this;
    }

    public ImageConfig border(java.awt.Color color, float width) {
        this.drawBorder = true;
        this.borderColor = color;
        this.borderWidth = width;
        return this;
    }

    public ImageConfig border(java.awt.Color color) {
        return border(color, 1f);
    }

    public ImageConfig noBorder() {
        this.drawBorder = false;
        return this;
    }

    // ==================== CONFIGURACIONES PREDEFINIDAS ====================

    public static ImageConfig thumbnail() {
        return new ImageConfig()
                .size(100f, 100f)
                .scaleMode(ScaleMode.FIT_BOX);
    }

    public static ImageConfig medium() {
        return new ImageConfig()
                .size(200f, 150f)
                .scaleMode(ScaleMode.FIT_BOX);
    }

    public static ImageConfig large() {
        return new ImageConfig()
                .size(400f, 300f)
                .scaleMode(ScaleMode.FIT_BOX);
    }

    public static ImageConfig fullWidth(float pageWidth) {
        return new ImageConfig()
                .width(pageWidth)
                .scaleMode(ScaleMode.FIT_WIDTH);
    }

    public static ImageConfig centered() {
        return new ImageConfig()
                .center()
                .scaleMode(ScaleMode.FIT_BOX);
    }

    // ==================== GETTERS Y SETTERS ====================

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public ScaleMode getScaleMode() {
        return scaleMode;
    }

    public void setScaleMode(ScaleMode scaleMode) {
        this.scaleMode = scaleMode;
    }

    public HorizontalAlign getHorizontalAlign() {
        return horizontalAlign;
    }

    public void setHorizontalAlign(HorizontalAlign horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public boolean isDrawBorder() {
        return drawBorder;
    }

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public java.awt.Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(java.awt.Color borderColor) {
        this.borderColor = borderColor;
    }
}

