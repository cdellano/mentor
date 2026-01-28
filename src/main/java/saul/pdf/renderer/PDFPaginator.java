package saul.pdf.renderer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

/**
 * Gestiona la paginación automática de un documento PDF.
 * Crea nuevas páginas automáticamente cuando el contenido excede el espacio disponible.
 *
 * Implementa AutoCloseable para cerrar automáticamente el stream de contenido.
 */
public class PDFPaginator implements AutoCloseable {

    private final PDDocument document;
    private final PDFPageConfig pageConfig;

    private PDPage currentPage;
    private PDPageContentStream contentStream;
    private float currentY;
    private int pageCount;

    /**
     * Constructor con configuración de página.
     *
     * @param document documento PDF
     * @param pageConfig configuración de página
     * @throws IOException si ocurre un error al crear la primera página
     */
    public PDFPaginator(PDDocument document, PDFPageConfig pageConfig) throws IOException {
        if (document == null) {
            throw new IllegalArgumentException("El documento no puede ser null");
        }
        this.document = document;
        this.pageConfig = pageConfig != null ? pageConfig : new PDFPageConfig();
        this.pageCount = 0;
        newPage();
    }

    /**
     * Constructor con valores por defecto.
     *
     * @param document documento PDF
     * @throws IOException si ocurre un error al crear la primera página
     */
    public PDFPaginator(PDDocument document) throws IOException {
        this(document, new PDFPageConfig());
    }

    /**
     * Crea una nueva página y actualiza el stream de contenido.
     *
     * @throws IOException si ocurre un error al crear la página
     */
    public void newPage() throws IOException {
        closeCurrentStream();

        currentPage = new PDPage(pageConfig.getEffectivePageSize());
        document.addPage(currentPage);
        contentStream = new PDPageContentStream(document, currentPage);
        currentY = pageConfig.getStartY();
        pageCount++;
    }

    /**
     * Verifica si hay espacio suficiente para el contenido.
     * Si no hay espacio, crea automáticamente una nueva página.
     *
     * @param requiredHeight espacio vertical necesario en puntos
     * @return true si se creó una nueva página
     * @throws IOException si ocurre un error al crear la nueva página
     */
    public boolean checkSpace(float requiredHeight) throws IOException {
        if (currentY - requiredHeight < pageConfig.getMinY()) {
            newPage();
            return true;
        }
        return false;
    }

    /**
     * Verifica si hay espacio sin crear nueva página automáticamente.
     *
     * @param requiredHeight espacio vertical necesario
     * @return true si hay espacio suficiente
     */
    public boolean hasSpace(float requiredHeight) {
        return (currentY - requiredHeight) >= pageConfig.getMinY();
    }

    /**
     * Avanza la posición Y (hacia abajo).
     *
     * @param amount cantidad de puntos a avanzar
     */
    public void advanceY(float amount) {
        currentY -= amount;
    }

    /**
     * Agrega un espacio vertical.
     *
     * @param space espacio en puntos
     * @throws IOException si se necesita crear nueva página
     */
    public void addSpace(float space) throws IOException {
        checkSpace(space);
        advanceY(space);
    }

    /**
     * Agrega un espacio vertical estándar de 20 puntos.
     *
     * @throws IOException si se necesita crear nueva página
     */
    public void addSpace() throws IOException {
        addSpace(20f);
    }

    // ==================== GETTERS ====================

    public PDDocument getDocument() {
        return document;
    }

    public PDFPageConfig getPageConfig() {
        return pageConfig;
    }

    public PDPage getCurrentPage() {
        return currentPage;
    }

    public PDPageContentStream getContentStream() {
        return contentStream;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void setCurrentY(float y) {
        this.currentY = y;
    }

    public int getPageCount() {
        return pageCount;
    }

    /**
     * Obtiene la posición X inicial (margen izquierdo).
     */
    public float getStartX() {
        return pageConfig.getStartX();
    }

    /**
     * Obtiene el ancho útil disponible.
     */
    public float getUsableWidth() {
        return pageConfig.getUsableWidth();
    }

    /**
     * Obtiene el espacio vertical disponible desde la posición actual.
     */
    public float getAvailableHeight() {
        return currentY - pageConfig.getMinY();
    }

    /**
     * Cierra el stream de contenido actual si existe.
     */
    private void closeCurrentStream() throws IOException {
        if (contentStream != null) {
            contentStream.close();
            contentStream = null;
        }
    }

    /**
     * Cierra el paginador y libera recursos.
     */
    @Override
    public void close() throws IOException {
        closeCurrentStream();
    }
}

