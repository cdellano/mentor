package saul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "salidas_toner", indexes = {
    @Index(name = "idx_salidas_toner_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalidasToner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioinstala", nullable = false, foreignKey = @ForeignKey(name = "fk_salidas_usuario_instala"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuarioInstala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuariorecibe", nullable = false, foreignKey = @ForeignKey(name = "fk_salidas_usuario_recibe"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuarioRecibe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipotoner", nullable = false, foreignKey = @ForeignKey(name = "fk_salidas_tipotoner"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoToner tipoToner;

    @Column(name = "fechasalida", nullable = false)
    private LocalDateTime fechaSalida = LocalDateTime.now();

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento", nullable = false, foreignKey = @ForeignKey(name = "fk_salidas_lugar"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Lugar departamento;

    @Column(name = "observaciones", length = 650)
    private String observaciones;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
