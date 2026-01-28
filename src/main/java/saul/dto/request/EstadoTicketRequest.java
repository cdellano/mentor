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
public class EstadoTicketRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 30, message = "El nombre no puede exceder 30 caracteres")
    private String nombre;
    @Size(max = 100, message = "La descripcion no puede exceder 100 caracteres")
    private String descripcion;
    @Size(max = 7, message = "El color no puede exceder 7 caracteres")
    private String color;
    private Boolean activo;
    private Integer orden;
}
