package saul.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.BitacoraServicio;
import saul.entity.EstadoBitacora;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface BitacoraServicioRepository extends JpaRepository<BitacoraServicio, Long> {
    @Query("SELECT b FROM BitacoraServicio b WHERE b.id = :id AND b.borrado = true")
    Optional<BitacoraServicio> findByIdDeleted(@Param("id") Long id);
    Page<BitacoraServicio> findByServicioId(Long servicioId, Pageable pageable);
    Page<BitacoraServicio> findByEstado(EstadoBitacora estado, Pageable pageable);
    Page<BitacoraServicio> findByFechaInicioBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<BitacoraServicio> findByReportadoPorIdUsuario(Long idUsuario, Pageable pageable);
    Page<BitacoraServicio> findByTipoIncidenteId(Long tipoIncidenteId, Pageable pageable);

    @Query(value = "SELECT * FROM bitacora_servicios b " +
           "WHERE b.borrado = false " +
           "AND b.created_at BETWEEN :fechaInicio AND :fechaFin " +
           "AND (:idUsuario IS NULL OR b.reportado_por = :idUsuario) " +
           "AND (:comentario IS NULL OR b.comentario ILIKE CONCAT('%', :comentario, '%')) " +
           "AND (:estado IS NULL OR b.estado = CAST(:estado AS VARCHAR)) " +
           "AND (:idTipoIncidente IS NULL OR b.tipo_incidente_id = :idTipoIncidente) " +
           "ORDER BY b.created_at DESC",
           nativeQuery = true)
    List<BitacoraServicio> findForReport(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("idUsuario") Long idUsuario,
            @Param("comentario") String comentario,
            @Param("estado") String estado,
            @Param("idTipoIncidente") Long idTipoIncidente
    );

    @Modifying
    @Query("UPDATE BitacoraServicio b SET b.borrado = false WHERE b.id = :id")
    void restore(@Param("id") Long id);
}
