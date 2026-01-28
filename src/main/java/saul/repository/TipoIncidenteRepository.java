package saul.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.TipoIncidente;
import java.util.Optional;
@Repository
public interface TipoIncidenteRepository extends JpaRepository<TipoIncidente, Long> {
    @Query("SELECT t FROM TipoIncidente t WHERE t.id = :id AND t.borrado = true")
    Optional<TipoIncidente> findByIdDeleted(@Param("id") Long id);
    @Modifying
    @Query("UPDATE TipoIncidente t SET t.borrado = false WHERE t.id = :id")
    void restore(@Param("id") Long id);
}
