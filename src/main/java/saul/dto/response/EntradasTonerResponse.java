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
public class EntradasTonerResponse {
    private Integer id;
    private UsuarioResponse usuarioEntrada;
    private LocalDateTime fechaEntrada;
    private TipoTonerResponse tipoToner;
    private Integer cantidad;
    private String observaciones;
}
