package saul.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoPrioridadTicketRequest {
    @NotBlank(message = "El nombre de la prioridad es obligatorio")
    @Size(max = 30, message = "El nombre de la prioridad no puede exceder 30 caracteres")
    private String nombrePrioridad;
    @Size(max = 100, message = "La descripción no puede exceder 100 caracteres")
    private String descripcion;
}
