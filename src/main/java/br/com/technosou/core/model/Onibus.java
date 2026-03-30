package br.com.technosou.core.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.time.LocalDateTime;

public record Onibus(
        String id,
        String linha,
        @JsonUnwrapped
        PosicaoGeografica posicao,
        Velocidade velocidade,
        LocalDateTime dataHora
) {}