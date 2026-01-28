package saul.pdf.renderer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
/**
 * Generador de códigos de barras y códigos QR usando ZXing.
 * Proporciona métodos para generar imágenes BufferedImage que pueden
 * ser insertadas en documentos PDF.
 */
public class BarcodeGenerator {
    /**
     * Genera un código de barras como BufferedImage.
     *
     * @param content Contenido a codificar
     * @param config  Configuración del código de barras
     * @return BufferedImage con el código de barras generado
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateBarcode(String content, BarcodeConfig config) throws WriterException {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("El contenido del código de barras no puede estar vacío");
        }
        BarcodeFormat format = mapBarcodeType(config.getType());
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, config.getMargin());
        // Configuración específica según el tipo
        if (config.getType() == BarcodeConfig.BarcodeType.CODE_128 ||
            config.getType() == BarcodeConfig.BarcodeType.CODE_39) {
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(content, format, config.getWidth(), config.getHeight(), hints);
        // Configurar colores
        int foregroundRGB = config.getForegroundColor().getRGB();
        int backgroundRGB = config.getBackgroundColor().getRGB();
        MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundRGB, backgroundRGB);
        return MatrixToImageWriter.toBufferedImage(bitMatrix, imageConfig);
    }
    /**
     * Genera un código de barras con configuración por defecto.
     *
     * @param content Contenido a codificar
     * @return BufferedImage con el código de barras
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateBarcode(String content) throws WriterException {
        return generateBarcode(content, new BarcodeConfig());
    }
    /**
     * Genera un código de barras CODE_128.
     *
     * @param content Contenido a codificar
     * @param width   Ancho en píxeles
     * @param height  Alto en píxeles
     * @return BufferedImage con el código de barras
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateCode128(String content, int width, int height) throws WriterException {
        return generateBarcode(content, BarcodeConfig.code128().size(width, height));
    }
    /**
     * Genera un código de barras CODE_39.
     *
     * @param content Contenido a codificar
     * @param width   Ancho en píxeles
     * @param height  Alto en píxeles
     * @return BufferedImage con el código de barras
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateCode39(String content, int width, int height) throws WriterException {
        return generateBarcode(content, BarcodeConfig.code39().size(width, height));
    }
    /**
     * Genera un código de barras EAN-13.
     *
     * @param content Contenido a codificar (13 dígitos)
     * @param width   Ancho en píxeles
     * @param height  Alto en píxeles
     * @return BufferedImage con el código de barras
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateEAN13(String content, int width, int height) throws WriterException {
        return generateBarcode(content, BarcodeConfig.ean13().size(width, height));
    }
    /**
     * Genera un código QR como BufferedImage.
     *
     * @param content Contenido a codificar (URL, texto, etc.)
     * @param config  Configuración del código QR
     * @return BufferedImage con el código QR generado
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateQRCode(String content, QRCodeConfig config) throws WriterException {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("El contenido del código QR no puede estar vacío");
        }
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, config.getCharset());
        hints.put(EncodeHintType.MARGIN, config.getMargin());
        hints.put(EncodeHintType.ERROR_CORRECTION, mapErrorCorrectionLevel(config.getErrorCorrectionLevel()));
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, config.getSize(), config.getSize(), hints);
        // Configurar colores
        int foregroundRGB = config.getForegroundColor().getRGB();
        int backgroundRGB = config.getBackgroundColor().getRGB();
        MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundRGB, backgroundRGB);
        return MatrixToImageWriter.toBufferedImage(bitMatrix, imageConfig);
    }
    /**
     * Genera un código QR con configuración por defecto.
     *
     * @param content Contenido a codificar
     * @return BufferedImage con el código QR
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateQRCode(String content) throws WriterException {
        return generateQRCode(content, new QRCodeConfig());
    }
    /**
     * Genera un código QR con tamaño específico.
     *
     * @param content Contenido a codificar
     * @param size    Tamaño en píxeles (ancho y alto)
     * @return BufferedImage con el código QR
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateQRCode(String content, int size) throws WriterException {
        return generateQRCode(content, QRCodeConfig.defaults().size(size));
    }
    /**
     * Genera un código QR para una URL.
     *
     * @param url  URL a codificar
     * @param size Tamaño en píxeles
     * @return BufferedImage con el código QR
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateURLQRCode(String url, int size) throws WriterException {
        return generateQRCode(url, QRCodeConfig.defaults().size(size).highErrorCorrection());
    }
    /**
     * Genera un código QR para información de contacto vCard.
     *
     * @param name    Nombre completo
     * @param phone   Teléfono
     * @param email   Email
     * @param company Empresa (opcional)
     * @param size    Tamaño en píxeles
     * @return BufferedImage con el código QR
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateVCardQRCode(String name, String phone, String email, 
                                                     String company, int size) throws WriterException {
        StringBuilder vcard = new StringBuilder();
        vcard.append("BEGIN:VCARD\n");
        vcard.append("VERSION:3.0\n");
        vcard.append("FN:").append(name).append("\n");
        if (phone != null && !phone.isEmpty()) {
            vcard.append("TEL:").append(phone).append("\n");
        }
        if (email != null && !email.isEmpty()) {
            vcard.append("EMAIL:").append(email).append("\n");
        }
        if (company != null && !company.isEmpty()) {
            vcard.append("ORG:").append(company).append("\n");
        }
        vcard.append("END:VCARD");
        return generateQRCode(vcard.toString(), QRCodeConfig.defaults().size(size).highErrorCorrection());
    }
    /**
     * Genera un código QR para conexión WiFi.
     *
     * @param ssid       Nombre de la red
     * @param password   Contraseña
     * @param encryption Tipo de encriptación (WPA, WEP, nopass)
     * @param hidden     Si la red está oculta
     * @param size       Tamaño en píxeles
     * @return BufferedImage con el código QR
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateWiFiQRCode(String ssid, String password, 
                                                    String encryption, boolean hidden, int size) throws WriterException {
        StringBuilder wifi = new StringBuilder();
        wifi.append("WIFI:");
        wifi.append("T:").append(encryption != null ? encryption : "WPA").append(";");
        wifi.append("S:").append(ssid).append(";");
        if (password != null && !password.isEmpty()) {
            wifi.append("P:").append(password).append(";");
        }
        if (hidden) {
            wifi.append("H:true;");
        }
        wifi.append(";");
        return generateQRCode(wifi.toString(), QRCodeConfig.defaults().size(size).highErrorCorrection());
    }
    /**
     * Genera un código QR para email.
     *
     * @param email   Dirección de email
     * @param subject Asunto (opcional)
     * @param body    Cuerpo del mensaje (opcional)
     * @param size    Tamaño en píxeles
     * @return BufferedImage con el código QR
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateEmailQRCode(String email, String subject, 
                                                     String body, int size) throws WriterException {
        StringBuilder mailto = new StringBuilder();
        mailto.append("mailto:").append(email);
        boolean hasParams = false;
        if (subject != null && !subject.isEmpty()) {
            mailto.append("?subject=").append(subject);
            hasParams = true;
        }
        if (body != null && !body.isEmpty()) {
            mailto.append(hasParams ? "&" : "?").append("body=").append(body);
        }
        return generateQRCode(mailto.toString(), QRCodeConfig.defaults().size(size));
    }
    /**
     * Genera un código QR para número de teléfono.
     *
     * @param phoneNumber Número de teléfono
     * @param size        Tamaño en píxeles
     * @return BufferedImage con el código QR
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generatePhoneQRCode(String phoneNumber, int size) throws WriterException {
        return generateQRCode("tel:" + phoneNumber, QRCodeConfig.defaults().size(size));
    }
    /**
     * Genera un código QR para SMS.
     *
     * @param phoneNumber Número de teléfono
     * @param message     Mensaje (opcional)
     * @param size        Tamaño en píxeles
     * @return BufferedImage con el código QR
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateSMSQRCode(String phoneNumber, String message, int size) throws WriterException {
        StringBuilder sms = new StringBuilder();
        sms.append("sms:").append(phoneNumber);
        if (message != null && !message.isEmpty()) {
            sms.append("?body=").append(message);
        }
        return generateQRCode(sms.toString(), QRCodeConfig.defaults().size(size));
    }
    /**
     * Genera un código QR para geolocalización.
     *
     * @param latitude  Latitud
     * @param longitude Longitud
     * @param size      Tamaño en píxeles
     * @return BufferedImage con el código QR
     * @throws WriterException Si hay error al generar el código
     */
    public static BufferedImage generateGeoQRCode(double latitude, double longitude, int size) throws WriterException {
        String geo = String.format("geo:%f,%f", latitude, longitude);
        return generateQRCode(geo, QRCodeConfig.defaults().size(size));
    }
    // ==================== MÉTODOS AUXILIARES ====================
    /**
     * Mapea el tipo de código de barras interno al formato de ZXing.
     */
    private static BarcodeFormat mapBarcodeType(BarcodeConfig.BarcodeType type) {
        return switch (type) {
            case CODE_128 -> BarcodeFormat.CODE_128;
            case CODE_39 -> BarcodeFormat.CODE_39;
            case EAN_13 -> BarcodeFormat.EAN_13;
            case EAN_8 -> BarcodeFormat.EAN_8;
            case UPC_A -> BarcodeFormat.UPC_A;
            case ITF -> BarcodeFormat.ITF;
            case CODABAR -> BarcodeFormat.CODABAR;
        };
    }
    /**
     * Mapea el nivel de corrección de errores interno al de ZXing.
     */
    private static ErrorCorrectionLevel mapErrorCorrectionLevel(QRCodeConfig.ErrorCorrectionLevel level) {
        return switch (level) {
            case LOW -> ErrorCorrectionLevel.L;
            case MEDIUM -> ErrorCorrectionLevel.M;
            case QUARTILE -> ErrorCorrectionLevel.Q;
            case HIGH -> ErrorCorrectionLevel.H;
        };
    }
}
