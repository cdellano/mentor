package saul.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para representar los datos de un ticket en reportes PDF.
 * Contiene la información de los tickets con sus relaciones.
 */
@Data
@Builder
public class TicketReportDTO {
    private Long idTicket;

    // Usuario creador
    private String usuarioCreador;

    // Usuario asignado
    private String usuarioAsignado;

    // Departamento
    private String departamento;

    // Información del ticket
    private String asunto;
    private String descripcion;

    // Estado del ticket
    private String estadoTicket;
    private String colorEstado;

    // Prioridad
    private String prioridad;

    // Fechas
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCierre;
}
