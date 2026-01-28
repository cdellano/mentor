package saul.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketRequest {
    @NotNull(message = "El usuario creador es obligatorio")
    private Long idUsuarioCreador;
    private Long idUsuarioAsignado;
    @NotNull(message = "El departamento es obligatorio")
    private Integer idDepartamento;
    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 120, message = "El asunto no puede exceder 120 caracteres")
    private String asunto;
    private String descripcion;
    @NotNull(message = "El estado del ticket es obligatorio")
    private Integer idEstado;
    @NotNull(message = "La prioridad es obligatoria")
    private Integer idPrioridad;
}
