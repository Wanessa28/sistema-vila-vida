package sistema_vila_vida.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sistema_vila_vida.model.Funcionario;
import sistema_vila_vida.repository.FuncionarioRepository;
import java.time.LocalDate;
import java.util.Comparator;

@Controller
public class FuncionarioController {
    private final FuncionarioRepository repository;
    public FuncionarioController(FuncionarioRepository repository) { this.repository = repository; }

    @GetMapping("/funcionarios")
    public String listar(Model model) {
        var funcionarios = repository.findAll();
        funcionarios.sort(Comparator.comparing(Funcionario::getNome, String.CASE_INSENSITIVE_ORDER));
        model.addAttribute("funcionarios", funcionarios);
        return "funcionarios";
    }
    @GetMapping("/funcionarios/novo")
    public String novo() { return "funcionario-form"; }
    @PostMapping("/funcionarios/salvar")
    public String salvar(@ModelAttribute Funcionario funcionario) {
        validar(funcionario);
        funcionario.setId(null);
        repository.save(funcionario);
        return "redirect:/funcionarios";
    }
    @GetMapping("/funcionarios/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("funcionario", buscar(id));
        return "visualizar-funcionario";
    }
    @GetMapping("/funcionarios/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("funcionario", buscar(id));
        return "editar-funcionario";
    }
    @PostMapping("/funcionarios/editar/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Funcionario dados) {
        Funcionario atual = buscar(id);
        validar(dados);
        atual.setNome(dados.getNome().trim());
        atual.setCpf(dados.getCpf().trim());
        atual.setCargo(dados.getCargo().trim());
        atual.setTelefone(dados.getTelefone());
        atual.setDataAdmissao(dados.getDataAdmissao());
        atual.setSituacao(dados.getSituacao());
        atual.setObservacoes(dados.getObservacoes());
        repository.save(atual);
        return "redirect:/funcionarios";
    }


    // EXCLUIR FUNCIONÁRIO
    @PostMapping("/funcionarios/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        Funcionario funcionario = buscar(id);

        repository.delete(funcionario);

        return "redirect:/funcionarios";
    }


    private Funcionario buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
    }
    private void validar(Funcionario f) {
        if (f.getNome() == null || f.getNome().isBlank() || f.getNome().length() > 255
            || f.getCpf() == null || f.getCpf().isBlank() || f.getCpf().length() > 255
            || f.getCargo() == null || f.getCargo().isBlank() || f.getCargo().length() > 255
            || f.getDataAdmissao() == null || f.getDataAdmissao().isAfter(LocalDate.now())
            || !("Ativo".equals(f.getSituacao()) || "Inativo".equals(f.getSituacao()))
            || (f.getTelefone() != null && f.getTelefone().length() > 255)
            || (f.getObservacoes() != null && f.getObservacoes().length() > 1000)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verifique os dados do funcionário");
        }
    }
}
