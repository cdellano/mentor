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
public class HistorialUbicacionResponse {
    private Long idHistorial;
    private Long idDispositivo;
    private LugarResponse lugar;
    private UsuarioResponse usuario;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;
}
