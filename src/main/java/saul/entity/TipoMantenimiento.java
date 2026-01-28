package saul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tipos_mantenimiento", indexes = {
    @Index(name = "idx_tipomantenimiento_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_mantenimiento")
    private Integer id;

    @Column(name = "nombre_tipo_mantenimiento", nullable = false, length = 50, unique = true)
    private String nombreTipoMantenimiento;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
