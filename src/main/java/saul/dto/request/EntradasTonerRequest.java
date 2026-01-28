package saul.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntradasTonerRequest {
    @NotNull(message = "El usuario de entrada es obligatorio")
    private Long idUsuarioEntrada;
    @NotNull(message = "El tipo de toner es obligatorio")
    private Integer idTipoToner;
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
    private String observaciones;
}
