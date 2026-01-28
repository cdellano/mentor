package saul.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO especial para el reporte de información de un dispositivo individual.
 * Incluye información detallada del dispositivo y su historial de ubicaciones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispositivoReportDTO {

    // Información del dispositivo
    private Long idDispositivo;
    private String tipoDispositivo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String inventario;
    private String estadoActual;
    private LocalDate fechaCompra;
    private BigDecimal costo;
    private String notas;
    private LocalDateTime fechaRegistro;
    private LocalDate fechaBaja;

    // Historial de ubicaciones
    private List<UbicacionHistorialDTO> historialUbicaciones;

    /**
     * DTO interno para representar cada ubicación en el historial.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UbicacionHistorialDTO {
        private Long idHistorial;
        private String nombreLugar;
        private String departamento;
        private String piso;
        private String edificio;
        private LocalDateTime fechaEntrada;
        private LocalDateTime fechaSalida;
        private Long diasEnLugar;
        private String usuarioAsigno;
    }
}
