package saul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tipotoner", indexes = {
    @Index(name = "idx_tipotoner_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoToner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombretipotoner", nullable = false, length = 50)
    private String nombreTipoToner;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
