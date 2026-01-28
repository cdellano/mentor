package saul.dto.request;
import jakarta.validation.constraints.Email;
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
public class UsuarioRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede exceder 80 caracteres")
    private String nombre;
    @Size(max = 80, message = "El apellido no puede exceder 80 caracteres")
    private String apellido;
    @NotBlank(message = "El usuario de login es obligatorio")
    @Size(max = 50, message = "El usuario de login no puede exceder 50 caracteres")
    private String usuarioLogin;
    private String contrasena;
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 120, message = "El email no puede exceder 120 caracteres")
    private String email;
    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    private String estado;
    @NotBlank(message = "El rol es obligatorio")
    @Size(max = 50, message = "El rol no puede exceder 50 caracteres")
    private String rol;
}
