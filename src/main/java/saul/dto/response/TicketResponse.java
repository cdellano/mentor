package saul.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponse {
    private Long idTicket;
    private UsuarioResponse usuarioCreador;
    private UsuarioResponse usuarioAsignado;
    private DepartamentoResponse departamento;
    private String asunto;
    private String descripcion;
    private EstadoTicketResponse estadoTicket;
    private TipoPrioridadTicketResponse prioridad;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCierre;
}
