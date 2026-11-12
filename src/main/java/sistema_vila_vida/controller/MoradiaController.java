package sistema_vila_vida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import sistema_vila_vida.model.Moradia;
import sistema_vila_vida.repository.MoradiaRepository;
import sistema_vila_vida.repository.MoradorRepository;

@Controller
public class MoradiaController {
    private final MoradiaRepository moradias;
    private final MoradorRepository moradores;
    public MoradiaController(MoradiaRepository moradias, MoradorRepository moradores) {
        this.moradias = moradias; this.moradores = moradores;
    }
    @GetMapping("/moradias")
    public String listar(Model model) {
        model.addAttribute("moradias", moradias.findAll(Sort.by("numero")));
        return "moradias";
    }
   @GetMapping("/moradias/visualizar/{id}")
public String visualizar(@PathVariable Long id, Model model) {
    Moradia casa = buscar(id);

    model.addAttribute("moradia", casa);

    
moradores.buscarMoradoresAtivosDaCasa(
        casa.getNumero(),
        java.time.LocalDate.now(
            java.time.ZoneId.of("America/Sao_Paulo")
        )
    )
    .stream()
    .findFirst()
    .ifPresent(m ->
        model.addAttribute("moradorDaCasa", m)
    );


    return "visualizar-moradia";
}
    @GetMapping("/moradias/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("moradia", buscar(id));
        return "editar-moradia";
    }
    @PostMapping("/moradias/editar/{id}")
    @Transactional
    public String atualizar(@PathVariable Long id, @RequestParam String situacao,
            @RequestParam(required = false) String observacoes) {
        if (!("Disponível".equals(situacao) || "Ocupada".equals(situacao)
                || "Em manutenção".equals(situacao))
                || (observacoes != null && observacoes.length() > 255))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        Moradia casa = buscar(id);
        
boolean temMorador =
    !moradores.buscarMoradoresAtivosDaCasa(
        casa.getNumero(),
        java.time.LocalDate.now(
            java.time.ZoneId.of("America/Sao_Paulo")
        )
    ).isEmpty();
        if (temMorador && !"Ocupada".equals(situacao))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Esta casa possui morador cadastrado e deve permanecer ocupada");
        if (!temMorador && "Ocupada".equals(situacao))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Para ocupar esta casa, cadastre ou transfira um morador");
        casa.setSituacao(situacao);
        casa.setObservacoes(observacoes);
        moradias.save(casa);
        return "redirect:/moradias";
    }
    private Moradia buscar(Long id) {
        return moradias.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Moradia não encontrada"));
    }
}
