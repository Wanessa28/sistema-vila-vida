
package sistema_vila_vida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema_vila_vida.model.Funcionario;

public interface FuncionarioRepository
        extends JpaRepository<Funcionario, Long> {

    long countBySituacao(String situacao);

}
