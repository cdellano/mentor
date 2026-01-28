package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.SalidasToner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalidasTonerRepository extends JpaRepository<SalidasToner, Integer> {

    @Query("SELECT s FROM SalidasToner s WHERE s.id = :id AND s.borrado = true")
    Optional<SalidasToner> findByIdDeleted(@Param("id") Integer id);

    Page<SalidasToner> findByTipoTonerId(Integer idTipoToner, Pageable pageable);

    Page<SalidasToner> findByUsuarioInstalaIdUsuario(Long idUsuario, Pageable pageable);

    Page<SalidasToner> findByFechaSalidaBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    // Métodos para reportes (sin paginación)
    List<SalidasToner> findByFechaSalidaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<SalidasToner> findByFechaSalidaBetweenAndUsuarioInstalaIdUsuario(
            LocalDateTime inicio, LocalDateTime fin, Long idUsuario);

    // Para cálculo de stock
    @Query("SELECT COALESCE(SUM(s.cantidad), 0) FROM SalidasToner s WHERE s.tipoToner.id = :idTipoToner")
    Integer sumCantidadByTipoToner(@Param("idTipoToner") Integer idTipoToner);

    @Modifying
    @Query("UPDATE SalidasToner s SET s.borrado = false WHERE s.id = :id")
    void restore(@Param("id") Integer id);
}

