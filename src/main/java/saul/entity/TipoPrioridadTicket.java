package saul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tipos_prioridad_ticket", indexes = {
    @Index(name = "idx_tipoprioridadticket_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoPrioridadTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_prioridad")
    private Integer id;

    @Column(name = "nombre_prioridad", nullable = false, length = 30, unique = true)
    private String nombrePrioridad;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
