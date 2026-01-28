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
public class DispositivoRequest {
    @NotNull(message = "El tipo de dispositivo es obligatorio")
    private Integer idTipoDispositivo;
    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no puede exceder 50 caracteres")
    private String marca;
    @Size(max = 70, message = "El modelo no puede exceder 70 caracteres")
    private String modelo;
    @Size(max = 100, message = "El número de serie no puede exceder 100 caracteres")
    private String numeroSerie;
    @Size(max = 30, message = "El inventario no puede exceder 30 caracteres")
    private String inventario;
    @NotNull(message = "El estado del dispositivo es obligatorio")
    private Integer idTipoEstado;
    private LocalDate fechaCompra;
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo no puede ser negativo")
    private BigDecimal costo;
    private String notas;
    private LocalDate fechaBaja;
}
