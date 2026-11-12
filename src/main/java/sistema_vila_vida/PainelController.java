
package sistema_vila_vida;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sistema_vila_vida.repository.MoradorRepository;
import sistema_vila_vida.repository.MoradiaRepository;
import sistema_vila_vida.repository.FuncionarioRepository;

@Controller
public class PainelController {

    private final MoradorRepository moradorRepository;
    private final MoradiaRepository moradiaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public PainelController(
            MoradorRepository moradorRepository,
            MoradiaRepository moradiaRepository,
            FuncionarioRepository funcionarioRepository) {

        this.moradorRepository = moradorRepository;
        this.moradiaRepository = moradiaRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping("/painel.html")
    public String abrirPainel(Model model) {

        // INDICADORES GERAIS

        model.addAttribute(
            "totalMoradores",
            moradorRepository.count()
        );

        model.addAttribute(
            "totalMoradias",
            moradiaRepository.count()
        );

        model.addAttribute(
            "totalFuncionarios",
            funcionarioRepository.count()
        );

        // SITUAÇÃO DAS MORADIAS

        model.addAttribute(
            "moradiasDisponiveis",
            moradiaRepository.countBySituacao("Disponível")
        );

        model.addAttribute(
            "moradiasOcupadas",
            moradiaRepository.countBySituacao("Ocupada")
        );

        model.addAttribute(
            "moradiasManutencao",
            moradiaRepository.countBySituacao("Em manutenção")
        );

        // SITUAÇÃO DOS FUNCIONÁRIOS

        model.addAttribute(
            "funcionariosAtivos",
            funcionarioRepository.countBySituacao("Ativo")
        );

        model.addAttribute(
            "funcionariosInativos",
            funcionarioRepository.countBySituacao("Inativo")
        );

        return "painel";
    }
}
