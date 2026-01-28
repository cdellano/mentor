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
public class BitacoraServicioResponse {
    private Long id;
    private ServicioResponse servicio;
    private TipoIncidenteResponse tipoIncidente;
    private String estado;
    private String comentario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private UsuarioResponse reportadoPor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
