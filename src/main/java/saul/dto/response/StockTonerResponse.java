package saul.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTonerResponse {
    private Integer idTipoToner;
    private String nombreTipoToner;
    private Integer totalEntradas;
    private Integer totalSalidas;
    private Integer stockActual;
}
