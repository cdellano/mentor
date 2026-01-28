package saul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "anotaciones", indexes = {
    @Index(name = "idx_anotacion_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anotacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idanotacion")
    private Long idAnotacion;

    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    @Column(name = "titulo", nullable = false, length = 120)
    private String titulo;

    @Column(name = "contenido", nullable = false, length = 650)
    private String contenido;

    @Column(name = "pagina", nullable = false)
    private Integer pagina;

    @Column(name = "fechaanotacion", nullable = false)
    private LocalDateTime fechaAnotacion = LocalDateTime.now();

    @Column(name = "etiquetas", length = 200)
    private String etiquetas;

    @Column(name = "importante", nullable = false)
    private Boolean importante = false;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
