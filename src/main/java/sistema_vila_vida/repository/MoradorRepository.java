
package sistema_vila_vida.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sistema_vila_vida.model.Morador;

public interface MoradorRepository extends JpaRepository<Morador, Long> {

    @Query("""
        SELECT m FROM Morador m
        WHERE m.numeroCasa = :numeroCasa
        AND (m.dataSaida IS NULL OR m.dataSaida > :hoje)
    """)
    List<Morador> buscarMoradoresAtivosDaCasa(
        @Param("numeroCasa") Integer numeroCasa,
        @Param("hoje") LocalDate hoje
    );

}
