package saul.pdf.renderer;

import java.awt.*;

/**
 * Configuración para marcas de agua en PDF.
 * Define texto, rotación, opacidad, color y posición.
 */
public class WatermarkConfig {

    /**
     * Tipo de marca de agua.
     */
    public enum WatermarkType {
        /** Marca de agua de texto */
        TEXT,
        /** Marca de agua con imagen */
        IMAGE
    }

    /**
     * Posición de la marca de agua.
     */
    public enum WatermarkPosition {
        /** Centrada en la página */
        CENTER,
        /** Diagonal desde esquina inferior izquierda a superior derecha */
        DIAGONAL,
        /** En la parte superior */
        TOP,
        /** En la parte inferior */
        BOTTOM,
        /** Mosaico (repetida por toda la página) */
        TILED
    }

    private WatermarkType type;
    private String text;
    private byte[] imageBytes;
    private WatermarkPosition position;
    private float rotation;
    private float opacity;
    private Color color;
    private float fontSize;
    private TextBoxConfig.FontStyle fontStyle;
    private float tileSpacingX;
    private float tileSpacingY;

    /**
     * Constructor con valores por defecto.
     */
    public WatermarkConfig() {
        this.type = WatermarkType.TEXT;
        this.text = "CONFIDENCIAL";
        this.position = WatermarkPosition.DIAGONAL;
        this.rotation = 45f;
        this.opacity = 0.3f;
        this.color = Color.LIGHT_GRAY;
        this.fontSize = 60f;
        this.fontStyle = TextBoxConfig.FontStyle.BOLD;
        this.tileSpacingX = 200f;
        this.tileSpacingY = 200f;
    }

    // ==================== MÉTODOS FLUIDOS ====================

    public WatermarkConfig text(String text) {
        this.type = WatermarkType.TEXT;
        this.text = text;
        return this;
    }

    public WatermarkConfig image(byte[] imageBytes) {
        this.type = WatermarkType.IMAGE;
        this.imageBytes = imageBytes;
        return this;
    }

    public WatermarkConfig position(WatermarkPosition position) {
        this.position = position;
        return this;
    }

    public WatermarkConfig diagonal() {
        this.position = WatermarkPosition.DIAGONAL;
        this.rotation = 45f;
        return this;
    }

    public WatermarkConfig centered() {
        this.position = WatermarkPosition.CENTER;
        this.rotation = 0f;
        return this;
    }

    public WatermarkConfig tiled() {
        this.position = WatermarkPosition.TILED;
        return this;
    }

    public WatermarkConfig tiled(float spacingX, float spacingY) {
        this.position = WatermarkPosition.TILED;
        this.tileSpacingX = spacingX;
        this.tileSpacingY = spacingY;
        return this;
    }

    public WatermarkConfig rotation(float degrees) {
        this.rotation = degrees;
        return this;
    }

    public WatermarkConfig opacity(float opacity) {
        this.opacity = Math.max(0f, Math.min(1f, opacity));
        return this;
    }

    public WatermarkConfig color(Color color) {
        this.color = color;
        return this;
    }

    public WatermarkConfig fontSize(float size) {
        this.fontSize = size;
        return this;
    }

    public WatermarkConfig fontStyle(TextBoxConfig.FontStyle style) {
        this.fontStyle = style;
        return this;
    }

    // ==================== CONFIGURACIONES PREDEFINIDAS ====================

    public static WatermarkConfig confidential() {
        return new WatermarkConfig()
                .text("CONFIDENCIAL")
                .diagonal()
                .opacity(0.2f)
                .color(Color.RED);
    }

    public static WatermarkConfig draft() {
        return new WatermarkConfig()
                .text("BORRADOR")
                .diagonal()
                .opacity(0.3f)
                .color(Color.GRAY);
    }

    public static WatermarkConfig copy() {
        return new WatermarkConfig()
                .text("COPIA")
                .diagonal()
                .opacity(0.25f)
                .color(Color.BLUE);
    }

    public static WatermarkConfig sample() {
        return new WatermarkConfig()
                .text("MUESTRA")
                .diagonal()
                .opacity(0.2f)
                .color(new Color(128, 0, 128)); // Purple
    }

    public static WatermarkConfig custom(String text) {
        return new WatermarkConfig().text(text);
    }

    // ==================== GETTERS Y SETTERS ====================

    public WatermarkType getType() {
        return type;
    }

    public void setType(WatermarkType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public WatermarkPosition getPosition() {
        return position;
    }

    public void setPosition(WatermarkPosition position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public TextBoxConfig.FontStyle getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(TextBoxConfig.FontStyle fontStyle) {
        this.fontStyle = fontStyle;
    }

    public float getTileSpacingX() {
        return tileSpacingX;
    }

    public void setTileSpacingX(float tileSpacingX) {
        this.tileSpacingX = tileSpacingX;
    }

    public float getTileSpacingY() {
        return tileSpacingY;
    }

    public void setTileSpacingY(float tileSpacingY) {
        this.tileSpacingY = tileSpacingY;
    }
}

