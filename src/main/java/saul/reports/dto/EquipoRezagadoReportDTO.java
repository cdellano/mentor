package saul.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO especial para el reporte de equipos rezagados en mantenimiento.
 * Incluye información del dispositivo, último mantenimiento y días transcurridos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipoRezagadoReportDTO {

    // Información del dispositivo
    private Long idDispositivo;
    private String tipoDispositivo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String inventario;

    // Información del último mantenimiento
    private LocalDate fechaUltimoMantenimiento;
    private Long diasTranscurridos;

    // Ubicación actual del dispositivo
    private String lugarActual;
    private String departamentoActual;
}
