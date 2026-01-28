package saul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "estados_ticket", indexes = {
    @Index(name = "idx_estadoticket_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idestado")
    private Integer idEstado;

    @Column(name = "nombre", nullable = false, unique = true, length = 30)
    private String nombre;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "color", length = 7)
    private String color; // Color hexadecimal para UI

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "orden")
    private Integer orden; // Para ordenar los estados en la UI

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
