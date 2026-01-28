package saul.pdf.renderer;
import java.awt.Color;
/**
 * Configuración para códigos QR en PDF.
 * Soporta diferentes niveles de corrección de errores y personalización visual.
 */
public class QRCodeConfig {
    /**
     * Nivel de corrección de errores del QR.
     * A mayor nivel, más redundancia y mayor resistencia a daños.
     */
    public enum ErrorCorrectionLevel {
        /** ~7% de corrección - Menor tamaño */
        LOW,
        /** ~15% de corrección - Nivel medio */
        MEDIUM,
        /** ~25% de corrección - Nivel alto */
        QUARTILE,
        /** ~30% de corrección - Máxima corrección */
        HIGH
    }
    private int size;
    private Color foregroundColor;
    private Color backgroundColor;
    private ErrorCorrectionLevel errorCorrectionLevel;
    private int margin;
    private float opacity;
    private String charset;
    /**
     * Constructor con valores por defecto.
     */
    public QRCodeConfig() {
        this.size = 150;
        this.foregroundColor = Color.BLACK;
        this.backgroundColor = Color.WHITE;
        this.errorCorrectionLevel = ErrorCorrectionLevel.MEDIUM;
        this.margin = 1;
        this.opacity = 1.0f;
        this.charset = "UTF-8";
    }
    // ==================== MÉTODOS FLUIDOS ====================
    public QRCodeConfig size(int size) {
        this.size = size;
        return this;
    }
    public QRCodeConfig asSmall() {
        this.size = 80;
        return this;
    }
    public QRCodeConfig asMedium() {
        this.size = 150;
        return this;
    }
    public QRCodeConfig asLarge() {
        this.size = 250;
        return this;
    }
    public QRCodeConfig extraLarge() {
        this.size = 400;
        return this;
    }
    public QRCodeConfig foregroundColor(Color color) {
        this.foregroundColor = color;
        return this;
    }
    public QRCodeConfig backgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }
    public QRCodeConfig colors(Color foreground, Color background) {
        this.foregroundColor = foreground;
        this.backgroundColor = background;
        return this;
    }
    public QRCodeConfig errorCorrection(ErrorCorrectionLevel level) {
        this.errorCorrectionLevel = level;
        return this;
    }
    public QRCodeConfig lowErrorCorrection() {
        this.errorCorrectionLevel = ErrorCorrectionLevel.LOW;
        return this;
    }
    public QRCodeConfig mediumErrorCorrection() {
        this.errorCorrectionLevel = ErrorCorrectionLevel.MEDIUM;
        return this;
    }
    public QRCodeConfig quartileErrorCorrection() {
        this.errorCorrectionLevel = ErrorCorrectionLevel.QUARTILE;
        return this;
    }
    public QRCodeConfig highErrorCorrection() {
        this.errorCorrectionLevel = ErrorCorrectionLevel.HIGH;
        return this;
    }
    public QRCodeConfig margin(int margin) {
        this.margin = margin;
        return this;
    }
    public QRCodeConfig noMargin() {
        this.margin = 0;
        return this;
    }
    public QRCodeConfig opacity(float opacity) {
        this.opacity = Math.max(0f, Math.min(1f, opacity));
        return this;
    }
    public QRCodeConfig charset(String charset) {
        this.charset = charset;
        return this;
    }
    public QRCodeConfig utf8() {
        this.charset = "UTF-8";
        return this;
    }
    public QRCodeConfig iso88591() {
        this.charset = "ISO-8859-1";
        return this;
    }
    // ==================== GETTERS ====================
    public int getSize() {
        return size;
    }
    public Color getForegroundColor() {
        return foregroundColor;
    }
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    public ErrorCorrectionLevel getErrorCorrectionLevel() {
        return errorCorrectionLevel;
    }
    public int getMargin() {
        return margin;
    }
    public float getOpacity() {
        return opacity;
    }
    public String getCharset() {
        return charset;
    }
    // ==================== FACTORY METHODS ====================
    public static QRCodeConfig small() {
        return new QRCodeConfig().size(80);
    }
    public static QRCodeConfig medium() {
        return new QRCodeConfig().size(150);
    }
    public static QRCodeConfig large() {
        return new QRCodeConfig().size(250);
    }
    public static QRCodeConfig highQuality() {
        return new QRCodeConfig().size(300).highErrorCorrection();
    }
    public static QRCodeConfig compact() {
        return new QRCodeConfig().size(100).lowErrorCorrection().noMargin();
    }
    public static QRCodeConfig defaults() {
        return new QRCodeConfig();
    }
}
