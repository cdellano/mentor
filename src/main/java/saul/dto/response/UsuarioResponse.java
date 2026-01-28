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
public class UsuarioResponse {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String usuarioLogin;
    private String email;
    private String estado;
    private LocalDateTime fechaRegistro;
    private String rol;
}
