package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.TipoEstadoDisp;

import java.util.Optional;

@Repository
public interface TipoEstadoDispRepository extends JpaRepository<TipoEstadoDisp, Integer> {

    @Query("SELECT t FROM TipoEstadoDisp t WHERE t.idTipoEstado = :id AND t.borrado = true")
    Optional<TipoEstadoDisp> findByIdDeleted(@Param("id") Integer id);

    Page<TipoEstadoDisp> findByNombreEstadoContainingIgnoreCase(String nombre, Pageable pageable);

    Optional<TipoEstadoDisp> findByNombreEstadoIgnoreCase(String nombre);

    @Modifying
    @Query("UPDATE TipoEstadoDisp t SET t.borrado = false WHERE t.idTipoEstado = :id")
    void restore(@Param("id") Integer id);
}

