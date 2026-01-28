package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.Mantenimiento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    @Query("SELECT m FROM Mantenimiento m WHERE m.idMantenimiento = :id AND m.borrado = true")
    Optional<Mantenimiento> findByIdDeleted(@Param("id") Long id);

    Page<Mantenimiento> findByDispositivoIdDispositivo(Long idDispositivo, Pageable pageable);

    Page<Mantenimiento> findByTipoMantenimientoId(Integer idTipoMantenimiento, Pageable pageable);

    Page<Mantenimiento> findByFechaProgramadaBetween(LocalDate inicio, LocalDate fin, Pageable pageable);

    /**
     * Busca mantenimientos por rango de fechaRealizado para reportes.
     * Incluye fetch de dispositivo, tipoMantenimiento y usuarios.
     */
    @Query("SELECT m FROM Mantenimiento m " +
           "LEFT JOIN FETCH m.dispositivo d " +
           "LEFT JOIN FETCH d.tipoDispositivo " +
           "LEFT JOIN FETCH d.tipoEstado " +
           "LEFT JOIN FETCH m.tipoMantenimiento " +
           "LEFT JOIN FETCH m.usuarioSolicita " +
           "LEFT JOIN FETCH m.usuarioAtiende " +
           "WHERE m.fechaRealizado BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY m.fechaRealizado DESC")
    List<Mantenimiento> findByFechaRealizadoBetweenForReport(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    /**
     * Busca mantenimientos por rango de fechaRealizado para reportes con filtro opcional por tipo de dispositivo.
     * Si idTipoDispositivo es null, incluye todos los tipos de dispositivos.
     * Incluye fetch de dispositivo, tipoMantenimiento y usuarios.
     */
    @Query("SELECT m FROM Mantenimiento m " +
           "LEFT JOIN FETCH m.dispositivo d " +
           "LEFT JOIN FETCH d.tipoDispositivo " +
           "LEFT JOIN FETCH d.tipoEstado " +
           "LEFT JOIN FETCH m.tipoMantenimiento " +
           "LEFT JOIN FETCH m.usuarioSolicita " +
           "LEFT JOIN FETCH m.usuarioAtiende " +
           "WHERE m.fechaRealizado BETWEEN :fechaInicio AND :fechaFin " +
           "AND (:idTipoDispositivo IS NULL OR d.tipoDispositivo.idTipoDispositivo = :idTipoDispositivo) " +
           "ORDER BY m.fechaRealizado DESC")
    List<Mantenimiento> findByFechaRealizadoBetweenAndTipoDispositivoForReport(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("idTipoDispositivo") Integer idTipoDispositivo);

    /**
     * Busca el último mantenimiento realizado por dispositivo.
     */
    @Query("SELECT m FROM Mantenimiento m " +
           "WHERE m.dispositivo.idDispositivo = :idDispositivo " +
           "AND m.fechaRealizado IS NOT NULL " +
           "ORDER BY m.fechaRealizado DESC")
    List<Mantenimiento> findLastMantenimientoByDispositivo(@Param("idDispositivo") Long idDispositivo);

    @Modifying
    @Query("UPDATE Mantenimiento m SET m.borrado = false WHERE m.idMantenimiento = :id")
    void restore(@Param("id") Long id);
}

