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
public class SalidasTonerResponse {
    private Integer id;
    private UsuarioResponse usuarioInstala;
    private UsuarioResponse usuarioRecibe;
    private TipoTonerResponse tipoToner;
    private LocalDateTime fechaSalida;
    private Integer cantidad;
    private LugarResponse departamento;
    private String observaciones;
}
