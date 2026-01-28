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
public class TipoIncidenteRequest {
    @NotBlank(message = "El nombre del tipo de incidente es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;
    @Size(max = 150, message = "La descripción no puede exceder 150 caracteres")
    private String descripcion;
}
