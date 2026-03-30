package br.com.technosou.core.service;

import br.com.technosou.core.model.PosicaoGeografica;
import br.com.technosou.core.model.Rota;
import br.com.technosou.infra.entity.TrajetoOnibus;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RotaService {

    public Rota buscarRotaPelaLinha(String linhaDesejada) {
        List<TrajetoOnibus> listaDePontos = TrajetoOnibus
                .find("linha = ?1 ORDER BY id ASC", linhaDesejada.toUpperCase())
                .list();

        List<PosicaoGeografica> pontosReais = new ArrayList<>();
        int sequenciaAnterior = -1;

        for (TrajetoOnibus ponto : listaDePontos) {

            if (sequenciaAnterior != -1 && ponto.sequencia <= sequenciaAnterior) {
                break;
            }

            pontosReais.add(new PosicaoGeografica(ponto.latitude, ponto.longitude));
            sequenciaAnterior = ponto.sequencia;
        }

        return new Rota(linhaDesejada, pontosReais);
    }
}