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
public class DepartamentoRequest {
    @NotBlank(message = "El nombre del departamento es obligatorio")
    @Size(max = 100, message = "El nombre del departamento no puede exceder 100 caracteres")
    private String nombreDepartamento;
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    private String codigo;
    private String descripcion;
    private Boolean activo;
}
