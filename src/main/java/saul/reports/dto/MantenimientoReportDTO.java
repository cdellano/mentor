package saul.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO especial para el reporte de mantenimientos.
 * Incluye información del dispositivo y su ubicación más reciente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MantenimientoReportDTO {

    // Información del mantenimiento
    private Long idMantenimiento;
    private String tipoMantenimiento;
    private String descripcion;
    private LocalDate fechaProgramada;
    private LocalDate fechaRealizado;
    private String estado;
    private BigDecimal costo;
    private String notas;

    // Indicadores de mantenimiento
    private Boolean pilaCmos;
    private Boolean pastaCpu;
    private Boolean limpieza;

    // Información del dispositivo
    private Long idDispositivo;
    private String tipoDispositivo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String inventario;
    private String estadoDispositivo;

    // Información de usuarios
    private String usuarioSolicita;
    private String usuarioAtiende;

    // Ubicación más reciente del dispositivo
    private String lugarActual;
    private String departamentoActual;
    private String pisoActual;
    private String edificioActual;
}

