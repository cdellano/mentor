package saul.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.Servicio;
import java.util.Optional;
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    @Query("SELECT s FROM Servicio s WHERE s.id = :id AND s.borrado = true")
    Optional<Servicio> findByIdDeleted(@Param("id") Long id);
    Page<Servicio> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<Servicio> findByActivo(Boolean activo, Pageable pageable);
    @Modifying
    @Query("UPDATE Servicio s SET s.borrado = false WHERE s.id = :id")
    void restore(@Param("id") Long id);
}
