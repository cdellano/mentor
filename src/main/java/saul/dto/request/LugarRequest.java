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
public class LugarRequest {
    @NotBlank(message = "El nombre del lugar es obligatorio")
    @Size(max = 80, message = "El nombre del lugar no puede exceder 80 caracteres")
    private String nombreLugar;
    private Integer idDepartamento;
    @Size(max = 20, message = "El piso no puede exceder 20 caracteres")
    private String piso;
    @Size(max = 80, message = "El edificio no puede exceder 80 caracteres")
    private String edificio;
    private String descripcion;
}
