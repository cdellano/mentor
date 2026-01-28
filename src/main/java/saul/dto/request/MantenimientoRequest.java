package saul.dto.request;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MantenimientoRequest {
    @NotNull(message = "El dispositivo es obligatorio")
    private Long idDispositivo;
    private Long idUsuarioSolicita;
    private Long idUsuarioAtiende;
    @NotNull(message = "El tipo de mantenimiento es obligatorio")
    private Integer idTipoMantenimiento;
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    @NotNull(message = "La fecha programada es obligatoria")
    private LocalDate fechaProgramada;
    private LocalDate fechaRealizado;
    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    private String estado;
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo no puede ser negativo")
    private BigDecimal costo;
    private String notas;
    private Boolean pilaCmos;
    private Boolean pastaCpu;
    private Boolean limpieza;
}
