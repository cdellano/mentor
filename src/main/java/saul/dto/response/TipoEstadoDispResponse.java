package saul.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoEstadoDispResponse {
    private Integer idTipoEstado;
    private String nombreEstado;
    private String descripcion;
    private Boolean activo;
}
