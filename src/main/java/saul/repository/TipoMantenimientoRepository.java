package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.TipoMantenimiento;

import java.util.Optional;

@Repository
public interface TipoMantenimientoRepository extends JpaRepository<TipoMantenimiento, Integer> {

    @Query("SELECT t FROM TipoMantenimiento t WHERE t.id = :id AND t.borrado = true")
    Optional<TipoMantenimiento> findByIdDeleted(@Param("id") Integer id);

    Page<TipoMantenimiento> findByNombreTipoMantenimientoContainingIgnoreCase(String nombre, Pageable pageable);

    @Modifying
    @Query("UPDATE TipoMantenimiento t SET t.borrado = false WHERE t.id = :id")
    void restore(@Param("id") Integer id);
}

