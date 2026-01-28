package saul.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para representar los datos de una bitácora de servicio en reportes PDF.
 * Contiene la información específica requerida para los reportes de bitácora de servicios informáticos.
 */
@Data
@Builder
public class BitacoraServicioReportDTO {
    private Long id;
    private String servicio;
    private String tipoIncidente;
    private String estado;
    private String comentario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String reportadoPor;
    private LocalDateTime createdAt;
}
