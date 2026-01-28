package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.Lugar;

import java.util.Optional;

@Repository
public interface LugarRepository extends JpaRepository<Lugar, Integer> {

    @Query("SELECT l FROM Lugar l WHERE l.idLugar = :id AND l.borrado = true")
    Optional<Lugar> findByIdDeleted(@Param("id") Integer id);

    Page<Lugar> findByNombreLugarContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Lugar> findByDepartamentoIdDepartamento(Integer idDepartamento, Pageable pageable);

    @Modifying
    @Query("UPDATE Lugar l SET l.borrado = false WHERE l.idLugar = :id")
    void restore(@Param("id") Integer id);
}

