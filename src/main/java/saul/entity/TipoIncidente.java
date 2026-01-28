package saul.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
@Entity
@Table(name = "tipo_incidente", indexes = {
    @Index(name = "idx_tipo_incidente_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoIncidente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    @Column(name = "descripcion", length = 150)
    private String descripcion;
    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
