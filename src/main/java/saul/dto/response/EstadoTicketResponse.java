package saul.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoTicketResponse {
    private Integer idEstado;
    private String nombre;
    private String descripcion;
    private String color;
    private Boolean activo;
    private Integer orden;
}
