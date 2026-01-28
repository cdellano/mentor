package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.TipoToner;

import java.util.Optional;

@Repository
public interface TipoTonerRepository extends JpaRepository<TipoToner, Integer> {

    @Query("SELECT t FROM TipoToner t WHERE t.id = :id AND t.borrado = true")
    Optional<TipoToner> findByIdDeleted(@Param("id") Integer id);

    @Query("SELECT t FROM TipoToner t WHERE t.nombreTipoToner LIKE %:nombre%")
    Page<TipoToner> findByNombreTipoTonerContaining(@Param("nombre") String nombre, Pageable pageable);

    @Modifying
    @Query("UPDATE TipoToner t SET t.borrado = false WHERE t.id = :id")
    void restore(@Param("id") Integer id);
}

