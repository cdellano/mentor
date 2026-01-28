package saul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tipos_estado_dispositivo", indexes = {
    @Index(name = "idx_tipoestadodisp_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoEstadoDisp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipoestado")
    private Integer idTipoEstado;

    @Column(name = "nombreestado", nullable = false, unique = true, length = 30)
    private String nombreEstado;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
