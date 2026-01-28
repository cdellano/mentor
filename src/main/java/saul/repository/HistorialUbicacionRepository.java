package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.HistorialUbicacion;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface HistorialUbicacionRepository extends JpaRepository<HistorialUbicacion, Long> {

    @Query("SELECT h FROM HistorialUbicacion h WHERE h.idHistorial = :id AND h.borrado = true")
    Optional<HistorialUbicacion> findByIdDeleted(@Param("id") Long id);

    Page<HistorialUbicacion> findByDispositivoIdDispositivo(Long idDispositivo, Pageable pageable);

    Page<HistorialUbicacion> findByLugarIdLugar(Integer idLugar, Pageable pageable);

    Page<HistorialUbicacion> findByFechaEntradaBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    // Obtener ubicación actual de un dispositivo (sin fecha de salida)
    @Query("SELECT h FROM HistorialUbicacion h WHERE h.dispositivo.idDispositivo = :idDispositivo AND h.fechaSalida IS NULL")
    Optional<HistorialUbicacion> findCurrentByDispositivo(@Param("idDispositivo") Long idDispositivo);

    // Obtener todo el historial de ubicaciones de un dispositivo ordenado por fecha de entrada descendente
    @Query("SELECT h FROM HistorialUbicacion h WHERE h.dispositivo.idDispositivo = :idDispositivo ORDER BY h.fechaEntrada DESC")
    java.util.List<HistorialUbicacion> findAllByDispositivoIdOrderByFechaEntradaDesc(@Param("idDispositivo") Long idDispositivo);

    @Modifying
    @Query("UPDATE HistorialUbicacion h SET h.borrado = false WHERE h.idHistorial = :id")
    void restore(@Param("id") Long id);

    // Obtener historial de ubicaciones por rango de fechaEntrada ordenado descendente (más recientes primero)
    @Query("SELECT h FROM HistorialUbicacion h WHERE h.fechaEntrada BETWEEN :inicio AND :fin ORDER BY h.fechaEntrada DESC")
    java.util.List<HistorialUbicacion> findByFechaEntradaBetweenOrderByFechaEntradaDesc(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
}

