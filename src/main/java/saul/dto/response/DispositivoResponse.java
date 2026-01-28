package saul.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispositivoResponse {
    private Long idDispositivo;
    private TipoDispositivoResponse tipoDispositivo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String inventario;
    private TipoEstadoDispResponse tipoEstado;
    private LocalDate fechaCompra;
    private BigDecimal costo;
    private String notas;
    private LocalDateTime fechaRegistro;
    private LocalDate fechaBaja;
    private List<HistorialUbicacionResponse> historialUbicaciones;
}
