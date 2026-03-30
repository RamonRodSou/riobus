package br.com.technosou.core.model;

import java.util.List;

public record Rota(
        String linha,
        List<PosicaoGeografica> pontos
) {}