package saul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "entradas_toner", indexes = {
    @Index(name = "idx_entradas_toner_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntradasToner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioentrada", nullable = false, foreignKey = @ForeignKey(name = "fk_entradas_usuario"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuarioEntrada;

    @Column(name = "fechaentrada", nullable = false)
    private LocalDateTime fechaEntrada = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipotoner", nullable = false, foreignKey = @ForeignKey(name = "fk_entradas_tipotoner"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoToner tipoToner;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "observaciones", length = 650)
    private String observaciones;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
