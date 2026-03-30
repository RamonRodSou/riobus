package br.com.technosou.core.service;

import br.com.technosou.core.model.Onibus;
import br.com.technosou.core.model.PosicaoGeografica;
import br.com.technosou.core.model.Velocidade;
import br.com.technosou.core.utils.DataHoraUtils;
import br.com.technosou.infra.client.DataRioClient;
import br.com.technosou.infra.dto.PosicaoOnibusDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.*;

@ApplicationScoped
public class OnibusService {

    private static final Logger LOG = Logger.getLogger(OnibusService.class);

    private static final PosicaoGeografica LOCALIZACAO_USUARIO = new PosicaoGeografica(-22.8917226, -43.4452109);

    @ConfigProperty(name = "riobus.geo.raio-terra-km")
    private int raioTerraKm;

    @ConfigProperty(name = "riobus.api.limite-onibus")
    private int limiteOnibus;

    @Inject
    @RestClient
    DataRioClient dataRioClient;

    @Inject
    ObjectMapper objectMapper;

    private volatile List<Onibus> cacheOnibus = Collections.emptyList();

    private volatile Map<String, List<Onibus>> historicoPorOnibus = Collections.emptyMap();

    private static final int MAX_HISTORICO = 15;

    @Scheduled(every = "${riobus.api.intervalo-cache}")
    public void atualizarCacheDaPrefeitura() {
        try {
            String jsonBruto = dataRioClient.buscarTodosOsOnibusBruto();
            List<PosicaoOnibusDTO> dtos = objectMapper.readValue(
                    jsonBruto,
                    new TypeReference<List<PosicaoOnibusDTO>>() {}
            );

            Map<String, List<Onibus>> novoCache = new HashMap<>();

            dtos.stream()
                    .map(this::converterParaModel)
                    .flatMap(Optional::stream)
                    .forEach(onibusNovo -> {
                        List<Onibus> historicoAntigo = this.historicoPorOnibus.getOrDefault(onibusNovo.id(), new ArrayList<>());

                        List<Onibus> historicoAtualizado = new ArrayList<>(historicoAntigo);

                        if (historicoAtualizado.isEmpty() || historicoAtualizado.get(historicoAtualizado.size() - 1).dataHora().isBefore(onibusNovo.dataHora())) {
                            historicoAtualizado.add(onibusNovo);
                        }

                        if (historicoAtualizado.size() > MAX_HISTORICO) {
                            historicoAtualizado.remove(0);
                        }

                        novoCache.put(onibusNovo.id(), historicoAtualizado);
                    });

            this.historicoPorOnibus = novoCache;

            LOG.info("CACHE DE HISTÓRICO ATUALIZADO COM SUCESSO! Veículos rastreados: " + historicoPorOnibus.size());
        } catch (Exception e) {
            LOG.error("FALHA EM ATUALIZAR OS CACHES. MANTENDO OS DADOS ANTERIORES.", e);
        }
    }
    public List<Onibus> buscarOnibusPorLinha(String linhaDesejada) {
        if (linhaDesejada == null || linhaDesejada.isBlank()) {
            return Collections.emptyList();
        }

        return this.historicoPorOnibus.values().stream()
                .filter(historico -> !historico.isEmpty())
                .filter(historico -> historico.get(historico.size() - 1).linha().equalsIgnoreCase(linhaDesejada))
                .filter(historico -> isOnibusAtivo(historico.get(historico.size() - 1)))
                .flatMap(List::stream)
                .toList();
    }

    private Optional<Onibus> converterParaModel(PosicaoOnibusDTO dto) {
        Optional<PosicaoGeografica> posicao = PosicaoGeografica.fromStrings(dto.latitude(), dto.longitude());
        if (posicao.isEmpty()) return Optional.empty();

        LocalDateTime dataHoraConvertida = DataHoraUtils.converterTimestamp(dto.dataHora());

        return Optional.of(new Onibus(
                dto.ordem(),
                dto.linha(),
                posicao.get(),
                Velocidade.fromString(dto.velocidade()),
                dataHoraConvertida
        ));
    }

    private double calcularDistancia(PosicaoGeografica p1, PosicaoGeografica p2) {
        double latDistance = Math.toRadians(p2.latitude() - p1.latitude());
        double lonDistance = Math.toRadians(p2.longitude() - p1.longitude());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(p1.latitude())) * Math.cos(Math.toRadians(p2.latitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        return raioTerraKm * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }

    private boolean isOnibusAtivo(Onibus onibus) {
        if (onibus.dataHora() == null) {
            return false;
        }

        LocalDateTime quinzeMinutosAtras = LocalDateTime.now().minusMinutes(15);
        boolean gpsAtualizado = onibus.dataHora().isAfter(quinzeMinutosAtras);

        if (onibus.velocidade().valor() == 0) {
            LocalDateTime cincoMinutosAtras = LocalDateTime.now().minusMinutes(5);
            return onibus.dataHora().isAfter(cincoMinutosAtras);
        }
        return gpsAtualizado;
    }
}