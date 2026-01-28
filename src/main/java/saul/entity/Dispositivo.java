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
import java.util.List;

@Entity
@Table(name = "dispositivos", indexes = {
    @Index(name = "idx_dispositivo_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddispositivo")
    private Long idDispositivo;
    @ManyToOne
    @JoinColumn(name = "idtipodispositivo", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoDispositivo tipoDispositivo;
    @Column(name = "marca", nullable = false, length = 50)
    private String marca;
    @Column(name = "modelo", length = 70)
    private String modelo;
    @Column(name = "numeroserie", unique = true, length = 100)
    private String numeroSerie;
    @Column(name = "inventario", unique = true, length = 30)
    private String inventario;
    @ManyToOne
    @JoinColumn(name = "idtipoestado", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoEstadoDisp tipoEstado;
    @Column(name = "fechacompra")
    private LocalDate fechaCompra;
    @Column(name = "costo", precision = 12, scale = 2)
    private BigDecimal costo;
    @Column(name = "notas", length = 650)
    private String notas;
    @Column(name = "fecharegistro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    // fecha de baja del dispositivo
    @Column(name = "fechabaja")
    private LocalDate fechaBaja;

    @OneToMany(mappedBy = "dispositivo")
    @JsonIgnoreProperties({"dispositivo", "hibernateLazyInitializer", "handler"})
    private List<HistorialUbicacion> historialUbicaciones;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
