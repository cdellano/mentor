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
public class TipoDispositivoRequest {
    @NotBlank(message = "El nombre del tipo es obligatorio")
    @Size(max = 50, message = "El nombre del tipo no puede exceder 50 caracteres")
    private String nombreTipo;
}
