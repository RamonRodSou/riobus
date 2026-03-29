package br.com.technosou.core.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record Onibus(
        String id,
        String linha,
        @JsonUnwrapped
        PosicaoGeografica posicao,
        Velocidade velocidade
) {}