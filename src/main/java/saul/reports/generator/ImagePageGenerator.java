package saul.reports.generator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import saul.pdf.renderer.ImageConfig;
import saul.pdf.renderer.PDFPageConfig;
import saul.pdf.renderer.PDFPaginator;
import saul.pdf.service.PDFBoxService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Generador de reportes que crea una página tamaño carta con una imagen.
 * Utiliza PDFBoxService para la generación del PDF.
 */
@Component
public class ImagePageGenerator {

    private final PDFBoxService pdfBoxService;

    public ImagePageGenerator(PDFBoxService pdfBoxService) {
        this.pdfBoxService = pdfBoxService;
    }

    /**
     * Genera un PDF tamaño carta con la imagen "i2.png" centrada.
     *
     * @return byte[] del PDF generado
     * @throws IOException si ocurre un error al leer la imagen o generar el PDF
     */
    public byte[] generateImagePage() throws IOException {
        return generateImagePage("img/i2.png");
    }

    /**
     * Genera un PDF tamaño carta con una imagen específica centrada.
     *
     * @param imagePath ruta de la imagen en resources (ej: "img/i2.png")
     * @return byte[] del PDF generado
     * @throws IOException si ocurre un error al leer la imagen o generar el PDF
     */
    public byte[] generateImagePage(String imagePath) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Configuración de página tamaño carta con márgenes
            PDFPageConfig pageConfig = new PDFPageConfig()
                    .pageSize(PDRectangle.LETTER)
                    .margins(50f);

            // Cargar imagen desde resources
            byte[] imageBytes = loadImageFromResources(imagePath);

            try (PDFPaginator paginator = new PDFPaginator(document, pageConfig)) {
                // Configurar imagen para que se ajuste al ancho disponible
                // PDRectangle.LETTER = 612 x 792 puntos
                // Con márgenes de 50, ancho usable = 512
                ImageConfig imageConfig = new ImageConfig()
                        .size(paginator.getUsableWidth(), 600f)
                        .scaleMode(ImageConfig.ScaleMode.FIT_BOX)
                        .center();

                // Insertar imagen usando el paginador
                pdfBoxService.insertImage(paginator, imageBytes, imageConfig);
            }

            // Guardar documento
            document.save(baos);
            return baos.toByteArray();
        }
    }

    /**
     * Carga una imagen desde el directorio resources.
     *
     * @param resourcePath ruta relativa dentro de resources
     * @return byte[] de la imagen
     * @throws IOException si no se puede leer la imagen
     */
    private byte[] loadImageFromResources(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (InputStream is = resource.getInputStream()) {
            return is.readAllBytes();
        }
    }
}

