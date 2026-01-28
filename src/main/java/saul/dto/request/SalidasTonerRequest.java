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
public class SalidasTonerRequest {
    @NotNull(message = "El usuario que instala es obligatorio")
    private Long idUsuarioInstala;
    @NotNull(message = "El usuario que recibe es obligatorio")
    private Long idUsuarioRecibe;
    @NotNull(message = "El tipo de toner es obligatorio")
    private Integer idTipoToner;
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
    @NotNull(message = "El departamento/lugar es obligatorio")
    private Integer idDepartamento;
    private String observaciones;
}
