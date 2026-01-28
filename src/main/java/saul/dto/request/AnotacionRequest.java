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
public class AnotacionRequest {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 120, message = "El título no puede exceder 120 caracteres")
    private String titulo;
    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;
    @NotNull(message = "La página es obligatoria")
    private Integer pagina;
    @Size(max = 200, message = "Las etiquetas no pueden exceder 200 caracteres")
    private String etiquetas;
    private Boolean importante;
}
