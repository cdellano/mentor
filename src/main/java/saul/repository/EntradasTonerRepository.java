package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.EntradasToner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntradasTonerRepository extends JpaRepository<EntradasToner, Integer> {

    @Query("SELECT e FROM EntradasToner e WHERE e.id = :id AND e.borrado = true")
    Optional<EntradasToner> findByIdDeleted(@Param("id") Integer id);

    Page<EntradasToner> findByTipoTonerId(Integer idTipoToner, Pageable pageable);

    Page<EntradasToner> findByUsuarioEntradaIdUsuario(Long idUsuario, Pageable pageable);

    Page<EntradasToner> findByFechaEntradaBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    List<EntradasToner> findByFechaEntradaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<EntradasToner> findByFechaEntradaBetweenAndUsuarioEntradaIdUsuario(LocalDateTime inicio, LocalDateTime fin, Long idUsuario);

    // Para cálculo de stock
    @Query("SELECT COALESCE(SUM(e.cantidad), 0) FROM EntradasToner e WHERE e.tipoToner.id = :idTipoToner")
    Integer sumCantidadByTipoToner(@Param("idTipoToner") Integer idTipoToner);

    @Modifying
    @Query("UPDATE EntradasToner e SET e.borrado = false WHERE e.id = :id")
    void restore(@Param("id") Integer id);
}

