package saul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saul.entity.Departamento;

import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {

    @Query("SELECT d FROM Departamento d WHERE d.idDepartamento = :id AND d.borrado = true")
    Optional<Departamento> findByIdDeleted(@Param("id") Integer id);

    Page<Departamento> findByNombreDepartamentoContainingIgnoreCase(String nombre, Pageable pageable);

    @Modifying
    @Query("UPDATE Departamento d SET d.borrado = false WHERE d.idDepartamento = :id")
    void restore(@Param("id") Integer id);
}

