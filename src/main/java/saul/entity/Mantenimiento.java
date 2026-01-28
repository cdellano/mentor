package saul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mantenimientos", indexes = {
    @Index(name = "idx_mantenimiento_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmantenimiento")
    private Long idMantenimiento;

    @ManyToOne
    @JoinColumn(name = "iddispositivo", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dispositivo dispositivo;

    @ManyToOne
    @JoinColumn(name = "idusuariosolicita")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuarioSolicita;

    @ManyToOne
    @JoinColumn(name = "idusuarioatiende")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuarioAtiende;

    @ManyToOne
    @JoinColumn(name = "idtipomantenimiento", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoMantenimiento tipoMantenimiento;

    @Column(name = "descripcion", nullable = false, length = 650)
    private String descripcion;

    @Column(name = "fechaprogramada", nullable = false)
    private LocalDate fechaProgramada;

    @Column(name = "fecharealizado")
    private LocalDate fechaRealizado;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "PROGRAMADO";

    @Column(name = "costo", precision = 12, scale = 2)
    private BigDecimal costo;

    @Column(name = "notas", length = 650)
    private String notas;

    @Column(name = "fecharegistro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "pila_cmos")
    private Boolean pilaCmos = false;

    @Column(name = "pasta_cpu")
    private Boolean pastaCpu = false;

    @Column(name = "limpieza")
    private Boolean limpieza = false;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
