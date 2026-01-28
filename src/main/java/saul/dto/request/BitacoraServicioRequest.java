package saul.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BitacoraServicioRequest {
    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;
    @NotNull(message = "El tipo de incidente es obligatorio")
    private Long tipoIncidenteId;
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
    private String comentario;
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    @NotNull(message = "El usuario que reporta es obligatorio")
    private Long reportadoPorId;
}
