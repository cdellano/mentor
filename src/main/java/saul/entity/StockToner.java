package saul.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockToner {
    private Integer idTipoToner;
    private String nombreTipoToner;
    private Integer totalEntradas;
    private Integer totalSalidas;
    private Integer stockActual;
}
