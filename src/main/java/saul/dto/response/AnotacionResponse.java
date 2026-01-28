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
public class AnotacionResponse {
    private Long idAnotacion;
    private UsuarioResponse usuario;
    private String titulo;
    private String contenido;
    private Integer pagina;
    private LocalDateTime fechaAnotacion;
    private String etiquetas;
    private Boolean importante;
}
