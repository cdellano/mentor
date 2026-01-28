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
public class LugarResponse {
    private Integer idLugar;
    private String nombreLugar;
    private DepartamentoResponse departamento;
    private String piso;
    private String edificio;
    private String descripcion;
    private LocalDateTime fechaRegistro;
}
