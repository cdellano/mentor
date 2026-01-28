package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.Anotacion;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {

    // Buscar por ID incluyendo borrados (para restauración)
    @Query("SELECT a FROM Anotacion a WHERE a.idAnotacion = :id AND a.borrado = true")
    Optional<Anotacion> findByIdDeleted(@Param("id") Long id);

    // Filtrar por usuario
    Page<Anotacion> findByUsuarioIdUsuario(Long idUsuario, Pageable pageable);

    // Filtrar por página del diario
    Page<Anotacion> findByPagina(Integer pagina, Pageable pageable);

    // Filtrar por etiqueta (LIKE)
    @Query("SELECT a FROM Anotacion a WHERE a.etiquetas LIKE %:etiqueta%")
    Page<Anotacion> findByEtiquetaContaining(@Param("etiqueta") String etiqueta, Pageable pageable);

    // Filtrar por contenido (LIKE)
    @Query("SELECT a FROM Anotacion a WHERE a.contenido LIKE %:contenido%")
    Page<Anotacion> findByContenidoContaining(@Param("contenido") String contenido, Pageable pageable);

    // Filtrar por rango de fechas
    Page<Anotacion> findByFechaAnotacionBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    // Restaurar (soft delete inverso)
    @Modifying
    @Query("UPDATE Anotacion a SET a.borrado = false WHERE a.idAnotacion = :id")
    void restore(@Param("id") Long id);

    // Consulta completa para reportes con filtros opcionales
    @Query("SELECT a FROM Anotacion a WHERE " +
           "a.fechaAnotacion BETWEEN :fechaInicio AND :fechaFin " +
           "AND (:idUsuario IS NULL OR a.usuario.idUsuario = :idUsuario) " +
           "AND (:contenido IS NULL OR a.contenido LIKE %:contenido%) " +
           "AND (:pagina IS NULL OR a.pagina = :pagina) " +
           "AND (:etiquetas IS NULL OR a.etiquetas LIKE %:etiquetas%) " +
           "AND (:importante IS NULL OR a.importante = :importante) " +
           "ORDER BY a.fechaAnotacion DESC")
    Page<Anotacion> findForReport(@Param("fechaInicio") LocalDateTime fechaInicio,
                                  @Param("fechaFin") LocalDateTime fechaFin,
                                  @Param("idUsuario") Long idUsuario,
                                  @Param("contenido") String contenido,
                                  @Param("pagina") Integer pagina,
                                  @Param("etiquetas") String etiquetas,
                                  @Param("importante") Boolean importante,
                                  Pageable pageable);
}

