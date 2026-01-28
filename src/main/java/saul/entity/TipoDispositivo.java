package saul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Table(name = "tipodispositivo", indexes = {
    @Index(name = "idx_tipodispositivo_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipodispositivo")
    private Integer idTipoDispositivo;

    @Column(name = "nombretipo", length = 50)
    private String nombreTipo;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
