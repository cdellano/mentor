package saul.pdf.renderer;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * Configuración de página para documentos PDF.
 * Define tamaño, márgenes y orientación de la página.
 */
public class PDFPageConfig {

    private PDRectangle pageSize;
    private float marginTop;
    private float marginBottom;
    private float marginLeft;
    private float marginRight;
    private boolean landscape;

    /**
     * Constructor con valores por defecto (LETTER, márgenes de 50pt).
     */
    public PDFPageConfig() {
        this.pageSize = PDRectangle.LETTER;
        this.marginTop = 50f;
        this.marginBottom = 50f;
        this.marginLeft = 50f;
        this.marginRight = 50f;
        this.landscape = false;
    }

    /**
     * Constructor con márgenes uniformes.
     */
    public PDFPageConfig(PDRectangle pageSize, float margin) {
        this.pageSize = pageSize;
        this.marginTop = margin;
        this.marginBottom = margin;
        this.marginLeft = margin;
        this.marginRight = margin;
        this.landscape = false;
    }

    /**
     * Constructor completo.
     */
    public PDFPageConfig(PDRectangle pageSize, float marginTop, float marginBottom,
                         float marginLeft, float marginRight, boolean landscape) {
        this.pageSize = pageSize;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.landscape = landscape;
    }

    // ==================== MÉTODOS FLUIDOS ====================

    public PDFPageConfig pageSize(PDRectangle size) {
        this.pageSize = size;
        return this;
    }

    public PDFPageConfig margins(float all) {
        this.marginTop = all;
        this.marginBottom = all;
        this.marginLeft = all;
        this.marginRight = all;
        return this;
    }

    public PDFPageConfig margins(float vertical, float horizontal) {
        this.marginTop = vertical;
        this.marginBottom = vertical;
        this.marginLeft = horizontal;
        this.marginRight = horizontal;
        return this;
    }

    public PDFPageConfig margins(float top, float bottom, float left, float right) {
        this.marginTop = top;
        this.marginBottom = bottom;
        this.marginLeft = left;
        this.marginRight = right;
        return this;
    }

    public PDFPageConfig landscape(boolean landscape) {
        this.landscape = landscape;
        return this;
    }

    // ==================== MÉTODOS DE CÁLCULO ====================

    /**
     * Obtiene el tamaño de página efectivo considerando orientación.
     */
    public PDRectangle getEffectivePageSize() {
        if (landscape) {
            return new PDRectangle(pageSize.getHeight(), pageSize.getWidth());
        }
        return pageSize;
    }

    /**
     * Obtiene el ancho útil (sin márgenes).
     */
    public float getUsableWidth() {
        return getEffectivePageSize().getWidth() - marginLeft - marginRight;
    }

    /**
     * Obtiene el alto útil (sin márgenes).
     */
    public float getUsableHeight() {
        return getEffectivePageSize().getHeight() - marginTop - marginBottom;
    }

    /**
     * Obtiene la posición X inicial (margen izquierdo).
     */
    public float getStartX() {
        return marginLeft;
    }

    /**
     * Obtiene la posición Y inicial (desde arriba).
     */
    public float getStartY() {
        return getEffectivePageSize().getHeight() - marginTop;
    }

    /**
     * Obtiene la posición Y mínima (margen inferior).
     */
    public float getMinY() {
        return marginBottom;
    }

    // ==================== GETTERS Y SETTERS ====================

    public PDRectangle getPageSize() {
        return pageSize;
    }

    public void setPageSize(PDRectangle pageSize) {
        this.pageSize = pageSize;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(float marginRight) {
        this.marginRight = marginRight;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    // ==================== CONFIGURACIONES PREDEFINIDAS ====================

    public static PDFPageConfig letter() {
        return new PDFPageConfig(PDRectangle.LETTER, 50f);
    }

    public static PDFPageConfig a4() {
        return new PDFPageConfig(PDRectangle.A4, 50f);
    }

    public static PDFPageConfig legal() {
        return new PDFPageConfig(PDRectangle.LEGAL, 50f);
    }

    public static PDFPageConfig letterLandscape() {
        return new PDFPageConfig(PDRectangle.LETTER, 50f, 50f, 50f, 50f, true);
    }

    public static PDFPageConfig a4Landscape() {
        return new PDFPageConfig(PDRectangle.A4, 50f, 50f, 50f, 50f, true);
    }
}

