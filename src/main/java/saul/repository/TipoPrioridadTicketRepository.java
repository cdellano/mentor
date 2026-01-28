package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.TipoPrioridadTicket;

import java.util.Optional;

@Repository
public interface TipoPrioridadTicketRepository extends JpaRepository<TipoPrioridadTicket, Integer> {

    @Query("SELECT t FROM TipoPrioridadTicket t WHERE t.id = :id AND t.borrado = true")
    Optional<TipoPrioridadTicket> findByIdDeleted(@Param("id") Integer id);

    Page<TipoPrioridadTicket> findByNombrePrioridadContainingIgnoreCase(String nombre, Pageable pageable);

    @Modifying
    @Query("UPDATE TipoPrioridadTicket t SET t.borrado = false WHERE t.id = :id")
    void restore(@Param("id") Integer id);
}

