package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = :id AND u.borrado = true")
    Optional<Usuario> findByIdDeleted(@Param("id") Long id);

    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE %:nombre%")
    Page<Usuario> findByNombreContaining(@Param("nombre") String nombre, Pageable pageable);

    Page<Usuario> findByRol(String rol, Pageable pageable);

    Optional<Usuario> findByUsuarioLogin(String usuarioLogin);

    Optional<Usuario> findByEmail(String email);

    @Modifying
    @Query("UPDATE Usuario u SET u.borrado = false WHERE u.idUsuario = :id")
    void restore(@Param("id") Long id);
}

