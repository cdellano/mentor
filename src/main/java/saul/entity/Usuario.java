package saul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", indexes = {
    @Index(name = "idx_usuario_borrado", columnList = "borrado")
})
@SQLRestriction("borrado = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Long idUsuario;
    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;
    @Column(name = "apellido", length = 80)
    private String apellido;
    @Column(name = "usuariologin", nullable = false, unique = true, length = 50)
    private String usuarioLogin;

    // Modificado: Se elimina 'nullable = false' para permitir usuarios autenticados externamente (PKCE) sin contraseña local
    @Column(name = "contrasenahash", length = 650)
    private String contrasenaHash;

    // Nuevo campo: Almacena el 'sub' (Subject) del token JWT para vincular con el proveedor de identidad
    @Column(name = "sub", unique = true, length = 255)
    private String sub;

    @Column(name = "email", unique = true, length = 120)
    private String email;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "ACTIVO";

    @Column(name = "fecharegistro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    // Es el rol del usuario, ej: ROLE_ADMIN, ROLE_USER
    @Column(name = "rol", nullable = false, length = 50)
    private String rol;

    @Column(name = "borrado", nullable = false)
    private Boolean borrado = false;

    /**
     * Obtiene el nombre completo del usuario (nombre + apellido).
     * @return Nombre completo del usuario
     */
    public String getNombreCompleto() {
        if (apellido != null && !apellido.isEmpty()) {
            return nombre + " " + apellido;
        }
        return nombre;
    }
}
