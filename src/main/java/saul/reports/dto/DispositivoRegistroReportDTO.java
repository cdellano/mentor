package saul.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para representar los datos de un dispositivo en reportes PDF de registro.
 * Contiene la información de los dispositivos registrados en el sistema.
 */
@Data
@Builder
public class DispositivoRegistroReportDTO {
    private Long idDispositivo;

    // Tipo de dispositivo
    private String tipoDispositivo;

    // Información del dispositivo
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String inventario;

    // Estado del dispositivo
    private String estadoDispositivo;

    // Fechas
    private LocalDate fechaCompra;
    private LocalDateTime fechaRegistro;

    // Costo
    private BigDecimal costo;

    // Notas
    private String notas;
}
