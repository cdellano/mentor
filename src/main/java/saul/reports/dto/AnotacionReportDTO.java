package saul.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para representar los datos de una anotación en reportes PDF.
 * Contiene la información específica requerida para los reportes de anotaciones.
 */
@Data
@Builder
public class AnotacionReportDTO {
    private Long idAnotacion;
    private LocalDateTime fechaAnotacion;
    private String usuario;
    private Integer pagina;
    private String etiquetas;
    private Boolean importante;
    private String contenido;
}
