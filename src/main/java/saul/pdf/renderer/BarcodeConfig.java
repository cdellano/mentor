package saul.pdf.renderer;
import java.awt.Color;
/**
 * Configuración para códigos de barras en PDF.
 * Soporta múltiples formatos: CODE_128, CODE_39, EAN_13, EAN_8, UPC_A, ITF, CODABAR.
 */
public class BarcodeConfig {
    /**
     * Tipos de códigos de barras soportados.
     */
    public enum BarcodeType {
        /** Código 128 - Alta densidad, alfanumérico */
        CODE_128,
        /** Código 39 - Alfanumérico, ampliamente usado */
        CODE_39,
        /** EAN-13 - Productos europeos (13 dígitos) */
        EAN_13,
        /** EAN-8 - Versión corta de EAN (8 dígitos) */
        EAN_8,
        /** UPC-A - Productos norteamericanos (12 dígitos) */
        UPC_A,
        /** ITF (Interleaved 2 of 5) - Solo dígitos */
        ITF,
        /** Codabar - Usado en bibliotecas y bancos de sangre */
        CODABAR
    }
    private BarcodeType type;
    private int width;
    private int height;
    private Color foregroundColor;
    private Color backgroundColor;
    private boolean showText;
    private int margin;
    private float opacity;
    /**
     * Constructor con valores por defecto.
     */
    public BarcodeConfig() {
        this.type = BarcodeType.CODE_128;
        this.width = 200;
        this.height = 60;
        this.foregroundColor = Color.BLACK;
        this.backgroundColor = Color.WHITE;
        this.showText = true;
        this.margin = 0;
        this.opacity = 1.0f;
    }
    // ==================== MÉTODOS FLUIDOS ====================
    public BarcodeConfig type(BarcodeType type) {
        this.type = type;
        return this;
    }
    public BarcodeConfig asCode128() {
        this.type = BarcodeType.CODE_128;
        return this;
    }
    public BarcodeConfig asCode39() {
        this.type = BarcodeType.CODE_39;
        return this;
    }
    public BarcodeConfig asEan13() {
        this.type = BarcodeType.EAN_13;
        return this;
    }
    public BarcodeConfig asEan8() {
        this.type = BarcodeType.EAN_8;
        return this;
    }
    public BarcodeConfig asUpcA() {
        this.type = BarcodeType.UPC_A;
        return this;
    }
    public BarcodeConfig asItf() {
        this.type = BarcodeType.ITF;
        return this;
    }
    public BarcodeConfig asCodabar() {
        this.type = BarcodeType.CODABAR;
        return this;
    }
    public BarcodeConfig width(int width) {
        this.width = width;
        return this;
    }
    public BarcodeConfig height(int height) {
        this.height = height;
        return this;
    }
    public BarcodeConfig size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }
    public BarcodeConfig foregroundColor(Color color) {
        this.foregroundColor = color;
        return this;
    }
    public BarcodeConfig backgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }
    public BarcodeConfig colors(Color foreground, Color background) {
        this.foregroundColor = foreground;
        this.backgroundColor = background;
        return this;
    }
    public BarcodeConfig showText(boolean show) {
        this.showText = show;
        return this;
    }
    public BarcodeConfig hideText() {
        this.showText = false;
        return this;
    }
    public BarcodeConfig margin(int margin) {
        this.margin = margin;
        return this;
    }
    public BarcodeConfig opacity(float opacity) {
        this.opacity = Math.max(0f, Math.min(1f, opacity));
        return this;
    }
    // ==================== GETTERS ====================
    public BarcodeType getType() {
        return type;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Color getForegroundColor() {
        return foregroundColor;
    }
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    public boolean isShowText() {
        return showText;
    }
    public int getMargin() {
        return margin;
    }
    public float getOpacity() {
        return opacity;
    }
    // ==================== FACTORY METHODS ====================
    public static BarcodeConfig code128() {
        return new BarcodeConfig().type(BarcodeType.CODE_128);
    }
    public static BarcodeConfig code39() {
        return new BarcodeConfig().type(BarcodeType.CODE_39);
    }
    public static BarcodeConfig ean13() {
        return new BarcodeConfig().type(BarcodeType.EAN_13);
    }
    public static BarcodeConfig ean8() {
        return new BarcodeConfig().type(BarcodeType.EAN_8);
    }
    public static BarcodeConfig upcA() {
        return new BarcodeConfig().type(BarcodeType.UPC_A);
    }
    public static BarcodeConfig itf() {
        return new BarcodeConfig().type(BarcodeType.ITF);
    }
    public static BarcodeConfig codabar() {
        return new BarcodeConfig().type(BarcodeType.CODABAR);
    }
    public static BarcodeConfig defaults() {
        return new BarcodeConfig();
    }
}
