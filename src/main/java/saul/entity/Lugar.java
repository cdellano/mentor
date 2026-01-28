package saul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "lugares", indexes = {
    @Index(name = "idx_lugar_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlugar")
    private Integer idLugar;

    @Column(name = "nombrelugar", nullable = false, length = 80)
    private String nombreLugar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iddepartamento")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Departamento departamento;

    @Column(name = "piso", length = 20)
    private String piso;

    @Column(name = "edificio", length = 80)
    private String edificio;

    @Column(name = "descripcion", length = 650)
    private String descripcion;

    @Column(name = "fecharegistro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
