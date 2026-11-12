
package sistema_vila_vida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema_vila_vida.model.Moradia;

public interface MoradiaRepository extends JpaRepository<Moradia, Long> {

    long countBySituacao(String situacao);

}