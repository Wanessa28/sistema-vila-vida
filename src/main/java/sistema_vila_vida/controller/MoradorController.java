
package sistema_vila_vida.controller;

import java.time.LocalDate;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import sistema_vila_vida.model.Morador;
import sistema_vila_vida.model.Moradia;
import sistema_vila_vida.repository.MoradorRepository;
import sistema_vila_vida.repository.MoradiaRepository;

@Controller
public class MoradorController {

    private final MoradorRepository moradores;
    private final MoradiaRepository moradias;

    public MoradorController(
            MoradorRepository moradores,
            MoradiaRepository moradias) {
        this.moradores = moradores;
        this.moradias = moradias;
    }

    @GetMapping("/moradores")
    public String listar(Model model) {
        model.addAttribute(
            "moradores",
            moradores.findAll(Sort.by("nome"))
        );
        return "moradores";
    }

    @GetMapping("/moradores/novo")
    public String novo(Model model) {
        model.addAttribute(
            "moradias",
            moradias.findAll(Sort.by("numero"))
        );
        return "novo-morador";
    }

    @PostMapping("/moradores/salvar")
    @Transactional
    public String salvar(
            @RequestParam String nome,
            @RequestParam LocalDate dataNascimento,
            @RequestParam Integer numeroCasa,
            @RequestParam LocalDate dataEntrada,
            @RequestParam(required = false) LocalDate dataSaida,
            @RequestParam(required = false) String nomeContatoEmergencia,
            @RequestParam(required = false) String telefoneEmergencia,
            @RequestParam(required = false) String observacoes) {

        validar(
            nome, dataNascimento, numeroCasa, dataEntrada,
            dataSaida, nomeContatoEmergencia,
            telefoneEmergencia, observacoes
        );

        Moradia casa = buscarCasa(numeroCasa);
        validarDestino(numeroCasa, null, casa);

        Morador morador = new Morador();

        preencher(
            morador, nome, dataNascimento, numeroCasa,
            dataEntrada, dataSaida, nomeContatoEmergencia,
            telefoneEmergencia, observacoes
        );

        
moradores.save(morador);

LocalDate hoje = LocalDate.now(
    java.time.ZoneId.of("America/Sao_Paulo")
);

boolean moradorAtivo =
    dataSaida == null || dataSaida.isAfter(hoje);

casa.setSituacao(
    moradorAtivo ? "Ocupada" : "Disponível"
);

moradias.save(casa);


        return "redirect:/moradores";
    }

    @GetMapping("/moradores/visualizar/{id}")
    public String visualizar(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("morador", buscarMorador(id));
        return "visualizar-morador";
    }

    @GetMapping("/moradores/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("morador", buscarMorador(id));
        model.addAttribute(
            "moradias",
            moradias.findAll(Sort.by("numero"))
        );

        return "editar-morador";
    }

    @PostMapping("/moradores/editar/{id}")
    @Transactional
    public String atualizar(
            @PathVariable Long id,
            @RequestParam String nome,
            @RequestParam LocalDate dataNascimento,
            @RequestParam Integer numeroCasa,
            @RequestParam LocalDate dataEntrada,
            @RequestParam(required = false) LocalDate dataSaida,
            @RequestParam(required = false) String nomeContatoEmergencia,
            @RequestParam(required = false) String telefoneEmergencia,
            @RequestParam(required = false) String observacoes) {

        validar(
            nome, dataNascimento, numeroCasa, dataEntrada,
            dataSaida, nomeContatoEmergencia,
            telefoneEmergencia, observacoes
        );

        Morador atual = buscarMorador(id);
        Integer casaAntiga = atual.getNumeroCasa();


LocalDate dataAtual = LocalDate.now(
    java.time.ZoneId.of("America/Sao_Paulo")
);

boolean moradorJaSaiu =
    atual.getDataSaida() != null
    && !atual.getDataSaida().isAfter(dataAtual);

if (moradorJaSaiu && !numeroCasa.equals(casaAntiga)) {
    throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Não é permitido transferir um ex-morador. " +
        "O histórico da moradia deve ser preservado."
    );
}


boolean ficaraAtivo =
    dataSaida == null || dataSaida.isAfter(dataAtual);

boolean outroMoradorAtivoNaCasa =
    moradores.buscarMoradoresAtivosDaCasa(
        numeroCasa, dataAtual
    )
    .stream()
    .anyMatch(m -> !id.equals(m.getId()));

if (ficaraAtivo && outroMoradorAtivoNaCasa) {
    throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Esta casa já possui outro morador ativo. " +
        "Não é possível reativar este cadastro."
    );
}


        if (!numeroCasa.equals(casaAntiga)) {

            Moradia destino = buscarCasa(numeroCasa);
            validarDestino(numeroCasa, id, destino);

            Moradia origem = buscarCasa(casaAntiga);

            
boolean outroNaOrigem = moradores
    .buscarMoradoresAtivosDaCasa(
        casaAntiga,
        LocalDate.now(
            java.time.ZoneId.of("America/Sao_Paulo")
        )
    )
    .stream()
    .anyMatch(m -> !id.equals(m.getId()));


            
if (!outroNaOrigem
        && !"Em manutenção".equals(origem.getSituacao())) {

    origem.setSituacao("Disponível");
    moradias.save(origem);
}


            destino.setSituacao("Ocupada");
            moradias.save(destino);
        }

        preencher(
            atual, nome, dataNascimento, numeroCasa,
            dataEntrada, dataSaida, nomeContatoEmergencia,
            telefoneEmergencia, observacoes
        );

        moradores.save(atual);

LocalDate hoje = LocalDate.now(
    java.time.ZoneId.of("America/Sao_Paulo")
);

Moradia casaAtual = buscarCasa(numeroCasa);

boolean possuiMoradorAtivo =
    !moradores.buscarMoradoresAtivosDaCasa(
        numeroCasa, hoje
    ).isEmpty();

if (!"Em manutenção".equals(casaAtual.getSituacao())) {
    casaAtual.setSituacao(
        possuiMoradorAtivo ? "Ocupada" : "Disponível"
    );
    moradias.save(casaAtual);
}


        return "redirect:/moradores";
    }


    // EXCLUIR MORADOR
    @PostMapping("/moradores/excluir/{id}")
    @Transactional
    public String excluir(@PathVariable Long id) {

        Morador morador = buscarMorador(id);

        Integer numeroCasa = morador.getNumeroCasa();

        // Exclui o cadastro do morador
        moradores.delete(morador);
        moradores.flush();

        // Verifica se ainda existem moradores ativos na casa
        LocalDate hoje = LocalDate.now(
            java.time.ZoneId.of("America/Sao_Paulo")
        );

        boolean possuiMoradorAtivo =
            !moradores.buscarMoradoresAtivosDaCasa(
                numeroCasa, hoje
            ).isEmpty();

        // Atualiza a situação da moradia
        Moradia casa = buscarCasa(numeroCasa);

        if (!"Em manutenção".equals(casa.getSituacao())) {

            casa.setSituacao(
                possuiMoradorAtivo ? "Ocupada" : "Disponível"
            );

            moradias.save(casa);
        }

        return "redirect:/moradores";
    }

    private Morador buscarMorador(Long id) {
        return moradores.findById(id).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Morador não encontrado"
            )
        );
    }

    private Moradia buscarCasa(Integer numero) {
        return moradias.findAll()
            .stream()
            .filter(m -> numero.equals(m.getNumero()))
            .findFirst()
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Casa não encontrada"
                )
            );
    }

    private void validarDestino(
            Integer numero,
            Long idAtual,
            Moradia casa) {

        if (!"Disponível".equals(casa.getSituacao())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Casa não disponível"
            );
        }

        boolean ocupada = moradores
    .buscarMoradoresAtivosDaCasa(
        numero,
        LocalDate.now(
            java.time.ZoneId.of("America/Sao_Paulo")
        )
    )
    .stream()
    .anyMatch(m ->
        idAtual == null || !idAtual.equals(m.getId())
    );

        if (ocupada) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Casa já possui morador"
            );
        }
    }

    private void validar(
            String nome,
            LocalDate nascimento,
            Integer casa,
            LocalDate entrada,
            LocalDate saida,
            String contato,
            String telefone,
            String obs) {

        if (nome == null
                || nome.isBlank()
                || nome.length() > 255
                || nascimento == null
                || casa == null
                || entrada == null
                || nascimento.isAfter(LocalDate.now())
                || entrada.isAfter(LocalDate.now())
                || entrada.isBefore(nascimento)
                || (saida != null && saida.isBefore(entrada))
                || (contato != null && contato.length() > 255)
                || (telefone != null && telefone.length() > 20)
                || (obs != null && obs.length() > 255)) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Verifique os dados informados"
            );
        }
    }

    private void preencher(
            Morador m,
            String nome,
            LocalDate nasc,
            Integer casa,
            LocalDate entrada,
            LocalDate saida,
            String contato,
            String telefone,
            String obs) {

        m.setNome(nome.trim());
        m.setDataNascimento(nasc);
        m.setNumeroCasa(casa);
        m.setDataEntrada(entrada);
        m.setDataSaida(saida);
        m.setNomeContatoEmergencia(
            contato == null ? null : contato.trim()
        );
        m.setTelefoneEmergencia(
            telefone == null ? null : telefone.trim()
        );
        m.setObservacoes(obs);
    }
}
