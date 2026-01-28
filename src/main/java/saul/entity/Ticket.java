package saul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets", indexes = {
    @Index(name = "idx_ticket_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idticket")
    private Long idTicket;

    @ManyToOne
    @JoinColumn(name = "idusuariocreador", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuarioCreador;

    @ManyToOne
    @JoinColumn(name = "idusuarioasignado")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuarioAsignado;

    @ManyToOne
    @JoinColumn(name = "iddepartamento", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Departamento departamento;

    @Column(name = "asunto", nullable = false, length = 120)
    private String asunto;

    @Column(name = "descripcion", length = 650)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "idestado", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EstadoTicket estadoTicket;

    @ManyToOne
    @JoinColumn(name = "id_prioridad", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoPrioridadTicket prioridad;

    @Column(name = "fechacreacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fechacierre")
    private LocalDateTime fechaCierre;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
