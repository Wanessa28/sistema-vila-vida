
package sistema_vila_vida.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sistema_vila_vida.model.Moradia;
import sistema_vila_vida.repository.MoradorRepository;
import sistema_vila_vida.repository.MoradiaRepository;
import sistema_vila_vida.repository.FuncionarioRepository;

@Controller
public class RelatorioController {

    private final MoradorRepository moradorRepository;
    private final MoradiaRepository moradiaRepository;
    private final FuncionarioRepository funcionarioRepository;

    // CONSTRUTOR
    public RelatorioController(
            MoradorRepository moradorRepository,
            MoradiaRepository moradiaRepository,
            FuncionarioRepository funcionarioRepository) {

        this.moradorRepository = moradorRepository;
        this.moradiaRepository = moradiaRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    // CENTRAL DE RELATÓRIOS
    @GetMapping("/relatorios")
    public String centralRelatorios() {
        return "central-relatorios";
    }

    // RELATÓRIO DE MORADORES
    @GetMapping("/relatorios/moradores")
    public String relatorioMoradores(Model model) {

        model.addAttribute(
            "moradores",
            moradorRepository.findAll(Sort.by("nome"))
        );

        model.addAttribute(
            "totalMoradores",
            moradorRepository.count()
        );

        return "relatorio-moradores";
    }

    // RELATÓRIO DE MORADIAS
    @GetMapping("/relatorios/moradias")
    public String relatorioMoradias(Model model) {

        model.addAttribute(
            "moradias",
            moradiaRepository.findAll(Sort.by("numero"))
        );

        model.addAttribute(
            "totalMoradias",
            moradiaRepository.count()
        );

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

        // IDENTIFICA OS OCUPANTES DE CADA CASA
        Map<Integer, String> ocupantesPorCasa = new HashMap<>();

        LocalDate hoje = LocalDate.now();

        for (Moradia moradia : moradiaRepository.findAll()) {

            var moradoresAtivos =
                moradorRepository.buscarMoradoresAtivosDaCasa(
                    moradia.getNumero(),
                    hoje
                );

            String nomes = moradoresAtivos.stream()
                .map(morador -> morador.getNome())
                .sorted()
                .collect(Collectors.joining(", "));

            ocupantesPorCasa.put(
                moradia.getNumero(),
                nomes.isEmpty() ? "—" : nomes
            );
        }

        model.addAttribute(
            "ocupantesPorCasa",
            ocupantesPorCasa
        );

        return "relatorio-moradias";
    }

    // RELATÓRIO DE FUNCIONÁRIOS
    @GetMapping("/relatorios/funcionarios")
    public String relatorioFuncionarios(Model model) {

        model.addAttribute(
            "funcionarios",
            funcionarioRepository.findAll(Sort.by("nome"))
        );

        model.addAttribute(
            "totalFuncionarios",
            funcionarioRepository.count()
        );

        model.addAttribute(
            "funcionariosAtivos",
            funcionarioRepository.countBySituacao("Ativo")
        );

        model.addAttribute(
            "funcionariosInativos",
            funcionarioRepository.countBySituacao("Inativo")
        );

        return "relatorio-funcionarios";
    }
}
