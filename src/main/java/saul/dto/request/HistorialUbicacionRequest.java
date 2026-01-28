package saul.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialUbicacionRequest {
    @NotNull(message = "El dispositivo es obligatorio")
    private Long idDispositivo;
    @NotNull(message = "El lugar es obligatorio")
    private Integer idLugar;
    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;
    @NotNull(message = "La fecha de entrada es obligatoria")
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;
}
