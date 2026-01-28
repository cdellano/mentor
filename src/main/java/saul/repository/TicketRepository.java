package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.idTicket = :id AND t.borrado = true")
    Optional<Ticket> findByIdDeleted(@Param("id") Long id);

    Page<Ticket> findByEstadoTicketIdEstado(Integer idEstado, Pageable pageable);

    Page<Ticket> findByPrioridadId(Integer idPrioridad, Pageable pageable);

    Page<Ticket> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    Page<Ticket> findByDepartamentoIdDepartamento(Integer idDepartamento, Pageable pageable);

    @Modifying
    @Query("UPDATE Ticket t SET t.borrado = false WHERE t.idTicket = :id")
    void restore(@Param("id") Long id);

    /**
     * Query nativa para reportes de tickets con múltiples filtros opcionales.
     * Considera el soft delete (borrado = false).
     */
    @Query(value = """
            SELECT t.* FROM tickets t
            WHERE t.borrado = false
            AND t.fechacreacion BETWEEN :fechaInicio AND :fechaFin
            AND (:idDepartamento IS NULL OR t.iddepartamento = :idDepartamento)
            AND (:idEstado IS NULL OR t.idestado = :idEstado)
            AND (:idPrioridad IS NULL OR t.id_prioridad = :idPrioridad)
            AND (:descripcion IS NULL OR LOWER(CAST(t.descripcion AS TEXT)) ILIKE LOWER(CONCAT('%', CAST(:descripcion AS TEXT), '%')))
            ORDER BY t.fechacreacion DESC
            """, nativeQuery = true)
    List<Ticket> findTicketsForReport(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("idDepartamento") Integer idDepartamento,
            @Param("idEstado") Integer idEstado,
            @Param("idPrioridad") Integer idPrioridad,
            @Param("descripcion") String descripcion
    );
}

