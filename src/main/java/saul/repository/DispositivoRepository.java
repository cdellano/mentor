package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.Dispositivo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {

    @Query("SELECT d FROM Dispositivo d WHERE d.idDispositivo = :id AND d.borrado = true")
    Optional<Dispositivo> findByIdDeleted(@Param("id") Long id);

    Page<Dispositivo> findByTipoEstadoIdTipoEstado(Integer idTipoEstado, Pageable pageable);

    Optional<Dispositivo> findByNumeroSerie(String numeroSerie);

    Page<Dispositivo> findByTipoDispositivoIdTipoDispositivo(Integer idTipoDispositivo, Pageable pageable);

    Page<Dispositivo> findByModeloContainingIgnoreCase(String modelo, Pageable pageable);

    // Dispositivos por lugar actual
    @Query("SELECT DISTINCT d FROM Dispositivo d " +
           "JOIN d.historialUbicaciones h " +
           "WHERE d.tipoEstado.nombreEstado <> 'Baja' " +
           "AND d.borrado = false " +
           "AND h.lugar.idLugar = :idLugar " +
           "AND h.fechaSalida IS NULL")
    Page<Dispositivo> findByCurrentLugar(@Param("idLugar") Integer idLugar, Pageable pageable);

    // Dispositivos por departamento actual
    @Query("SELECT DISTINCT d FROM Dispositivo d " +
           "JOIN d.historialUbicaciones h " +
           "WHERE d.tipoEstado.nombreEstado <> 'Baja' " +
           "AND d.borrado = false " +
           "AND h.lugar.departamento.idDepartamento = :idDepartamento " +
           "AND h.fechaSalida IS NULL")
    Page<Dispositivo> findByCurrentDepartamento(@Param("idDepartamento") Integer idDepartamento, Pageable pageable);

    // Todos los dispositivos activos con su ubicación más actual
    @Query("SELECT DISTINCT d FROM Dispositivo d " +
           "LEFT JOIN FETCH d.historialUbicaciones h " +
           "WHERE d.tipoEstado.nombreEstado <> 'Baja' " +
           "AND d.borrado = false " +
           "AND (h.fechaSalida IS NULL OR h IS NULL)")
    Page<Dispositivo> findAllActiveWithCurrentLocation(Pageable pageable);

    /**
     * Busca dispositivos que no han recibido mantenimiento en un número específico de días mínimo,
     * filtrados por tipo de dispositivo. Usa consulta nativa para manejo de fechas.
     * Si idTipoDispositivo es -1, muestra todos los tipos de dispositivos.
     */
    @Query(value = "SELECT DISTINCT d.* FROM dispositivos d " +
                  "LEFT JOIN tipodispositivo td ON d.idtipodispositivo = td.idtipodispositivo " +
                  "LEFT JOIN tipos_estado_dispositivo te ON d.idtipoestado = te.idtipoestado " +
                  "WHERE te.nombreestado <> 'Baja' " +
                  "AND d.borrado = false " +
                  "AND (:idTipoDispositivo IS NULL OR :idTipoDispositivo = -1 OR d.idtipodispositivo = :idTipoDispositivo) " +
                  "AND d.iddispositivo NOT IN (" +
                  "    SELECT DISTINCT m.iddispositivo FROM mantenimientos m " +
                  "    WHERE m.fecharealizado IS NOT NULL " +
                  "    AND m.fecharealizado >= (CURRENT_DATE - :diasMinimos)" +
                  ") " +
                  "ORDER BY d.iddispositivo",
           nativeQuery = true)
    List<Dispositivo> findEquiposRezagadosEnMantenimiento(
            @Param("diasMinimos") Integer diasMinimos,
            @Param("idTipoDispositivo") Integer idTipoDispositivo);

    /**
     * Busca un dispositivo aleatorio que no ha recibido mantenimiento en un número específico de días mínimo,
     * filtrado por tipo de dispositivo.
     */
    @Query(value = "SELECT d.* FROM dispositivos d " +
                  "LEFT JOIN tipodispositivo td ON d.idtipodispositivo = td.idtipodispositivo " +
                  "LEFT JOIN tipos_estado_dispositivo te ON d.idtipoestado = te.idtipoestado " +
                  "WHERE te.nombreestado <> 'Baja' " +
                  "AND d.borrado = false " +
                  "AND d.idtipodispositivo = :idTipoDispositivo " +
                  "AND d.iddispositivo NOT IN (" +
                  "    SELECT DISTINCT m.iddispositivo FROM mantenimientos m " +
                  "    WHERE m.fecharealizado IS NOT NULL " +
                  "    AND m.fecharealizado >= (CURRENT_DATE - :diasMinimos)" +
                  ") " +
                  "ORDER BY RANDOM() " +
                  "LIMIT 1",
           nativeQuery = true)
    Optional<Dispositivo> findRandomEquipoRezagadoEnMantenimiento(
            @Param("diasMinimos") Integer diasMinimos,
            @Param("idTipoDispositivo") Integer idTipoDispositivo);

    @Modifying
    @Query("UPDATE Dispositivo d SET d.borrado = false WHERE d.idDispositivo = :id")
    void restore(@Param("id") Long id);

    /**
     * Query nativa para reportes de registro de dispositivos con múltiples filtros opcionales.
     * Considera el soft delete (borrado = false).
     * Filtra por fechaRegistro y opcionalmente por marca, modelo, numeroserie, inventario, notas (usando ILIKE) y tipoEstado.
     */
    @Query(value = """
            SELECT d.* FROM dispositivos d
            WHERE d.borrado = false
            AND d.fecharegistro BETWEEN :fechaInicio AND :fechaFin
            AND (:marca IS NULL OR LOWER(d.marca) ILIKE LOWER(CONCAT('%', CAST(:marca AS TEXT), '%')))
            AND (:modelo IS NULL OR LOWER(d.modelo) ILIKE LOWER(CONCAT('%', CAST(:modelo AS TEXT), '%')))
            AND (:numeroserie IS NULL OR LOWER(d.numeroserie) ILIKE LOWER(CONCAT('%', CAST(:numeroserie AS TEXT), '%')))
            AND (:inventario IS NULL OR LOWER(d.inventario) ILIKE LOWER(CONCAT('%', CAST(:inventario AS TEXT), '%')))
            AND (:notas IS NULL OR LOWER(CAST(d.notas AS TEXT)) ILIKE LOWER(CONCAT('%', CAST(:notas AS TEXT), '%')))
            AND (:idTipoEstado IS NULL OR d.idtipoestado = :idTipoEstado)
            ORDER BY d.fecharegistro DESC
            """, nativeQuery = true)
    List<Dispositivo> findDispositivosForReport(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("marca") String marca,
            @Param("modelo") String modelo,
            @Param("numeroserie") String numeroserie,
            @Param("inventario") String inventario,
            @Param("notas") String notas,
            @Param("idTipoEstado") Integer idTipoEstado
    );
}
