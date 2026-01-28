package saul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "historialubicacion", indexes = {
    @Index(name = "idx_historial_ubicacion_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialUbicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idhistorial")
    private Long idHistorial;

    @ManyToOne
    @JoinColumn(name = "iddispositivo", nullable = false)
    @JsonIgnoreProperties({"historialUbicaciones", "hibernateLazyInitializer", "handler"})
    private Dispositivo dispositivo;

    @ManyToOne
    @JoinColumn(name = "idlugar", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Lugar lugar;

    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    @Column(name = "fechaentrada", nullable = false)
    private LocalDateTime fechaEntrada;

    @Column(name = "fechasalida")
    private LocalDateTime fechaSalida;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
