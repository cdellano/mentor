package saul.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartamentoResponse {
    private Integer idDepartamento;
    private String nombreDepartamento;
    private String codigo;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
}
