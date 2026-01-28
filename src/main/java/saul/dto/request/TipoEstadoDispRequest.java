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
public class TipoEstadoDispRequest {
    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(max = 30, message = "El nombre del estado no puede exceder 30 caracteres")
    private String nombreEstado;
    @Size(max = 100, message = "La descripción no puede exceder 100 caracteres")
    private String descripcion;
    private Boolean activo;
}
