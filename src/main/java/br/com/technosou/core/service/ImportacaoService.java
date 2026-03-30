package br.com.technosou.core.service;

import br.com.technosou.infra.entity.TrajetoOnibus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.InputStream;

@ApplicationScoped
public class ImportacaoService {

    @Inject
    ObjectMapper objectMapper;

    public void importar() {
        try (InputStream is = getClass().getResourceAsStream("/itinerarios.json")) {
            JsonNode root = objectMapper.readTree(is);
            JsonNode features = root.get("features");
            int totalProcessado = 0;

            for (JsonNode feature : features) {
                totalProcessado += processarLinhaIndividual(feature);
            }

            System.out.println("IMPORTAÇÃO FINALIZADA. TOTAL DE PONTOS: " + totalProcessado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public int processarLinhaIndividual(JsonNode feature) {
        String linha = feature.get("properties").get("servico").asText();
        JsonNode coordinates = feature.get("geometry").get("coordinates");
        int count = 0;

        for (JsonNode ponto : coordinates) {
            TrajetoOnibus t = new TrajetoOnibus();
            t.linha = linha.toUpperCase();
            t.longitude = ponto.get(0).asDouble();
            t.latitude = ponto.get(1).asDouble();
            t.sequencia = ++count;
            t.persist();
        }

        TrajetoOnibus.getEntityManager().flush();
        TrajetoOnibus.getEntityManager().clear();
        return count;
    }
}