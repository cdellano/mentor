package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.EstadoTicket;

import java.util.Optional;

@Repository
public interface EstadoTicketRepository extends JpaRepository<EstadoTicket, Integer> {

    @Query("SELECT e FROM EstadoTicket e WHERE e.idEstado = :id AND e.borrado = true")
    Optional<EstadoTicket> findByIdDeleted(@Param("id") Integer id);

    Page<EstadoTicket> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    @Modifying
    @Query("UPDATE EstadoTicket e SET e.borrado = false WHERE e.idEstado = :id")
    void restore(@Param("id") Integer id);
}

