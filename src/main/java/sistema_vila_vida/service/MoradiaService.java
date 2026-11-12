
package sistema_vila_vida.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistema_vila_vida.model.Moradia;
import sistema_vila_vida.repository.MoradiaRepository;
import sistema_vila_vida.repository.MoradorRepository;

@Service
public class MoradiaService {

    private final MoradiaRepository moradias;
    private final MoradorRepository moradores;

    public MoradiaService(
            MoradiaRepository moradias,
            MoradorRepository moradores) {
        this.moradias = moradias;
        this.moradores = moradores;
    }
    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    @Transactional
    public void atualizarSituacaoDasCasas() {

        LocalDate hoje = LocalDate.now(
            java.time.ZoneId.of("America/Sao_Paulo")
        );

        List<Moradia> casas = moradias.findAll();

        for (Moradia casa : casas) {

            // Não alterar casas em manutenção.
            if ("Em manutenção".equals(casa.getSituacao())) {
                continue;
            }

            boolean temMoradorAtivo =
                !moradores.buscarMoradoresAtivosDaCasa(
                    casa.getNumero(), hoje
                ).isEmpty();

            String situacaoCorreta =
                temMoradorAtivo ? "Ocupada" : "Disponível";

            if (!situacaoCorreta.equals(casa.getSituacao())) {
                casa.setSituacao(situacaoCorreta);
                moradias.save(casa);
            }
        }
    }
}
