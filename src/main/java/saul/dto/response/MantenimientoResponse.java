package saul.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MantenimientoResponse {
    private Long idMantenimiento;
    private DispositivoResponse dispositivo;
    private UsuarioResponse usuarioSolicita;
    private UsuarioResponse usuarioAtiende;
    private TipoMantenimientoResponse tipoMantenimiento;
    private String descripcion;
    private LocalDate fechaProgramada;
    private LocalDate fechaRealizado;
    private String estado;
    private BigDecimal costo;
    private String notas;
    private LocalDateTime fechaRegistro;
    private Boolean pilaCmos;
    private Boolean pastaCpu;
    private Boolean limpieza;
}
