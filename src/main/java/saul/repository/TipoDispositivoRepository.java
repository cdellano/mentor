package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.TipoDispositivo;

import java.util.Optional;

@Repository
public interface TipoDispositivoRepository extends JpaRepository<TipoDispositivo, Integer> {

    @Query("SELECT t FROM TipoDispositivo t WHERE t.idTipoDispositivo = :id AND t.borrado = true")
    Optional<TipoDispositivo> findByIdDeleted(@Param("id") Integer id);

    Page<TipoDispositivo> findByNombreTipoContainingIgnoreCase(String nombre, Pageable pageable);

    @Modifying
    @Query("UPDATE TipoDispositivo t SET t.borrado = false WHERE t.idTipoDispositivo = :id")
    void restore(@Param("id") Integer id);
}

