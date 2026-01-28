package saul.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para representar los datos de un historial de ubicación en reportes PDF.
 * Contiene la información de los movimientos de dispositivos entre ubicaciones.
 */
@Data
@Builder
public class HistorialUbicacionReportDTO {
    private Long idHistorial;

    // Información del dispositivo
    private Long idDispositivo;
    private String inventario;
    private String numeroSerie;
    private String tipoDispositivo;
    private String marcaModelo;

    // Información del lugar
    private String nombreLugar;
    private String departamento;
    private String piso;
    private String edificio;

    // Información de fechas
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;
    private Long diasEnLugar;

    // Usuario que asignó
    private String usuarioAsigno;
}
