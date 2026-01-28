package saul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "departamentos", indexes = {
    @Index(name = "idx_departamento_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddepartamento")
    private Integer idDepartamento;

    @Column(name = "nombredepartamento", nullable = false, length = 100)
    private String nombreDepartamento;

    @Column(name = "codigo", length = 20)
    private String codigo;

    @Column(name = "descripcion", length = 650)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecharegistro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;
}
